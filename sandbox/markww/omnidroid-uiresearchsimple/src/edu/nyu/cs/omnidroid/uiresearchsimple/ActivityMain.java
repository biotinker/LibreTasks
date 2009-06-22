package edu.nyu.cs.omnidroid.uiresearchsimple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This is the main entry point of the application.
 */
public class ActivityMain extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Load all events and filters from database once.
        DatabaseMemCache.instance().reloadFromDatabase();
        
        // Link up click handlers with their buttons.
        Button btnCreateRule = (Button)findViewById(R.id.activity_dummyui_btnCreateRule);
        btnCreateRule.setOnClickListener(listenerBtnClickCreateRule);

        Button btnViewRules = (Button)findViewById(R.id.activity_dummyui_btnViewRules);
        btnViewRules.setOnClickListener(listenerBtnClickViewRules);
        
        Button btnHelp = (Button)findViewById(R.id.activity_dummyui_btnHelp);
        btnHelp.setOnClickListener(listenerBtnClickHelp);
    }
    
    /**
     * Launch the create rule activity.
     */
    private OnClickListener listenerBtnClickCreateRule = new OnClickListener() {
        public void onClick(View v) {
        	// User wants to create a new rule, move along to that Activity.
        	Intent intent = new Intent();
            intent.setClass(getApplicationContext(), ActivityChooseRootEvent.class);
            startActivity(intent);
        }
    };
    
    private OnClickListener listenerBtnClickViewRules = new OnClickListener() {
        public void onClick(View v) {
        	Util.showAlert(v.getContext(), "Sorry!", "Viewing saved rules is not yet implemented!");
        }
    };
    
    private OnClickListener listenerBtnClickHelp = new OnClickListener() {
        public void onClick(View v) {
        	Util.showAlert(v.getContext(), "Sorry!", "Help is not yet available!");
        }
    };
}