package su.ict.business59.partnersforpurchasing.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import su.ict.business59.partnersforpurchasing.models.Shop;

/**
 * Created by kaowneaw on 12/11/2016.
 */

public interface UserService {

    @GET("me")
    Call<Shop> me(@Query("userId") String userId);
}
