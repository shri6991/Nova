package com.studapps.shrikant.novamaterial;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserProfile2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView headerText, curloc, desloc, experience, noComs, position, com1name, com2name, com3name, com1dates, com2dates, com3dates, com1pos, com2pos, com3pos, com1resp, com2resp, com3resp;
    ImageView whdown, com1down, com2down, com3down, pddown;
    RelativeLayout com1rel, com2rel, com3rel, reladdcompany;
    LinearLayout com1master, com2master, com3master, addcompanymaster, addcompanylayout;
    Toolbar toolbar;
    ServerRequests serverRequests;
    UserLocalStore userLocalStore;
    User receivedUser;
    NavigationView navView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    String comno = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);

        serverRequests = new ServerRequests(this);
        userLocalStore = new UserLocalStore(this);
        receivedUser = userLocalStore.getAllDetails();

        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setText(receivedUser.getName());
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        navView = (NavigationView) findViewById(R.id.nav_drawer);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_userprofile2);
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_closed);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        noComs = (TextView) findViewById(R.id.noComsTextView);
        position = (TextView) findViewById(R.id.workPosition);
        experience = (TextView) findViewById(R.id.workExperience);
        curloc = (TextView) findViewById(R.id.workCurloc);
        desloc = (TextView) findViewById(R.id.workDesloc);
        com1name = (TextView) findViewById(R.id.com1name);
        com1pos = (TextView) findViewById(R.id.com1pos);
        com1dates = (TextView) findViewById(R.id.com1dates);
        com1resp = (TextView) findViewById(R.id.com1resp);
        com2name = (TextView) findViewById(R.id.com2name);
        com2pos = (TextView) findViewById(R.id.com2pos);
        com2dates = (TextView) findViewById(R.id.com2dates);
        com2resp = (TextView) findViewById(R.id.com2resp);
        com3name = (TextView) findViewById(R.id.com3name);
        com3pos = (TextView) findViewById(R.id.com3pos);
        com3dates = (TextView) findViewById(R.id.com3dates);
        com3resp = (TextView) findViewById(R.id.com3resp);
        whdown = (ImageView) findViewById(R.id.whdown);
        com1down = (ImageView) findViewById(R.id.com1down);
        com2down = (ImageView) findViewById(R.id.com2down);
        com3down = (ImageView) findViewById(R.id.com3down);
        pddown = (ImageView) findViewById(R.id.pddown);

        com1rel = (RelativeLayout) findViewById(R.id.com1rel);
        com2rel = (RelativeLayout) findViewById(R.id.com2rel);
        com3rel = (RelativeLayout) findViewById(R.id.com3rel);
        reladdcompany = (RelativeLayout) findViewById(R.id.reladdcompany);

        com1master = (LinearLayout) findViewById(R.id.com1master);
        com2master = (LinearLayout) findViewById(R.id.com2master);
        com3master = (LinearLayout) findViewById(R.id.com3master);
        addcompanymaster = (LinearLayout) findViewById(R.id.addcompanymaster);
        addcompanylayout = (LinearLayout) findViewById(R.id.addcompanylayout);


        position.setText(receivedUser.getPosition());
        experience.setText(receivedUser.getExperience());
        curloc.setText(receivedUser.getCurloc());
        desloc.setText(receivedUser.getDesloc());

        com1name.setText(receivedUser.getCom1name());
        com1pos.setText(receivedUser.getCom1pos());
        com1dates.setText(receivedUser.getCom1from() + " to " + receivedUser.getCom1to());
        com1resp.setText(receivedUser.getCom1resp());
        com2name.setText(receivedUser.getCom2name());
        com2pos.setText(receivedUser.getCom2pos());
        com2dates.setText(receivedUser.getCom2from() + " to " + receivedUser.getCom2to());
        com2resp.setText(receivedUser.getCom2resp());
        com3name.setText(receivedUser.getCom3name());
        com3pos.setText(receivedUser.getCom3pos());
        com3dates.setText(receivedUser.getCom3from() + " to " + receivedUser.getCom3to());
        com3resp.setText(receivedUser.getCom3resp());
        if (receivedUser.getCom1name().equals("") || receivedUser.getCom1name().equals(" ")) {
            com1rel.setVisibility(View.GONE);
            com2rel.setVisibility(View.GONE);
            com3rel.setVisibility(View.GONE);
            noComs.setVisibility(View.VISIBLE);
            reladdcompany.setVisibility(View.VISIBLE);
        } else if (receivedUser.getCom2name().equals("") || receivedUser.getCom2name().equals(" ")) {
            com2rel.setVisibility(View.GONE);
            com3rel.setVisibility(View.GONE);
            reladdcompany.setVisibility(View.VISIBLE);
        } else if (receivedUser.getCom3name().equals("") || receivedUser.getCom3name().equals(" "))
            com3rel.setVisibility(View.GONE);
        reladdcompany.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (!menuItem.isChecked()) {
            menuItem.setChecked(true);
            menuItem.setEnabled(true);
        } else {
            menuItem.setChecked(false);
            menuItem.setEnabled(false);
        }
        switch (menuItem.getItemId()) {
            case R.id.item_personal:
                startActivity(new Intent(this, UserProfile.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_findjob:
                startActivity(new Intent(this, JobFinder.class));
                break;
            case R.id.item_accountsettings:
                startActivity(new Intent(this, AccountSettings.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void arrowPressed(View view) {
        switch (view.getId()) {
            case R.id.whdown:
                if (findViewById(R.id.com1master).getVisibility() == View.GONE) {
                    com1master.setVisibility(View.VISIBLE);
                    com2master.setVisibility(View.VISIBLE);
                    com3master.setVisibility(View.VISIBLE);
                    addcompanymaster.setVisibility(View.VISIBLE);
                    whdown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                } else {
                    com1master.setVisibility(View.GONE);
                    com2master.setVisibility(View.GONE);
                    com3master.setVisibility(View.GONE);
                    addcompanymaster.setVisibility(View.GONE);
                    whdown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                }
                break;
            case R.id.com1down:
                if (findViewById(R.id.com1name).getVisibility() == View.GONE) {
                    com1down.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                    com1name.setVisibility(View.VISIBLE);
                    com1pos.setVisibility(View.VISIBLE);
                    com1dates.setVisibility(View.VISIBLE);
                    com1resp.setVisibility(View.VISIBLE);
                } else {
                    com1down.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                    com1name.setVisibility(View.GONE);
                    com1pos.setVisibility(View.GONE);
                    com1dates.setVisibility(View.GONE);
                    com1resp.setVisibility(View.GONE);
                }
                break;

            case R.id.com2down:
                if (findViewById(R.id.com2name).getVisibility() == View.GONE) {
                    com2down.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                    com2name.setVisibility(View.VISIBLE);
                    com2pos.setVisibility(View.VISIBLE);
                    com2dates.setVisibility(View.VISIBLE);
                    com2resp.setVisibility(View.VISIBLE);
                } else {
                    com2down.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                    com2name.setVisibility(View.GONE);
                    com2pos.setVisibility(View.GONE);
                    com2dates.setVisibility(View.GONE);
                    com2resp.setVisibility(View.GONE);
                }
                break;

            case R.id.com3down:
                if (findViewById(R.id.com3name).getVisibility() == View.GONE) {
                    com3down.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                    com3name.setVisibility(View.VISIBLE);
                    com3pos.setVisibility(View.VISIBLE);
                    com3dates.setVisibility(View.VISIBLE);
                    com3resp.setVisibility(View.VISIBLE);
                } else {
                    com3down.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                    com3name.setVisibility(View.GONE);
                    com3pos.setVisibility(View.GONE);
                    com3dates.setVisibility(View.GONE);
                    com3resp.setVisibility(View.GONE);
                }
                break;
            case R.id.pddown:
                if (findViewById(R.id.workPosition).getVisibility() == View.GONE) {
                    pddown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                    position.setVisibility(View.VISIBLE);
                    experience.setVisibility(View.VISIBLE);
                    curloc.setVisibility(View.VISIBLE);
                    desloc.setVisibility(View.VISIBLE);
                } else {
                    pddown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                    position.setVisibility(View.GONE);
                    experience.setVisibility(View.GONE);
                    curloc.setVisibility(View.GONE);
                    desloc.setVisibility(View.GONE);
                }
        }
    }

    public void editProfessional(View v) {
        final LinearLayout view = (LinearLayout) findViewById(R.id.llviewprof);
        final LinearLayout edit = (LinearLayout) findViewById(R.id.lleditprof);
        final EditText et1, et2, et3, et4;
        Button save = (Button) findViewById(R.id.bSaveEdit);
        Button discard = (Button) findViewById(R.id.bDiscardEdit);
        et1 = (EditText) findViewById(R.id.editPosition);
        et2 = (EditText) findViewById(R.id.editExperience);
        et3 = (EditText) findViewById(R.id.editCurloc);
        et4 = (EditText) findViewById(R.id.editDesloc);
        view.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        et1.setText(receivedUser.getPosition());
        et2.setText(receivedUser.getExperience());
        et3.setText(receivedUser.getCurloc());
        et4.setText(receivedUser.getDesloc());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverRequests.editProfessional(receivedUser.getUsername(), et1.getText().toString(), et2.getText().toString(), et3.getText().toString(), et4.getText().toString());
                position.setText(et1.getText().toString());
                experience.setText(et2.getText().toString());
                curloc.setText(et3.getText().toString());
                desloc.setText(et4.getText().toString());
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public void addCompany(View view) {
        if (com1name.getText().toString().equals("")) comno = "1";
        else if (com2name.getText().toString().equals("")) comno = "2";
        else comno = "3";
        addcompanylayout.setVisibility(View.VISIBLE);
        reladdcompany.setVisibility(View.GONE);
        Button save, discard;
        final EditText etname, etfrom, etto, etpos, etresp;
        etname = (EditText) findViewById(R.id.addcomname);
        etfrom = (EditText) findViewById(R.id.addcomfrom);
        etpos = (EditText) findViewById(R.id.addcompos);
        etto = (EditText) findViewById(R.id.addcomto);
        etresp = (EditText) findViewById(R.id.addcomresp);
        save = (Button) findViewById(R.id.bSaveCom);
        discard = (Button) findViewById(R.id.bDiscardCom);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverRequests.addCompany(receivedUser.getUsername(), etname.getText().toString(), etpos.getText().toString(), etfrom.getText().toString(), etto.getText().toString(), etresp.getText().toString(), comno);
                addcompanylayout.setVisibility(View.GONE);
                reladdcompany.setVisibility(View.VISIBLE);
                if (comno.equals("3")) addcompanymaster.setVisibility(View.GONE);
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcompanylayout.setVisibility(View.GONE);
                reladdcompany.setVisibility(View.GONE);
            }
        });
    }
}


