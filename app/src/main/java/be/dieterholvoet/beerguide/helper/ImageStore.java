package be.dieterholvoet.beerguide.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dieter on 5/02/2016.
 */
public class ImageStore {
    private final int REQUEST_TAKE_PHOTO = 1;

    private Activity context;
    private PackageManager pm;

    private String timeStamp;
    private String mCurrentPhotoPath;
    private String imageFileName;
    private File storageDir;
    private File image;

    public ImageStore(Activity context) {
        this.context = context;
        this.pm = context.getPackageManager();
    }

    public File createImageFile() {
        this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.imageFileName = "JPEG_" + timeStamp + "_";
        this.storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/BeerGuide");
        this.storageDir.mkdirs();
        try {
            this.image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        this.mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.pm) != null) {

            // Create the File where the photo should go
            File photoFile = createImageFile();

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                context.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void addToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
