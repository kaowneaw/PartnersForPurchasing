package su.ict.business59.partnersforpurchasing.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import su.ict.business59.partnersforpurchasing.models.ListData;

/**
 * Created by kaowneaw on 11/26/2016.
 */

public interface CategoryService {

    @GET("category")
    Call<ListData> CategoryList(@Query("parentId") String parentId);

}
