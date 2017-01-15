package su.ict.business59.partnersforpurchasing.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import su.ict.business59.partnersforpurchasing.models.ListData;

/**
 * Created by kaowneaw on 1/15/2017.
 */

public interface PromotionService {
    @GET("promotion")
    Call<ListData> getPromotion();
}
