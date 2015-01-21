package com.asb.spandan2014.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asb.spandan2014.R;
import com.asb.spandan2014.db.eo.AlertsDetails;

public class AlertsAdapter extends BaseAdapter {

  private Context context;
  private final List<AlertsDetails> alertsDetailsList;

  public AlertsAdapter(Context context, List<AlertsDetails> alertsDetailsList) {
    super();
    this.context = context;
    this.alertsDetailsList = alertsDetailsList;
  }

  @Override
  public int getCount() {
    return alertsDetailsList.size();
  }

  @Override
  public Object getItem(int arg0) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View gridView;
    

    // get layout from mobile.xml
    gridView = inflater.inflate(R.layout.alert_details, null);

    // set value into textview
    TextView alert = (TextView) gridView.findViewById(R.id.alert_message);
    TextView alertInfo = (TextView) gridView.findViewById(R.id.alert_info);

    AlertsDetails alertsDetails = alertsDetailsList.get(position);

    alert.setTextColor(Color.BLACK);
    alert.setText(alertsDetails.getAlertMessage());

    String alertInfoText = alertsDetails.getAlertFrom() + " posted at "
        + alertsDetails.getAlertTime();
    alertInfo.setTextColor(Color.BLACK);
    alertInfo.setText(alertInfoText);
    Runtime.getRuntime().gc();
    return gridView;
  }

}
