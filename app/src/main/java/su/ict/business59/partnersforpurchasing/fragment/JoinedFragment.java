package su.ict.business59.partnersforpurchasing.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.PostAdapter;
import su.ict.business59.partnersforpurchasing.adapter.PostJoinedAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.PostService;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinedFragment extends Fragment implements PostJoinedAdapter.OnItemClickListener {

    RecyclerView rcPostJoined;
    private Shop currentUser;
    private List<Post> listPost;
    private PostJoinedAdapter adapter;
    private ProgressDialog progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPreference pref = new UserPreference(getActivity());
        currentUser = pref.getUserObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_joined, container, false);
        rcPostJoined = (RecyclerView) v.findViewById(R.id.rcPostJoined);
        init();
        return v;
    }

    private void init() {
        listPost = new ArrayList<>();
        progress = ProgressDialog.show(getActivity(), "", "Loading...", true);
        adapter = new PostJoinedAdapter(listPost, getActivity(), JoinedFragment.this);
        rcPostJoined.setAdapter(adapter);
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<ListData> call = service.getPostJoined(currentUser.getUser_id());
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    listPost = response.body().getItemsPost();
                    adapter.updateData(listPost);
                    rcPostJoined.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
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


}
