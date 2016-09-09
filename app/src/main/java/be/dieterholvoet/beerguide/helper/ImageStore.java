package be.dieterholvoet.beerguide.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.model.Beer;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Dieter on 5/02/2016.
 * Source: http://developer.android.com/training/camera/photobasics.html
 */

public class ImageStore extends RealmObject implements Serializable {
    @PrimaryKey
    private long primaryKey;
    private String path;

    @Ignore
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    @Ignore
    public static final int REQUEST_TAKE_PHOTO = 1;
    @Ignore
    private String timeStamp;
    @Ignore
    private String imageFileName;
    @Ignore
    private File file;
    @Ignore
    private Uri uri;

    public ImageStore() {
        this.createImageFile();
    }

    public ImageStore(String path) {
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public ImageStore save(Realm realm) {
        ImageStore newObj;
        boolean inTransactionBefore = realm.isInTransaction();

        try {
            if(!inTransactionBefore) realm.beginTransaction();

            if(this.primaryKey == 0) throw new RealmPrimaryKeyConstraintException("");

        } catch(RealmPrimaryKeyConstraintException e) {
            this.primaryKey = PrimaryKeyFactory.getInstance().nextKey(this.getClass());

        } finally {
            newObj = realm.copyToRealm(this);
            if(!inTransactionBefore) realm.commitTransaction();
        }

        return newObj;
    }

    public void delete(Realm realm) {
        boolean inTransactionBefore = realm.isInTransaction();

        try {
            if(!inTransactionBefore) realm.beginTransaction();
            if(this.primaryKey == 0) throw new Exception("Cannot delete: this is not a managed Realm object.");

        } catch(Exception e) {
            Log.d("BEER", e.getMessage());
            return;

        } finally {
            this.deleteFromRealm();
            if(!inTransactionBefore) realm.commitTransaction();
        }
    }
}
