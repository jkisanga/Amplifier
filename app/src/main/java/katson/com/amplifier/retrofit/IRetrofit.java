package katson.com.amplifier.retrofit;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;
import katson.com.amplifier.pojo.Channel;

/**
 * Created by user on 5/27/2017.
 */

public interface IRetrofit {

    @GET("channels")
    Call<List<Channel>> getChannels();

    @GET("twitters")
    Call<List<Channel>> getTweeter();

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
