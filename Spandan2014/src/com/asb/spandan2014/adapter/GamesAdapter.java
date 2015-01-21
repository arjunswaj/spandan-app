package com.asb.spandan2014.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.spandan2014.R;
import com.asb.spandan2014.db.eo.SportsImageDetails;

public class GamesAdapter extends BaseAdapter {

  private Context context;
  private final List<SportsImageDetails> sportsImageDetailsList;

  public GamesAdapter(Context context,
      List<SportsImageDetails> sportsImageDetailsList) {
    super();
    this.context = context;
    this.sportsImageDetailsList = sportsImageDetailsList;
  }

  @Override
  public int getCount() {
    return sportsImageDetailsList.size();
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
    gridView = inflater.inflate(R.layout.rule_grids, null);

    // set value into textview
    TextView textView = (TextView) gridView
        .findViewById(R.id.rule_grid_item_label);

    // set image based on selected text
    ImageView imageView = (ImageView) gridView
        .findViewById(R.id.rule_grid_item_image);

    SportsImageDetails sportsImageDetails = sportsImageDetailsList
        .get(position);

    String uri = "drawable/" + sportsImageDetails.getImageFileName();
    int imageResourceID = context.getResources().getIdentifier(uri, null,
        context.getPackageName());
    imageView.setImageResource(imageResourceID);
    textView.setTextColor(Color.BLACK);
    textView.setText(sportsImageDetails.getGameName());
    Runtime.getRuntime().gc();
    return gridView;
  }

}
