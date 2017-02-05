package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.interfaces.AuthService;
import su.ict.business59.partnersforpurchasing.interfaces.UserService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.models.User;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UpdateStatusUser;
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
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    //facebook
    // https://www.sitepoint.com/integrating-the-facebook-api-with-android/

    // Callback registration
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };

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
        initFacebookBtn();
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void initFacebookBtn() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
//        accessTokenTracker.startTracking();
//        profileTracker.startTracking();

        // callback on click button login
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity Response ", response.toString());
                        try {
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String gender = object.getString("gender");
                            Log.v("Email = ", email);
                            Toast.makeText(getApplicationContext(), "Name " + name, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException e) {
            }
        };

        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);
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
                        UpdateStatusUser update = new UpdateStatusUser(user.getUser_id(), SHOPSHARE.ONLINE, new UpdateStatusUser.UpdateResponse() {
                            @Override
                            public void updateCallback(BaseResponse response) {
                                Toast.makeText(getApplicationContext(), "ONLINE NOW", Toast.LENGTH_SHORT).show();
                            }
                        });
                        update.update();
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

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void nextActivity(Profile profile) {
        if (profile != null) {
            Log.v("FirstName", profile.getFirstName());
            Log.v("LastName", profile.getLastName());
            Log.v("ID", profile.getId());
            Log.v("Img", profile.getProfilePictureUri(200, 200).toString());
            Log.v("Name", profile.getName());
        }
    }

    private void signUpApi() {
//        AuthService service = ServiceGenerator.createService(AuthService.class);
//        // create a map of data to pass along
//        RequestBody username = createPartFromString(et.getText().toString());
//        RequestBody password = createPartFromString(et2.getText().toString());
//        RequestBody email = createPartFromString(et4.getText().toString());
//        RequestBody role = createPartFromString("U");
//        RequestBody sex = createPartFromString(this.sex);
//        HashMap<String, RequestBody> map = new HashMap<>();
//        map.put("username", username);
//        map.put("password", password);
//        map.put("email", email);
//        map.put("role", role);
//        map.put("sex", sex);
//
//        // finally, execute the request
//        Call<ResponseBody> call = service.Signup(map, body);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }


}
