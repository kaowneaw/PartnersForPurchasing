package su.ict.business59.partnersforpurchasing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import su.ict.business59.partnersforpurchasing.interfaces.CategoryService;
import su.ict.business59.partnersforpurchasing.models.Category;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;

public class CategoryActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private List<Category> categorys = new ArrayList<>();
    private List<Category> categorys1 = new ArrayList<>();
    private List<Category> categorys2 = new ArrayList<>();
    @Bind(R.id.radioGroupSex)
    RadioGroup radioGroupSex;
    @Bind(R.id.radioMale)
    RadioButton radioMale;
    @Bind(R.id.radioFemale)
    RadioButton radioFemale;
    @Bind(R.id.catlvl1)
    Spinner catlvl1;
    @Bind(R.id.catlvl2)
    Spinner catlvl2;
    @Bind(R.id.catlvl3)
    Spinner catlvl3;
    @Bind(R.id.warpSpin1)
    TableRow warpSpin1;
    @Bind(R.id.warpSpin2)
    TableRow warpSpin2;
    @Bind(R.id.warpSpin3)
    TableRow warpSpin3;
    ProgressDialog loading = null;
    @Bind(R.id.ok_btn)
    Button ok_btn;
    Category catDefalt = new Category();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        setTitle("ค้นหาประเภทสินค้า");
        catDefalt.setCat_id("-99999");
        catDefalt.setCat_name("ไม่มี");
        radioGroupSex.setOnCheckedChangeListener(this);
        ok_btn.setOnClickListener(this);
        catlvl1.setOnItemSelectedListener(this);
        catlvl2.setOnItemSelectedListener(this);
        catlvl3.setOnItemSelectedListener(this);
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage("Loading");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        callCategory(SHOPSHARE.MALE_ID, catlvl1);

    }

    private void callCategory(String parentId, final Spinner catSpin) {
        loading.show();
        CategoryService service = ServiceGenerator.createService(CategoryService.class);
        Call<ListData> call = service.CategoryList(parentId);
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    if (catSpin == catlvl1) {
                        categorys = response.body().getItemsCategory();
                        if (categorys.size() == 0) {
                            categorys.clear();
                            categorys.add(catDefalt);
                        }
                        populateSpinner(getApplicationContext(), categorys, catSpin);
                        categorys1.clear();
                        categorys1.add(catDefalt);
                        populateSpinner(getApplicationContext(), categorys1, catlvl2);
                        categorys2.clear();
                        categorys2.add(catDefalt);
                        populateSpinner(getApplicationContext(), categorys2, catlvl3);
                    } else if (catSpin == catlvl2) {
                        categorys1 = response.body().getItemsCategory();
                        if (categorys1.size() == 0) {
                            categorys1.clear();
                            categorys1.add(catDefalt);
                        }
                        populateSpinner(getApplicationContext(), categorys1, catSpin);
                        categorys2.clear();
                        categorys2.add(catDefalt);
                        populateSpinner(getApplicationContext(), categorys2, catlvl3);
                    } else if (catSpin == catlvl3) {
                        categorys2 = response.body().getItemsCategory();
                        if (categorys2.size() == 0) {
                            categorys2.clear();
                            categorys2.add(catDefalt);
                        }
                        populateSpinner(getApplicationContext(), categorys2, catSpin);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
                loading.dismiss();
            }
        });
    }

    public void populateSpinner(Context context, List<Category> categorys, Spinner spinner) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(context, R.layout.spinner_item, categorys);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioMale.isChecked()) {
            callCategory(SHOPSHARE.MALE_ID, catlvl1); // init category_id of male
        } else if (radioFemale.isChecked()) {
            callCategory(SHOPSHARE.FEMALE_ID, catlvl1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.catlvl1) {
            callCategory(categorys.get(i).getCat_id(), catlvl2);
        } else if (adapterView.getId() == R.id.catlvl2) {
            callCategory(categorys1.get(i).getCat_id(), catlvl3);
        } else if (adapterView.getId() == R.id.catlvl3) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private Category findLastValueCatId() {

        if (categorys2.get(catlvl3.getSelectedItemPosition()).getCat_id().equals("-99999")) {
            if (categorys1.get(catlvl2.getSelectedItemPosition()).getCat_id().equals("-99999")) {
                return categorys.get(catlvl1.getSelectedItemPosition());
            }
            return categorys1.get(catlvl2.getSelectedItemPosition());
        } else {
            return categorys2.get(catlvl3.getSelectedItemPosition());

        }
    }

    @Override
    public void onClick(View view) {
        Category cateSelected = findLastValueCatId();
        Intent output = new Intent();
        output.putExtra("catId", cateSelected.getCat_id());
        output.putExtra("catName", cateSelected.getCat_name());
        setResult(RESULT_OK, output);
        finish();
    }
}
