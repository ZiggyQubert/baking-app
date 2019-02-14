package com.ziggyqubert.android.baking_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ziggyqubert.android.baking_app.model.Step;
import com.ziggyqubert.android.baking_app.utilities.Utilities;


public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";

    private OnFragmentInteractionListener mListener;

    private Button nextButton;

    private Step step = null;
    private Boolean isLastStep = false;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;

    private static final String TAG = BakingApp.APP_TAG;
    private static MediaSessionCompat mMediaSession;

    private Integer startWindow;
    private Long startPosition;

    /**
     * constructor
     */
    public StepDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * public method to set teh step data
     *
     * @param stepData
     * @param isLast
     */
    public void setStepData(Step stepData, Boolean isLast) {
        step = stepData;
        isLastStep = isLast;
        displayStepInView();
    }

    /**
     * handels displaying the step dta in the view
     */
    protected void displayStepInView() {

        View rootView = getView();

        if (rootView != null && step != null) {

            ((TextView) rootView.findViewById(R.id.tv_step_description)).setText(step.getDescription());

            releasePlayer();
            if (step.hasVideo()) {
                mPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(step.getVideoUri());
            } else {
                mPlayerView.setVisibility(View.GONE);
            }

            ImageView imageView = rootView.findViewById(R.id.step_image);
            if (step.hasImage()) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(step.getThumbnailURL())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.recepie_not_found)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(BakingApp.APP_TAG, "Error loading image");
                                e.printStackTrace();
                            }
                        });
            } else {
                imageView.setVisibility(View.GONE);
            }

            if (isLastStep) {
                nextButton.setVisibility(View.GONE);
            } else {
                nextButton.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * initializes teh video player
     *
     * @param mediaUri
     */
    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            Context context = getView().getContext();
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, BakingApp.APP_TAG);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(context, userAgent), new DefaultExtractorsFactory(), null, null);

            if (startWindow != C.INDEX_UNSET) {
                mExoPlayer.seekTo(startWindow, startPosition);
            }

            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(true);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        updateStartPosition();
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            startWindow = mExoPlayer.getCurrentWindowIndex();
            startPosition = Math.max(0, mExoPlayer.getCurrentPosition());
        }
    }

    private void clearStartPosition() {
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * pause teh video when fragment suspends
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            releasePlayer();
        }
    }

    /**
     * pause the video when fragment suspends
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            releasePlayer();
        }
    }

    /**
     * resume the video when the the fragment resumes
     */
    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(step.getVideoUri());
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getView().getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * releases the player resources
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateStartPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            clearStartPosition();
        }
    }

    /**
     * handels creating and setting up the view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        //gets a reference to the video player view and sets up the height of that view
        mPlayerView = rootView.findViewById(R.id.playerView);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                int width = rootView.getMeasuredWidth(); // for instance
                if (mPlayerView != null) {
                    initializeMediaSession();
                    ViewGroup.LayoutParams videoLayoutParams = mPlayerView.getLayoutParams();
                    videoLayoutParams.height = Math.round(width / 2);
                    mPlayerView.setLayoutParams(videoLayoutParams);
                }
            }
        });

        //sets up the next button
        nextButton = (Button) rootView.findViewById(R.id.bt_next_step);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSelectNextButton(true);
            }
        });

        displayStepInView();

        return rootView;
    }

    /**
     * on attach check that the context impliments teh correct listiner
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepDetailsFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        /**
         * called when the next button is selected
         *
         * @param navigateToNext
         */
        void onSelectNextButton(Boolean navigateToNext);
    }
}
