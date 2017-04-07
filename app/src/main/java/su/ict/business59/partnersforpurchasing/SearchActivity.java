package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, PostAdapter.OnItemClickListener {
    @Bind(R.id.postRc)
    RecyclerView postRc;
    @Bind(R.id.textsearch)
    EditText textsearch;
    @Bind(R.id.btn_search)
    Button btn_search;
    @Bind(R.id.txt_notfound)
    TextView txt_notfound;
    private PostAdapter adapter;
    List<Post> listPost;
    private UserPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setTitle("ค้นหาโพส");
        pref = new UserPreference(this);
        btn_search.setOnClickListener(this);

    }

    private void search() {
        listPost = new ArrayList<>();
        adapter = new PostAdapter(listPost, this, SearchActivity.this);
        postRc.setAdapter(adapter);
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<ListData> call = service.searchPost(textsearch.getText().toString());
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    ListData res = response.body();
                    listPost = res.getItemsPost();
                    adapter.updateData(listPost);
                    postRc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter.notifyDataSetChanged();
                    if (listPost.size() > 0) {
                        txt_notfound.setVisibility(View.GONE);
                        postRc.setVisibility(View.VISIBLE);
                    } else {
                        postRc.setVisibility(View.GONE);
                        txt_notfound.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getApplicationContext(), res.getItemsPost().size() + "", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        search();
    }

    @Override
    public void onItemClick(Post item) {
        Intent i = new Intent(this, PostDetailActivity.class);
        i.putExtra("post", item);
        startActivity(i);
    }


    @Override
    public void onJoinButtonClick(final int index) {
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<BaseResponse> call = service.joinPost("1", listPost.get(index).getPostId() + "", 0);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse res = response.body();
                    if (res.isStatus()) {
                        listPost.remove(index);
                        adapter.notifyDataSetChanged();
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
    }

    @Override
    public void onClosePost(int index) {

    }
}
