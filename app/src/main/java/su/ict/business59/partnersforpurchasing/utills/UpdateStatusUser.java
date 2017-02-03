package su.ict.business59.partnersforpurchasing.utills;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.interfaces.UserService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;

/**
 * Created by bas on 2/3/2017 AD.
 */

public class UpdateStatusUser {

    private String userId;
    private String status;
    UpdateResponse callback;

    public UpdateStatusUser(String userId, String status,UpdateResponse callback) {
        this.userId = userId;
        this.status = status;
        this.callback = callback;
    }


    public void update() {
        UserService service = ServiceGenerator.createService(UserService.class);
        Call<BaseResponse> call = service.statusOnline(this.userId, this.status);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    callback.updateCallback(response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    public interface UpdateResponse {
        void updateCallback(BaseResponse response);
    }

}
