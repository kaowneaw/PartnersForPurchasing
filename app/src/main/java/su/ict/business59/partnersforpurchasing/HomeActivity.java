package su.ict.business59.partnersforpurchasing;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import su.ict.business59.partnersforpurchasing.fragment.FavoriteProductFragment;
import su.ict.business59.partnersforpurchasing.fragment.FeedFragment;
import su.ict.business59.partnersforpurchasing.fragment.ProductFragment;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));// init set feed menu
        navigationView.setCheckedItem(R.id.nav_feed);// init set selected check feed menu
        UserPreference pref = new UserPreference(getApplicationContext());
        if (pref.getRoleKey().equals("U")) {
            hideMenuNavLeftForNormalUser(navigationView);
        }
        View headView = navigationView.getHeaderView(0);
        TextView nav_username = (TextView) headView.findViewById(R.id.nav_username);
        TextView nav_email = (TextView) headView.findViewById(R.id.nav_email);
        ImageView nav_img = (ImageView) headView.findViewById(R.id.nav_img);
        nav_username.setText(pref.getUsername());
        nav_email.setText(pref.getEmail());
        String host = getResources().getString(R.string.host);
        Picasso.with(getApplicationContext()).load(host + pref.getImg()).fit().centerCrop().into(nav_img);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void hideMenuNavLeftForNormalUser(NavigationView navigationView) {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_product).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        Fragment myFragment = null;
        if (id == R.id.nav_feed) {
            myFragment = new FeedFragment();
            setTitle("หน้าแรก");
        } else if (id == R.id.nav_product) {
            myFragment = new ProductFragment();
            setTitle(getResources().getString(R.string.product));
        } else if (id == R.id.nav_fav) {
            myFragment = new FavoriteProductFragment();
            setTitle(getResources().getString(R.string.favorite_product));
        } else if (id == R.id.nav_logout) {
            UserPreference pref = new UserPreference(this);
            pref.clearPreference();
            startActivity(new Intent(this, MainActivity.class));
            return false;
        }
        fm.beginTransaction().replace(R.id.container, myFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
