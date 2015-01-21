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
import com.asb.spandan2014.db.eo.ContactDetails;

public class ContactAdapter extends BaseAdapter {

  private Context context;
  private final List<ContactDetails> sacDetailsList;

  public ContactAdapter(Context context, List<ContactDetails> sacDetailsList) {
    super();
    this.context = context;
    this.sacDetailsList = sacDetailsList;
  }

  @Override
  public int getCount() {
    return sacDetailsList.size();
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
    gridView = inflater.inflate(R.layout.sac_details, null);

    // set value into textview
    final TextView sacName = (TextView) gridView.findViewById(R.id.sac_name);
    final TextView sacEmail = (TextView) gridView.findViewById(R.id.sac_email);
    final TextView sacNumber = (TextView) gridView
        .findViewById(R.id.sac_number);
    final TextView sacCourse = (TextView) gridView
        .findViewById(R.id.sac_course);
    

    ContactDetails sacDetails = sacDetailsList.get(position);    
    sacName.setTextColor(Color.BLACK);
    sacName.setText(sacDetails.getContactName());

    sacEmail.setTextColor(Color.BLACK);
    SpannableString emailString = new SpannableString(sacDetails.getContactEmail());            
    emailString.setSpan(new StyleSpan(Typeface.ITALIC), 0, emailString.length(), 0);
    sacEmail.setText(emailString);

    sacNumber.setTextColor(Color.BLACK);
    SpannableString numberString = new SpannableString(sacDetails.getContactNumber());
    numberString.setSpan(new UnderlineSpan(), 0, numberString.length(), 0);
    numberString.setSpan(new StyleSpan(Typeface.BOLD), 0, numberString.length(), 0);
    numberString.setSpan(new StyleSpan(Typeface.ITALIC), 0, numberString.length(), 0);
    sacNumber.setText(numberString);

    sacCourse.setTextColor(Color.BLACK);
    SpannableString courseString = new SpannableString(sacDetails.getContactCourse());            
    courseString.setSpan(new StyleSpan(Typeface.ITALIC), 0, courseString.length(), 0);
    sacCourse.setText(courseString);

    sacNumber.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);

        phoneIntent.setData(Uri.parse("tel:+" + sacNumber.getText()));
        phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent phoneChooser = Intent.createChooser(phoneIntent,
            context.getString(R.string.contact_sac));
        phoneChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
          context.startActivity(phoneChooser);
        } catch (ActivityNotFoundException e) {
          // If there is nothing that can send a text/html MIME type
          e.printStackTrace();
        }
      }
    });

    sacEmail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { sacEmail
            .getText().toString() });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,
            context.getString(R.string.spandan_2014));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        Intent emailChooser = Intent.createChooser(emailIntent,
            context.getString(R.string.contact_sac));
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
    return gridView;
  }
}
