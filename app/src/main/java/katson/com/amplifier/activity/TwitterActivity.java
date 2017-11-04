package katson.com.amplifier.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.io.File;

import katson.com.amplifier.R;
import katson.com.amplifier.app.Config;

public class TwitterActivity extends ListActivity {
    String loadMsg, channelID;
    private TwitterLoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        Bundle bundles = getIntent().getExtras();
        if (bundles != null) {
            bundles.getString(Config.THUMBNAIL_PATH);
            String path = getExternalFilesDir(null) + File.separator + bundles.getString(Config.THUMBNAIL_PATH);
            toolbar.setTitle(bundles.getString(Config.TITLE));
            channelID = bundles.getString(Config.CHANNEL_URL);
        }

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(channelID)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        // ListView listView = (ListView) view.findViewById(R.id.twitterList);
        setListAdapter(adapter);






//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
