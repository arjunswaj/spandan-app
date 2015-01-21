package com.asb.spandan2014;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.asb.spandan2014.constants.AppConstants;

@SuppressLint("SetJavaScriptEnabled")
public class GalleryActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gallery);
    setupActionBar();
    WebView webView = (WebView) findViewById(R.id.galleryView);
    WebSettings webSettings = webView.getSettings();
    webSettings.setBuiltInZoomControls(true);
    webSettings.setJavaScriptEnabled(true);
    webView.setWebViewClient(new Callback()); // HERE IS THE MAIN CHANGE
    webView.loadUrl(AppConstants.GALLERY_LINK);
    Runtime.getRuntime().gc();
  }

  private class Callback extends WebViewClient { // HERE IS THE MAIN CHANGE.

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      return (false);
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
