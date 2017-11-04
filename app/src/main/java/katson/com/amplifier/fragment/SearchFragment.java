package katson.com.amplifier.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class SearchFragment extends Fragment {

    List<HomeVideo> displaylistArray = new ArrayList<>();
    private RecyclerView mVideoRecyclerView;
    private HomeAdapter mVideoAdapter;
    Context context;
    String loadMsg;
    String loadTitle;
    private String searchQuery;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchQuery = bundle.getString(Constatnts.KEY_SEARCH_QUERY);

            searchQuery = searchQuery.replace(' ', '&');
        }

        // Inflate the layout for this fragment
        context = getActivity().getApplicationContext();
        View thisScreensView = inflater.inflate(R.layout.fragment_search, container, false);
        loadTitle = "Loading...";
        loadMsg = "Loading videos...";

        int cornerRadius = 5;
        mVideoRecyclerView = (RecyclerView) thisScreensView.findViewById(R.id.yt_recycler_view_search);

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
                String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + searchQuery + "&key=AIzaSyBOxtd6JsWAyj53EPio4CZE5YpDOLTcsUc&maxResults=50";

               // String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + Constatnts.ayo_playlist_id + "&key=" + Constatnts.browserKey + "&maxResults=50";

                String response = getUrlString(url);




                JSONObject json = new JSONObject(response.toString());

                JSONArray jsonArray = json.getJSONArray("items");
                //  Log.d("doInBackground", "doInBackground: " + jsonArray.getJSONArray());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.getJSONObject("snippet").getString("title");
                    String date = jsonObject.getJSONObject("snippet").getString("publishedAt");
                    String description = jsonObject.getJSONObject("snippet").getString("description");
                    String id = jsonObject.getJSONObject("id").getString("videoId");


                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");

                    displaylist = new HomeVideo(title, thumbUrl, id,date,description, id);
                    displaylistArray.add(displaylist);
                }


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
    public void onDetach() {
        super.onDetach();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
