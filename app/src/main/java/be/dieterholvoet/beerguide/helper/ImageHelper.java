package be.dieterholvoet.beerguide.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.model.ImageStore;

/**
 * Created by Dieter on 9/09/2016.
 */

public class ImageHelper {

    private static final String FOLDER_NAME = "BeerGuide";

    public static ImageStore takePicture(Activity context, int requestCode) {
        ImageStore image = new ImageStore();

        if (hasCamera(context)) {

            // Create the File
            image = createImageFile(image);

            // Continue only if the File was successfully created
            if (image.getFile() != null) {
                sendTakePictureIntent(context, image.getUri(), requestCode);

            } else {
                Log.e("LOG", "Failed creating the image.");
                return null;
            }

            return image;

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

    private static void sendTakePictureIntent(Activity context, Uri uri, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(takePictureIntent, requestCode);
    }

    private static ImageStore createImageFile(ImageStore image) {
        image.setTimestamp(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        image.setFilename("JPEG_" + image.getTimestamp());

        File file = new File(image.getFullPath());
        try {
            if(file.exists() == false) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static void addToGallery(Activity context, Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        context.sendBroadcast(mediaScanIntent);
    }

    // Source: http://stackoverflow.com/a/6772455/2637528
    public static void openInGallery(Activity context, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);
    }

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
}
