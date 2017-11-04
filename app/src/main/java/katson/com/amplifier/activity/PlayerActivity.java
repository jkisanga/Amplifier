package katson.com.amplifier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import katson.com.amplifier.R;
import katson.com.amplifier.app.Constatnts;

/**
 * Created by user on 2/25/2017.
 */

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private TextView videoTitle, videoDesc,videoDate;
    private ImageButton shareBtn;
    private String VideoTitle, VideoDesc,VideoDate, downloadLink, videpID;
    private YouTubePlayerView myouTubePlayerView;
    private YouTubePlayer.OnInitializedListener monInitializedListener;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);


        Bundle bundles = getIntent().getExtras();
        if (bundles != null) {
            videpID = bundles.getString(Constatnts.KEY_VIDEO_ID);
            VideoTitle = bundles.getString(Constatnts.KEY_VIDEO_TITLE);
           VideoDesc = bundles.getString(Constatnts.KEY_VIDEO_DESC);
           VideoDate = bundles.getString(Constatnts.KEY_VIDEO_DATE);
            downloadLink = bundles.getString(Constatnts.KEY_VIDEO_DOWNLOAD_LINK);

            myouTubePlayerView = (YouTubePlayerView) findViewById(R.id.player_view);
            videoTitle = (TextView) findViewById(R.id.text_video_title);
            videoDesc = (TextView) findViewById(R.id.text_video_desc);
            shareBtn = (ImageButton) findViewById(R.id.btn_share);
            monInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(videpID);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            };

            myouTubePlayerView.initialize(Constatnts.browserKey, monInitializedListener);

        }






        videoTitle.setText(VideoTitle);
        videoDesc.setText(VideoDesc);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
       });

    }

    private void share() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Amplifaya");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,"amplifaya :  https://www.youtube.com/watch?v="+ videpID);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            youTubePlayer.cueVideo(getIntent().getStringExtra(Constatnts.KEY_VIDEO_ID));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
        Log.d("onInitializationFailure", "onInitializationFailure: " + youTubeInitializationResult.toString());
    }
}
