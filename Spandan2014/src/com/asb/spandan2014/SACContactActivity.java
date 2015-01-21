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

import com.asb.spandan2014.adapter.ContactAdapter;
import com.asb.spandan2014.constants.DBConstants;
import com.asb.spandan2014.db.SpandanDBHelper;
import com.asb.spandan2014.db.eo.ContactDetails;

public class SACContactActivity extends Activity {
  List<ContactDetails> sacDetailsList = null;
  ListView listView = null;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_saccontact);
    setupActionBar();
    context = getApplicationContext();
    listView = (ListView) findViewById(R.id.sacView);    

    SpandanDBHelper myDbHelper = null;
    myDbHelper = new SpandanDBHelper(this);
    Cursor cursor = null;
    sacDetailsList = new ArrayList<ContactDetails>();
    try {
      myDbHelper.openDataBase();
      cursor = myDbHelper.getContacts();
      if (cursor.moveToFirst()) {
        do {
          String sacName = cursor.getString(cursor
              .getColumnIndex(DBConstants.CONTACT_NAME));
          String sacEmail = cursor.getString(cursor
              .getColumnIndex(DBConstants.CONTACT_EMAIL));
          String sacNumber = cursor.getString(cursor
              .getColumnIndex(DBConstants.CONTACT_PHONE));
          String sacImage = cursor.getString(cursor
              .getColumnIndex(DBConstants.CONTACT_IMAGE_NAME));
          String sacCourse = cursor.getString(cursor
              .getColumnIndex(DBConstants.CONTACT_COURSE));
          int sacId = cursor.getInt(cursor
              .getColumnIndex(DBConstants.CONTACT_ID));
          ContactDetails sacDetails = new ContactDetails(sacId, sacName,
              sacNumber, sacEmail, sacImage, sacCourse);
          sacDetailsList.add(sacDetails);
          // do what ever you want here
        } while (cursor.moveToNext());
      }
      cursor.close();
      myDbHelper.close();
    } catch (SQLException sqle) {
      throw sqle;
    }
    listView.setAdapter(new ContactAdapter(context, sacDetailsList));
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
