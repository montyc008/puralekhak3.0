package com.mygdx.game;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by monty on 1/13/2016.
 * Acts as a Delegator between Android and Libgdx since it acts as an entry point
 * for libgdx.
 * Required because ImageProcessing module has entry points through both of them
 * TODO: which needs be removed ??
 */

public class LibgdxFragment extends AndroidFragmentApplication
        implements View.OnClickListener, View.OnLongClickListener{

    private ControllerViewInterface cvDelegator;
    private ViewControllerInterface vcDelegator;
    private MainActivity parentActivity;
    private ProgressDialog progressBar;
    private Button unicodeButton;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (MainActivity)activity;
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(false);
        progressBar.setMessage("Processing ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vcDelegator = parentActivity;
        MyImageViewer imageViewer = new MyImageViewer(vcDelegator,"inscription.jpg");
        cvDelegator = imageViewer;
        FrameLayout parent = (FrameLayout)inflater.inflate(R.layout.libgdxview,null);
        parent.addView(initializeForView(imageViewer));
        unicodeButton = (Button)parent.findViewById(R.id.unicodeButton);
        unicodeButton.setOnClickListener(this);
        unicodeButton.setOnLongClickListener(this);
        SurfaceView v = (SurfaceView)parent.findViewById(R.id.surfaceview);
        v.bringToFront();
        unicodeButton.bringToFront();

        parentActivity.ImageviewerReady(cvDelegator);
        return parent;
    }

    public ControllerViewInterface getCvDelegator() { return cvDelegator; }

    @Override
    public void onClick(View v) { vcDelegator.ShowKeyboard(((Button) v).getText()); }

    @Override
    public boolean onLongClick(View v) {
        vcDelegator.StartSpotting();
        return true;
    }

    /**
     * Progress bar to display spotting progress
     */
    public void ShowProgressBar() {
        progressBar.show();
    }

    public void UpdateProgressBar(final int progress) {
        if (progress >= 100 ) progressBar.dismiss();
        postRunnable(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
            }
        });
    }

    public String getUnicodeText() {
        return unicodeButton.getText().toString();
    }
    public void setUnicodeText(String text) {
        unicodeButton.setText(text);
        cvDelegator.UnicodeSelected(text);
    }
}
