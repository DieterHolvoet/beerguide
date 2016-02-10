package be.dieterholvoet.beerguide.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.model.Beer;

/**
 * Created by Dieter on 5/02/2016.
 * Source: http://developer.android.com/training/camera/photobasics.html
 */

@Table(name = "Images")
public class ImageStore extends Model implements Serializable {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_TAKE_PHOTO = 1;

    private String timeStamp;
    private String imageFileName;
    private File storageDir;
    private File image;

    @Column(name = "imagePath")
    private String path;

    @Column(name = "beer")
    private Beer beer;

    public ImageStore() {
        this.createImageFile();
    }

    public ImageStore(String path) {
        this.path = path;
    }

    public ImageStore(String path, Beer beer) {
        this.path = path;
        this.beer = beer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    private void createImageFile() {
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
        this.path = "file:" + image.getAbsolutePath();
    }

    public Uri dispatchTakePictureIntent(Activity context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(this.image);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {

            if(this.image == null) {
               createImageFile();
            }

            // Continue only if the File was successfully created
            if (this.image != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                context.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }

            return uri;

        } else {
            return null;
        }
    }

    public void addToGallery(Activity context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void saveToDatabase(Beer beerToSave) {
        setBeer(beerToSave);
        save();
    }

    // Source: http://stackoverflow.com/a/6772455/2637528
    public void openInGallery(Activity context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "image/*");
        context.startActivity(intent);
    }

    public void loadInView(Activity context, ImageView view, int width, int height) {
        Picasso.with(context)
                .load(path)
                .resize(width, height)
                .centerCrop()
                .placeholder(R.drawable.beer_placeholder)
                .into(view);
    }
}
