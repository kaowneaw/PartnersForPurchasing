package su.ict.business59.partnersforpurchasing.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import su.ict.business59.partnersforpurchasing.fragment.MyPostFragment;

public class TabProfileAdapter extends FragmentPagerAdapter {
    public int tabItems = 1;

    public TabProfileAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyPostFragment();

        }
        return new MyPostFragment();
    }

    @Override
    public int getCount() {
        return tabItems;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "My Post";
        }
        return null;
    }
}
