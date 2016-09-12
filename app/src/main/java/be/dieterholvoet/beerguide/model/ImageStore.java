package be.dieterholvoet.beerguide.model;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.dieterholvoet.beerguide.helper.ImageHelper;
import be.dieterholvoet.beerguide.helper.PrimaryKeyFactory;
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
    private String filename;
    private String filetype;
    private long timestamp;

    public ImageStore() {}

    public ImageStore(String filetype, long timestamp) {
        this.filetype = filetype;
        this.timestamp = timestamp;

        generateFilename();
    }

    public ImageStore(String filetype) {
        this.filetype = filetype;

        generateTimestamp();
        generateFilename();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    /*
        HELPER
     */

    public String getFullPath() {
        return ImageHelper.getStorageDirectory() + File.separator + ImageHelper.getFolderName() + File.separator + filename + "." + filetype;
    }

    public File getFile() {
        return new File(getFullPath());
    }

    public Uri getUri() {
        return Uri.fromFile(getFile());
    }

    public long generateTimestamp() {
        return this.timestamp = System.currentTimeMillis() / 1000;
    }

    public String generateFilename() {
        return this.filename = "BEER_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(timestamp);
    }

    /*
        REALM
     */

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
