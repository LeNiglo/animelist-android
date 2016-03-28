package com.lefrantguillaume.animelist.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.controllers.ShowFocusChangeListener;
import com.lefrantguillaume.animelist.models.ShowModel;

import java.net.URISyntaxException;
import java.util.concurrent.CancellationException;

public class DetailsActivity extends AppCompatActivity {

    private ShowModel item;
    private EditText mEditCommentary;
    private EditText mEditSeason;
    private EditText mEditEpisode;
    private ImageView mDetailsPicture;
    private EditText mEditLink;
    private EditText mEditPicture;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item = getIntent().getParcelableExtra("ShowModel");

        mDetailsPicture = (ImageView) findViewById(R.id.details_picture);
        mEditCommentary = (EditText) findViewById(R.id.edit_commentary);
        mEditSeason = (EditText) findViewById(R.id.edit_season);
        mEditEpisode = (EditText) findViewById(R.id.edit_episode);
        mEditLink = (EditText) findViewById(R.id.edit_link);
        mEditPicture = (EditText) findViewById(R.id.edit_picture);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        bind();
    }

    private void bind() {
        mEditCommentary.setText(item.getCommentary());
        mEditCommentary.setOnFocusChangeListener(new ShowFocusChangeListener(this, item, "commentary"));
        mEditSeason.setText(String.format("%d", item.getSeason()));
        mEditSeason.setOnFocusChangeListener(new ShowFocusChangeListener(this, item, "season"));
        mEditEpisode.setText(String.format("%d", item.getEpisode()));
        mEditEpisode.setOnFocusChangeListener(new ShowFocusChangeListener(this, item, "episode"));
        mEditLink.setText(item.getLink());
        mEditLink.setOnFocusChangeListener(new ShowFocusChangeListener(this, item, "link"));
        mEditPicture.setText(item.getPic());
        mEditPicture.setOnFocusChangeListener(new ShowFocusChangeListener(this, item, "pic"));

        if (getActionBar() != null)
            getActionBar().setTitle(item.getName());
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(item.getName());

        if (!item.getPic().startsWith("http://") && !item.getPic().startsWith("https://")) {
            Ion.with(mDetailsPicture).error(R.drawable.ic_tosee).load(SplashActivity.ROOT_URL + item.getPic());
        } else {
            Ion.with(mDetailsPicture).error(R.drawable.ic_tosee).load(item.getPic());
        }

        if (!item.getLink().startsWith("http://") && !item.getLink().startsWith("https://")) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            mFab.setLayoutParams(p);
            mFab.setVisibility(View.GONE);
        } else {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(item.getLink()));
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        try {
            Ion.getDefault(this).cancelAll(this);
        } catch (CancellationException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

}
