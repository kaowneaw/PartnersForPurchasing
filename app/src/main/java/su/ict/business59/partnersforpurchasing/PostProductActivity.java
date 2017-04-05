package su.ict.business59.partnersforpurchasing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import su.ict.business59.partnersforpurchasing.adapter.SelectImageAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.PostService;
import su.ict.business59.partnersforpurchasing.interfaces.ShopService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.models.ShopClass;
import su.ict.business59.partnersforpurchasing.models.ShopRoom;
import su.ict.business59.partnersforpurchasing.models.ShopSoi;
import su.ict.business59.partnersforpurchasing.utills.FileUtils;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class PostProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    Product productObj;
    @Bind(R.id.topic_post)
    EditText topic_post;
    @Bind(R.id.desc_post)
    EditText desc_post;
    @Bind(R.id.img_product)
    ImageView img_product;
    @Bind(R.id.shop_name)
    EditText shop_name;
    @Bind(R.id.category_name)
    EditText category_name;
    @Bind(R.id.amountRequire)
    EditText amountRequire;
    @Bind(R.id.unitName)
    EditText unitName;
    @Bind(R.id.spin_class)
    Spinner spin_class;
    @Bind(R.id.spin_soi)
    Spinner spin_soi;
    @Bind(R.id.spin_room)
    Spinner spin_room;
    @Bind(R.id.btnOptionPost)
    Button btnOptionPost;
    @Bind(R.id.optionPost)
    LinearLayout optionPost;
    @Bind(R.id.shop_address)
    TextView shop_address;
    @Bind(R.id.warpSpinAddress)
    LinearLayout warpSpinAddress;
    @Bind(R.id.addMoreImg)
    TableRow addMoreImg;
    @Bind(R.id.containerImg)
    RecyclerView containerImg;
    @Bind(R.id.requireImg)
    TextView requireImg;
    @Bind(R.id.selectDate)
    Button selectDate;
    @Bind(R.id.showDate)
    TextView showDate;

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
    private boolean isShareProduct = false;
    private List<Uri> imgList;
    private SelectImageAdapter imgListadapter;
    private Calendar calendar;
    private Calendar dateSelected;
    private Calendar startDateCalendar;

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
            isShareProduct = true;
            this.productObj = bundle.getParcelable("product");
            selectedImage = Uri.EMPTY;
            initFromShareProduct();
        } else {
            isShareProduct = false;
        }
        init();
        imgList = new ArrayList<>();
        imgListadapter = new SelectImageAdapter(this, imgList);
        img_product.setOnClickListener(this);
        shop_name.setOnClickListener(this);
        category_name.setOnClickListener(this);
        spin_class.setOnItemSelectedListener(this);
        spin_soi.setOnItemSelectedListener(this);
        spin_room.setOnItemSelectedListener(this);
        btnOptionPost.setOnClickListener(this);
        addMoreImg.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        arrayAdapter = new ArrayAdapter<>(PostProductActivity.this, android.R.layout.select_dialog_singlechoice);
        containerImg.setAdapter(imgListadapter);
        containerImg.setLayoutManager(new LinearLayoutManager(this));
        calendar = Calendar.getInstance();
        startDateCalendar = Calendar.getInstance();

    }

    private void initFromShareProduct() {
        String host = getResources().getString(R.string.host);
        Picasso.with(getApplicationContext()).load(host + this.productObj.getImgList().get(0).getPimg_url()).fit().centerCrop().into(img_product);
        topic_post.setText(productObj.getProductName());
        category_name.setText(productObj.getCatName());
        this.catId = productObj.getCategoryId();
        btnOptionPost.setVisibility(View.GONE);
        optionPost.setVisibility(View.VISIBLE);
        shop_address.setText(productObj.getAddressShopString());
        addMoreImg.setVisibility(View.GONE);
        requireImg.setVisibility(View.GONE);
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
                }
            });
            builderSingle.show();
        } else if (view == category_name) {
            Intent i = new Intent(this, CategoryActivity.class);
            startActivityForResult(i, PICK_CATEGORY);
        } else if (view == btnOptionPost) {
            if (optionPost.getVisibility() == View.VISIBLE) {
                optionPost.setVisibility(View.GONE);
            } else {
                optionPost.setVisibility(View.VISIBLE);
            }
        } else if (view == addMoreImg) {
            //Intent intent = new Intent();
            //intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent,0);
//            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(pickPhoto , 1);
            new AlertDialog.Builder(this)
                    .setTitle("เพิ่มรูปภาพโดย")
                    //.setMessage("เพิ่มรูปภาพโดย")
                    .setNegativeButton("กล้อง'" +
                            "", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 5);
                        }
                    })
                    .setPositiveButton("แกลอรี่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                        }
                    })
                    .setIcon(R.drawable.pics)
                    .show();

        } else if (view == selectDate) {
            DatePickerDialog dateDialog = new DatePickerDialog(PostProductActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dateDialog.getDatePicker().setMinDate(startDateCalendar.getTime().getTime());
            dateDialog.show();
        }
    }

    private void init() {
        if (isShareProduct) {
            warpSpinAddress.setVisibility(View.GONE);
        } else {
            warpSpinAddress.setVisibility(View.VISIBLE);
        }
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

            if (imgList.size() < 3) {
                imgList.add(data.getData());
                imgListadapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "เพิ่มไฟล์ได้สูงสุด 3 รูป", Toast.LENGTH_SHORT).show();
            }
