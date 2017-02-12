package su.ict.business59.partnersforpurchasing.fragment;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.ict.business59.partnersforpurchasing.HomeActivity;
import su.ict.business59.partnersforpurchasing.ProductPostDetailActivity;
import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.ProductPostAdapter;
import su.ict.business59.partnersforpurchasing.interfaces.ProductService;
import su.ict.business59.partnersforpurchasing.interfaces.PromotionService;
import su.ict.business59.partnersforpurchasing.models.ListData;
import su.ict.business59.partnersforpurchasing.models.Product;
import su.ict.business59.partnersforpurchasing.models.Promotion;
import su.ict.business59.partnersforpurchasing.utills.ServiceGenerator;


public class ProductPostFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private List<Product> productList = new ArrayList<>();
    private List<Product> productListFilter = new ArrayList<>();
    RecyclerView rcView;
    private ProductPostAdapter adapter;
    Spinner promotion_spinner;
    List<Promotion> listPromotion = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_product_post, container, false);
        rcView = (RecyclerView) myView.findViewById(R.id.productRc);
        promotion_spinner = (Spinner) myView.findViewById(R.id.promotion_spinner);
        promotion_spinner.setOnItemSelectedListener(this);
        init();
        return myView;
    }

    public void populateSpinner(Context context, List<Promotion> promotions, Spinner spinner) {
        ArrayAdapter<Promotion> adapter = new ArrayAdapter<Promotion>(context, R.layout.spinner_item, promotions);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }


    private void init() {
        PromotionService service2 = ServiceGenerator.createService(PromotionService.class);
        Call<ListData> call2 = service2.getPromotion();
        call2.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    listPromotion = response.body().getItemsPromotion();
                    if (getActivity() != null) {
                        populateSpinner(getActivity(), listPromotion, promotion_spinner);
                    }
                } else {
                    Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });

        ProductService service = ServiceGenerator.createService(ProductService.class);
        Call<ListData> call = service.getProductList();
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful()) {
                    productList = response.body().getItemsProduct(); // list product original
                    productListFilter = response.body().getItemsProduct();
                    adapter = new ProductPostAdapter(productList, getActivity(), new ProductPostAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Product item) {
                            Intent intent = new Intent(getActivity(), ProductPostDetailActivity.class);
                            intent.putExtra("product", item);
                            startActivity(intent);
                            Toast.makeText(getActivity(), item.getProductName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    rcView.setAdapter(adapter);
                    rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
        if (listPromotion.get(index).getPromotion_id().equals("1")) { // 1 id is all promotion
            productListFilter = new ArrayList<>(productList);
        } else {
            productListFilter.clear();
            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                if (product.getPromotion_id().equals(listPromotion.get(index).getPromotion_id())) {
                    productListFilter.add(product);
                }
            }
        }
        adapter = new ProductPostAdapter(productListFilter, getActivity(), new ProductPostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item) {
                Intent intent = new Intent(getActivity(), ProductPostDetailActivity.class);
                intent.putExtra("product", item);
                startActivity(intent);
                Toast.makeText(getActivity(), item.getProductName(), Toast.LENGTH_SHORT).show();
            }
        });
        rcView.setAdapter(adapter);
        rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
