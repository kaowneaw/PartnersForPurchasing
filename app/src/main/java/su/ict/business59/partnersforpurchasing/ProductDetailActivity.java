package su.ict.business59.partnersforpurchasing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.models.ProductImg;

public class ProductDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    @Bind(R.id.slider)
    SliderLayout mDemoSlider;
    Product productObj;
    @Bind(R.id.product_name)
    TextView product_name;
    @Bind(R.id.product_desc)
    TextView product_desc;
    @Bind(R.id.product_price)
    TextView product_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        this.productObj = bundle.getParcelable("product");
        setTitle(this.productObj.getProductName());
        init();
    }

    private void init() {
        product_name.setText(this.productObj.getProductName());
        product_desc.setText(this.productObj.getProductDesc());
        product_price.setText(String.valueOf(this.productObj.getProductPrice()) + " บาท");
        HashMap<Integer, String> url_maps = new HashMap<>();
        String host = getResources().getString(R.string.host);
        int index = 0;
        for (ProductImg img : productObj.getImgList()) {
            url_maps.put(index, host + img.getPimg_url());
            Log.v("img", host + img.getPimg_url());
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
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
