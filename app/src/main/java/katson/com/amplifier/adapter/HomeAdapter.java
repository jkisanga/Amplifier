package katson.com.amplifier.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import katson.com.amplifier.R;
import katson.com.amplifier.activity.PlayerActivity;
import katson.com.amplifier.app.Constatnts;
import katson.com.amplifier.pojo.HomeVideo;


/**
 * Created by user on 4/2/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<HomeVideo> homeVideos;
    String key;
    Activity activity;
    private int REQ_PLAYER_CODE  = 1;
    int cornerRadius;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, description;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.imageView) ;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeVideo video = homeVideos.get(getAdapterPosition());
                    Intent intent = new Intent(activity, PlayerActivity.class);
                    intent.putExtra(Constatnts.KEY_VIDEO_ID, video.getVideoID());
                    intent.putExtra(Constatnts.KEY_VIDEO_TITLE, video.getTitle());
                    intent.putExtra(Constatnts.KEY_VIDEO_DESC, video.getDescription());
                    intent.putExtra(Constatnts.KEY_VIDEO_DATE, video.getDate());
                    activity.startActivityForResult(intent, REQ_PLAYER_CODE);

                }
            });

            name = (TextView) view.findViewById(R.id.txt_title);
            description = (TextView) view.findViewById(R.id.description);


        }
    }






    public HomeAdapter(List<HomeVideo> videoList, String yt_key, Activity activity, int cornerRadius) {
        this.activity  = activity;
        this.key = yt_key;
        this.homeVideos = videoList;
        this.cornerRadius = cornerRadius;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HomeVideo video = homeVideos.get(position);
        holder.name.setText(video.getTitle());
        holder.description.setText(video.getDescription());
        Picasso.with(activity).load(video.getThumbnailUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return homeVideos.size();
    }
}
