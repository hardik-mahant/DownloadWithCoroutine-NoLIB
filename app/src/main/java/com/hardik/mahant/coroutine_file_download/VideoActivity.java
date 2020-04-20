package com.hardik.mahant.coroutine_file_download;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * JAVA DEMONSTRATION FOR USING KOTLIN CO-ROUTINE CLASS
 * */

public class VideoActivity extends AppCompatActivity implements DownloadCallBack{

    String videoURL;
    VideoView videoView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoURL = getIntent().getStringExtra("videoURL");
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        new CRDownloadFile(this, videoURL, this).execute();
    }

    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        progressBar.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownloadStart() {
        showLoading();
    }

    @Override
    public void onDownloadComplete(String uri) {
        hideLoading();
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    public void onDownloadFailed() {
        Toast.makeText(this, "Download failed!", Toast.LENGTH_SHORT).show();
        hideLoading();
    }
}
