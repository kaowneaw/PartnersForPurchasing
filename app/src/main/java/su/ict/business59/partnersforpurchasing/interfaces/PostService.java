package su.ict.business59.partnersforpurchasing.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import su.ict.business59.partnersforpurchasing.models.ListData;

/**
 * Created by kaowneaw on 12/25/2016.
 */

public interface PostService {

    @Multipart
    @POST("post")
    Call<ResponseBody> postProduct(@PartMap() Map<String, RequestBody> dataStr, @Part MultipartBody.Part file1);

    @GET("post")
    Call<ListData> getPostList();
}
