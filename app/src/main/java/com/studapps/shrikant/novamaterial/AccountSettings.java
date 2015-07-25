package com.studapps.shrikant.novamaterial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountSettings extends AppCompatActivity {

    ImageView asresetUp, asDeleteDown, asContactDown, asAboutDown, asImageView;
    EditText oldpassword, newpassword, messagesubject, messagebody;
    TextView asname, asphone, asemail, asage;
    ServerRequests serverRequests;
    User receivedUser;
    UserLocalStore userLocalStore;
    Toolbar toolbar;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        serverRequests = new ServerRequests(this);
        userLocalStore = new UserLocalStore(this);
        receivedUser = userLocalStore.getAllDetails();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Account Settings ");

        new importimageasync().execute();

        asresetUp = (ImageView) findViewById(R.id.asResetUp);
        asDeleteDown = (ImageView) findViewById(R.id.asDeleteDown);
        asContactDown = (ImageView) findViewById(R.id.asContactDown);
        asAboutDown = (ImageView) findViewById(R.id.asAboutDown);
        asImageView = (ImageView) findViewById(R.id.asImageView);

        asname = (TextView) findViewById(R.id.asname);
        asphone = (TextView) findViewById(R.id.asphone);
        asage = (TextView) findViewById(R.id.asage);

        oldpassword = (EditText) findViewById(R.id.asetopass);
        newpassword = (EditText) findViewById(R.id.asetnpass);
        messagesubject = (EditText) findViewById(R.id.asetsubject);
        messagebody = (EditText) findViewById(R.id.asetbody);

        asname.setText(receivedUser.getName());
        asphone.setText("+91 " + receivedUser.getPhone());
        asage.setText(receivedUser.getAge() + " years");


        //Check password validity(length etc) after it loses focus
        newpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String temppass = newpassword.getText().toString();
                    temppass = temppass.trim();
                    int l = temppass.length();
                    if (l == 0) {
                        newpassword.setHint("Enter New Password");
                        newpassword.setHintTextColor(Color.LTGRAY);
                    } else if (l < 8) {
                        newpassword.setText("");
                        newpassword.setHintTextColor(Color.RED);
                        newpassword.setHint("Password must contain atleast 8 characters");
                    }
                }
            }
        });


    }

    public void changePassword(View view) {

        if (newpassword.getText().toString().equals("") || oldpassword.getText().toString().equals("") || newpassword.getText().toString().length() < 8 || oldpassword.getText().toString().length() < 8) {
            negativeAlert();
            return;
        }

        serverRequests.updatePassword(oldpassword.getText().toString(), newpassword.getText().toString(), new GetUserCallBack() {

            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    negativeAlert();
                } else positiveAlert();
            }
        });
        resetAllFields();
    }

    public void sendEmail(View view) {
        if (messagebody.getText().toString().equals("") || messagesubject.getText().toString().equals("")) {
            negativeAlert();
            return;
        }

        serverRequests.sendEmail(receivedUser.getEmail(), messagesubject.getText().toString(), messagebody.getText().toString(), receivedUser.getName(), new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                positiveAlert();
            }
        });

        resetAllFields();
    }

    public void deleteAccount(View view) {
        serverRequests.deleteUser(receivedUser.getUsername(), new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                positiveAlert();
            }
        });
        userLocalStore.logOutUser();
        startActivity(new Intent(AccountSettings.this, UserLogin.class));
    }

    public void showAbout(View view) {

    }

    public void positiveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettings.this).setTitle("Successful!")
                .setMessage("The task has been completed successfully")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void negativeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettings.this).setTitle("Unsuccessful!")
                .setMessage("Please check the data you entered and try again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void resetAllFields() {
        oldpassword.setText("");
        newpassword.setText("");
        messagebody.setText("");
        messagesubject.setText("");
        oldpassword.setHint("Enter Old Password");
        newpassword.setHint("Enter New Password");
        messagebody.setHint("Enter Message Body");
        messagesubject.setHint("Enter Message Subject");
    }

    public void resetAllFields(View view) {
        oldpassword.setText("");
        newpassword.setText("");
        messagebody.setText("");
        messagesubject.setText("");
        oldpassword.setHint("Enter Old Password");
        newpassword.setHint("Enter New Password");
        messagebody.setHint("Enter Message Body");
        messagesubject.setHint("Enter Message Subject");
    }

    public void arrowPressed(View view) {
        switch (view.getId()) {
            case R.id.asResetUp:
                if (findViewById(R.id.resetrel).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.resetrel).setVisibility(View.GONE);
                    asresetUp.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                } else {
                    findViewById(R.id.resetrel).setVisibility(View.VISIBLE);
                    asresetUp.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                }
                break;
            case R.id.asContactDown:
                if (findViewById(R.id.contactrel).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.contactrel).setVisibility(View.GONE);
                    asContactDown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                } else {
                    findViewById(R.id.contactrel).setVisibility(View.VISIBLE);
                    asContactDown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                }
                break;
            case R.id.asDeleteDown:
                if (findViewById(R.id.bdelaccount).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.bdelaccount).setVisibility(View.GONE);
                    asDeleteDown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                } else {
                    findViewById(R.id.bdelaccount).setVisibility(View.VISIBLE);
                    asDeleteDown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                }
                break;
            case R.id.asAboutDown:
                if (findViewById(R.id.aboutText).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.aboutText).setVisibility(View.GONE);
                    asAboutDown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                } else {
                    findViewById(R.id.aboutText).setVisibility(View.VISIBLE);
                    asAboutDown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                }


        }

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

    private class importimageasync extends AsyncTask<Void, Void, Void> {

        Bitmap decoded;

        @Override
        protected Void doInBackground(Void... params) {
            if (!(receivedUser.getImageUri().equals("NA"))) {
                decoded = decodeBase64(receivedUser.getImageUri());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (decoded != null) {
                asImageView.setImageBitmap(decoded);
            } else
                asImageView.setImageResource(R.drawable.drawable);
            super.onPostExecute(aVoid);
        }
    }

}