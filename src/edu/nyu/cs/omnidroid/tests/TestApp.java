package edu.nyu.cs.omnidroid.tests;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.nyu.cs.omnidroid.external.eventapp.ExternalCPActivity;
import edu.nyu.cs.omnidroid.ui.PackageLister;
import edu.nyu.cs.omnidroid.ui.Actions;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * This class will present a list of test applications that we can run via UI selection.
 * 
 * @author acase
 */
public class TestApp extends ListActivity {

  static final String[] TESTS = new String[] {
      "Test CP",
      "Test Service",
      "PackageLister",
      "External Event App",
      "Checkable List",
      "Fancy List",
      "Test User Config",
      "Test App Config",
      "Test Exception Handler",
      "Test Action Data"
  };

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
      i.setClass(this.getApplicationContext(), TestCP.class);
      break;
    case 1:
      i.setClass(this.getApplicationContext(), TestService.class);
      break;
    case 2:
      i.setClass(this.getApplicationContext(), PackageLister.class);
      break;
    case 3:
      i.setClass(this.getApplicationContext(), ExternalCPActivity.class);
      break;
    case 4:
      i.setClass(this.getApplicationContext(), CheckableList.class);
      break;
    case 5:
      i.setClass(this.getApplicationContext(), CheckListViewDemo.class);
      break;
    case 6:
      i.setClass(this.getApplicationContext(), TestUserConfig.class);
      break;
    case 7:
      i.setClass(this.getApplicationContext(), TestAppConfig.class);
      break;
    case 8:
      i.setClass(this.getApplicationContext(), TestExceptionHandler.class);
      break;
    case 9:
      i.putExtra(AGParser.KEY_APPLICATION, "SMS");
      i.putExtra(UGParser.KEY_EVENT_TYPE, "SMS_RECEIVED");
      //i.putExtra(UGParser.KEY_ActionApp, "SMS");
      //i.putExtra(UGParser.KEY_ActionName, "SMS_SEND");
      i.setClass(this.getApplicationContext(), Actions.class);
      break;
    default:
      Log.i(this.getLocalClassName(), "Invalid Test Selection");
      break;
    }
    startActivity(i);
    finish();
  }
}