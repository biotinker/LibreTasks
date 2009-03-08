package edu.nyu.cs.android.testnotification;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TestNotification extends Activity {
    public static final int INSERT_ID = Menu.FIRST;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
   }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.create_notification);
        return result;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            createNotification();
            return true;
        }
       
        return super.onOptionsItemSelected(item);
    }

    private void createNotification() {
        //Intent i = new Intent(this, CreateNotification.class);
        // TODO: See if this should be "StartActivitySomethingOrOther"
        //startActivity(i);
    }

}