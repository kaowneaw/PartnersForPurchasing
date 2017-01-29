package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.interfaces.AuthService;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.models.User;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.user)
    EditText et;
    @Bind(R.id.pass)
    EditText et2;
    @Bind(R.id.btsignin)
    Button bt1;
    @Bind(R.id.btsignup)
    Button bt2;
    private UserPreference pref;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        pref = new UserPreference(this);
        if (pref.getUserObject() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            this.finish();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bt1) {
            signIn();
        } else if (view == bt2) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void signIn() {
        String username = et.getText().toString();
        String password = et2.getText().toString();
        if (!username.equals("") && !password.equals("")) {
            AuthService service = ServiceGenerator.createService(AuthService.class);
            Call<Shop> call = service.login(username, password);
            call.enqueue(new Callback<Shop>() {
                @Override
                public void onResponse(Call<Shop> call, Response<Shop> response) {
                    Shop user = response.body();
                    if (user.isStatus()) {
                        pref.setUserObject(user);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Shop> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Fill in the blank", Toast.LENGTH_SHORT).show();
        }
    }
}
