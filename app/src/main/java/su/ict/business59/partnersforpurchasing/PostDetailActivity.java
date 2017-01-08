package su.ict.business59.partnersforpurchasing;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.adapter.MemberJoinAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.PostService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class PostDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

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
    @Bind(R.id.desc)
    TextView desc;
    @Bind(R.id.user_img)
    ImageView user_img;
    @Bind(R.id.user_text)
    TextView user_text;
    @Bind(R.id.text_joined)
    TextView text_joined;

    private UserPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_joined.setPaintFlags(text_joined.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        pref = new UserPreference(this);
        try {
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ParseException {
        Bundle bundle = getIntent().getExtras();
        this.postObj = bundle.getParcelable("post");
        assert this.postObj != null;
        setTitle(this.postObj.getPostName());
        String myFormat = "yyyy-MM-dd hh:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String host = getResources().getString(R.string.host);
        Picasso.with(getApplicationContext()).load(host + this.postObj.getImage_url()).fit().centerCrop().into(user_img);
        user_text.setText(this.postObj.getUsername());
        topic.setText(this.postObj.getPostName());
        dateStart.setText((sdf.format(sdf.parse(this.postObj.getPostStart()))));
        dateEnd.setText((sdf.format(sdf.parse(this.postObj.getPostEnd()))));
        category_name.setText(this.postObj.getCatName());
        shop_name.setText(this.postObj.getShopName());
        desc.setText(this.postObj.getPostDesc());
        text_joined.setText(String.valueOf(this.postObj.getMemberJoin().size()));
        text_joined.setOnClickListener(this);
        HashMap<Integer, String> url_maps = new HashMap<>();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.join_menu, menu);
        MenuItem item = menu.findItem(R.id.join);
        if (this.postObj.getUser_id().equals(pref.getUserID())) item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.join) {
            PostService service = ServiceGenerator.createService(PostService.class);
            Call<BaseResponse> call = service.joinPost(pref.getUserID(), this.postObj.getPostId() + "");
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        BaseResponse res = response.body();
                        if (res.isStatus()) {
                            Toast.makeText(getApplicationContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
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
        } else if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View view) {
        if (view == text_joined) {
//            Toast.makeText(getApplicationContext(), "E", Toast.LENGTH_SHORT).show();
            dialog();
        }
    }

    private void dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_member_join, null);
        RecyclerView rcv = (RecyclerView) dialogView.findViewById(R.id.rcv_dialog_member_join);
        MemberJoinAdapter adapter = new MemberJoinAdapter(getApplicationContext(), this.postObj.getMemberJoin());
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
