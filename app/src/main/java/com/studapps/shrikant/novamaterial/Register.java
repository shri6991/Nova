package com.studapps.shrikant.novamaterial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;


public class Register extends AppCompatActivity implements View.OnClickListener {

    User user = new User();
    ServerRequests serverRequests;
    UserLocalStore userLocalStore;
    ScrollView scrollView;
    EditText etun, etpass, etmail, etage, retpass, etphone, etname;
    Button bSave;
    Toolbar toolbar;
    String username, name, password, email, age, phone = " ";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        serverRequests = new ServerRequests(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Registration - Step 1");

        scrollView = (ScrollView) findViewById(R.id.scrollViewR);

        userLocalStore = new UserLocalStore(this);

        etun = (EditText) findViewById(R.id.etun);
        etpass = (EditText) findViewById(R.id.etpass);
        retpass = (EditText) findViewById(R.id.retpass);
        etmail = (EditText) findViewById(R.id.etmail);
        etage = (EditText) findViewById(R.id.etage);
        etphone = (EditText) findViewById(R.id.etphone);
        etname = (EditText) findViewById(R.id.etname);

        bSave = (Button) findViewById(R.id.bSave);
        bSave.setOnClickListener(this);

        //To check username after Username text box loses focus
        etun.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    username = etun.getText().toString();
                    if (etun.getText().toString().equals("")) {
                        etun.setHint("Enter Username");
                        etun.setHintTextColor(Color.GRAY);
                    }
                    if (username.length() > 0)
                        if ((serverRequests.checkUsernameAvailabilty(username, new GetUserCallBack() {
                            @Override
                            public void done(User user) {
                                if (!user.getUsername().equals("")) {
                                    etun.setText("");
                                    etun.setHintTextColor(Color.RED);
                                    etun.setHint("Username unavailable");
                                }
                            }
                        }))) ;
                }
            }
        });

        //Check password validity(length etc) after it loses focus
        etpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String temppass = etpass.getText().toString();
                    temppass = temppass.trim();
                    int l = temppass.length();
                    if (temppass.equals("")) {
                        etpass.setHint("Enter Password");
                        etpass.setHintTextColor(Color.GRAY);
                    } else if (l < 8) {
                        etpass.setText("");
                        etpass.setHintTextColor(Color.RED);
                        etpass.setHint("Password must contain atleast 8 characters");
                    }
                }
            }
        });

        //Check if re entered password matches the original
        retpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String pass, repass;
                    repass = retpass.getText().toString();
                    pass = etpass.getText().toString();
                    if (retpass.getText().toString().equals("")) {
                        retpass.setHint("Re-enter Password");
                        retpass.setHintTextColor(Color.GRAY);
                    } else if (!(pass.equals(repass))) {
                        retpass.setText("");
                        retpass.setHintTextColor(Color.RED);
                        retpass.setHint("Does not match entered Password");
                    }

                }
            }
        });

        //Check email id validity
        etmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    email = etmail.getText().toString();
                    if (etmail.getText().toString().equals("")) {
                        etmail.setHint("Enter Email ID");
                        etmail.setHintTextColor(Color.GRAY);
                    } else if (!isEmailValid(email)) {
                        etmail.setText("");
                        etmail.setHintTextColor(Color.RED);
                        etmail.setHint("Enter a valid Email ID");
                    }
                }
            }
        });

        etage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etage.getText().toString().equals("")) {
                        etage.setHint("Enter Age");
                        etage.setHintTextColor(Color.GRAY);
                    } else if (Integer.parseInt(etage.getText().toString()) > 120 || Integer.parseInt(etage.getText().toString()) == 0) {
                        etage.setText("");
                        etage.setHint("Age must be between 1-120");
                        etage.setHintTextColor(Color.RED);
                    }
                }
            }
        });
        //To enable register button only after age is entered
        etphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bSave.setEnabled(!(etphone.getText().toString().trim().equals("")));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void writeDatatoLocalStore() {
        getInputData();
        trimAll();
        if (checkFieldsInvalid()) {
            buildFieldEmptyAlert();
            return;
        }
        user = new User(username, name, password, email, age, phone);
        userLocalStore.storePersonal(user);
        startActivity(new Intent(this, Register_2.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSave:
                writeDatatoLocalStore();
                break;
        }
    }

    public boolean checkFieldsInvalid() {
        if (name.isEmpty() || age.isEmpty() || password.isEmpty() || username.isEmpty() || email.isEmpty() || (phone.length() != 10)) {
            if (phone.length() != 10) {
                etphone.setText("");
                etphone.setHintTextColor(Color.RED);
                etphone.setHint("Enter a valid Mobile number");
            }
            return true;
        }
        return false;
    }

    private void getInputData() {
        username = etun.getText().toString();
        name = etname.getText().toString();
        password = etpass.getText().toString();
        age = etage.getText().toString();
        email = etmail.getText().toString();
        phone = etphone.getText().toString();
    }

    public void buildFieldEmptyAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Cannot Proceed!")
                .setMessage("One or more fields are blank/invalid")
                .setInverseBackgroundForced(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void trimAll() {
        username = removeSpaces(username);
        name = removeSpaces(name);
        password = removeSpaces(password);
        age = removeSpaces(age);
        email = removeSpaces(email);
        phone = removeSpaces(phone);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
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

    String removeSpaces(String string) {
        string = string.trim();
        string = string.replace(' ', '+');
        return string;

    }

}