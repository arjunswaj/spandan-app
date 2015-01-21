package com.asb.spandan2014;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.asb.spandan2014.adapter.MyExpandableGamesListAdapter;
import com.asb.spandan2014.constants.AppConstants;
import com.asb.spandan2014.constants.DBConstants;
import com.asb.spandan2014.db.SpandanDBHelper;
import com.asb.spandan2014.db.eo.GameRulesEO;

public class RulesDetailsActivity extends Activity {

  SparseArray<GameRulesEO> groups = new SparseArray<GameRulesEO>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rules_details);
    // Show the Up button in the action bar.    
    setupActionBar();
    int gameId = getIntent().getExtras().getInt(AppConstants.GAME_ID);
    String gameName = getIntent().getExtras().getString(AppConstants.GAME_NAME);
    setTitle(gameName + " " + getString(R.string.rules));
    populateRulesData(gameId);
    ExpandableListView listView = (ExpandableListView) findViewById(R.id.gameRulesDetails);
    MyExpandableGamesListAdapter adapter = new MyExpandableGamesListAdapter(
        this, groups);
    listView.setAdapter(adapter);
  }

  private void populateRulesData(int gameId) {

    SpandanDBHelper myDbHelper = null;
    myDbHelper = new SpandanDBHelper(this);
    Cursor cursor = null;    
    int key = 0;
    try {
      myDbHelper.openDataBase();
      cursor = myDbHelper.getGameRulesByGameId(gameId);
      if (cursor.moveToFirst()) {
        do {          
          int gameDetailsId = cursor.getInt(cursor
              .getColumnIndex(DBConstants.GAME_ID));
          String infoType = cursor.getString(cursor
              .getColumnIndex(DBConstants.INFO_TYPE));
          String info = cursor.getString(cursor
              .getColumnIndex(DBConstants.INFO));
          
          GameRulesEO gameRulesEO = new GameRulesEO(gameDetailsId, infoType, info);
          groups.append(key, gameRulesEO);
          key += 1;
          // do what ever you want here
        } while (cursor.moveToNext());
      }
      cursor.close();
      myDbHelper.close();
    } catch (SQLException sqle) {
      throw sqle;
    }
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
