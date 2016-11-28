package su.ict.business59.partnersforpurchasing.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import su.ict.business59.partnersforpurchasing.R;

/**
 * Created by kaowneaw on 11/26/2016.
 */

public class FeedFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.feed_layout, container, false);
        return myView;
    }
}
