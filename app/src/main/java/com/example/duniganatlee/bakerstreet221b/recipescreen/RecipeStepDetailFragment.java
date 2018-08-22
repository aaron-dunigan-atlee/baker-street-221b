package com.example.duniganatlee.bakerstreet221b.recipescreen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duniganatlee.bakerstreet221b.R;
import com.example.duniganatlee.bakerstreet221b.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    // Layout view variables to be bound using ButterKnife.
    @BindView(R.id.player_view) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.text_description) TextView descriptionTextView;

    // Member variables.
    private SimpleExoPlayer mExoPlayer;
    private Step mRecipeStep;
    private int id;
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;
    private String mRecipeName;
    private long exoPlayerPlaybackPosition = 0;
    private int exoPlayerWindowIndex = 0;
    private boolean exoPlayerAutoPlay = true;
    private static final String PLAYBACK_POSITION = "playback_position";
    private static final String WINDOW_INDEX = "window_index";
    private static final String AUTOPLAY = "autoplay";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(Uri.parse(mVideoURL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this,rootView);

        mDescription = mRecipeStep.getDescription();
        descriptionTextView.setText(mDescription);

        // Initialize the media player.

        mVideoURL = mRecipeStep.getVideoURL();
        if (savedInstanceState != null) {
            exoPlayerAutoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
            exoPlayerWindowIndex = savedInstanceState.getInt(WINDOW_INDEX, 0);
            exoPlayerPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
        }
        return rootView;
    }

    public void initializePlayer(Uri mediaUri) {
        Context context = getContext();
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);
            //Prepare media source.
            String userAgent = Util.getUserAgent(context, "BakerStreet221B");
            MediaSource mediaSource = new ExtractorMediaSource(
                    mediaUri,
                    new DefaultDataSourceFactory(context, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(exoPlayerAutoPlay);
            mExoPlayer.seekTo(exoPlayerWindowIndex, exoPlayerPlaybackPosition);
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    // Release ExoPlayer
    private void releasePlayer() {
        // Get current state of player before releasing, in case we need to
        // restore the player state/position later (e.g. on screen rotation).
        exoPlayerPlaybackPosition = mExoPlayer.getCurrentPosition();
        exoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
        // getPlayWhenReady() will be true if player is currently playing; false otherwise.
        // Therefore, this tells us whether to continue playing when the activity resumes, or just
        // queue up the track in paused state.
        // See https://github.com/google/ExoPlayer/issues/3570
        exoPlayerAutoPlay = mExoPlayer.getPlayWhenReady();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    // Help for saving and restoring player state came from
    // https://gist.github.com/codeshifu/c26bb8a5f27f94d73b3a4888a509927c
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(WINDOW_INDEX,exoPlayerWindowIndex);
        outState.putLong(PLAYBACK_POSITION, exoPlayerPlaybackPosition);
        outState.putBoolean(AUTOPLAY, exoPlayerAutoPlay);
    }

    public void setRecipeStep(Step recipeStep) {
        mRecipeStep = recipeStep;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }
}
