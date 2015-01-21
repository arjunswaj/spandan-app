package com.asb.spandan2014.adapter;

import com.asb.spandan2014.R;
import com.asb.spandan2014.constants.AppConstants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpandanHomeAdapter extends BaseAdapter {

  private Context context;
  private final String[] contentValues;

  public SpandanHomeAdapter(Context context, String[] contentValues) {
    super();
    this.context = context;
    this.contentValues = contentValues;
  }

  @Override
  public int getCount() {
    return contentValues.length;
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

    if (convertView == null) {      

      // get layout from mobile.xml
      gridView = inflater.inflate(R.layout.home_grids, null);

      // set value into textview
      TextView textView = (TextView) gridView
          .findViewById(R.id.home_grid_item_label);

      // set image based on selected text
      ImageView imageView = (ImageView) gridView
          .findViewById(R.id.home_grid_item_image);

      String content = contentValues[position];

      if (content.equals(AppConstants.REGISTER)) {
        imageView.setImageResource(R.drawable.register);
        textView.setText(context.getString(R.string.register));
      } else if (content.equals(AppConstants.CONTACT)) {
        imageView.setImageResource(R.drawable.contact);
        textView.setText(context.getString(R.string.contact));
      } else if (content.equals(AppConstants.RULES)) {
        imageView.setImageResource(R.drawable.rules);
        textView.setText(context.getString(R.string.rules));
      } else if (content.equals(AppConstants.GALLERY)) {
        imageView.setImageResource(R.drawable.gallery);
        textView.setText(context.getString(R.string.gallery));
      } else if (content.equals(AppConstants.ANNOUNCEMENTS)) {
        imageView.setImageResource(R.drawable.announcements);
        textView.setText(context.getString(R.string.announcements));
      } else if (content.equals(AppConstants.SPOC)) {
        imageView.setImageResource(R.drawable.spoc);
        textView.setText(context.getString(R.string.spoc));
      }

    } else {
      gridView = (View) convertView;
    }
    Runtime.getRuntime().gc();
    return gridView;
  }

}