//            if (data == null) {
//                //Display an error
//                Toast.makeText(getApplicationContext(), "Can't use this image", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            selectedImage = data.getData();
//            Picasso.with(getApplicationContext()).load(selectedImage).fit().centerCrop().into(img_product);
        } else if (requestCode == PICK_CATEGORY && resultCode == Activity.RESULT_OK) {
            this.catId = data.getStringExtra("catId");
            String catName = data.getStringExtra("catName");
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
            RequestBody img_url = null, img_url2, img_url3;
            img_url = createPartFromString("");// send blank value
            img_url2 = createPartFromString("");// send blank value
            img_url3 = createPartFromString("");// send blank value
            MultipartBody.Part file = null, file2 = null, file3 = null;
            if (productObj != null) {
                for (int i = 0; i < productObj.getImgList().size(); i++) {
                    if (i == 0) {
                        img_url = createPartFromString(productObj.getImgList().get(0).getPimg_url());
                    } else if (i == 1) {
                        img_url2 = createPartFromString(productObj.getImgList().get(1).getPimg_url());
                    } else if (i == 2) {
                        img_url3 = createPartFromString(productObj.getImgList().get(2).getPimg_url());
                    }
                }

            } else {
                for (int i = 0; i < imgList.size(); i++) {
                    if (i == 0) {
                        file = prepareFilePart("img", imgList.get(0));
                    } else if (i == 1) {
                        file2 = prepareFilePart("img2", imgList.get(1));
                    } else if (i == 2) {
                        file3 = prepareFilePart("img3", imgList.get(2));
                    }
                }
            }

            RequestBody category_id = createPartFromString(catId);
            RequestBody user_id = createPartFromString(pref.getUserObject().getUser_id());
            RequestBody post_name = createPartFromString(topic_post.getText().toString());
            RequestBody post_desc = createPartFromString(desc_post.getText().toString());
            RequestBody shopName = createPartFromString(shop_name.getText().toString());
            RequestBody amount_require = createPartFromString(amountRequire.getText().toString());
            RequestBody unit_name_require = createPartFromString(unitName.getText().toString());
            RequestBody shop_class, shop_soi, shop_room, promotionId, promotion_desc;
            if (spin_class.getSelectedItemPosition() != -1) {
                shop_class = createPartFromString(listClass.get(spin_class.getSelectedItemPosition()).getClass_name());
                if (listClass.get(spin_class.getSelectedItemPosition()).getClass_id().equals("-1")) {
                    shop_class = createPartFromString("");
                }
            } else {
                shop_class = createPartFromString("");
            }
            if (spin_soi.getSelectedItemPosition() != -1) {
                shop_soi = createPartFromString(listSoi.get(spin_soi.getSelectedItemPosition()).toString());
                if (listSoi.get(spin_soi.getSelectedItemPosition()).getSoi_id().equals("-1")) {
                    shop_soi = createPartFromString("");
                }
            } else {
                shop_soi = createPartFromString("");
            }
            if (spin_room.getSelectedItemPosition() != -1) {
                shop_room = createPartFromString(listRoom.get(spin_room.getSelectedItemPosition()).getRoom_no());
                if (listRoom.get(spin_room.getSelectedItemPosition()).getRoom_id().equals("-1")) {
                    shop_room = createPartFromString("");
                }
            } else {
                shop_room = createPartFromString("");
            }
            promotionId = createPartFromString("0");
            promotion_desc = createPartFromString("");
            if (isShareProduct) {
                shop_class = createPartFromString(productObj.getClass_name());
                shop_soi = createPartFromString(productObj.getSoi_zone() + " " + productObj.getSoi_name());
                shop_room = createPartFromString(productObj.getRoom_no());
                promotionId = createPartFromString(productObj.getPromotion_id());
                promotion_desc = createPartFromString(productObj.getPromotion_desc());
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
            map.put("img_url2", img_url2);
            map.put("img_url3", img_url3);
            map.put("promotion_id", promotionId);
            map.put("promotion_desc", promotion_desc);
            map.put("amount_require", amount_require);
            map.put("unit_require", unit_name_require);

            final PostProductActivity activity = this;
            PostService service = ServiceGenerator.createService(PostService.class);
            Call<ResponseBody> call = service.postProduct(map, file, file2, file3);
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
        return (imgList.size() > 0 || isShareProduct) && !topic_post.getText().toString().equals("") && !catId.equals("") && !unitName.toString().equals("") && !amountRequire.toString().equals("");
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
//        File file = FileUtils.getFile(this, fileUri);
//        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        ImageUtils imgUtil = new ImageUtils();
//        Bitmap scaledBitmap = imgUtil.scaleDown(bitmap, FILE_MAX_SIZE, true);
//        File fileResize = imgUtil.saveBitmapToFile(scaledBitmap);
//        // create RequestBody instance from file
//        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), fileResize);

        File file = FileUtils.getFile(this, fileUri);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

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
                        ShopSoi defaultObj = new ShopSoi("-1", "กรุณาเลือกซอย", "", "");
                        listSoi = data.getItemsShopSoi();
                        listSoi.add(0, defaultObj);
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
                            ShopRoom defaultObj = new ShopRoom("-1", "กรุณาเลือกห้อง", "");
                            listRoom = data.getItemsShopRoom();
                            listRoom.add(0, defaultObj);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        showDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        dateSelected = Calendar.getInstance();
        dateSelected.set(year, month, dayOfMonth);
    }
}
