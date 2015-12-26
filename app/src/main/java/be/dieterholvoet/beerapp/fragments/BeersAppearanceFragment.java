package be.dieterholvoet.beerapp.fragments;

/**
 * Created by Dieter on 26/12/2015.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.dieterholvoet.beerapp.R;

public class BeersAppearanceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.beers_appearance, null);
    }
}