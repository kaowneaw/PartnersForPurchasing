package su.ict.business59.partnersforpurchasing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.adapter.SelectImageAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.CategoryService;
import su.ict.business59.partnersforpurchasing.models.Category;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;

public class ProductManageActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.spin_category)
    Spinner spin_category;
    @Bind(R.id.addMoreImg)
    TableRow addMoreImg;
    @Bind(R.id.containerImg)
    RecyclerView containerImg;
    private final int PICK_IMAGE = 1;
    private List<Uri> imgList;
    private SelectImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
        ButterKnife.bind(this);
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
                    List<Category> categorys = categoryList.getItemsCategory();
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

            imgList.add(data.getData());
            adapter.notifyDataSetChanged();
        }
    }


}
