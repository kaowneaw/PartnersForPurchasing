package su.ict.business59.partnersforpurchasing.interfaces;

import java.util.List;
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
import retrofit2.http.Query;
import su.ict.business59.partnersforpurchasing.models.ListData;

/**
 * Created by kaowneaw on 11/29/2016.
 */

public interface ProductService {

    @Multipart
    @POST("product")
    Call<ResponseBody> addProduct(@PartMap() Map<String, RequestBody> dataStr, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3);

    @GET("product")
    Call<ListData> getProductList(@Query("shopId") String shopId);
}
