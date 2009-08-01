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
package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.nyu.cs.omnidroid.contprovider.PopulateAppConfigSetup;

/**
 * This class will present a list of all the packages installed on Android.
 * 
 */
public class PackageLister extends ListActivity {
  private static final String TAG = "EventCatcher";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    Log.i(this.getLocalClassName(), "onCreate start");
    super.onCreate(savedInstanceState);

    PackageManager pm = this.getPackageManager();
    List<PackageInfo> pkgList = pm.getInstalledPackages(0);
    Iterator<PackageInfo> i = pkgList.iterator();
    List<String> pkgNames = new ArrayList<String>();
    while (i.hasNext()) {
      PackageInfo pkg = i.next();
      pkgNames.add(pkg.packageName);
    }

    Iterator<String> i2 = pkgNames.iterator();
    while (i2.hasNext()) {
      String name = i2.next();
      Log.i(this.getLocalClassName(), name);
    }

    // TODO (acase): Convert to human readable names
    // TODO (acase): Filter based on if it can catch events
    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pkgNames));
    getListView().setTextFilterEnabled(true);
    Log.i(this.getLocalClassName(), "onCreate exit");
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Log.i(TAG, "Enter");
    Log.i(TAG, "listview: " + l);
    Log.i(TAG, "view: " + v);
    Log.i(TAG, "position: " + position);
    Log.i(TAG, "id: " + id);

    // TODO: Pull config file from application and write it to our AGParser
    /*
     * The application mapping will be pulled from the Content Provider of the application Its
     * hard-coded for now.
     */
    final Context context = this.getApplicationContext();
    PopulateAppConfigSetup ac = new PopulateAppConfigSetup(context);
    ac.populate();
    ac.retrieveAppCfg();
    Log.i(TAG, "Exit");
  }

}