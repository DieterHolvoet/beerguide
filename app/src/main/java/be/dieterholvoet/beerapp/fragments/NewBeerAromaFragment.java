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
public class NewBeerAromaFragment extends Fragment {
    RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_new_beer_aroma, null);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        List<RatingElement> elements = new ArrayList<>();
        // elements.add(new RatingElement("Foam", "How much would you rate the quality of the beer foam?", R.drawable.beer_foam_m));
        RatingAdapter ra = new RatingAdapter(elements);

        recycler.setHasFixedSize(true);
        recycler.setAdapter(ra);
        recycler.setLayoutManager(llm);

        return recycler;
    }
}
