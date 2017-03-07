package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
import su.ict.business59.partnersforpurchasing.models.MemberJoin;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class PostDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    @Bind(R.id.slider)
    SliderLayout mDemoSlider;
    @Bind(R.id.topic)
    TextView topic;
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
    @Bind(R.id.chat)
    TableRow chat;
    @Bind(R.id.promotion)
    TextView promotion;
    @Bind(R.id.warpPromotion)
    LinearLayout warpPromotion;
    @Bind(R.id.tvView)
    TextView tvView;
    private Shop currentUser;
    private Post postObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_joined.setPaintFlags(text_joined.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        UserPreference pref = new UserPreference(this);
        this.currentUser = pref.getUserObject();
        chat.setOnClickListener(this);
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
        String host = getResources().getString(R.string.host);
        Picasso.with(getApplicationContext()).load(host + this.postObj.getImage_url()).fit().centerCrop().into(user_img);
        user_text.setText(this.postObj.getUsername());
        topic.setText(this.postObj.getPostName());
        category_name.setText(this.postObj.getCatName());
        shop_name.setText(this.postObj.getShopName() + "\n\n" + this.postObj.getAddressPostShop());
        desc.setText(this.postObj.getPostDesc());
        text_joined.setText(String.valueOf(this.postObj.getMemberJoin().size()));
        text_joined.setOnClickListener(this);
        tvView.setText("มีผู้เข้าชม " + this.postObj.getPostView() + " ครั้ง");
        if (postObj.getPromotionDesc().equals("")) {
            promotion.setText(postObj.getPromotionName());
            if (promotion.getText().toString().equals("")) {
                warpPromotion.setVisibility(View.GONE);
            } else {
                warpPromotion.setVisibility(View.VISIBLE);
            }
        } else {
            promotion.setText(postObj.getPromotionName() + " ( " + postObj.getPromotionDesc() + " ) ");
        }
        checkShowChatButton();
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
            textSliderView.getBundle().putString("extra", postObj.getPostName());

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(0);
        mDemoSlider.addOnPageChangeListener(this);
    }

    private boolean checkShowChatButton() {
        for (int i = 0; i < this.postObj.getMemberJoin().size(); i++) {
            MemberJoin member = this.postObj.getMemberJoin().get(i);
            if (member.getUser_id().equals(this.currentUser.getUser_id())) {
                chat.setVisibility(View.VISIBLE);
                return true;
            }
        }
        if (this.postObj.getUser_id().equals(this.currentUser.getUser_id())) {
            chat.setVisibility(View.VISIBLE);
            return true;
        }
        chat.setVisibility(View.GONE);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // increase view post
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<BaseResponse> call = service.postUpdateView(this.postObj.getPostId(), this.postObj.getPostView() + 1);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
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
        if (this.postObj.getUser_id().equals(currentUser.getUser_id()))
            item.setVisible(false); // check if own post
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.join) {
            PostService service = ServiceGenerator.createService(PostService.class);
            Call<BaseResponse> call = service.joinPost(this.currentUser.getUser_id(), this.postObj.getPostId() + "", 0);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        BaseResponse res = response.body();
                        if (res.isStatus()) {
                            chat.setVisibility(View.VISIBLE);
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
            dialog();
        } else if (view == chat) {
            Intent i = new Intent(this, ChatActivity.class);
            i.putExtra("postId", this.postObj.getPostId());
            startActivity(i);
        }
    }

    private void dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_member_join, null);
        RecyclerView rcv = (RecyclerView) dialogView.findViewById(R.id.rcv_dialog_member_join);
        MemberJoinAdapter adapter = new MemberJoinAdapter(getApplicationContext(), this.postObj.getMemberJoin(), this.postObj.getUnitRequire());
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
