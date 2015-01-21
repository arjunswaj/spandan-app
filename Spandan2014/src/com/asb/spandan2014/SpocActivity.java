package com.asb.spandan2014;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.asb.spandan2014.adapter.SpocAdapter;
import com.asb.spandan2014.constants.AppConstants;
import com.asb.spandan2014.constants.DBConstants;
import com.asb.spandan2014.db.SpandanDBHelper;
import com.asb.spandan2014.db.eo.SpocDetails;

public class SpocActivity extends Activity {

  List<SpocDetails> spocDetailsList = null;
  ListView listView = null;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_spoc);
    setupActionBar();
    context = getApplicationContext();
    listView = (ListView) findViewById(R.id.spocView);

    int gameId = getIntent().getExtras().getInt(AppConstants.GAME_ID);
    String gameName = getIntent().getExtras().getString(AppConstants.GAME_NAME);
    
    setTitle(gameName + " " + getString(R.string.spoc));
    SpandanDBHelper myDbHelper = null;
    myDbHelper = new SpandanDBHelper(this);
    Cursor cursor = null;
    spocDetailsList = new ArrayList<SpocDetails>();
    try {
      myDbHelper.openDataBase();
      cursor = myDbHelper.getSpocsByGameId(gameId);
      if (cursor.moveToFirst()) {
        do {
          String spocName = cursor.getString(cursor
              .getColumnIndex(DBConstants.SPOC_NAME));
          String spocEmail = cursor.getString(cursor
              .getColumnIndex(DBConstants.SPOC_EMAIL));
          String spocNumber = cursor.getString(cursor
              .getColumnIndex(DBConstants.SPOC_PHONE));
          String spocImage = cursor.getString(cursor
              .getColumnIndex(DBConstants.SPOC_IMAGE_NAME));
          int spocId = cursor.getInt(cursor
              .getColumnIndex(DBConstants.SPOC_ID));
          SpocDetails spocDetails = new SpocDetails(spocId, spocName,
              spocNumber, spocEmail, spocImage);
          spocDetailsList.add(spocDetails);
          // do what ever you want here
        } while (cursor.moveToNext());
      }
      cursor.close();
      myDbHelper.close();
    } catch (SQLException sqle) {
      throw sqle;
    }
    listView.setAdapter(new SpocAdapter(context, spocDetailsList));
    Runtime.getRuntime().gc();
  }

  /**
   * Set up the {@link android.app.ActionBar}, if the API is available.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void setupActionBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean returnValue = false;
    switch (item.getItemId()) {
    case android.R.id.home:
      finish();
      returnValue = true;
      break;
    }
    return returnValue;
  }

}
