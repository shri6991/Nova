package com.nova.hro.novamaterial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    User receivedUser;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    ServerRequests serverRequests;
    int flag;
    Bitmap returndecoded;
    UserLocalStore userLocalStore;
    TextView headerText, displayedUsername, displayedEmail, displayedAge, displayedPhone;
    ImageView headerImageView, captureImageView;
    int editMode = 0;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newprofile);
        flag = 0;
        serverRequests = new ServerRequests(this);
        userLocalStore = new UserLocalStore(this);
        receivedUser = userLocalStore.getAllDetails();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(receivedUser.getName());
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        navView = (NavigationView) findViewById(R.id.nav_drawer);
        navView.setNavigationItemSelectedListener(this);
        View header = navView.getHeaderView(0);
        headerText = (TextView) header.findViewById(R.id.headerText);
        headerText.setText(receivedUser.getName());
        headerImageView = (ImageView) header.findViewById(R.id.headerImage);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_userprofile);
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_closed);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        new importimageasync().execute();
        captureImageView = (ImageView) findViewById(R.id.img);
        displayedAge = (TextView) findViewById(R.id.profileAge);
        displayedEmail = (TextView) findViewById(R.id.profileEmail);
        displayedPhone = (TextView) findViewById(R.id.profilePhone);
        displayedUsername = (TextView) findViewById(R.id.profileUsername);

        displayedUsername.setText("      " + receivedUser.getUsername());
        displayedAge.setText("      " + receivedUser.getAge() + " years");
        displayedPhone.setText("      +91 " + receivedUser.getPhone());
        displayedEmail.setText("      " + receivedUser.getEmail());
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
            case R.id.item_professional:
                startActivity(new Intent(this, UserProfile2.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_accountsettings:
                startActivity(new Intent(this, AccountSettings.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_findjob:
                startActivity(new Intent(this, JobFinder.class));
                break;
            case R.id.item_logout:
                userLocalStore.logOutUser();
                startActivity(new Intent(this, Login.class));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String encoded = "";
            Uri PhotoUri = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(PhotoUri);
                Bitmap yourBitmap = BitmapFactory.decodeStream(is);

                int scale = setScaleFactor(yourBitmap.getWidth(), yourBitmap.getHeight());

                final Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, (yourBitmap.getWidth() / scale), (yourBitmap.getHeight() / scale), true);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.URL_SAFE);
                System.out.println("Encoded the string");
                userLocalStore.updateImage(encoded);
                serverRequests.updateDPinBackground(encoded, receivedUser.getUsername(), new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser != null) {
                            System.out.println("Not null");
                            returndecoded = decodeBase64(receivedUser.getImageUri());
                            flag = 10;
                            captureImageView.setImageBitmap(resized);
                            Bitmap resized1 = RoundedImageView.getCroppedBitmap(resized, 35);
                            headerImageView.setImageBitmap(resized1);
                        } else
                            Toast.makeText(getApplication(), "Image Update unsuccessful", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void SetImage(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    public void removeImage(View view) {
        serverRequests.updateDPinBackground("NA", receivedUser.getUsername(), new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
            }
        });
        captureImageView.setImageResource(R.drawable.drawable);
    }

    public int setScaleFactor(int width, int height) {
        if (width > 3500 || height > 3500) return 9;
        if (width >= 3000 || height >= 3000) return 8;
        if (width >= 2500 || height >= 2500) return 7;
        if (width >= 2000 || height >= 2000) return 6;
        if (width >= 1500 || height >= 1500) return 4;
        if (width >= 1000 || height >= 1000) return 3;
        if (width >= 500 || height >= 500) return 2;
        return 1;
    }

    public void editEmail(View view) {
        final EditText editEmail;
        final ImageView pencil, tick;
        pencil = (ImageView) findViewById(R.id.imgeditemail);
        tick = (ImageView) findViewById(R.id.imgconfirmemail);
        pencil.setVisibility(View.GONE);
        tick.setVisibility(View.VISIBLE);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editEmail.setVisibility(View.VISIBLE);
        displayedEmail.setVisibility(View.GONE);
        editEmail.setText(receivedUser.getEmail());
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = 1;
                serverRequests.editGeneric(receivedUser.getUsername(), "email", editEmail.getText().toString());
                editEmail.setVisibility(View.GONE);
                displayedEmail.setVisibility(View.VISIBLE);
                tick.setVisibility(View.GONE);
                pencil.setVisibility(View.VISIBLE);
            }
        });
    }

    public void editPhone(View view) {
        final EditText editPhone;
        final ImageView pencil, tick;
        pencil = (ImageView) findViewById(R.id.imgeditphone);
        tick = (ImageView) findViewById(R.id.imgconfirmphone);
        pencil.setVisibility(View.GONE);
        tick.setVisibility(View.VISIBLE);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPhone.setVisibility(View.VISIBLE);
        displayedPhone.setVisibility(View.GONE);
        editPhone.setText(receivedUser.getPhone());
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = 1;
                serverRequests.editGeneric(receivedUser.getUsername(), "phone", editPhone.getText().toString());
                editPhone.setVisibility(View.GONE);
                displayedPhone.setVisibility(View.VISIBLE);
                tick.setVisibility(View.GONE);
                pencil.setVisibility(View.VISIBLE);
            }
        });
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
                captureImageView.setImageBitmap(decoded);
                decoded = RoundedImageView.getCroppedBitmap(decoded, 35);
                headerImageView.setImageBitmap(decoded);
            } else
                captureImageView.setImageResource(R.drawable.drawable);
            super.onPostExecute(aVoid);
        }
    }
}
