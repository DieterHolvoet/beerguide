package be.dieterholvoet.beerapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.dieterholvoet.beerapp.R;
import be.dieterholvoet.beerapp.model.RatingElement;

/**
 * Created by Dieter on 26/12/2015.
 */

public class NewBeerTasteFragment extends Fragment {
    RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_new_beer_taste, null);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        List<RatingElement> elements = new ArrayList<>();
        elements.add(new RatingElement("Sweet", "Describe the sweetness of the beer.", R.drawable.beer_sweet_m));
        elements.add(new RatingElement("Sour", "Describe the sourness of the beer.", R.drawable.beer_sour_m));
        elements.add(new RatingElement("Bitter", "Describe the bitterness of the beer.", R.drawable.beer_bitter_m));
        elements.add(new RatingElement("Full", "Describe the fullness of the beer.", R.drawable.beer_color_m));
        RatingAdapter ra = new RatingAdapter(elements);

        recycler.setHasFixedSize(true);
        recycler.setAdapter(ra);
        recycler.setLayoutManager(llm);

        return recycler;
    }
}
