package com.nova.hro.novamaterial;

/**
 * Created by shrikant on 6/21/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServerRequests {
    private static final String SERVER_ADDRESS = "http://shrikantbhaskar.comlu.com/";
    private final int CONNECTION_TIMEOUT = 15 * 1000;
    private final String CONNECTION_ERROR = "Cannot connect to the server, please check your connection and try again";
    ProgressDialog progressDialog, progressDialog1;
    String serverresponse;
    Context context;
    UserLocalStore userLocalStore;
    Activity parentActivity;
    UserProfile userProfile;

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog1 = new ProgressDialog(context);
        progressDialog1.setIndeterminate(false);
        progressDialog1.setTitle("Uploading image");
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        userProfile = new UserProfile();
        this.context = context;
        this.userLocalStore = new UserLocalStore(context);
        parentActivity = (Activity) context;
    }

    public boolean checkUsernameAvailabilty(String username, GetUserCallBack getUserCallBack) {
        new checkUsernameAvailablilityAsync(username, getUserCallBack).execute();
        return true;
    }

    public void storeUserDatainBackground(User user, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, getUserCallBack).execute();
    }

    public void fetchUserDatafromBackground(User user, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, getUserCallBack).execute();
    }

    public void updateDPinBackground(String imageUri, String username, GetUserCallBack getUserCallBack) {
        progressDialog1.show();
        progressDialog1.setTitle("Uploading image");
        progressDialog1.setMax(100);
        progressDialog1.setProgress(0);
        new updateImageinBackgroundAsync(imageUri, username, getUserCallBack).execute();

    }

    public void resetPassword(String username, String email, String password, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new resetPasswordAsync(username, email, password, getUserCallBack).execute();
    }

    public void updatePassword(String opassword, String npassword, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new updatePasswordAsync(opassword, npassword, getUserCallBack).execute();
    }

    public void sendEmail(String email, String subject, String body, String name, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new sendEmailAsync(email, subject, body, name, getUserCallBack).execute();
    }

    public void deleteUser(String username, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new deleteAccountAsync(username, getUserCallBack).execute();
    }

    public void addJob(Job job, GetJobCallBack getJobCallBack) {
        progressDialog.show();
        new addJobAsync(job, getJobCallBack).execute();
    }

    public void updateJob(Job job, GetJobCallBack getJobCallBack) {
        progressDialog.show();
        new updateJobAsync(job, getJobCallBack).execute();
    }

    public void deleteJob(String ID, GetJobCallBack getJobCallBack) {
        progressDialog.show();
        new deleteJobAsync(ID, getJobCallBack).execute();
    }

    public void fetchUsersQuery(String searchTerm, GetUserObjectsCallBack getUserObjectsCallBack) {
        progressDialog.show();
        new fetchUserQueryAsync(searchTerm, getUserObjectsCallBack).execute();
    }

    public void fetchJobListings(String userPosition, String userExperience, GetJobObjectsCallBack getJobObjectsCallBack) {
        progressDialog.show();
        new fetchJobListingsAsync(userPosition, userExperience, getJobObjectsCallBack).execute();
    }

    public void searchJobListings(String searchTerm, GetJobObjectsCallBack getJobObjectsCallBack) {
        progressDialog.show();
        new searchJobListingsAsync(searchTerm, getJobObjectsCallBack).execute();
    }

    public void applyForJob(String email, String name, String ID, String position) {
        progressDialog.show();
        new applyForJobAsync(email, name, ID, position).execute();
    }

    public void editProfessional(String username, String position, String experience, String curloc, String desloc) {
        progressDialog.show();
        new editProfessionalAsync(username, position, experience, curloc, desloc).execute();
    }

    public void editGeneric(String username, String editable, String variable) {
        progressDialog.show();
        new editGenericAsync(username, editable, variable).execute();
    }

    public void addCompany(String username, String comname, String compos, String comfrom, String comto, String comresp, String comno) {
        progressDialog.show();
        new addCompanyAsync(username, comname, compos, comfrom, comto, comresp, comno).execute();
    }

    public String getStringfromContentValues(ContentValues dataToSend) {
        String newpostedData = "";
        String postedData = dataToSend.toString().trim();
        for (int i = 0; i < postedData.length(); i++) {
            if (postedData.charAt(i) == ' ' && postedData.charAt(i + 1) != '&')
                newpostedData += postedData.charAt(i);
            else if (postedData.charAt(i) == ' ' && postedData.charAt(++i) != '&') continue;
            else newpostedData += postedData.charAt(i);
        }
        return newpostedData;
    }

    public void positiveAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Successful!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void negativeAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Unsuccessful!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    String getSpaces(String string) {
        string = string.trim();
        string = string.replace('+', ' ');
        return string;

    }


    private class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallBack getUserCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallBack getUserCallBack) {
            this.user = user;
            this.getUserCallback = getUserCallBack;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            else {
                Toast toast = Toast.makeText(context, "Registered Successfully! Login to continue", Toast.LENGTH_LONG);
                toast.show();
            }
            getUserCallback.done(null);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&username", user.getUsername().trim());
            dataToSend.put("&password", user.getPassword().trim());
            dataToSend.put("&name", user.getName().trim());
            dataToSend.put("&email", user.getEmail().trim());
            dataToSend.put("&age", user.getAge().trim());
            dataToSend.put("&phone", user.getPhone().trim());
            dataToSend.put("&position", user.getPosition().trim());
            dataToSend.put("&experience", user.getExperience().trim());
            dataToSend.put("&curloc", user.getCurloc().trim());
            dataToSend.put("&desloc", user.getDesloc().trim());
            dataToSend.put("&imageuri", user.getImageUri().trim());
            dataToSend.put("&com1name", user.getCom1name().trim());
            dataToSend.put("&com1pos", user.getCom1pos().trim());
            dataToSend.put("&com1from", user.getCom1from().trim());
            dataToSend.put("&com1to", user.getCom1to().trim());
            dataToSend.put("&com1resp", user.getCom1resp().trim());
            dataToSend.put("&com2name", user.getCom2name().trim());
            dataToSend.put("&com2pos", user.getCom2pos().trim());
            dataToSend.put("&com2from", user.getCom2from().trim());
            dataToSend.put("&com2to", user.getCom2to().trim());
            dataToSend.put("&com2resp", user.getCom2resp().trim());
            dataToSend.put("&com3name", user.getCom3name().trim());
            dataToSend.put("&com3pos", user.getCom3pos().trim());
            dataToSend.put("&com3from", user.getCom3from().trim());
            dataToSend.put("&com3to", user.getCom3to().trim());
            dataToSend.put("&com3resp", user.getCom3resp().trim());

            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            URL url;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "AddUser.php");

                //setup connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //setup posting the data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        serverresponse += line;
                    }
                    System.out.println("RESPONSE = " + serverresponse);
                } else {
                    serverresponse = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack getUserCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallBack getUserCallBack) {
            this.user = user;
            this.getUserCallback = getUserCallBack;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            else if (returnedUser == null) negativeAlert("Invalid username/password");
            getUserCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }

        @Override
        protected User doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&username", user.getUsername().trim());
            dataToSend.put("&password", user.getPassword().trim());
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            URL url;
            JSONObject jsonObject;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "FetchUserData.php");

                //setup connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //setup posting the data
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                System.out.println("Writing");
                writer.write(postedData);
                writer.flush();
                writer.close();
                System.out.println("Written");
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        serverresponse += line;
                    }
                    jsonObject = new JSONObject(serverresponse);
                    if (jsonObject.length() == 0) return null;
                    else {
                        System.out.println("RESPONSE = " + serverresponse);//"+" + jsonObject.getString("com1name") + "+" + jsonObject.getString("com2name") + "+" + jsonObject.getString("com3name"));
                        return new User(getSpaces(jsonObject.getString("username")), getSpaces(jsonObject.getString("name")), getSpaces(jsonObject.getString("password")), getSpaces(jsonObject.getString("email")), getSpaces(jsonObject.getString("age")), getSpaces(jsonObject.getString("phone")), getSpaces(jsonObject.getString("position")), getSpaces(jsonObject.getString("experience")), getSpaces(jsonObject.getString("curloc")), getSpaces(jsonObject.getString("desloc")), jsonObject.getString("imageuri"), getSpaces(jsonObject.getString("com1name")), getSpaces(jsonObject.getString("com1pos")), getSpaces(jsonObject.getString("com1from")), getSpaces(jsonObject.getString("com1to")), getSpaces(jsonObject.getString("com1resp")), getSpaces(jsonObject.getString("com2name")), getSpaces(jsonObject.getString("com2pos")), getSpaces(jsonObject.getString("com2from")), getSpaces(jsonObject.getString("com2to")), getSpaces(jsonObject.getString("com1resp")), getSpaces(jsonObject.getString("com3name")), getSpaces(jsonObject.getString("com3pos")), getSpaces(jsonObject.getString("com3from")), getSpaces(jsonObject.getString("com3to")), getSpaces(jsonObject.getString("com3resp")));
                    }
                } else serverresponse = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class updateImageinBackgroundAsync extends AsyncTask<Void, Integer, User> {
        GetUserCallBack userCallBack;
        String username;
        String imageuri;

        updateImageinBackgroundAsync(String imageuri, String username, GetUserCallBack userCallBack) {
            this.userCallBack = userCallBack;
            this.imageuri = imageuri;
            this.username = username;
        }

        @Override
        protected User doInBackground(Void... params) {
            System.out.println("Putting Data");
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&imageuri", imageuri);
            dataToSend.put("&username", username.trim());
            System.out.println(username);
            String postedData = "username=" + username + "&imageuri=" + imageuri;
            URL url;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "UpdateImage.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(conn.getOutputStream(), "UTF-8")));
                System.out.println("Writing");
                bw.write(postedData);
                bw.flush();
                bw.close();
                System.out.println("Written");
                publishProgress(25);
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    System.out.println("response ok");
                    publishProgress(50);
                    InputStream is = new BufferedInputStream(conn.getInputStream());
                    //is.mark(99999999);
                    //BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    /*float c = 0;
                    /float k = 0;
                    while (br.readLine() != null) {
                        c++;
                        //System.out.println("\ncounting\n" + c);
                    }
                    is.reset(); */
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
                    while (br1.readLine() != null) {
                        //k++;
                        line = br1.readLine();
                        //System.out.println("Reading response");
                        serverresponse += line;
                        //publishProgress(50 + (int) ((k / c) * 50));
                        //System.out.print("published progress" + " k,c = " + k + " " + c + "  " + ((int) ((k / c) * 100)) + "\n");
                    }
                    publishProgress(100);
                    System.out.println("Response = " + serverresponse);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("failure");
            }
            return new User(username, username);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog1.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            progressDialog1.dismiss();
            if (serverresponse.equals("") || serverresponse.startsWith("<")) {
                negativeAlert(CONNECTION_ERROR);
                userCallBack.done(null);
            } else
                userCallBack.done(user);
            System.out.println("Gave callback");
            super.onPostExecute(user);
        }
    }

    private class checkUsernameAvailablilityAsync extends AsyncTask<Void, Void, User> {

        String username;
        GetUserCallBack getUserCallBack;

        checkUsernameAvailablilityAsync(String username, GetUserCallBack getUserCallBack) {
            this.username = username;
            this.getUserCallBack = getUserCallBack;
        }

        @Override
        protected void onPostExecute(User user) {
            getUserCallBack.done(user);
            super.onPostExecute(user);
        }

        @Override
        protected User doInBackground(Void... params) {
            String unm = "";
            String password = " ";
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&username", username.trim());
            String postedData = getStringfromContentValues(dataToSend);
            URL url;

            try {
                url = new URL(SERVER_ADDRESS + "CheckUsername.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setReadTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setRequestMethod("POST");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                System.out.println("Writing");
                writer.write(postedData);
                writer.flush();
                writer.close();

                System.out.println("Written");
                System.out.println(postedData);
                serverresponse = "";
                int responseCode = httpURLConnection.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    System.out.println("Reading");
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                JSONObject jsonObject = new JSONObject(serverresponse);
                if (jsonObject.length() == 0) return null;
                else {
                    System.out.println("Got JSON" + unm + password);
                    unm = jsonObject.getString("username");
                    password = jsonObject.getString("password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new User(unm, password);
        }
    }

    private class resetPasswordAsync extends AsyncTask<Void, Void, Void> {
        String email, password, username;
        GetUserCallBack getUserCallBack;

        resetPasswordAsync(String username, String email, String password, GetUserCallBack getUserCallBack) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.getUserCallBack = getUserCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues datatosend = new ContentValues();
            datatosend.put("&email", email.trim());
            datatosend.put("&username", username.trim());
            datatosend.put("&password", password.trim());
            serverresponse = "";
            String postedData = getStringfromContentValues(datatosend);
            URL url;
            try {
                url = new URL(SERVER_ADDRESS + "PasswordReset.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
                negativeAlert(CONNECTION_ERROR);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            System.out.println("Done");
            getUserCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }

    private class updatePasswordAsync extends AsyncTask<Void, Void, User> {
        String opass, npass;
        GetUserCallBack getUserCallBack;

        public updatePasswordAsync(String opass, String npass, GetUserCallBack getUserCallBack) {
            this.getUserCallBack = getUserCallBack;
            this.npass = npass;
            this.opass = opass;
        }

        @Override
        protected User doInBackground(Void... params) {
            String username;
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&oldpassword", opass.trim());
            dataToSend.put("&newpassword", npass.trim());
            String postedData = getStringfromContentValues(dataToSend);
            URL url;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "ChangePassword.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                System.out.println("Writing");
                writer.write(postedData);
                writer.flush();
                writer.close();

                System.out.println("Written");

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    String line;
                    System.out.println("Reading");
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                    System.out.println("RESPONSE = " + serverresponse);
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    if (jsonObject.length() == 0) return null;
                    else {
                        System.out.println("Read " + serverresponse);
                        username = jsonObject.getString("username");
                        return new User(username, npass);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            getUserCallBack.done(user);
            super.onPostExecute(user);
        }
    }

    private class sendEmailAsync extends AsyncTask<Void, Void, Void> {
        String email, subject, body, name;
        GetUserCallBack getUserCallBack;

        public sendEmailAsync(String email, String subject, String body, String name, GetUserCallBack getUserCallBack) {
            this.body = body;
            this.email = email;
            this.name = name;
            this.subject = subject;
            this.getUserCallBack = getUserCallBack;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            getUserCallBack.done(null);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&email", email.trim());
            dataToSend.put("&subject", subject.trim());
            dataToSend.put("&body", body.trim());
            dataToSend.put("&name", name.trim());
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            serverresponse = "";
            URL url;
            try {
                url = new URL(SERVER_ADDRESS + "SendEmail.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class deleteAccountAsync extends AsyncTask<Void, Void, Void> {
        String username;
        GetUserCallBack getUserCallBack;

        public deleteAccountAsync(String username, GetUserCallBack getUserCallBack) {
            this.getUserCallBack = getUserCallBack;
            this.username = username;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&username", username.trim());
            URL url;
            String postedData = getStringfromContentValues(dataToSend);
            serverresponse = "";
            try {

                url = new URL(SERVER_ADDRESS + "DeleteUser.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            getUserCallBack.done(null);
            super.onPostExecute(aVoid);
        }


    }

    private class addJobAsync extends AsyncTask<Void, Void, Void> {
        Job job;
        GetJobCallBack getJobCallBack;

        addJobAsync(Job job, GetJobCallBack getJobCallBack) {
            this.getJobCallBack = getJobCallBack;
            this.job = job;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&ID", job.getID().trim());
            dataToSend.put("&experience", job.getExperience().trim());
            dataToSend.put("&type", job.getType().trim());
            dataToSend.put("&position", job.getPosition().trim());
            dataToSend.put("&remarks", job.getRemarks().trim());
            dataToSend.put("&description", job.getDescription().trim());
            dataToSend.put("&location", job.getLocation().trim());
            dataToSend.put("&domain", job.getDomain().trim());
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            URL url;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "AddJob.php");

                //setup connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //setup posting the data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        serverresponse += line;
                    }
                } else {
                    serverresponse = "";
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (!serverresponse.equals(""))
                positiveAlert("Job has been posted to the server successfully!");
            else
                negativeAlert(CONNECTION_ERROR);
            getJobCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }

    private class updateJobAsync extends AsyncTask<Void, Void, Void> {
        Job job;
        GetJobCallBack getJobCallBack;

        updateJobAsync(Job job, GetJobCallBack getJobCallBack) {
            this.getJobCallBack = getJobCallBack;
            this.job = job;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&ID", job.getID().trim());
            dataToSend.put("&experience", job.getExperience().trim());
            dataToSend.put("&type", job.getType().trim());
            dataToSend.put("&domain", job.getDomain().trim());
            dataToSend.put("&position", job.getPosition().trim());
            dataToSend.put("&remarks", job.getRemarks().trim());
            dataToSend.put("&description", job.getDescription().trim());
            dataToSend.put("&location", job.getLocation().trim());
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            System.out.println("dog" + job.getDomain().trim());

            URL url;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "UpdateJob.php");

                //setup connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //setup posting the data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        serverresponse += line;
                    }
                } else {
                    serverresponse = "";
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (!serverresponse.equals(""))
                positiveAlert("Job has been posted to the server successfully!");
            else
                negativeAlert(CONNECTION_ERROR);
            getJobCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }

    private class deleteJobAsync extends AsyncTask<Void, Void, Void> {
        String ID;
        GetJobCallBack getJobCallBack;

        deleteJobAsync(String ID, GetJobCallBack getJobCallBack) {
            this.ID = ID;
            this.getJobCallBack = getJobCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&ID", ID);
            URL url;
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            serverresponse = "";
            try {

                url = new URL(SERVER_ADDRESS + "DeleteJob.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));

                System.out.println("Writing");
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            getJobCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }

    private class fetchUserQueryAsync extends AsyncTask<Void, Void, User[]> {
        User[] returnedUsers;
        GetUserObjectsCallBack getUserObjectsCallBack;
        String searchTerm;

        fetchUserQueryAsync(String searchTerm, GetUserObjectsCallBack getUserObjectsCallBack) {
            this.getUserObjectsCallBack = getUserObjectsCallBack;
            this.searchTerm = searchTerm;
        }

        @Override
        protected User[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&searchterm", "%" + searchTerm + "%");
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            getSpaces(postedData);
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "SearchUser.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                final int size = jsonArray.length();
                returnedUsers = new User[size];
                String un, pd, name, em, age, ph, pos, exp, cl, dl, im, c1n, c1p, c1f, c1t, c1r, c2n, c2p, c2f, c2t, c2r, c3n, c3p, c3f, c3t, c3r;
                for (int i = 0; i < size; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) break;
                    un = jsonObject.getString("username");
                    System.out.println(un);
                    name = jsonObject.getString("name");
                    pd = jsonObject.getString("password");
                    em = jsonObject.getString("email");
                    age = jsonObject.getString("age");
                    cl = jsonObject.getString("curloc");
                    dl = jsonObject.getString("desloc");
                    pos = jsonObject.getString("position");
                    exp = jsonObject.getString("experience");
                    ph = jsonObject.getString("phone");
                    im = "NA";
                    c1n = jsonObject.getString("com1name");
                    c1p = jsonObject.getString("com1pos");
                    c1f = jsonObject.getString("com1from");
                    c1t = jsonObject.getString("com1to");
                    c1r = jsonObject.getString("com1resp");
                    c2p = jsonObject.getString("com2pos");
                    c2n = jsonObject.getString("com2name");
                    c2f = jsonObject.getString("com2from");
                    c2t = jsonObject.getString("com2to");
                    c2r = jsonObject.getString("com2resp");
                    c3n = jsonObject.getString("com3name");
                    c3p = jsonObject.getString("com3pos");
                    c3t = jsonObject.getString("com3to");
                    c3f = jsonObject.getString("com3from");
                    c3r = jsonObject.getString("com3resp");

                    returnedUsers[i] = new User(un, name, pd, em, age, ph, pos, exp, cl, dl, im, c1n, c1p, c1f, c1t, c1r, c2n, c2p, c2f, c2t, c2r, c3n, c3p, c3f, c3t, c3r);
                    System.out.println(returnedUsers[i].getName());
                }
                return returnedUsers;

            } catch (Exception e) {
                e.printStackTrace();

            }
            return returnedUsers;
        }

        @Override
        protected void onPostExecute(User[] returnedUsers) {
            progressDialog.dismiss();
            if (returnedUsers != null)
                getUserObjectsCallBack.done(returnedUsers);
            else
                System.out.println("server side returned null");
            super.onPostExecute(returnedUsers);
        }
    }

    private class fetchJobListingsAsync extends AsyncTask<Void, Void, Job[]> {
        String userPosition;
        int errCode;
        String userExperience;
        GetJobObjectsCallBack getJobObjectsCallBack;
        Job[] returnedJobs;

        fetchJobListingsAsync(String userPosition, String userExperience, GetJobObjectsCallBack getJobObjectsCallBack) {
            this.userPosition = userPosition;
            this.userExperience = userExperience;
            this.getJobObjectsCallBack = getJobObjectsCallBack;
        }

        @Override
        protected Job[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&position", userPosition);
            dataToSend.put("&experience", userExperience);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "SuggestJob.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                final int size = jsonArray.length();
                returnedJobs = new Job[size];

                for (int i = 0; i < size; i++) {
                    if (jsonArray.getJSONObject(i) == null) break;
                    else
                        jsonObject = jsonArray.getJSONObject(i);
                    returnedJobs[i] = new Job(jsonObject.getString("id"), jsonObject.getString("domain"), jsonObject.getString("position"), jsonObject.getString("type"), jsonObject.getString("description"), jsonObject.getString("experience"), jsonObject.getString("location"), jsonObject.getString("remarks"));
                }
            } catch (Exception e) {
                if (e instanceof IOException)
                    errCode = 1;
                if (e instanceof JSONException)
                    errCode = 2;
                e.printStackTrace();
            }
            return returnedJobs;
        }

        @Override
        protected void onPostExecute(Job[] returnedJobs) {
            progressDialog.dismiss();
            if (errCode == 1)
                negativeAlert("Connection to server failed. Check your connection and try again");
            else if (errCode == 2)
                negativeAlert("There are no jobs currently matching your profile. Please check again later");
            getJobObjectsCallBack.done(returnedJobs);
            super.onPostExecute(returnedJobs);
        }
    }

    private class searchJobListingsAsync extends AsyncTask<Void, Void, Job[]> {
        String searchTerm;
        GetJobObjectsCallBack getJobObjectsCallBack;
        Job[] returnedJobs;

        searchJobListingsAsync(String searchterm, GetJobObjectsCallBack getJobObjectsCallBack) {
            this.searchTerm = searchterm;
            this.getJobObjectsCallBack = getJobObjectsCallBack;
        }

        @Override
        protected Job[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&searchterm", "%" + searchTerm + "%");
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            getSpaces(postedData);
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "SearchJob.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                final int size = jsonArray.length();
                returnedJobs = new Job[size];
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) break;
                    returnedJobs[i] = new Job(jsonObject.getString("id"), jsonObject.getString("domain"), jsonObject.getString("position"), jsonObject.getString("type"), jsonObject.getString("experience"), jsonObject.getString("description"), jsonObject.getString("location"), jsonObject.getString("remarks"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedJobs;
        }

        @Override
        protected void onPostExecute(Job[] jobs) {
            progressDialog.dismiss();
            getJobObjectsCallBack.done(jobs);
            super.onPostExecute(jobs);
        }
    }

    private class applyForJobAsync extends AsyncTask<Void, Void, Void> {
        String email;
        String name;
        String ID;
        String position;

        applyForJobAsync(String email, String name, String ID, String position) {
            this.email = email;
            this.name = name;
            this.ID = ID;
            this.position = position;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            else
                positiveAlert("Applied successfully!\nYou will receive an acknowledgement via email shortly.");
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&email", email);
            dataToSend.put("&name", name);
            dataToSend.put("&ID", ID);
            dataToSend.put("&position", position);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            serverresponse = "";
            URL url;
            try {
                url = new URL(SERVER_ADDRESS + "ApplyForJob.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class editProfessionalAsync extends AsyncTask<Void, Void, Void> {
        String username, position, experience, curloc, desloc;

        editProfessionalAsync(String username, String position, String experience, String curloc, String desloc) {
            this.username = username;
            this.position = position;
            this.experience = experience;
            this.curloc = curloc;
            this.desloc = desloc;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!serverresponse.equals("")) {
                userLocalStore.update("position", position);
                userLocalStore.update("experience", experience);
                userLocalStore.update("curloc", curloc);
                userLocalStore.update("desloc", desloc);
            }
            progressDialog.dismiss();
            parentActivity.recreate();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&position", position);
            dataToSend.put("&username", username);
            dataToSend.put("&experience", experience);
            dataToSend.put("&curloc", curloc);
            dataToSend.put("&desloc", desloc);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            serverresponse = "";
            URL url;
            try {
                url = new URL(SERVER_ADDRESS + "EditProfessional.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class editGenericAsync extends AsyncTask<Void, Void, Void> {

        String variable;
        String editable;
        String username;

        editGenericAsync(String username, String editable, String variable) {
            this.username = username;
            this.editable = editable;
            this.variable = variable;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues values = new ContentValues();
            values.put("&editable", editable);
            values.put("&username", username);
            values.put("&variable", variable);
            String postedData = getStringfromContentValues(values);
            System.out.println(postedData);
            serverresponse = "";
            URL url;
            try {
                url = new URL(SERVER_ADDRESS + "EditGeneric.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!serverresponse.equals(""))
                userLocalStore.update(editable, variable);
            progressDialog.dismiss();
            parentActivity.recreate();
            super.onPostExecute(aVoid);
        }
    }

    private class addCompanyAsync extends AsyncTask<Void, Void, Void> {
        String username, comname, compos, comfrom, comto, comresp, comno;

        addCompanyAsync(String username, String comname, String compos, String comfrom, String comto, String comresp, String comno) {
            this.username = username;
            this.comname = comname;
            this.compos = compos;
            this.comfrom = comfrom;
            this.comto = comto;
            this.comresp = comresp;
            this.comno = comno;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!serverresponse.equals("")) {
                userLocalStore.update("com" + comno + "name", comname);
                userLocalStore.update("com" + comno + "from", comfrom);
                userLocalStore.update("com" + comno + "to", comto);
                userLocalStore.update("com" + comno + "pos", compos);
                userLocalStore.update("com" + comno + "resp", comresp);
            }
            parentActivity.recreate();
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues values = new ContentValues();
            values.put("&comno", comno.trim());
            values.put("&comname", comname);
            values.put("&username", username);
            values.put("&compos", compos);
            values.put("&comfrom", comfrom);
            values.put("&comto", comto);
            values.put("&comresp", comresp);
            String postedData = getStringfromContentValues(values);
            System.out.println(postedData);
            serverresponse = "";
            URL url;
            try {
                url = new URL(SERVER_ADDRESS + "AddCompany.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
