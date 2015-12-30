package be.dieterholvoet.beerapp.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import be.dieterholvoet.beerapp.R;

/**
 * Created by Dieter on 30/12/2015.
 */

public class RatingCardView extends android.support.v7.widget.CardView {
    private ImageView banner;
    private TextView title;
    private TextView description;
    private RatingBar rating;

    public RatingCardView(Context context) {
        super(context);

        View.inflate(context, R.layout.rating_card_view, this);
        banner = (ImageView) findViewById(R.id.rating_card_banner);
        title = (TextView) findViewById(R.id.rating_card_title);
        description = (TextView) findViewById(R.id.rating_card_description);
        rating = (RatingBar) findViewById(R.id.rating_card_rating);
    }

    public RatingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBanner(int id) {
        this.banner.setImageResource(id);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public int getRating() {
        return rating.getNumStars();
    }

    public void setRating(int rating) {
        this.rating.setNumStars(rating);
    }

    public String getTitle() {
        return (String) title.getText();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
