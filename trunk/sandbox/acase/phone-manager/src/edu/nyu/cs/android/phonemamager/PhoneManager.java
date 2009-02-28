package edu.nyu.cs.android.phonemamager;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class PhoneManager extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, APPLICATIONS));
        getListView().setTextFilterEnabled(true);
    }

    static final String[] APPLICATIONS = new String[] {
        "Email", "Messaging", "Dialer", "Weather" };

}