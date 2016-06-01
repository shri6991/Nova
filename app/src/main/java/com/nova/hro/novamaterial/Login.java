package com.nova.hro.novamaterial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    UserLocalStore userLocalStore;
    TextView regbutton;
    String email;
    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;
    User userToLogin;
    ServerRequests serverRequests;
    EditText etun, etpass, resetEmail, resetUsername;
    Button blogin, resetButton;
    Context context;
    String username, password = "";

    private static String randomPasswordGenerate() {
        Random generator = new Random();
        String set = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String password = "";
        char tempChar;
        for (int i = 0; i < 8; i++) {
            tempChar = set.charAt(generator.nextInt(62));
            password += tempChar;
        }
        return password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        context = this.getApplicationContext();
        setupGoogleSignIn();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        serverRequests = new ServerRequests(this);
        userLocalStore = new UserLocalStore(this);
        if (authenticateifLoggedIn()) {
            startActivity(new Intent(this, UserProfile.class));
        }

        resetUsername = (EditText) findViewById(R.id.resetUsername);
        resetEmail = (EditText) findViewById(R.id.resetEmail);
        etun = (EditText) findViewById(R.id.log2etun);
        regbutton = (TextView) findViewById(R.id.log2register);
        etpass = (EditText) findViewById(R.id.log2etpass);

        resetButton = (Button) findViewById(R.id.resetButton);
        blogin = (Button) findViewById(R.id.log2login);

        blogin.setOnClickListener(this);
        regbutton.setOnClickListener(this);

        //Txtchanged listener to enable Login button only after password is entered
        etpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                blogin.setEnabled(!etpass.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //Disables the back button
    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log2login:
                username = etun.getText().toString();
                password = etpass.getText().toString();
                //If user wants to continue being logged in
                userToLogin = new User(username, password);
                //Select corresponding user from the database after passing username(ONLINE DB)
                serverRequests.fetchUserDatafromBackground(userToLogin, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser == null) ;
                        else if (returnedUser.getUsername().equals("admin"))
                            startActivity(new Intent(Login.this, AdminPanel.class));
                        else {
                            userLocalStore.putAllDetails(returnedUser);
                            logUserIn();
                        }
                    }
                });
                break;
            case R.id.log2register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    public boolean authenticateifLoggedIn() {
        System.out.println("Authenticating");
        User user = userLocalStore.getAllDetails();
        System.out.println("Got " + user.getUsername());
        return (user.getUsername() != null && (!user.getUsername().equals("")));
    }

    public void logUserIn() {
        Intent i = new Intent(this, UserProfile.class);
        startActivity(i);
    }

    public void startResetProcess(View view) {
        resetButton.setVisibility(View.VISIBLE);
        resetEmail.setVisibility(View.VISIBLE);
        resetUsername.setVisibility(View.VISIBLE);
    }

    public void resetPassword(View view) {
        String newpass = randomPasswordGenerate();
        newpass = newpass.trim();
        System.out.println(newpass);
        email = resetEmail.getText().toString();
        username = resetUsername.getText().toString();
        resetEmail.setHint("Enter Email ID");
        resetEmail.setHintTextColor(Color.LTGRAY);
        if (email.length() == 0) {
            resetEmail.setText("");
            resetEmail.setHintTextColor(Color.RED);
            resetEmail.setHint("Enter a valid Email ID");
            return;
        }
        if ((email.length() > 0))
            if (!((email.contains("@")) && ((email.endsWith(".com") || email.endsWith("co.in") || email.endsWith(".org") || email.endsWith(".biz") || email.endsWith(".co.uk") || email.endsWith(".ac.in"))))) {
                resetEmail.setText("");
                resetEmail.setHintTextColor(Color.RED);
                resetEmail.setHint("Enter a valid Email ID");
                return;
            }
        serverRequests.resetPassword(username, resetEmail.getText().toString(), newpass, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this)
                        .setTitle("Password Reset!")
                        .setMessage("Please check your registered Email ID for further instructions.")
                        .setInverseBackgroundForced(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                resetEmail.setText("");
                resetButton.setVisibility(View.GONE);
                resetEmail.setVisibility(View.GONE);

            }
        });
    }

    public void setupGoogleSignIn() {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        Intent gLogin = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(gLogin, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                String userEmail = account.getEmail();
                String scope = "oauth2: profile email";
                try {
                    String token = GoogleAuthUtil.getToken(Login.this, userEmail, scope);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
