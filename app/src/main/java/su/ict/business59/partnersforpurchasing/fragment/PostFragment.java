package su.ict.business59.partnersforpurchasing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.PostDetailActivity;
import su.ict.business59.partnersforpurchasing.PostProductActivity;
import su.ict.business59.partnersforpurchasing.ProductDetailActivity;
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.PostAdapter;
import su.ict.business59.partnersforpurchasing.adapter.ProductAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.PostService;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener {
    //Post List Page
    private RecyclerView postRc;
    private PostAdapter adapter;
    List<Post> listPost;
    private UserPreference pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new UserPreference(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        postRc = (RecyclerView) v.findViewById(R.id.postRc);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PostProductActivity.class);
                startActivity(i);
            }
        });
        init();
        // Inflate the layout for this fragment
        return v;
    }

    private void init() {
        listPost = new ArrayList<>();
        adapter = new PostAdapter(listPost, getActivity(), PostFragment.this);
        postRc.setAdapter(adapter);
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<ListData> call = service.getPostList();
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    listPost = response.body().getItemsPost();
                    adapter.updateData(listPost);
                    postRc.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(getActivity(), PostDetailActivity.class);
        i.putExtra("post", item);
        startActivity(i);
    }

    @Override
    public void onJoinButtonClick(final int index) {
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<BaseResponse> call = service.joinPost(pref.getUserID(), listPost.get(index).getPostId() + "");
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse res = response.body();
                    if (res.isStatus()) {
                        listPost.remove(index);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }
}
