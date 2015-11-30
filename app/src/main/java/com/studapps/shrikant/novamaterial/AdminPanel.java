package com.studapps.shrikant.novamaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
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
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Post a Job"));
        tabLayout.addTab(tabLayout.newTab().setText("Edit an existing Job"));
        tabLayout.addTab(tabLayout.newTab().setText("Search in Database"));
        Fragment initial = new AdminPanelManager.FragmentNewJob();
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.adminrelScroll, initial).commit();

        serverRequests = new ServerRequests(this);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Fragment f = new AdminPanelManager.FragmentNewJob();
                        getSupportFragmentManager()
                                .beginTransaction().replace(R.id.adminrelScroll, f).commit();
                        break;
                    case 1:
                        Fragment f1 = new AdminPanelManager.JobEditor();
                        getSupportFragmentManager()
                                .beginTransaction().replace(R.id.adminrelScroll, f1).commit();
                        break;
                    case 2:
                        Fragment f2 = new AdminPanelManager.FragmentQueryDB();
                        getSupportFragmentManager()
                                .beginTransaction().replace(R.id.adminrelScroll, f2).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

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

}

