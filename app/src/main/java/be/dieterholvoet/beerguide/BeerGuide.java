package be.dieterholvoet.beerguide;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import be.dieterholvoet.beerguide.helper.PrimaryKeyFactory;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dieter on 6/09/2016.
 */

public class BeerGuide extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        LeakCanary.install(this);
    }

    protected void initRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);

        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.getInstance().initialize(realm);
        realm.close();
    }
}