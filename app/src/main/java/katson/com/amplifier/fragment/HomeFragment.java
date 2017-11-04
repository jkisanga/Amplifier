package katson.com.amplifier.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import katson.com.amplifier.R;
import katson.com.amplifier.adapter.HomeAdapter;
import katson.com.amplifier.app.Constatnts;
import katson.com.amplifier.pojo.HomeVideo;


public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    List<HomeVideo> displaylistArray = new ArrayList<>();
    private RecyclerView mVideoRecyclerView;
    private HomeAdapter mVideoAdapter;
    Context context;
    String loadMsg;
    String loadTitle;
    FloatingActionButton changeChannel;

   // private AdView mAdView;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity().getApplicationContext();
        View thisScreensView = inflater.inflate(R.layout.fragment_home, container, false);
        loadMsg = "Loading videos...";


//        mAdView = (AdView) thisScreensView.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        int cornerRadius = 5;
        mVideoRecyclerView = (RecyclerView) thisScreensView.findViewById(R.id.yt_recycler_view);
       // changeChannel = (FloatingActionButton) thisScreensView.findViewById(R.id.fab_change_channel_news);
        mVideoAdapter = new HomeAdapter(displaylistArray, Constatnts.browserKey, getActivity(), cornerRadius);

        mVideoRecyclerView.setAdapter(mVideoAdapter);

        mVideoRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mVideoRecyclerView.setLayoutManager(mLayoutManager);
        mVideoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mVideoRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        new TheTask().execute();

        mVideoAdapter.notifyDataSetChanged();
        return  thisScreensView;
    }



    @Override
    public void onRefresh() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        Toast.makeText(getActivity(), "andika neno", Toast.LENGTH_SHORT).show();
                        return true; // Return true to expand action view
                    }
                });

    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        Bundle b = new Bundle();
        b.putString(Constatnts.KEY_SEARCH_QUERY, query);

       SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(b);
        //getFragmentManager().beginTransaction().replace(R.id.fragment_frame, searchFragment).commit();

        Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        return false;
    }

    private class TheTask extends AsyncTask<Void, Void, Void> {

        HomeVideo displaylist;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(loadTitle);
            dialog.setMessage(loadMsg);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
               // String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=magufuli&key=AIzaSyBOxtd6JsWAyj53EPio4CZE5YpDOLTcsUc&maxResults=40";

               //  String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + Constatnts.ayo_playlist_id + "&key=" + Constatnts.browserKey + "&maxResults=50";
                String url = "https://www.googleapis.com/youtube/v3/search?key=" + Constatnts.browserKey +  "&channelId=UCg_GB9WS6YKHilQZIJEC4kg&part=snippet,id&order=date&maxResults=50";

                String response = getUrlString(url);


                JSONObject json = new JSONObject(response.toString());

                JSONArray jsonArray = json.getJSONArray("items");


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("doInBackground", "doInBackground: " + jsonObject.getJSONObject("snippet").getString("publishedAt"));
                    String title = jsonObject.getJSONObject("snippet").getString("title");
                    String date = jsonObject.getJSONObject("snippet").getString("publishedAt");
                    String description = jsonObject.getJSONObject("snippet").getString("description");

                    String id = jsonObject.getJSONObject("id").getString("videoId");
                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    displaylist = new HomeVideo(title, thumbUrl, id,date,description, id);
                    displaylistArray.add(displaylist);
                }

//for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                    JSONObject video = jsonObject.getJSONObject("snippet").getJSONObject("resourceId");
//                    String title = jsonObject.getJSONObject("snippet").getString("title");
//                    String date = jsonObject.getJSONObject("snippet").getString("publishedAt");
//                    String description = jsonObject.getJSONObject("snippet").getString("description");
//
//                    String id = video.getString("videoId");
//                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
//                    displaylist = new HomeVideo(title, thumbUrl, id,date,description, id);
//                    displaylistArray.add(displaylist);
//                }
//

            } catch (Exception e1) {
                e1.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mVideoAdapter.notifyDataSetChanged();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

    }


    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // setHasOptionsMenu(true);

    }



}
