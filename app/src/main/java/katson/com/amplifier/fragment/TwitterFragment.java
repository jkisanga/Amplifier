package katson.com.amplifier.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import katson.com.amplifier.R;
import katson.com.amplifier.adapter.TwitterAdapter;
import katson.com.amplifier.app.AutoFitGridLayoutManager;
import katson.com.amplifier.app.Config;
import katson.com.amplifier.pojo.Channel;
import katson.com.amplifier.retrofit.IRetrofit;
import katson.com.amplifier.table.RChannel;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by user on 2/18/2017.
 */

public class TwitterFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private Realm realm;
    private List<RChannel> rChannels = new ArrayList<>();
    private TwitterAdapter channelAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_twitter, container, false);
        // this.realm = RealmController.with(this).getRealm();
        // rChannels  = RealmController.with(this).getChannels();
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 300);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        getChannelsFromSeverToDatabase();

        return view;
    }


    private void getChannelsFromSeverToDatabase() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IRetrofit service = retrofit.create(IRetrofit.class);

        try {

            Call<List<Channel>> call = service.getTweeter();
            call.enqueue(new Callback<List<Channel>>() {
                @Override
                public void onResponse(Response<List<Channel>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {
                        //channelAdapter = new ChannelAdapter(rChannels,getActivity());


                        List<Channel>   channels = response.body();

                        channelAdapter = new TwitterAdapter(channels,getActivity());
                        recyclerView.setAdapter(channelAdapter);

                    }else{
                    }


                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        }catch (Exception e){

        }

    }

}
