package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.models.ProductImg;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class ProductPostDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    Product productObj;
    @Bind(R.id.slider)
    SliderLayout mDemoSlider;
    @Bind(R.id.product_name)
    TextView product_name;
    @Bind(R.id.product_desc)
    TextView product_desc;
    @Bind(R.id.product_price)
    TextView product_price;
    @Bind(R.id.category_name)
    TextView category_name;
    @Bind(R.id.btn_share)
    TableRow btn_share;
    @Bind(R.id.btn_fav)
    TableRow btn_fav;
    @Bind(R.id.shop_info)
    TextView shop_info;
    @Bind(R.id.promotion)
    TextView promotion;
    @Bind(R.id.tvPromotionDesc)
    TextView tvPromotionDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_post_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        this.productObj = bundle.getParcelable("product");
        setTitle(this.productObj.getProductName());
        btn_share.setOnClickListener(this);
        btn_fav.setOnClickListener(this);
        init();
    }

    private void init() {
        product_name.setText(this.productObj.getProductName());
        product_desc.setText(this.productObj.getProductDesc());
        category_name.setText(this.productObj.getCatName());
        product_price.setText(String.valueOf(this.productObj.getProductPrice()) + " บาท");
        shop_info.setText(this.productObj.getShopName() + '\n' + this.productObj.getAddressShopString());
        promotion.setText(checkNoPromotion(this.productObj.getPromotion_name()));
        if (this.productObj.getPromotion_id() != null) {
            if (this.productObj.getPromotion_id().equals("20")) { // id promotion other
                tvPromotionDesc.setVisibility(View.VISIBLE);
                tvPromotionDesc.setText(Html.fromHtml("<b>รายละเอียดโปรโมชั่น : </b>" + this.productObj.getPromotion_desc()));
            } else {
                tvPromotionDesc.setVisibility(View.GONE);
            }
        }
        HashMap<Integer, String> url_maps = new HashMap<>();
        String host = getResources().getString(R.string.host);
        int index = 0;
        for (ProductImg img : productObj.getImgList()) {
            url_maps.put(index, host + img.getPimg_url());
            index++;
        }

        for (Integer position : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(productObj.getProductName())
                    .image(url_maps.get(position))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", productObj.getProductName());

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(6000);
        mDemoSlider.addOnPageChangeListener(this);
    }


    String checkNoPromotion(String val) {
        if (val == null) {
            return "ไม่มีโปรโมชั่น";
        }
        return val;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_share) {
            Intent i = new Intent(this, PostProductActivity.class);
            i.putExtra("product", this.productObj);
            startActivity(i);
        } else if (view == btn_fav) {
            favoriteProduct();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void favoriteProduct() {
        UserPreference pref = new UserPreference(this);
        String userId = pref.getUserObject().getUser_id();
        ProductService service = ServiceGenerator.createService(ProductService.class);
        Call<BaseResponse> call = service.favoriteProduct(String.valueOf(this.productObj.getProductId()), userId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse res = response.body();
                    if (res.isStatus()) {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }
}
