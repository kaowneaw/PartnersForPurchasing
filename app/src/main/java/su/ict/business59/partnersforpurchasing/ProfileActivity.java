package su.ict.business59.partnersforpurchasing;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.adapter.TabAdapter;
import su.ict.business59.partnersforpurchasing.adapter.TabProfileAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.UserService;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.profile_username)
    TextView profile_username;
    @Bind(R.id.profile_email)
    TextView profile_email;
    @Bind(R.id.profile_img)
    ImageView profile_img;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.shop_info)
    Button shop_info;
    private Shop CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setTitle("Profile");
        init();
    }

    private void init() {
        UserPreference pref = new UserPreference(this);
        CurrentUser = pref.getUserObject();
        viewPager.setAdapter(new TabProfileAdapter(getSupportFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        profile_username.setText(CurrentUser.getUsername());
        profile_email.setText(CurrentUser.getEmail());
        String host = getResources().getString(R.string.host);
        Picasso.with(getApplicationContext()).load(host + CurrentUser.getImage_url()).fit().centerCrop().into(profile_img);
    }

}
