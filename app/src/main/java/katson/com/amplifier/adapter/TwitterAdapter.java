package katson.com.amplifier.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import katson.com.amplifier.R;
import katson.com.amplifier.activity.TwitterActivity;
import katson.com.amplifier.app.Config;
import katson.com.amplifier.pojo.Channel;


/**
 * Created by user on 8/17/2017.
 */

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.MyViewHolder> {

    private List<Channel> channels;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, desc;
        public CircleImageView thumbnail;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.top_layout) ;

            thumbnail = (CircleImageView) view.findViewById(R.id.circleView);
            title = (TextView) view.findViewById(R.id.channel_title);
           // desc = (TextView) view.findViewById(R.id.channel_desc);


        }
    }



    public TwitterAdapter(List<Channel> channelList, Activity activity) {
        this.activity  = activity;
        this.channels = channelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Channel channel = channels.get(position);

        String path = activity.getExternalFilesDir(null) + File.separator + channel.getThumbnail();
        holder.title.setText(channel.getTitle());
      //  holder.desc.setText(channel.getDesc());
        Picasso.with(activity).load(Config.url +"document/" + channel.getThumbnail()).into(holder.thumbnail);
        if(position % 2 == 0 ){
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#2196F3"));
        }else {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#16a085"));
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, TwitterActivity.class);
                intent.putExtra(Config.THUMBNAIL_PATH, channel.getThumbnail());
                intent.putExtra(Config.TITLE, channel.getTitle());
                intent.putExtra(Config.CHANNEL_URL, channel.getUrl());
                activity.startActivity(intent);
//                profileDetailFragment.setArguments(bundle);
//                activity.getFragmentManager().beginTransaction()
//                        .replace(R.id.layout_container, profileDetailFragment,"FRAGMENT")
//                        .addToBackStack(null)
//                        .commit();

            }
        });




    }

    @Override
    public int getItemCount() {
        return channels.size();
    }


    public void setFilter(List<Channel> docs){
        channels = new ArrayList<>();
        channels.addAll(docs);
        notifyDataSetChanged();
    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
