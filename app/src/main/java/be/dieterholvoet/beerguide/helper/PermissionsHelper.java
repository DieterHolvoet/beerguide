package be.dieterholvoet.beerguide.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dieter on 9/09/2016.
 */

public class PermissionsHelper {

    private static final String LOG = "PermissionsHelper";

    public static void showExplanationDialog(final Activity activity, final String permission, final int identifier) {
        String message;

        switch(permission) {
            case Manifest.permission.INTERNET:
                message = "Internet permission is necessary to perform searches.";
                break;

            default:
                message = "No explanation provided.";
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle("Permission necessary")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, identifier);
                    }
                });

        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public static boolean requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ArrayList<String> permissionsToAsk = new ArrayList<>();

        for(String permission : permissions) {
            int perm = ContextCompat.checkSelfPermission(activity, permission);
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    showExplanationDialog(activity, permission, requestCode);

                } else {
                    permissionsToAsk.add(permission);
                }
            }
        }


        if(permissionsToAsk.size() > 0) {
            activity.requestPermissions(permissionsToAsk.toArray(new String[permissionsToAsk.size()]), requestCode);
            return true;

        } else {
            Log.d(LOG, "No permissions need to be requested.");

            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);

            activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return false;
        }
    }

    public static boolean requestPermission(Activity activity, String permission, int identifier) {
        return requestPermissions(activity, new String[]{permission}, identifier);
    }

    public static boolean isRunningMarshmallowOrAbove() {
        return(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }
}
