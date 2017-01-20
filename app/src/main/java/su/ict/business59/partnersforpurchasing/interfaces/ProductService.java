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


public interface ProductService {

    @Multipart
    @POST("product")
    Call<ResponseBody> addProduct(@PartMap() Map<String, RequestBody> dataStr, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3);

    @GET("product")
    Call<ListData> getProductList(@Query("shopId") String shopId);

    @GET("product")
    Call<ListData> getProductList();

    @GET("product/remove")
    Call<BaseResponse> removeProduct(@Query("id") String productId);

    @FormUrlEncoded
    @POST("product/favorite")
    Call<BaseResponse> favoriteProduct(@Field("productId") String productId, @Field("userId") String userId);

    @GET("product/favorite")
    Call<ListData> getFavoriteProduct(@Query("userId") String userId);
}
