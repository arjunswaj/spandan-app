package com.asb.spandan2014;

import java.util.ArrayList;
import java.util.List;

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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.asb.spandan2014.utils.HttpClientUtil;

public class RegistrationActivity extends Activity {

  Button submit;
  EditText name;
  EditText phone;
  EditText email;
  EditText yoj;
  CheckBox chess;
  CheckBox nfs;
  CheckBox fifa;
  CheckBox marathon;
  CheckBox bikeRace;
  CheckBox tableTennis;
  CheckBox badminton;
  ProgressDialog pb = null;
  List<String> choices = new ArrayList<String>();
  Context context;
  final private String events[] = { "Chess", "Most Wanted", "Badminton",
      "FIFA", "Mini Marathon", "Slow Bike Race", "Table Tennis" };

  private void initProgressBar() {
    pb = new ProgressDialog(this);
    pb.setMessage(getResources().getString(R.string.loading));
    pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    pb.setProgress(0);
    pb.setMax(100);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);
    setupActionBar();
    context = this;
    initProgressBar();
    submit = (Button) findViewById(R.id.register);
    name = (EditText) findViewById(R.id.name);
    phone = (EditText) findViewById(R.id.phoneNumber);
    email = (EditText) findViewById(R.id.emailId);
    yoj = (EditText) findViewById(R.id.yearOfJoining);
    chess = (CheckBox) findViewById(R.id.chess);
    nfs = (CheckBox) findViewById(R.id.nfs);
    fifa = (CheckBox) findViewById(R.id.fifa);
    badminton = (CheckBox) findViewById(R.id.badminton);
    marathon = (CheckBox) findViewById(R.id.marathon);
    bikeRace = (CheckBox) findViewById(R.id.bikerace);
    tableTennis = (CheckBox) findViewById(R.id.tabletennis);

    submit.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        List<String> errors = new ArrayList<String>();

        String nameText = name.getText().toString();
        if (null == nameText || nameText.equals("")) {
          errors.add("Name cannot be empty");
        }
        String phoneText = phone.getText().toString();
        if (null == phoneText || phoneText.equals("")) {
          errors.add("Phone Number cannot be empty");
        } else {
          try {
            long number = Long.parseLong(phoneText);
          } catch (NumberFormatException nfe) {
            errors.add("Phone Number cannot be non-numeric");
          }
        }
        String emailText = email.getText().toString();
        if (null == emailText || emailText.equals("")) {
          errors.add("Email cannot be empty");
        }

        String yojText = yoj.getText().toString();
        if (null == yojText || yojText.equals("")) {
          errors.add("Year of Joining cannot be empty");
        } else {
          try {
            int number = Integer.parseInt(yojText);
            if (number < 1995 && number > 2014) {
              errors.add("Invalid Year of Joining");
            }
          } catch (NumberFormatException nfe) {
            errors.add("Year of Joining cannot be non-numeric");
          }
        }
        int count = 0;
        boolean isChess = chess.isChecked();
        if (isChess) {
          choices.add(events[0]);
          count += 1;
        }
        boolean isNFS = nfs.isChecked();
        if (isNFS) {
          choices.add(events[1]);
          count += 1;
        }

        boolean isBadminton = badminton.isChecked();
        if (isBadminton) {
          choices.add(events[2]);
          count += 1;
        }
        boolean isFifa = fifa.isChecked();
        if (isFifa) {
          choices.add(events[3]);
          count += 1;
        }
        boolean isMarathon = marathon.isChecked();
        if (isMarathon) {
          choices.add(events[4]);
          count += 1;
        }
        boolean isBikeRace = bikeRace.isChecked();
        if (isBikeRace) {
          choices.add(events[5]);
          count += 1;
        }
        boolean isTableTennis = tableTennis.isChecked();
        if (isTableTennis) {
          choices.add(events[6]);
          count += 1;
        }

        if (0 == count) {
          errors.add("Select at least one game");
        }

        if (0 < errors.size()) {
          StringBuilder sb = new StringBuilder();
          for (String error : errors) {
            sb.append(error).append("\n");
          }
          showToast(sb.toString());
        } else {
          registerForEventsInBackground(nameText, phoneText, emailText, yojText);
        }
      }
    });

  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null;
  }

  private void registerForEventsInBackground(final String name,
      final String phone, final String email, final String yoj) {

    if (isNetworkAvailable()) {
      pb.show();
      new AsyncTask<String, String, JSONObject>() {

        @Override
        protected JSONObject doInBackground(String... params) {
          String alerts = HttpClientUtil.registerForIndividual(name, phone,
              email, yoj, choices);
          JSONObject alertArr = null;
          if (null != alerts) {
            try {
              alertArr = new JSONObject(alerts);
            } catch (JSONException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }

          }
          return alertArr;
        }

        @Override
        protected void onPostExecute(JSONObject webServiceResult) {
          if (null != webServiceResult) {
            String result = webServiceResult.optString("json_string");
            if (null != result) {
              showToast(getString(R.string.success));
            }
          } else {
            showToast(getString(R.string.somethings_wrong));
          }
          pb.dismiss();
        }

      }.execute();
    } else {
      showToast(getString(R.string.enable_net));
      return;
    }
  }

  private void showToast(String message) {
    CharSequence text = message;
    int duration = Toast.LENGTH_LONG;
    Toast toast = Toast.makeText(context, text, duration);
    toast.show();
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
