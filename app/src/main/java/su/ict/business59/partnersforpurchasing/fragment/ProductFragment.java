package su.ict.business59.partnersforpurchasing.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.CategoryActivity;
import su.ict.business59.partnersforpurchasing.PostByCategoryActivity;
import su.ict.business59.partnersforpurchasing.ProductDetailActivity;
import su.ict.business59.partnersforpurchasing.ProductManageActivity;
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.ProductAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.CategoryService;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.models.Category;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Post;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

import static android.app.Activity.RESULT_OK;


public class ProductFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private List<Product> productList = new ArrayList<>();
    private List<Product> productLisFilter = new ArrayList<>();
    private ProductAdapter adapter;
    @Bind(R.id.productRc)
    RecyclerView productRc;
    private Shop currentUser;

    // this view use only shop
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPreference pref = new UserPreference(getContext());
        currentUser = pref.getUserObject();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_product, container, false);
        productRc = (RecyclerView) myView.findViewById(R.id.productRc);
        productRc.setHasFixedSize(true);
        ButterKnife.bind(getActivity(), myView);
        setHasOptionsMenu(true);
        init();
        return myView;
    }

    private void init() {
        ProductService service = ServiceGenerator.createService(ProductService.class);
        Call<ListData> call = service.getProductList(currentUser.getShopId());
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    productList = response.body().getItemsProduct(); // list product original
                    productLisFilter = new ArrayList<>(productList);
                    adapter = new ProductAdapter(productLisFilter, getActivity(), new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Product item) {
                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                            intent.putExtra("product", item);
                            startActivity(intent);
                            Toast.makeText(getActivity(), item.getProductName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    productRc.setAdapter(adapter);
                    productRc.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
    public void onResume() {
        super.onResume();
        init();
    }

    public void populateSpinner(Context context, List<Category> categorys, Spinner spinner) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(context, R.layout.spinner_item, categorys);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
//        Toast.makeText(getActivity(), categorys.get(index).getCategory_name(), Toast.LENGTH_SHORT).show();
//        if (categorys.get(index).getCategory_name().equals("ALL")) {
//            productLisFilter = new ArrayList<>(productList);
//        } else {
//            productLisFilter.clear();
//            for (int i = 0; i < productList.size(); i++) {
//                Product product = productList.get(i);
//                if (product.getCategoryId().equals(categorys.get(index).getCategory_id())) {
//                    productLisFilter.add(product);
//                }
//            }
//        }
//        adapter = new ProductAdapter(productLisFilter, getActivity(), new ProductAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Product item) {
//                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//                intent.putExtra("product", item);
//                startActivity(intent);
//                Toast.makeText(getActivity(), item.getProductName(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        productRc.setAdapter(adapter);
//        productRc.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

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
}
