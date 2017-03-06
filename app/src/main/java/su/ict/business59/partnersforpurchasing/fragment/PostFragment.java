package su.ict.business59.partnersforpurchasing.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.CategoryActivity;
import su.ict.business59.partnersforpurchasing.PostByCategoryActivity;
import su.ict.business59.partnersforpurchasing.PostDetailActivity;
import su.ict.business59.partnersforpurchasing.PostProductActivity;
import su.ict.business59.partnersforpurchasing.ProductDetailActivity;
import su.ict.business59.partnersforpurchasing.ProductManageActivity;
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.PostAdapter;
import su.ict.business59.partnersforpurchasing.adapter.ProductAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.PostService;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.interfaces.PromotionService;
import su.ict.business59.partnersforpurchasing.models.BaseResponse;
import su.ict.business59.partnersforpurchasing.models.Category;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.models.Promotion;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.AlertInputDialog;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    //Post List Page
    private RecyclerView postRc;
    private PostAdapter adapter;
    private List<Post> listPost;
    private List<Post> postListFilter = new ArrayList<>();
    private List<Promotion> listPromotion;
    private UserPreference pref;
    private FloatingActionButton fab;
    private ImageButton category_btn;
    private Spinner promotion_spinner;
    private ProgressDialog progress;
    private Shop currentUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new UserPreference(getActivity());
        currentUser = pref.getUserObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        setHasOptionsMenu(true);
        postRc = (RecyclerView) v.findViewById(R.id.postRc);
        postRc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PostProductActivity.class);
                startActivity(i);
            }
        });
        category_btn = (ImageButton) v.findViewById(R.id.category_btn);
        category_btn.setOnClickListener(this);
        promotion_spinner = (Spinner) v.findViewById(R.id.promotion_spinner);
        promotion_spinner.setOnItemSelectedListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        listPost = new ArrayList<>();
        adapter = new PostAdapter(listPost, getActivity(), PostFragment.this);
        postRc.setAdapter(adapter);
        progress = ProgressDialog.show(getActivity(), "", "Loading...", true);
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
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });

        PromotionService service2 = ServiceGenerator.createService(PromotionService.class);
        Call<ListData> call2 = service2.getPromotion();
        call2.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    listPromotion = response.body().getItemsPromotion();
                    populateSpinner(getActivity(), listPromotion, promotion_spinner);
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

    public void populateSpinner(Context context, List<Promotion> promotions, Spinner spinner) {
        ArrayAdapter<Promotion> adapter = new ArrayAdapter<Promotion>(context, R.layout.spinner_item, promotions);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Post item) {
        Intent i = new Intent(getActivity(), PostDetailActivity.class);
        i.putExtra("post", item);
        startActivity(i);
    }

    @Override
    public void onJoinButtonClick(final int index) {
        alertJoinInput(index);
    }

    @Override
    public void onClosePost(final int index) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        PostService service = ServiceGenerator.createService(PostService.class);
                        String StatusClosePost = "0";
                        Call<BaseResponse> call = service.postStatus(listPost.get(index).getPostId(), StatusClosePost);
                        call.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                if (response.isSuccessful()) {
                                    listPost.remove(index);
                                    adapter.updateData(listPost);
                                    postRc.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Close Post", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {

                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("ต้องการปิดโพสน์ใช่หรือไม่ ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onClick(View view) {
        if (view == category_btn) {
            Intent i = new Intent(getActivity(), CategoryActivity.class);
            startActivityForResult(i, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String catId = data.getStringExtra("catId");
            String catName = data.getStringExtra("catName");
            Intent i = new Intent(getActivity(), PostByCategoryActivity.class);
            i.putExtra("catId", catId);
            i.putExtra("catName", catName);
            startActivity(i);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
        if (listPromotion.get(index).getPromotion_id().equals("1")) { // 1 id is all promotion
            postListFilter = new ArrayList<>(listPost);
        } else {
            postListFilter.clear();
            for (int i = 0; i < listPost.size(); i++) {
                Post post = listPost.get(i);
                if (post.getPromotionId() != null) {
                    if (post.getPromotionId().equals(listPromotion.get(index).getPromotion_id())) {
                        postListFilter.add(post);
                    }
                }
            }
        }
        adapter = new PostAdapter(postListFilter, getActivity(), PostFragment.this);
        postRc.setAdapter(adapter);
        postRc.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void alertJoinInput(final int index) {

        final AlertInputDialog alertInput = new AlertInputDialog();
        final AlertDialog.Builder alert = alertInput.getDialog(this);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //OR
                int amount = alertInput.getValueAmount();

                if (amount > 0) {
                    PostService service = ServiceGenerator.createService(PostService.class);
                    Call<BaseResponse> call = service.joinPost(currentUser.getUser_id(), String.valueOf(listPost.get(index).getPostId()), amount);
                    call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.isSuccessful()) {
                                BaseResponse res = response.body();
                                if (res.isStatus()) {
                                    listPost.remove(index);
                                    adapter.updateData(listPost);
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
                } else {
                    Toast.makeText(getContext(), "ระบุุตัวเลขที่มากกว่า 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }
}
