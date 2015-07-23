package com.studapps.shrikant.novamaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

public class Register_2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner expSpinner;
    AutoCompleteTextView clocSpinner, dlocSpinner, opSpinner;
    User user = new User();
    EditText com1name, com2name, com3name, com1pos, com2pos, com3pos, com1from, com2from, com3from, com1to, com2to, com3to, com1resp, com2resp, com3resp;
    ServerRequests serverRequests;
    UserLocalStore userLocalStore;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        serverRequests = new ServerRequests(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Registration - Step 2");

        userLocalStore = new UserLocalStore(this);


        opSpinner = (AutoCompleteTextView) findViewById(R.id.spinnerOpening);
        expSpinner = (Spinner) findViewById(R.id.spinnerExp);
        clocSpinner = (AutoCompleteTextView) findViewById(R.id.textViewCurloc);
        dlocSpinner = (AutoCompleteTextView) findViewById(R.id.textViewDesLoc);

        com1name = (EditText) findViewById(R.id.com1name);
        com2name = (EditText) findViewById(R.id.com2name);
        com3name = (EditText) findViewById(R.id.com3name);
        com1pos = (EditText) findViewById(R.id.com1pos);
        com2pos = (EditText) findViewById(R.id.com2pos);
        com3pos = (EditText) findViewById(R.id.com3pos);
        com1from = (EditText) findViewById(R.id.com1from);
        com2from = (EditText) findViewById(R.id.com2from);
        com3from = (EditText) findViewById(R.id.com3from);
        com1to = (EditText) findViewById(R.id.com1to);
        com2to = (EditText) findViewById(R.id.com2to);
        com3to = (EditText) findViewById(R.id.com3to);
        com1resp = (EditText) findViewById(R.id.com1resp);
        com2resp = (EditText) findViewById(R.id.com2resp);
        com3resp = (EditText) findViewById(R.id.com3resp);

        String[] openings = {"Sales", "Marketing", "Advertising", "Business Development", "Accounts", "Finance", "Treasury", "Audit", "Compliance", "Taxation", "Procurement", "Manufacturing", "Quality", "IT Networking", "Software Development", "Java", "Database Administration", "SQL server", "Oracle", "mySQL", "Progress", "DB2", "Mainframe Technologies", "HR operations", "Learning", "Talent Management", "OD", "HR Compliances", "Compensation & Benefits", "Storage Management", "IT Infrastructure Mangement", "Big Data", "Web Development", "Analytics", "General Management", "Microsoft Technologies", "SAP", "Oracle ERP", "Peoplesoft", "ERP - Other", "Cloud Technologies", "Administrator - UNIX", "Administrator - AIX", "Administrator - Windows", "Weblogic", "Websphere", "Tuxedo", "Business Intelligence Tools", "Hyperion", "MSBI", "CRM", "Android", "iOS", "AWS", "Microsoft Azure", "Investment Banking", "Corporate Banking", "Retail Banking", "Fund Management", "Wealth Management", "Stock Broking", "Commodity Broking", "Backoffice Jobs", "General Administration", "Teaching", "Others- specify"};
        String[] experience = {"Fresher", "1 year", "2 years", "3 years", "4 years", "5 years", "6 years", "7 years", "8 years", "9 years", "10 years", "11 years", "12 years", "13 years", "14 years", "15 years", "16 years", "17 years", "18 years", "19 years", "20 years", "21 years", "22 years", "23 years", "24 years", "25 years", "More than 25 years"};
        final String[] loclist = {"Mumbai", "Chennai", "New Delhi", "Kolkata", "Bangalore", "Hyderabad", "Allahabad", "Patna", "Jaipur", "Pune", "Noida", "Bhubhaneshwar", "Ahmedabad", "Surat", "Lucknow", "Kanpur", "Nagpur", "Bhopal", "Trichy"};

        ArrayAdapter<String> spinnerOpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, openings);
        ArrayAdapter<String> spinnerExpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, experience);
        ArrayAdapter<String> spinnerLocAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, loclist);

        spinnerOpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opSpinner.setAdapter(spinnerOpAdapter);

        spinnerExpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expSpinner.setAdapter(spinnerExpAdapter);

        spinnerLocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clocSpinner.setAdapter(spinnerLocAdapter);

        spinnerLocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dlocSpinner.setAdapter(spinnerLocAdapter);

        opSpinner.setOnItemSelectedListener(this);
        expSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user.setExperience(removeSpaces(expSpinner.getSelectedItem().toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void RegisterFinal(View view) {
        getAllDetails();
        userLocalStore.storeProfessional(user);
        user = userLocalStore.getPersonalDetails(user);
        user.setImageUri("NA");
        userLocalStore.logOutUser();
        serverRequests.storeUserDatainBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
            }
        });
        startActivity(new Intent(this, UserLogin.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAllDetails() {
        user.setPosition(removeSpaces(opSpinner.getText().toString()));
        user.setCurloc(removeSpaces(clocSpinner.getText().toString()));
        user.setDesloc(removeSpaces(dlocSpinner.getText().toString()));
        user.setCom1name(removeSpaces(com1name.getText().toString()));
        user.setCom1pos(removeSpaces(com1pos.getText().toString()));
        user.setCom1from(removeSpaces(com1from.getText().toString()));
        user.setCom1to(removeSpaces(com1to.getText().toString()));
        user.setCom1resp(removeSpaces(com1resp.getText().toString()));
        user.setCom2name(removeSpaces(com2name.getText().toString()));
        user.setCom2pos(removeSpaces(com2pos.getText().toString()));
        user.setCom2from(removeSpaces(com2from.getText().toString()));
        user.setCom2to(removeSpaces(com2to.getText().toString()));
        user.setCom2resp(removeSpaces(com2resp.getText().toString()));
        user.setCom3name(removeSpaces(com3name.getText().toString()));
        user.setCom3pos(removeSpaces(com3pos.getText().toString()));
        user.setCom3from(removeSpaces(com3from.getText().toString()));
        user.setCom3to(removeSpaces(com3to.getText().toString()));
        user.setCom3resp(removeSpaces(com3resp.getText().toString()));
    }

    String removeSpaces(String string) {
        string = string.trim();
        string = string.replace(' ', '+');
        return string;

    }
}

