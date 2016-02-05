package be.dieterholvoet.beerguide;

import android.content.Context;

/**
 * Created by Dieter on 24/01/2016.
 */
public class Helper {
    public static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }
}
