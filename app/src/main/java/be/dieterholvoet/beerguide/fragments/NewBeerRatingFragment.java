package be.dieterholvoet.beerguide.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import be.dieterholvoet.beerguide.MainActivity;
import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BeerRating;

/**
 * Created by Dieter on 26/12/2015.
 */

public class NewBeerRatingFragment extends Fragment {
    NewBeerActivity activity;
    CardView button;
    RatingBar overallRating;
    EditText notes;
    Beer beer;
    Drawable starDrawable;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        this.view = inflater.inflate(R.layout.fragment_new_beer_rating, null);
        this.activity = (NewBeerActivity) getActivity();
        this.beer = activity.getBeer();
        this.overallRating = (RatingBar) view.findViewById(R.id.overall_rating_card_rating);
        this.notes = (EditText) view.findViewById((R.id.beer_info_notes));
        this.button = (CardView) view.findViewById(R.id.overall_rating_card_submit);

        this.overallRating.setRating(beer.getRating().getRating());
        this.notes.setText(beer.getNotes());

        switch(getResources().getConfiguration().orientation) {
            // Portrait
            case 1:
                break;

            // Landscape
            case 2:
                starDrawable = ContextCompat.getDrawable(activity, R.drawable.star_medium_full);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) overallRating.getLayoutParams();
                params.height = starDrawable.getMinimumHeight();
                overallRating.setLayoutParams(params);
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(beer.getRating().getRating() == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.dialog_overall_rating_before_save), Toast.LENGTH_SHORT).show();

                } else {
                    BeerDAO.save(beer);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    activity.finish();
                }
            }
        });

        overallRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                BeerRating beerRating = beer.getRating();
                beerRating.setRating(ratingBar.getRating());
                beer.setRating(beerRating);
            }
        });

        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                beer.setNotes(s.toString());
            }
        });

        return view;
    }
}
