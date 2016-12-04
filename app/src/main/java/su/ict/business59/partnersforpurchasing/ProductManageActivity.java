package su.ict.business59.partnersforpurchasing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import su.ict.business59.partnersforpurchasing.interfaces.CategoryService;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.Category;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.utills.FileUtils;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;

public class ProductManageActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.spin_category)
    Spinner spin_category;
    @Bind(R.id.addMoreImg)
    TableRow addMoreImg;
    @Bind(R.id.containerImg)
    RecyclerView containerImg;
    @Bind(R.id.edt_product_name)
    EditText edt_product_name;
    @Bind(R.id.edt_product_desc)
    EditText edt_product_desc;
    @Bind(R.id.edt_product_price)
    EditText edt_product_price;
    private final int PICK_IMAGE = 1;
    private List<Uri> imgList;
    private SelectImageAdapter adapter;
    private List<Category> categorys = new ArrayList<>();
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.new_product));
        addMoreImg.setOnClickListener(this);
        init();
    }

    private void init() {
        imgList = new ArrayList<>();
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

        adapter = new SelectImageAdapter(this, imgList);
        containerImg.setAdapter(adapter);
        containerImg.setLayoutManager(new LinearLayoutManager(this));
    }

    public void populateSpinner(Context context, List<Category> categorys, Spinner spinner) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(context, R.layout.spinner_item, categorys);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
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
            saveProduct();
        }
        return super.onOptionsItemSelected(item);
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
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void saveProduct() {
        progress = ProgressDialog.show(this, "", "Saving...", true);
        MultipartBody.Part file1 = null, file2 = null, file3 = null;
        for (int i = 0; i < imgList.size(); i++) {
            if (i == 0) {
                file1 = prepareFilePart("img1", imgList.get(0));
            } else if (i == 1) {
                file2 = prepareFilePart("img2", imgList.get(1));
            } else if (i == 2) {
                file3 = prepareFilePart("img3", imgList.get(2));
            }
        }
        // create a map of data to pass along
        RequestBody productName = createPartFromString(edt_product_name.getText().toString());
        RequestBody productDesc = createPartFromString(edt_product_desc.getText().toString());
        RequestBody productPrice = createPartFromString(edt_product_price.getText().toString());
        RequestBody catId = createPartFromString(categorys.get(spin_category.getSelectedItemPosition()).getCategory_id());
        RequestBody shopId = createPartFromString("1");

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("product_name", productName);
        map.put("product_desc", productDesc);
        map.put("product_price", productPrice);
        map.put("category_id", catId);
        map.put("shop_id", shopId);
        final Activity mActivity = this;
        ProductService service = ServiceGenerator.createService(ProductService.class);
        Call<ResponseBody> call = service.addProduct(map, file1, file2, file3);
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
                            // signUp success
                            Toast.makeText(getApplicationContext(), "Save Success", Toast.LENGTH_SHORT).show();
                            mActivity.finish();
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

    @Override
    public void onClick(View view) {
        if (view == addMoreImg) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(getApplicationContext(), "Can't use this image", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imgList.size() < 3) {
                imgList.add(data.getData());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Limit File Max at 3 Item", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
