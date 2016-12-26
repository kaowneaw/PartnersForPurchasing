package su.ict.business59.partnersforpurchasing.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Shop;

/**
 * Created by kaowneaw on 12/25/2016.
 */

public interface ShopService {

    @GET("shop")
    Call<ListData> ShopList();
}
