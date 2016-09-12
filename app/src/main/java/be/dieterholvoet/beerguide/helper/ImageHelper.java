package be.dieterholvoet.beerguide.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.model.ImageStore;

/**
 * Created by Dieter on 9/09/2016.
 */

public class ImageHelper {

    private static final String FOLDER_NAME = "BeerGuide";

    public static void loadInView(Activity context, ImageView view, int width, int height, Uri uri) {
        Picasso.with(context)
                .load(uri)
                .resize(width, height)
                .centerCrop()
                .placeholder(R.drawable.beer_placeholder)
                .into(view);
    }

    public static String getStorageDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
    }

    public static String getFolderName() {
        return FOLDER_NAME;
    }

    /*
        CAMERA METHODS
     */

    public static ImageStore takePicture(Activity activity, int requestCode, Fragment fragment) {
        if (hasCamera(activity)) {
            if(Helper.isExternalStoragePresent()) {

                // Create the File
                ImageStore image = createImageFile();

                // Continue only if the File was successfully created
                if (image.getFile() != null) {
                    if(fragment == null) {
                        sendTakePictureIntent(activity, image.getUri(), requestCode);

                    } else {
                        sendTakePictureIntent(fragment, image.getUri(), requestCode);
                    }

                } else {
                    Log.e("LOG", "Failed creating the image.");
                    return null;
                }

                return image;

            } else {
                Log.e("LOG", "No external storage available.");
                return null;
            }

        } else {
            Log.e("LOG", "No camera activity available.");
            return null;
        }
    }

    // Ensure that there's a camera activity to handle the intent
    public static boolean hasCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean hasCameraHardware = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean hasCameraResolver = takePictureIntent.resolveActivity(context.getPackageManager()) != null;

        return hasCameraHardware && hasCameraResolver;
    }

    /*
        GALLERY METHODS
     */

    public static void addToGallery(Activity context, Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        context.sendBroadcast(mediaScanIntent);
    }

    // Source: http://stackoverflow.com/a/6772455/2637528
    public static boolean openInGallery(Activity context, Uri uri) {
        if(new File(uri.getPath()).exists()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/*");
            context.startActivity(intent);
            return true;

        } else {
            Log.e("ImageHelper", "File does not exist.");
            return false;
        }
    }

    public static void pickFromGallery(Activity activity, int requestCode) {
        if(Helper.isExternalStoragePresent()) {
            sendGetImageIntent(activity, requestCode);
        } else {
            Log.e("LOG", "No external storage available.");
        }
    }

    public static void pickFromGallery(Fragment fragment, int requestCode) {
        if(Helper.isExternalStoragePresent()) {
            sendGetImageIntent(fragment, requestCode);
        } else {
            Log.e("LOG", "No external storage available.");
        }
    }

    /*
        INTENT METHODS
     */

    private static void sendTakePictureIntent(Activity activity, Uri uri, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    private static void sendTakePictureIntent(Fragment fragment, Uri uri, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static void sendGetImageIntent(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    private static void sendGetImageIntent(Fragment fragment, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    /*
        FILE METHODS
     */

    private static ImageStore createImageFile() {
        ImageStore image = new ImageStore("jpg");
        FileHelper.createFile(image.getFullPath());
        return image;
    }
}
