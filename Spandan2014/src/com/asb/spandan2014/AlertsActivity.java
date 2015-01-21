package com.asb.spandan2014;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.asb.spandan2014.adapter.AlertsAdapter;
import com.asb.spandan2014.constants.AppConstants;
import com.asb.spandan2014.db.eo.AlertsDetails;
import com.asb.spandan2014.utils.HttpClientUtil;

public class AlertsActivity extends Activity {
  List<AlertsDetails> alertDetailsList = null;
  ListView listView = null;
  Context context;
  AlertsAdapter alertsAdapter;
  private static final int OFFSET = 0;
  private static final int LIMIT = 8;
  ProgressDialog pb = null;
  int offset = OFFSET;
  int limit = LIMIT;
  boolean anyContentRemaining = true;

  int lastFirstVisible = 0;
  int lastVisibleItemCount = 0;
  int lastTotalItemCount = 0;

  private void initProgressBar() {
    pb = new ProgressDialog(this);
    pb.setMessage(getResources().getString(R.string.loading));
    pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    pb.setProgress(0);
    pb.setMax(100);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    initProgressBar();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alerts);
    setupActionBar();
    context = getApplicationContext();
    alertDetailsList = new ArrayList<AlertsDetails>();
    listView = (ListView) findViewById(R.id.alertsList);
    alertsAdapter = new AlertsAdapter(context, alertDetailsList);
    listView.setAdapter(alertsAdapter);
    fetchAlertsInBackground(LIMIT, OFFSET);

    listView.setOnScrollListener(new OnScrollListener() {

      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {

      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem,
          int visibleItemCount, int totalItemCount) {
        boolean loadMore = /* maybe add a padding */
        (firstVisibleItem + visibleItemCount >= totalItemCount)
            && (totalItemCount != 0)
            && !isLastRequestSame(firstVisibleItem, visibleItemCount,
                totalItemCount);

        if (loadMore && anyContentRemaining) {
          offset += LIMIT;
          lastFirstVisible = firstVisibleItem;
          lastVisibleItemCount = visibleItemCount;
          lastTotalItemCount = totalItemCount;
          fetchAlertsInBackground(LIMIT, offset);
          Log.i("Scroll Scrolling: ", "firstVisibleItem: " + firstVisibleItem
              + ", visibleItemCount" + visibleItemCount + ", totalItemCount"
              + totalItemCount);
          Log.i("Infinite Scrolling: ", "Offset: " + offset + ", Limit" + limit);
        }
      }

      private boolean isLastRequestSame(int firstVisibleItem,
          int visibleItemCount, int totalItemCount) {
        if (lastFirstVisible == firstVisibleItem
            && lastVisibleItemCount == visibleItemCount
            && lastTotalItemCount == totalItemCount) {
          return true;
        }
        return false;
      }

    });
    Runtime.getRuntime().gc();
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null;
  }

  private void fetchAlertsInBackground(final int limit, final int offset) {

    if (isNetworkAvailable()) {
      pb.show();
      new AsyncTask<String, String, JSONArray>() {

        @Override
        protected JSONArray doInBackground(String... params) {
          String alerts = HttpClientUtil.fetchAlerts(limit, offset);
          JSONArray alertArr = null;
          if (null != alerts) {
            try {
              alertArr = new JSONArray(alerts);
            } catch (JSONException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
          return alertArr;
        }

        @Override
        protected void onPostExecute(JSONArray webServiceResult) {
          if (null != webServiceResult) {
            int index = 0;
            for (index = 0; index < webServiceResult.length(); index += 1) {
              try {
                JSONObject alert = webServiceResult.getJSONObject(index);
                AlertsDetails alertsDetails = new AlertsDetails(
                    alert.getString(AppConstants.ALERT_TIME),
                    alert.getString(AppConstants.ALERT_FROM),
                    alert.getString(AppConstants.ALERT_MESSAGE));
                alertDetailsList.add(alertsDetails);
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
            alertsAdapter.notifyDataSetChanged();
            if (index < LIMIT) {
              anyContentRemaining = false;
            }
            pb.dismiss();
          } else {
            pb.dismiss();
            CharSequence text = getString(R.string.oops);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
          }
        }
      }.execute();
    } else {
      CharSequence text = getString(R.string.enable_net);
      int duration = Toast.LENGTH_LONG;
      Toast toast = Toast.makeText(context, text, duration);
      toast.show();
      return;
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
