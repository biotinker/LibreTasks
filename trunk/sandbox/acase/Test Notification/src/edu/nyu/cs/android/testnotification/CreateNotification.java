package edu.nyu.cs.android.testnotification;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateNotification extends Activity {    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notify_with_text);
        
        Button button;

        // short notification
        button = (Button) findViewById(R.id.short_notify);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Note that we create the Toast object and call the show() method
                // on it all on one line.  Most uses look like this, but there
                // are other methods on Toast that you can call to configure how
                // it appears.
                //
                // Note also that we use the version of makeText that takes a
                // resource id (R.string.short_notification_text).  There is also
                // a version that takes a CharSequence if you must construct
                // the text yourself.
                Toast.makeText(CreateNotification.this, R.string.short_notification_text,
                    Toast.LENGTH_SHORT).show();
            }
        });

        // long notification
        // The only difference here is that the notification stays up longer.
        // You might want to use this if there is more text that they're going
        // to read.
        button = (Button) findViewById(R.id.long_notify);
               button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(CreateNotification.this, R.string.long_notification_text,
                    Toast.LENGTH_LONG).show();
            }
        });
    }




}

