package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private FacebookCallback<LoginResult> callback;
    private ProfileTracker profileTracker;
    //facebook
    // https://www.sitepoint.com/integrating-the-facebook-api-with-android/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getKeyHash();
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

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("su.ict.business59.partnersforpurchasing", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void initFacebookBtn() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        // callback on click button login
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                final Profile profile = Profile.getCurrentProfile();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivityResponse", response.toString());
                        try {
                            String fbId = object.getString("id");
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String gender = object.getString("gender");
                            String imgUrl = Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString();
                            loginFacebook(fbId, name, email, gender, imgUrl); // insert if not have data this user
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "ข้อมูล Facebook ผิดพลาด", Toast.LENGTH_SHORT).show();
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
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.v("profile", "PROFILE");
            }
        };
        profileTracker.startTracking();
        List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");
        loginButton.setReadPermissions(permissions);
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
//                        UpdateStatusUser update = new UpdateStatusUser(user.getUser_id(), SHOPSHARE.ONLINE, new UpdateStatusUser.UpdateResponse() {
//                            @Override
//                            public void updateCallback(BaseResponse response) {
//                                Toast.makeText(getApplicationContext(), "ONLINE NOW", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        update.update();
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


    private void loginFacebook(String fbId, String username, String email, String gender, String imgUrl) {
        AuthService service = ServiceGenerator.createService(AuthService.class);
        // create a map of data to pass along
        RequestBody fbIdBody = createPartFromString(fbId);
        RequestBody usernameBody = createPartFromString(username);
        RequestBody emailBody = createPartFromString(email);
        RequestBody sex = createPartFromString(gender.toUpperCase().charAt(0) + "");
        RequestBody imgBody = createPartFromString(imgUrl);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("fb_id", fbIdBody);
        map.put("username", usernameBody);
        map.put("email", emailBody);
        map.put("sex", sex);
        map.put("img_url", imgBody);

        // finally, execute the request
        Call<Shop> call = service.loginFacebook(map);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                Shop user = response.body();
                if (user.isStatus()) {
                    pref.setUserObject(user);
//                    UpdateStatusUser update = new UpdateStatusUser(user.getUser_id(), SHOPSHARE.ONLINE, new UpdateStatusUser.UpdateResponse() {
//                        @Override
//                        public void updateCallback(BaseResponse response) {
//                            Toast.makeText(getApplicationContext(), "ONLINE NOW", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    update.update();
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
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        profileTracker.stopTracking();
    }


    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }


}
