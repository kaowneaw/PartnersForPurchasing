package su.ict.business59.partnersforpurchasing.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener {
    //Post List Page
    private RecyclerView postRc;
    private PostAdapter adapter;
    List<Post> listPost;

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
        final PostFragment mContext = this;
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<ListData> call = service.getPostList();
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    listPost = response.body().getItemsPost();
                    adapter = new PostAdapter(listPost, getActivity(), mContext);
                    postRc.setAdapter(adapter);
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
    public void onJoinButtonClick(int index) {
        listPost.remove(index);
        adapter.notifyDataSetChanged();
    }
}
