package su.ict.business59.partnersforpurchasing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import su.ict.business59.partnersforpurchasing.R;
import su.ict.business59.partnersforpurchasing.adapter.TabAdapter;

/**
 * Created by kaowneaw on 11/26/2016.
 */

public class FeedFragment extends Fragment {
    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.feed_fragment, container, false);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        setHasOptionsMenu(true);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        getActivity().setTitle("Feed");

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
