package com.nova.hro.novamaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class AdminPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ServerRequests serverRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Administrator Panel");
        navView = (NavigationView) findViewById(R.id.nav_drawer);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_closed);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.adminViewPager);
        viewPager.setAdapter(new AdminPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        serverRequests = new ServerRequests(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_personal:
                break;
            case R.id.item_logout:
                startActivity(new Intent(this, Login.class));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class AdminPagerAdapter extends FragmentStatePagerAdapter {

        public AdminPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new AdminPanelManager.FragmentNewJob();
            else if (position == 1)
                return new AdminPanelManager.EditJob();
            return new AdminPanelManager.FragmentQueryDB();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Add a New Job";
                case 1:
                    return "Edit an existing Job";
                default:
                    return "Search the Database";
            }
        }
    }

}

