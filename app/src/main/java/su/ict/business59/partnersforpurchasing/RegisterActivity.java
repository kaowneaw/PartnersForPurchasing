package su.ict.business59.partnersforpurchasing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
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
import su.ict.business59.partnersforpurchasing.interfaces.AuthenApi;
import su.ict.business59.partnersforpurchasing.utills.FileUtils;
import su.ict.business59.partnersforpurchasing.utills.ImageUtils;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.user2)
    EditText et;
    @Bind(R.id.pass2)
    EditText et2;

    @Bind(R.id.email)
    EditText et4;
    @Bind(R.id.imgv)
    ImageView imv;
    @Bind(R.id.btregis)
    Button bt1;
    private final int PICK_IMAGE = 1;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private Uri selectedImage = null;
    private final int FILE_MAX_SIZE = 500;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        bt1.setOnClickListener(this);
        imv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==bt1){
            Toast.makeText(getApplicationContext(),et.getText().toString(),Toast.LENGTH_SHORT).show();
        }else if (view==imv){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ImageUtils imgUtil = new ImageUtils();
        Bitmap scaledBitmap = imgUtil.scaleDown(bitmap, FILE_MAX_SIZE, true);
        File fileResize = imgUtil.saveBitmapToFile(scaledBitmap);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), fileResize);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    private void signup(){

        progress = ProgressDialog.show(this, "SignUp", "Saving...", true);
        AuthenApi service = ServiceGenerator.createService(AuthenApi.class);
        // create part for file (photo, video, ...)
        MultipartBody.Part body = prepareFilePart("img", selectedImage);
        // create a map of data to pass along
        RequestBody username = createPartFromString(et.getText().toString());
        RequestBody password = createPartFromString(et2.getText().toString());
        RequestBody email = createPartFromString(et4.getText().toString());

        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("username", username);
        map.put("password", password);
        map.put("email", email);
        // finally, execute the request
        Call<ResponseBody> call = service.Signup(map, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // use multipart can't convert GSON
                    try {
                        Gson gson = new Gson();
                        String json = response.body().string();
//                        BaseEntity base = gson.fromJson(json, BaseEntity.class);
//                        if (base.isStatus()) {
//                            Toast.makeText(getApplicationContext(), "Signup Success", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        } else {
//                            Toast.makeText(getApplicationContext(), base.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                        Toast.makeText(getApplicationContext(), "Signup Success", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    try {
                        Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(getApplicationContext(), "Can't use this image", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedImage = data.getData();
            Picasso.with(getApplicationContext()).load(selectedImage).fit().centerCrop().into(imv);
        }
    }
}
