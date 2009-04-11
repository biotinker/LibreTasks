package edu.nyu.cs.omnidroid.tests;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This class will present a list of test applications that we can run via UI selection.
 * 
 * @author acase
 */
public class TestApp extends ListActivity {

  static final String[] TESTS = new String[] { "Test User Config", "Test App Config Get",
      "Test CP", "Test Exception Handler", "Test Service", "TestApp" };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);
    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, TESTS));
    getListView().setTextFilterEnabled(true);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {

    Log.i(this.getLocalClassName(), "Enter");
    Log.i(this.getLocalClassName(), "listview: " + l);
    Log.i(this.getLocalClassName(), "view: " + v);
    Log.i(this.getLocalClassName(), "position: " + position);
    Log.i(this.getLocalClassName(), "id: " + id);

    Intent i = new Intent();

    switch (position) {
    case 0:
      i.setClassName(this.getApplicationContext(), "edu.nyu.cs.omnidroid.tests.TestUserConfig");
      break;
    case 1:
      i.setClassName(this.getApplicationContext(), "edu.nyu.cs.omnidroid.tests.TestAppConfig");
      break;
    case 2:
      i.setClassName(this.getApplicationContext(), "edu.nyu.cs.omnidroid.tests.TestCP");
      break;
    case 3:
      i.setClassName(this.getApplicationContext(), "edu.nyu.cs.omnidroid.tests.TestExceptionHandler");
      break;
    case 4:
      i.setClassName(this.getApplicationContext(), "edu.nyu.cs.omnidroid.tests.TestService");
      break;
    case 5:
      i.setClassName(this.getApplicationContext(), "edu.nyu.cs.omnidroid.tests.TestApp");
      break;
    default:
      Log.i(this.getLocalClassName(), "Invalid Test Selection");
      // TODO (acase): Catch this as an exception
      break;
    }
    startActivity(i);
  }
}