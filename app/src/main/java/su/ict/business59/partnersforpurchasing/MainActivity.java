package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.interfaces.AuthenApi;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPreference pref = new UserPreference(this);
        if (!pref.getUserID().equals("NULL")) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            this.finish();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View view) {
        if (view == bt1) {
            signin();
        } else if (view == bt2) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

    }

    private void signin() {
        String username = et.getText().toString();
        String password = et2.getText().toString();
        if (!username.equals("") && !password.equals("")) {
            AuthenApi service = ServiceGenerator.createService(AuthenApi.class);
            Call<User> call = service.login(username, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if (user.isStatus()) {
                        UserPreference pref = new UserPreference(getApplicationContext(), user.getUser_id(), user.getUsername(), user.getEmail(), user.getImage_url(), user.getRole());
                        pref.commit();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Fill in the blank", Toast.LENGTH_SHORT).show();
        }
    }
}
