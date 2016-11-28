package su.ict.business59.partnersforpurchasing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import su.ict.business59.partnersforpurchasing.ProductManageActivity;
import su.ict.business59.partnersforpurchasing.R;

/**
 * Created by kaowneaw on 11/26/2016.
 */

public class ProductFragment extends android.app.Fragment {

    View myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.product_layout, container, false);
        return myView;
    }

    private void init() {
        getActivity().setTitle(getResources().getString(R.string.product));
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
