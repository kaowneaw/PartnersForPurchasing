package su.ict.business59.partnersforpurchasing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.interfaces.CategoryService;
import su.ict.business59.partnersforpurchasing.interfaces.PostService;
import su.ict.business59.partnersforpurchasing.interfaces.ShopService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.Category;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.FileUtils;
import su.ict.business59.partnersforpurchasing.utills.ImageUtils;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class PostProductActivity extends AppCompatActivity implements View.OnClickListener {
    Product productObj;
    @Bind(R.id.topic_post)
    EditText topic_post;
    @Bind(R.id.desc_post)
    EditText desc_post;
    @Bind(R.id.is_share_product)
    LinearLayout is_share_product;
    @Bind(R.id.is_create_product)
    LinearLayout is_create_product;
    @Bind(R.id.dateEnd)
    EditText dateEnd;
    @Bind(R.id.timeEnd)
    EditText timeEnd;
    @Bind(R.id.spin_category)
    Spinner spin_category;
    @Bind(R.id.img_product)
    ImageView img_product;
    @Bind(R.id.shop_name)
    EditText shop_name;
    TimePickerDialog mTimePicker;
    DatePickerDialog mDatePickerDialog;
    Calendar myCalendar = Calendar.getInstance();
    private List<Category> categorys = new ArrayList<>();
    private List<Shop> shops = new ArrayList<>();
    private final int PICK_IMAGE = 1;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private Uri selectedImage = null;
    private final int FILE_MAX_SIZE = 500;
    ProgressDialog progress;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.productObj = bundle.getParcelable("product");
        }
        setTitle("Post");
        init();
        dateEnd.setOnClickListener(this);
        timeEnd.setOnClickListener(this);
        img_product.setOnClickListener(this);
        shop_name.setOnClickListener(this);
        arrayAdapter = new ArrayAdapter<>(PostProductActivity.this, android.R.layout.select_dialog_singlechoice);
    }

    @Override
    public void onClick(View view) {
        if (view == dateEnd) {
            mDatePickerDialog = new DatePickerDialog(PostProductActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setDateLabel(dateEnd);
                }
            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.show();
        } else if (view == timeEnd) {
            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = myCalendar.get(Calendar.MINUTE);
            mTimePicker = new TimePickerDialog(PostProductActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    myCalendar.set(Calendar.HOUR, selectedHour);
                    myCalendar.set(Calendar.MINUTE, selectedMinute);
                    setTimeLabel(timeEnd);
                }
            }, hour, minute, true);//Yes 24 hour time

            mTimePicker.show();
        } else if (view == img_product) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        } else if (view == shop_name) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(PostProductActivity.this);
            builderSingle.setTitle("Select Shop");
            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    shop_name.setText(strName);
                    shop_name.setTag(shops.get(which).getShopId() + "");
                }
            });
            builderSingle.show();
        }
    }

    private void setDateLabel(EditText v) {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        v.setText(sdf.format(myCalendar.getTime()));
    }

    private void setTimeLabel(EditText v) {

        String myFormat = "hh:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        v.setText(sdf.format(myCalendar.getTime()));
    }

    private void init() {
        CategoryService service = ServiceGenerator.createService(CategoryService.class);
        Call<ListData> datas = service.CategoryList();
        datas.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    ListData categoryList = response.body();
                    categorys = categoryList.getItemsCategory();
                    populateSpinner(getApplicationContext(), categorys, spin_category);
                } else {
                    Toast.makeText(getApplication(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {

            }
        });

        ShopService shopService = ServiceGenerator.createService(ShopService.class);
        Call<ListData> dataShop = shopService.ShopList();
        dataShop.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    ListData shopList = response.body();
                    shops = shopList.getItemsShop();
                    for (int i = 0; i < shops.size(); i++) {
                        arrayAdapter.add(shops.get(i).getShopName());
                    }

                } else {
                    Toast.makeText(getApplication(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {

            }
        });
    }

    public void populateSpinner(Context context, List<Category> categorys, Spinner spinner) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(context, R.layout.spinner_item, categorys);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
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
            Picasso.with(getApplicationContext()).load(selectedImage).fit().centerCrop().into(img_product);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            post();
        }
        return super.onOptionsItemSelected(item);
    }

    private void post() {
        String myFormat = "yyyy-MM-dd hh:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        UserPreference pref = new UserPreference(this);
        progress = ProgressDialog.show(this, "", "Saving...", true);
        MultipartBody.Part file = prepareFilePart("img", selectedImage);
        RequestBody post_name = createPartFromString(topic_post.getText().toString());
        RequestBody post_desc = createPartFromString(desc_post.getText().toString());
        RequestBody post_end = createPartFromString(sdf.format(myCalendar.getTime()));
        RequestBody shop_id = createPartFromString(shop_name.getTag().toString());
        RequestBody category_id = createPartFromString(categorys.get(spin_category.getSelectedItemPosition()).getCategory_id());
        RequestBody user_id = createPartFromString(pref.getUserID());
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("post_name", post_name);
        map.put("post_desc", post_desc);
        map.put("post_end", post_end);
        map.put("shop_id", shop_id);
        map.put("category_id", category_id);
        map.put("user_id", user_id);
        final PostProductActivity activity = this;
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<ResponseBody> call = service.postProduct(map, file);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        String json = response.body().string();
                        Log.e("JSON ", json);
                        BaseResponse base = gson.fromJson(json, BaseResponse.class);
                        if (base.isStatus()) {
                            // post success
                            activity.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), base.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
}
