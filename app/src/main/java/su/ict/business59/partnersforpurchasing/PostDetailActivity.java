package su.ict.business59.partnersforpurchasing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.ProductImg;

public class PostDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private Post postObj;
    @Bind(R.id.slider)
    SliderLayout mDemoSlider;
    @Bind(R.id.topic)
    TextView topic;
    @Bind(R.id.dateStart)
    TextView dateStart;
    @Bind(R.id.dateEnd)
    TextView dateEnd;
    @Bind(R.id.category_name)
    TextView category_name;
    @Bind(R.id.shop_name)
    TextView shop_name;
    @Bind(R.id.limit_join)
    TextView limit_join;
    @Bind(R.id.desc)
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        try {
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ParseException {
        String myFormat = "yyyy-MM-dd hh:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Bundle bundle = getIntent().getExtras();
        this.postObj = bundle.getParcelable("post");
        assert this.postObj != null;
        setTitle(this.postObj.getPostName());
        topic.setText(this.postObj.getPostName());
        dateStart.setText((sdf.format(sdf.parse(this.postObj.getPostStart()))));
        dateEnd.setText((sdf.format(sdf.parse(this.postObj.getPostEnd()))));
        category_name.setText(this.postObj.getCatName());
        shop_name.setText(this.postObj.getShopName());
        limit_join.setText(this.postObj.getMaxJoin());
        desc.setText(this.postObj.getPostDesc());
        HashMap<Integer, String> url_maps = new HashMap<>();
        String host = getResources().getString(R.string.host);
        int index = 0;
        url_maps.put(index, host + this.postObj.getPostImg());

        for (Integer position : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(postObj.getPostName())
                    .image(url_maps.get(position))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", postObj.getPostName());

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

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
