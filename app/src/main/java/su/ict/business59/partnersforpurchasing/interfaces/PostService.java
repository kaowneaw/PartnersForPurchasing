package su.ict.business59.partnersforpurchasing.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.ListData;

/**
 * Created by kaowneaw on 12/25/2016.
 */

public interface PostService {

    @Multipart
    @POST("post")
    Call<ResponseBody> postProduct(@PartMap() Map<String, RequestBody> dataStr, @Part MultipartBody.Part file, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3);

    @GET("post")
    Call<ListData> getPostList();

    @GET("post")
    Call<ListData> getPostList(@Query("user_id") String userId);

    @GET("post")
    Call<ListData> getPostListByCategory(@Query("cat_id") String catId);

    @GET("post/status")
    Call<BaseResponse> postStatus(@Query("post_id") String postId, @Query("post_status") String postStatus);

    @GET("post/view")
    Call<BaseResponse> postUpdateView(@Query("post_id") String postId, @Query("view") int view);

    @GET("list/post/joined")
    Call<ListData> getPostJoined(@Query("user_id") String userId);

    @FormUrlEncoded
    @POST("joinpost")
    Call<BaseResponse> joinPost(@Field("user_id") String userId, @Field("post_id") String postId, @Field("amount") int amount, @Field("token") String token, @Field("isSuccess") String isSuccess, @Field("joinedName") String joinedName);

    @FormUrlEncoded
    @POST("post/search")
    Call<ListData> searchPost(@Field("keyword") String keyword);
}
