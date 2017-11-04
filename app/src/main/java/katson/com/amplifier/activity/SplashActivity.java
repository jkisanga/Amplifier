package katson.com.amplifier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import katson.com.amplifier.R;
import katson.com.amplifier.app.Config;
import katson.com.amplifier.pojo.Channel;
import katson.com.amplifier.realm.RealmController;
import katson.com.amplifier.retrofit.IRetrofit;
import katson.com.amplifier.table.RChannel;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class SplashActivity extends AppCompatActivity {
    private Realm realm;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.image_logo);
        textView = (TextView) findViewById(R.id.txt_app);
        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.tz);
        imageView.startAnimation(animation_1);
        textView.startAnimation(animation_2);

        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            startActivity(new Intent(SplashActivity.this, TabActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //get realm instance
        this.realm = RealmController.with(this).getRealm();
       // getChannelsFromSeverToDatabase();
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

            Call<List<Channel>> call = service.getChannels();
            call.enqueue(new Callback<List<Channel>>() {
                @Override
                public void onResponse(Response<List<Channel>> response, Retrofit retrofit) {


                    if(response.isSuccess()) {

                        List<Channel>   channels = response.body();
                        for(int i = 0; i < channels.size(); i++){

                            RChannel rChannel = new RChannel();
                            rChannel.setId(channels.get(i).getId());
                           rChannel.setTitle(channels.get(i).getTitle());
                            rChannel.setDesc(channels.get(i).getDesc());
                            rChannel.setThumbnail(channels.get(i).getThumbnail());
                            rChannel.setUrl(channels.get(i).getUrl());


                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(rChannel);
                            realm.commitTransaction();

                            String pathLinkImage = channels.get(i).getThumbnail();
                            Log.d("testing", "onResponse: " + pathLinkImage);
                            //download thumbnail and save to local
                            getThumbnailFromServer(pathLinkImage);

                        }


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

    void getThumbnailFromServer(final String path) {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRetrofit service = retrofit.create(IRetrofit.class);

        Call<ResponseBody> call = service.downloadFileWithDynamicUrlSync("document/" + path);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

                try {
                    if(response.isSuccess()) {

                        boolean FileDownloaded = writeResponseBodyToDisk(response.body(), path);


                    }else {

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {

            // todo change the file location/name according to your needs


            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("file-download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();



                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
