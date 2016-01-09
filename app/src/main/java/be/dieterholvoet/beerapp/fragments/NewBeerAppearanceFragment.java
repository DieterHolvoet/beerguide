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

import be.dieterholvoet.beerapp.NewBeerActivity;
import be.dieterholvoet.beerapp.R;
import be.dieterholvoet.beerapp.adapters.RatingAdapter;
import be.dieterholvoet.beerapp.model.Beer;
import be.dieterholvoet.beerapp.model.RatingElement;

/**
 * Created by Dieter on 26/12/2015.
 */
public class NewBeerAppearanceFragment extends Fragment {
    NewBeerActivity activity;
    Beer beer;
    RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (NewBeerActivity) getActivity();
        beer = activity.getBeer();
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_new_beer_appearance, null);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        List<RatingElement> elements = new ArrayList<>();
        elements.add(new RatingElement("Foam", "How much would you rate the quality of the beer foam?", R.drawable.beer_foam_m, beer.getFoam()));
        elements.add(new RatingElement("Color", "Describe the color of the beer, ranging from very light to very dark.", R.drawable.beer_clearness_m, beer.getColor()));
        elements.add(new RatingElement("Clearness", "Describe the clearness of the beer, ranging from very clear to cloudy.", R.drawable.beer_clearness_2_m, beer.getClarity()));
        RatingAdapter ra = new RatingAdapter(elements, getActivity());

        recycler.setHasFixedSize(true);
        recycler.setAdapter(ra);
        recycler.setLayoutManager(llm);

        return recycler;
    }
}
