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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
import su.ict.business59.partnersforpurchasing.models.ShopClass;
import su.ict.business59.partnersforpurchasing.models.ShopRoom;
import su.ict.business59.partnersforpurchasing.models.ShopSoi;
import su.ict.business59.partnersforpurchasing.utills.FileUtils;
import su.ict.business59.partnersforpurchasing.utills.ImageUtils;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class PostProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Product productObj;
    @Bind(R.id.topic_post)
    EditText topic_post;
    @Bind(R.id.desc_post)
    EditText desc_post;
    @Bind(R.id.is_share_product)
    LinearLayout is_share_product;
    @Bind(R.id.is_create_product)
    LinearLayout is_create_product;
    @Bind(R.id.img_product)
    ImageView img_product;
    @Bind(R.id.shop_name)
    EditText shop_name;
    @Bind(R.id.category_btn)
    Button category_btn;
    @Bind(R.id.category_name)
    TextView category_name;
    @Bind(R.id.spin_class)
    Spinner spin_class;
    @Bind(R.id.spin_soi)
    Spinner spin_soi;
    @Bind(R.id.spin_room)
    Spinner spin_room;

    private List<ShopSoi> listSoi;
    private List<ShopClass> listClass;
    private List<ShopRoom> listRoom;
    private List<Shop> shops = new ArrayList<>();
    private final int PICK_IMAGE = 1;
    private final int PICK_CATEGORY = 2;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private Uri selectedImage = null;
    private final int FILE_MAX_SIZE = 500;
    private ProgressDialog progress;
    private ArrayAdapter<String> arrayAdapter;
    private String catId = "";
    private ShopService shopService;
    private UserPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        setTitle("โพสต์");
        pref = new UserPreference(getApplicationContext());
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.productObj = bundle.getParcelable("product");
            selectedImage = Uri.EMPTY;
            initFromShareProduct();
        }
        init();
        img_product.setOnClickListener(this);
        shop_name.setOnClickListener(this);
        category_btn.setOnClickListener(this);
        spin_class.setOnItemSelectedListener(this);
        spin_soi.setOnItemSelectedListener(this);
        spin_room.setOnItemSelectedListener(this);
        arrayAdapter = new ArrayAdapter<>(PostProductActivity.this, android.R.layout.select_dialog_singlechoice);
    }

    private void initFromShareProduct() {
        String host = getResources().getString(R.string.host);
        Picasso.with(getApplicationContext()).load(host + this.productObj.getImgList().get(0).getPimg_url()).fit().centerCrop().into(img_product);
        topic_post.setText(productObj.getProductName());
    }

    @Override
    public void onClick(View view) {
        if (view == img_product) {
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
                    Shop shopSelected = shops.get(which);
                    shop_name.setTag(shopSelected.getShopId() + "");
//                    shopDescShow.setText("ที่อยู่ร้านค้า \n" + "ชั้นที่ " + shopSelected.getShopClass() + " ซอยที่ " + shopSelected.getShopSoi() + " ห้องที่ " + shopSelected.getShopRoom());
                }
            });
            builderSingle.show();
        } else if (view == category_btn) {
            Intent i = new Intent(this, CategoryActivity.class);
            startActivityForResult(i, PICK_CATEGORY);
        }
    }


    private void init() {
        this.shopService = ServiceGenerator.createService(ShopService.class);
        Call<ListData> dataShop = shopService.ShopList();
        dataShop.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    ListData shopList = response.body();
                    shops = shopList.getItemsShop();
                    Shop findShop = null;
                    for (int i = 0; i < shops.size(); i++) {
                        arrayAdapter.add(shops.get(i).getShopName());
                        if (productObj != null) {
                            if (productObj.getShopId().equals(shops.get(i).getShopId())) {
                                findShop = shops.get(i);
                            }
                        }
                    }
                    if (productObj != null && findShop != null) {
                        shop_name.setText(findShop.getShopName());
                        shop_name.setTag(findShop.getShopId());
                    }

                } else {
                    Toast.makeText(getApplication(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {

            }
        });

        Call<ListData> dataShopClass = shopService.ShopListClass();
        dataShopClass.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    ListData data = response.body();
                    ShopClass defaultObj = new ShopClass("-1", "กรุณาเลือกชั้น");
                    listClass = data.getItemsShopClass();
                    listClass.add(0, defaultObj);
                    populateClassSpinner(getApplicationContext(), listClass, spin_class);
                } else {
                    Toast.makeText(getApplication(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {

            }
        });

    }

    public void populateRoomSpinner(Context context, List<ShopRoom> shopRoom, Spinner spinner) {
        ArrayAdapter<ShopRoom> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, shopRoom);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    public void populateClassSpinner(Context context, List<ShopClass> shopClass, Spinner spinner) {
        ArrayAdapter<ShopClass> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, shopClass);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    public void populateSoiSpinner(Context context, List<ShopSoi> shopSoi, Spinner spinner) {
        ArrayAdapter<ShopSoi> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, shopSoi);
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
        } else if (requestCode == PICK_CATEGORY && resultCode == Activity.RESULT_OK) {
            this.catId = data.getStringExtra("catId");
            String catName = data.getStringExtra("catName");
            Toast.makeText(getApplicationContext(), this.catId, Toast.LENGTH_SHORT).show();
            category_name.setText(catName);
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
        } else if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void post() {
        if (validPost()) {
            progress = ProgressDialog.show(this, "", "Saving...", true);
            RequestBody img_url;
            MultipartBody.Part file = null;
            if (productObj != null) {
                img_url = createPartFromString(productObj.getImgList().get(0).getPimg_url());
            } else {
                img_url = createPartFromString("");// send blank value
                file = prepareFilePart("img", selectedImage);
            }
            RequestBody category_id = createPartFromString(catId);
            RequestBody user_id = createPartFromString(pref.getUserObject().getUser_id());
            RequestBody post_name = createPartFromString(topic_post.getText().toString());
            RequestBody post_desc = createPartFromString(desc_post.getText().toString());
            RequestBody shopName = createPartFromString(shop_name.getText().toString());
            RequestBody shop_class, shop_soi, shop_room;
            if (spin_class.getSelectedItemPosition() != -1) {
                shop_class = createPartFromString(listClass.get(spin_class.getSelectedItemPosition()).getClass_name());
            } else {
                shop_class = createPartFromString("");
            }
            if (spin_soi.getSelectedItemPosition() != -1) {
                shop_soi = createPartFromString(listSoi.get(spin_soi.getSelectedItemPosition()).toString());
            } else {
                shop_soi = createPartFromString("");
            }
            if (spin_room.getSelectedItemPosition() != -1) {
                shop_room = createPartFromString(listRoom.get(spin_room.getSelectedItemPosition()).getRoom_no());
            } else {
                shop_room = createPartFromString("");
            }

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("post_name", post_name);
            map.put("post_desc", post_desc);
            map.put("shop_name", shopName);
            map.put("shop_class", shop_class);
            map.put("shop_soi", shop_soi);
            map.put("shop_room", shop_room);
            map.put("category_id", category_id);
            map.put("user_id", user_id);
            map.put("img_url", img_url);
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
                    Log.v("onFailure", t.getMessage());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Plese Fill in the blank", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validPost() {
        return selectedImage != null && !topic_post.getText().toString().equals("") && !catId.equals("");
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spin_class) {
            Call<ListData> dataShopSoi = shopService.ShopListSoiByClassId(listClass.get(i).getClass_id());
            dataShopSoi.enqueue(new Callback<ListData>() {
                @Override
                public void onResponse(Call<ListData> call, Response<ListData> response) {
                    if (response.isSuccessful()) {
                        ListData data = response.body();
                        listSoi = data.getItemsShopSoi();
                        populateSoiSpinner(getApplicationContext(), listSoi, spin_soi);
                    } else {
                        Toast.makeText(getApplication(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ListData> call, Throwable t) {

                }
            });
        } else if (adapterView.getId() == R.id.spin_soi) {
            if (i < listSoi.size()) {
                Call<ListData> dataShopRoom = shopService.ShopListRoomBySoiId(listSoi.get(i).getSoi_id());
                dataShopRoom.enqueue(new Callback<ListData>() {
                    @Override
                    public void onResponse(Call<ListData> call, Response<ListData> response) {
                        if (response.isSuccessful()) {
                            ListData data = response.body();
                            listRoom = data.getItemsShopRoom();
                            populateRoomSpinner(getApplicationContext(), listRoom, spin_room);
                        } else {
                            Toast.makeText(getApplication(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ListData> call, Throwable t) {

                    }
                });
            } else {
                listRoom = new ArrayList<>();
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
