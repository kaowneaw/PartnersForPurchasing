package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.adapter.PostAdapter;
import su.ict.business59.partnersforpurchasing.fragment.PostFragment;
import su.ict.business59.partnersforpurchasing.interfaces.PostService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class PostByCategoryActivity extends AppCompatActivity implements PostAdapter.OnItemClickListener {
    @Bind(R.id.postRc)
    RecyclerView postRc;
    @Bind(R.id.txt_notfound)
    TextView txt_notfound;
    private PostAdapter adapter;
    List<Post> listPost;
    private UserPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_by_category);
        ButterKnife.bind(this);
        pref = new UserPreference(this);
        String catId = getIntent().getExtras().getString("catId");
        String catName = getIntent().getExtras().getString("catName");
        setTitle("หมวด " + catName);
        getPostByCategory(catId);
    }


    private void getPostByCategory(String catId) {
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<ListData> call = service.getPostListByCategory(catId);
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    listPost = response.body().getItemsPost();
                    if (listPost.size() == 0) {
                        txt_notfound.setVisibility(View.VISIBLE);
                        postRc.setVisibility(View.GONE);
                    } else {
                        txt_notfound.setVisibility(View.GONE);
                        postRc.setVisibility(View.VISIBLE);
                        adapter = new PostAdapter(listPost, getApplicationContext(), PostByCategoryActivity.this);
                        postRc.setAdapter(adapter);
                        postRc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(Post item) {
        Intent i = new Intent(this, PostDetailActivity.class);
        i.putExtra("post", item);
        startActivity(i);
    }

    @Override
    public void onJoinButtonClick(final int index) {
//        PostService service = ServiceGenerator.createService(PostService.class);
//        Call<BaseResponse> call = service.joinPost(pref.getUserObject().getUser_id(), listPost.get(index).getPostId() + "", 0);
//        call.enqueue(new Callback<BaseResponse>() {
//            @Override
//            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                if (response.isSuccessful()) {
//                    BaseResponse res = response.body();
//                    if (res.isStatus()) {
//                        listPost.remove(index);
//                        adapter.notifyDataSetChanged();
//                        Toast.makeText(getApplicationContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    public void onClosePost(int index) {

    }
}
