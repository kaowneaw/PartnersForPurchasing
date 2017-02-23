package su.ict.business59.partnersforpurchasing.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import su.ict.business59.partnersforpurchasing.fragment.PostFragment;
import su.ict.business59.partnersforpurchasing.fragment.ProductFragment;
import su.ict.business59.partnersforpurchasing.fragment.ProductPostFragment;

/**
 * Created by kaowneaw on 12/10/2016.
 */

public class TabAdapter extends FragmentPagerAdapter {
    public int tabItems = 2;

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PostFragment();
            case 1:
                return new ProductPostFragment();

        }
        return new ProductFragment();
    }

    @Override
    public int getCount() {
        return tabItems;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "โพสต์";
            case 1:
                return "สินค้า";
        }
        return null;
    }
}
