/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package edu.nyu.cs.omnidroid.app.util;

import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.core.ShowWebsiteAction;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Display a website for a given url
 */
public class ShowWebsiteActivity extends Activity {
  //1000 is 10% of the total progress
  private static final int PROGRESS_INTERVAL = 1000;
  
  @Override 
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle extras = getIntent().getExtras();
    String url = null;
    try {
      url = extras.getString(ShowWebsiteAction.PARAM_WEB_URL);
    }catch(Exception e) {
      url = "";
      Log.w("ShowWebsiteActivity", "no web url provided by user");
    }
    WebView webview = new WebView(this);
    // display the progress in the activity title bar, like the
    // browser app does.
    getWindow().requestFeature(Window.FEATURE_PROGRESS);

    webview.getSettings().setJavaScriptEnabled(true);

    final Activity activity = this;
    webview.setWebChromeClient(new WebChromeClient() {
      public void onProgressChanged(WebView view, int progress) {
        // Activities and WebViews measure progress with different scales.
        // The progress meter will automatically disappear when we reach 100%
        activity.setProgress(progress * PROGRESS_INTERVAL);
      }
    });
    webview.setWebViewClient(new WebViewClient() {
      public void onReceivedError(WebView view, int errorCode, 
          String description, String failingUrl) {
        Toast.makeText(activity, getString(R.string.oh_no) + description, Toast.LENGTH_SHORT).show();
      }
    });
    webview.loadUrl(url);
    setContentView(webview);
  } 
}
