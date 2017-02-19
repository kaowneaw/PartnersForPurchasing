package su.ict.business59.partnersforpurchasing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.ProductDetailActivity;
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.ProductAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class FavoriteProductFragment extends Fragment {

    private List<Product> productList = new ArrayList<>();
    private ProductAdapter adapter;
    private View rootView;
    RecyclerView rc_favProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        rc_favProduct = (RecyclerView) rootView.findViewById(R.id.rc_favProduct);
        init();
        return rootView;
    }

    private void init() {
        UserPreference pref = new UserPreference(getActivity());
        String userId = pref.getUserObject().getUser_id();
        ProductService service = ServiceGenerator.createService(ProductService.class);
        Call<ListData> call = service.getFavoriteProduct(userId);
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                productList = response.body().getItemsProduct();
                adapter = new ProductAdapter(productList, getActivity(), new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Product item) {
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("product", item);
                        startActivity(intent);
                        Toast.makeText(getActivity(), item.getProductName(), Toast.LENGTH_SHORT).show();
                    }
                });
                rc_favProduct.setAdapter(adapter);
                rc_favProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
