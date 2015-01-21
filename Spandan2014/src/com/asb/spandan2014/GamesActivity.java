package com.asb.spandan2014;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.asb.spandan2014.adapter.GamesAdapter;
import com.asb.spandan2014.constants.AppConstants;
import com.asb.spandan2014.constants.DBConstants;
import com.asb.spandan2014.db.SpandanDBHelper;
import com.asb.spandan2014.db.eo.SportsImageDetails;

public class GamesActivity extends Activity {
  Context context;
  List<SportsImageDetails> sportsImageDetailsList = null;
  GridView gridView = null;

  boolean rules = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rules);
    setupActionBar();
    context = getApplicationContext();
    gridView = (GridView) findViewById(R.id.gamesGrid);

    String pageType = getIntent().getExtras().getString(AppConstants.PAGE_TYPE);
    if (pageType.equals(AppConstants.RULES)) {
      setTitle(getString(R.string.rules));
      rules = true;
    } else if (pageType.equals(AppConstants.SPOC)) {
      setTitle(getString(R.string.spoc));
    }
    SpandanDBHelper myDbHelper = null;
    myDbHelper = new SpandanDBHelper(this);
    Cursor cursor = null;
    sportsImageDetailsList = new ArrayList<SportsImageDetails>();
    try {
      myDbHelper.openDataBase();
      cursor = myDbHelper.getAllGames();
      if (cursor.moveToFirst()) {
        do {
          String gameName = cursor.getString(cursor
              .getColumnIndex(DBConstants.GAME));
          String imageName = cursor.getString(cursor
              .getColumnIndex(DBConstants.IMAGE));
          int imageId = cursor.getInt(cursor
              .getColumnIndex(DBConstants.GAMES_ID));
          SportsImageDetails sportsImageDetails = new SportsImageDetails(
              imageId, gameName, imageName);
          sportsImageDetailsList.add(sportsImageDetails);
          // do what ever you want here
        } while (cursor.moveToNext());
      }
      cursor.close();
      myDbHelper.close();
    } catch (SQLException sqle) {
      throw sqle;
    }
    gridView.setAdapter(new GamesAdapter(context, sportsImageDetailsList));

    gridView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position,
          long id) {
        Intent nextPage = null;
        if (rules) {
          nextPage = new Intent(GamesActivity.this, RulesDetailsActivity.class);
          nextPage.putExtra(AppConstants.GAME_ID,
              sportsImageDetailsList.get(position).getGameId());
          nextPage.putExtra(AppConstants.GAME_NAME,
              sportsImageDetailsList.get(position).getGameName());
        } else {
          nextPage = new Intent(GamesActivity.this, SpocActivity.class);
          nextPage.putExtra(AppConstants.GAME_ID,
              sportsImageDetailsList.get(position).getGameId());
          nextPage.putExtra(AppConstants.GAME_NAME,
              sportsImageDetailsList.get(position).getGameName());
        }
        if (null != nextPage) {
          startActivity(nextPage);
        }
      }
    });
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
