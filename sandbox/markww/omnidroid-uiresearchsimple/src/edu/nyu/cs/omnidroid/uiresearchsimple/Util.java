package edu.nyu.cs.omnidroid.uiresearchsimple;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;


/**
 * Spare parts.
 */
public class Util
{
	public static void showAlert(Context context, String title, String message)
	{
		new AlertDialog.Builder(context).setTitle(title)
	    .setIcon(0)
	    .setMessage(message)
	    .setCancelable(true)
	    .setPositiveButton(
	      "OK",
          new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialoginterface, int i) {
              }
          }
	    ).show();
	}
	
	public static void inflateDialog(Context context, LinearLayout ll)
	{
		WindowManager wm  = (WindowManager)context.getSystemService ( Context.WINDOW_SERVICE ); 
        Display display = wm.getDefaultDisplay(); 
        ll.setMinimumWidth(display.getWidth()-40);
        ll.setMinimumHeight(display.getHeight()-40);
	}
}