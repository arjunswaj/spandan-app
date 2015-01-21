package com.asb.spandan2014;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.asb.spandan2014.adapter.SpandanHomeAdapter;
import com.asb.spandan2014.constants.AppConstants;
import com.asb.spandan2014.db.SpandanDBHelper;
import com.asb.spandan2014.utils.HttpClientUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class HomeActivity extends Activity {

  public static final String EXTRA_MESSAGE = "message";
  public static final String PROPERTY_REG_ID = "registration_id";
  private static final String PROPERTY_APP_VERSION = "appVersion";
  private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

  static final String[] HOME_CONTENT = new String[] { AppConstants.REGISTER,
      AppConstants.CONTACT, AppConstants.RULES, AppConstants.GALLERY,
      AppConstants.ANNOUNCEMENTS, AppConstants.SPOC };
  /**
   * Substitute you own sender ID here. This is the project number you got from
   * the API Console, as described in "Getting Started."
   */
  String SENDER_ID = "493542906199";

  /**
   * Tag used on log messages.
   */
  static final String TAG = "Spandan Demo";

  GoogleCloudMessaging gcm;
  AtomicInteger msgId = new AtomicInteger();
  Context context;

  String regid;
  GridView gridView = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_home);

    context = getApplicationContext();

    gridView = (GridView) findViewById(R.id.spandanGrid);

    gridView.setAdapter(new SpandanHomeAdapter(this, HOME_CONTENT));

    gridView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position,
          long id) {

        Intent nextPage = null;
        switch (position) {
        case 0:
          nextPage = new Intent(HomeActivity.this, RegistrationActivity.class);
          break;
        case 1:
          nextPage = new Intent(HomeActivity.this, SACContactActivity.class);
          break;
        case 2:
          nextPage = new Intent(HomeActivity.this, GamesActivity.class);
          nextPage.putExtra(AppConstants.PAGE_TYPE, AppConstants.RULES);
          break;
        case 3:
          nextPage = new Intent(HomeActivity.this, GalleryActivity.class);
          break;
        case 4:
          nextPage = new Intent(HomeActivity.this, AlertsActivity.class);
          break;
        case 5:
          nextPage = new Intent(HomeActivity.this, GamesActivity.class);
          nextPage.putExtra(AppConstants.PAGE_TYPE, AppConstants.SPOC);
          break;
        }
        if (null != nextPage) {
          startActivity(nextPage);
        }
      }
    });

    // Check device for Play Services APK. If check succeeds, proceed with GCM
    // registration.
    if (checkPlayServices()) {
      gcm = GoogleCloudMessaging.getInstance(this);
      regid = getRegistrationId(context);

      if (regid.equals("")) {
        registerInBackground();
      }
    } else {
      Log.i(TAG, "No valid Google Play Services APK found.");
    }

    SpandanDBHelper myDbHelper = null;
    myDbHelper = new SpandanDBHelper(this);

    try {
      myDbHelper.createDataBase();
      myDbHelper.close();
    } catch (IOException ioe) {
      throw new Error("Unable to create database");
    }

    try {
      myDbHelper.openDataBase();
      myDbHelper.close();
      Log.i(TAG, "Yo! Yo! Yo!");
    } catch (SQLException sqle) {
      throw sqle;
    }
    
    Runtime.getRuntime().gc();
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Check device for Play Services APK.
    checkPlayServices();
  }

  /**
   * Check the device to make sure it has the Google Play Services APK. If it
   * doesn't, display a dialog that allows users to download the APK from the
   * Google Play Store or enable it in the device's system settings.
   */
  private boolean checkPlayServices() {
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
        GooglePlayServicesUtil.getErrorDialog(resultCode, this,
            PLAY_SERVICES_RESOLUTION_REQUEST).show();
      } else {
        Log.i(TAG, "This device is not supported.");
        finish();
      }
      return false;
    }
    return true;
  }

  /**
   * Stores the registration ID and the app versionCode in the application's
   * {@code SharedPreferences}.
   * 
   * @param context
   *          application's context.
   * @param regId
   *          registration ID
   */
  private void storeRegistrationId(Context context, String regId) {
    final SharedPreferences prefs = getGcmPreferences(context);
    int appVersion = getAppVersion(context);
    Log.i(TAG, "Saving regId on app version " + appVersion);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(PROPERTY_REG_ID, regId);
    editor.putInt(PROPERTY_APP_VERSION, appVersion);
    editor.commit();
  }

  /**
   * Gets the current registration ID for application on GCM service, if there
   * is one.
   * <p>
   * If result is empty, the app needs to register.
   * 
   * @return registration ID, or empty string if there is no existing
   *         registration ID.
   */
  private String getRegistrationId(Context context) {
    final SharedPreferences prefs = getGcmPreferences(context);
    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
    if (registrationId.equals("")) {
      Log.i(TAG, "Registration not found.");
      return "";
    }
    // Check if app was updated; if so, it must clear the registration ID
    // since the existing regID is not guaranteed to work with the new
    // app version.
    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
        Integer.MIN_VALUE);
    int currentVersion = getAppVersion(context);
    if (registeredVersion != currentVersion) {
      Log.i(TAG, "App version changed.");
      return "";
    }
    return registrationId;
  }

  /**
   * Registers the application with GCM servers asynchronously.
   * <p>
   * Stores the registration ID and the app versionCode in the application's
   * shared preferences.
   */
  private void registerInBackground() {
    new AsyncTask<Void, Void, String>() {
      @Override
      protected String doInBackground(Void... params) {
        String msg = "";
        try {
          if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(context);
          }
          regid = gcm.register(SENDER_ID);
          msg = "Device registered, registration ID=" + regid;

          // You should send the registration ID to your server over HTTP, so it
          // can use GCM/HTTP or CCS to send messages to your app.
          sendRegistrationIdToBackend();

          // For this demo: we don't need to send it because the device will
          // send
          // upstream messages to a server that echo back the message using the
          // 'from' address in the message.

          // Persist the regID - no need to register again.
          storeRegistrationId(context, regid);
        } catch (IOException ex) {
          msg = "Error :" + ex.getMessage();
          // If there is an error, don't just keep trying to register.
          // Require the user to click a button again, or perform
          // exponential back-off.
        }
        return msg;
      }

      @Override
      protected void onPostExecute(String msg) {

      }
    }.execute(null, null, null);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  /**
   * @return Application's version code from the {@code PackageManager}.
   */
  private static int getAppVersion(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
          context.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (NameNotFoundException e) {
      // should never happen
      throw new RuntimeException("Could not get package name: " + e);
    }
  }

  /**
   * @return Application's {@code SharedPreferences}.
   */
  private SharedPreferences getGcmPreferences(Context context) {
    // This sample app persists the registration ID in shared preferences, but
    // how you store the regID in your app is up to you.
    return getSharedPreferences(HomeActivity.class.getSimpleName(),
        Context.MODE_PRIVATE);
  }

  /**
   * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
   * or CCS to send messages to your app. Not needed for this demo since the
   * device sends upstream messages to a server that echoes back the message
   * using the 'from' address in the message.
   */
  private void sendRegistrationIdToBackend() {
    // Your implementation here.
    String response = HttpClientUtil.registerDevice(this.regid);
    Log.i(TAG, response);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.home, menu);
      return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      // Handle item selection
    boolean returnValue = false;
      switch (item.getItemId()) {
          case R.id.share:                
            returnValue =  shareTheApp();
            break;
          case R.id.rate:
            returnValue = rateTheApp();
            break;          
          default:
            returnValue =  super.onOptionsItemSelected(item);
      }
      return returnValue;
  }
  
  private boolean shareTheApp() {
    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
        "Spandan 2014 \nhttp://bit.ly/spandan2014");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
        "Spandan 2014");
    startActivity(Intent.createChooser(sharingIntent, "Share using"));
    return false;
  }
  
  private boolean rateTheApp() {
    boolean returnValue =  false;
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse("market://details?id=com.asb.spandan2014"));      
    startActivity(intent);      
    returnValue = true;
    return returnValue;
  }

}
