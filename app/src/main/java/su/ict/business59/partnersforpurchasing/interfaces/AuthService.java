package su.ict.business59.partnersforpurchasing.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import su.ict.business59.partnersforpurchasing.models.User;

/**
 * Created by Lenovo on 19/11/2559.
 */

public interface AuthService {

    @FormUrlEncoded
    @POST("login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @Multipart
    @POST("signup")
    Call<ResponseBody> Signup(@PartMap() Map<String, RequestBody> datastr, @Part MultipartBody.Part file);
}