package be.dieterholvoet.beerguide.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
    private File file;
    private Uri uri;

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

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    private void createImageFile() {
        this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.imageFileName = "JPEG_" + timeStamp + "_";

        this.file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + "BeerGuide" + File.separator + imageFileName + ".jpg");
        try {
            if(file.exists() == false) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        this.path = "file:" + file.getAbsolutePath();
        this.uri = Uri.fromFile(this.file);
    }

    public Uri dispatchTakePictureIntent(Activity context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {

            if(this.file == null) {
               createImageFile();
            }

            // Continue only if the File was successfully created
            if (this.file != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                context.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            } else {
                Log.e("LOG", "Failed creating the image.");
            }

            return uri;

        } else {
            Log.e("LOG", "No camera activity available.");
            return null;
        }
    }

    public void addToGallery(Activity context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        context.sendBroadcast(mediaScanIntent);
    }

    // Source: http://stackoverflow.com/a/6772455/2637528
    public void openInGallery(Activity context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
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
