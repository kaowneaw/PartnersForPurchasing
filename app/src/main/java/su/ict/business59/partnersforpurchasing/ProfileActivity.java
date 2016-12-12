package su.ict.business59.partnersforpurchasing;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.interfaces.UserService;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.profile_username)
    EditText profile_username;
    @Bind(R.id.profile_email)
    EditText profile_email;
    @Bind(R.id.profile_img)
    ImageView profile_img;
    @Bind(R.id.info_shop)
    LinearLayout info_shop;
    @Bind(R.id.shop_name)
    EditText shop_name;
    @Bind(R.id.shop_desc)
    EditText shop_desc;
    @Bind(R.id.shop_class)
    EditText shop_class;
    @Bind(R.id.shop_soi)
    EditText shop_soi;
    @Bind(R.id.shop_room)
    EditText shop_room;
    @Bind(R.id.shop_promotion)
    EditText shop_promotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setTitle("Profile");
        init();
    }

    private void init() {
        UserService service = ServiceGenerator.createService(UserService.class);
        UserPreference pref = new UserPreference(this);
        Call<Shop> call = service.me(pref.getUserID());
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getUsername(), Toast.LENGTH_SHORT).show();
                    Shop myShop = response.body();
                    if (myShop.getRole().equals("U")) {
                        info_shop.setVisibility(View.GONE);
                    } else {
                        shop_name.setText(myShop.getShopName());
                        shop_desc.setText(myShop.getShopDesc());
                    }
                } else {
                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

            }
        });
        profile_username.setText(pref.getUsername());
        profile_email.setText(pref.getEmail());
        String host = getResources().getString(R.string.host);
        Picasso.with(getApplicationContext()).load(host + pref.getImg()).fit().centerCrop().into(profile_img);
    }

}
