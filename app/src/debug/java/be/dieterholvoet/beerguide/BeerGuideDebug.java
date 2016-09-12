package be.dieterholvoet.beerguide;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import be.dieterholvoet.beerguide.BeerGuide;

/**
 * Created by Dieter on 12/09/2016.
 */

public class BeerGuideDebug extends BeerGuide {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
