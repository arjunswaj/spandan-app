package com.asb.spandan2014.adapter;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asb.spandan2014.R;
import com.asb.spandan2014.db.eo.SpocDetails;

public class SpocAdapter extends BaseAdapter {

  private Context context;
  private final List<SpocDetails> spocDetailsList;

  public SpocAdapter(Context context, List<SpocDetails> spocDetailsList) {
    super();
    this.context = context;
    this.spocDetailsList = spocDetailsList;
  }

  @Override
  public int getCount() {
    return spocDetailsList.size();
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

    View gridView = null;
    if (null == convertView) {      
      // get layout from mobile.xml
      gridView = inflater.inflate(R.layout.spoc_details, null);

      // set value into textview
      final TextView spocName = (TextView) gridView
          .findViewById(R.id.spoc_name);
      final TextView spocEmail = (TextView) gridView
          .findViewById(R.id.spoc_email);
      final TextView spocNumber = (TextView) gridView
          .findViewById(R.id.spoc_number);

      SpocDetails spocDetails = spocDetailsList.get(position);
      spocName.setTextColor(Color.BLACK);
      spocName.setText(spocDetails.getSpocName());

      spocEmail.setTextColor(Color.BLACK);
      SpannableString emailString = new SpannableString(spocDetails.getSpocEmail());            
      emailString.setSpan(new StyleSpan(Typeface.ITALIC), 0, emailString.length(), 0);
      spocEmail.setText(emailString);

      spocNumber.setTextColor(Color.BLACK);
      SpannableString numberString = new SpannableString(spocDetails.getSpocNumber());
      numberString.setSpan(new UnderlineSpan(), 0, numberString.length(), 0);
      numberString.setSpan(new StyleSpan(Typeface.BOLD), 0, numberString.length(), 0);
      numberString.setSpan(new StyleSpan(Typeface.ITALIC), 0, numberString.length(), 0);
      spocNumber.setText(numberString);

      spocNumber.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent phoneIntent = new Intent(Intent.ACTION_DIAL);

          phoneIntent.setData(Uri.parse("tel:+" + spocNumber.getText()));
          phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          Intent phoneChooser = Intent.createChooser(phoneIntent,
              context.getString(R.string.contact_spoc));
          phoneChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          try {
            context.startActivity(phoneChooser);
          } catch (ActivityNotFoundException e) {
            // If there is nothing that can send a text/html MIME type
            e.printStackTrace();
          }
        }
      });

      spocEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent emailIntent = new Intent(Intent.ACTION_SEND);
          emailIntent.setType("text/html");
          emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { spocEmail
              .getText().toString() });
          emailIntent.putExtra(Intent.EXTRA_SUBJECT,
              context.getString(R.string.spandan_2014));
          emailIntent.putExtra(Intent.EXTRA_TEXT, "");
          Intent emailChooser = Intent.createChooser(emailIntent,
              context.getString(R.string.contact_spoc));
          emailChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          try {
            context.startActivity(emailChooser);
          } catch (ActivityNotFoundException e) {
            // If there is nothing that can send a text/html MIME type
            e.printStackTrace();
          }

        }
      });
      Runtime.getRuntime().gc();
    } else {
      gridView = convertView;
    }
    return gridView;
  }

}
