package su.ict.business59.partnersforpurchasing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.ProductDetailActivity;
import su.ict.business59.partnersforpurchasing.ProductManageActivity;
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.ProductAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;

/**
 * Created by kaowneaw on 11/26/2016.
 */

public class ProductFragment extends Fragment {

    private List<Product> productList = new ArrayList<>();
    private ProductAdapter adapter;
    @Bind(R.id.productRc)
    RecyclerView productRc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.product_layout, container, false);
        productRc = (RecyclerView) myView.findViewById(R.id.productRc);
        productRc.setHasFixedSize(true);
        ButterKnife.bind(getActivity(), myView);
        setHasOptionsMenu(true);
        init();
        return myView;
    }

    private void init() {
        ProductService service = ServiceGenerator.createService(ProductService.class);
        String shopId = "1";
        Call<ListData> call = service.getProductList(shopId);
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
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
                    productRc.setAdapter(adapter);
                    productRc.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.product_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.create_product) {
            startActivity(new Intent(getActivity(), ProductManageActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
