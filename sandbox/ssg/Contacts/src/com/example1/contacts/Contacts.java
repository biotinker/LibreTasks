package com.example1.contacts;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;

public class Contacts extends Activity {
	@Override

	public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);

	String[] cols=displayRecords();
	//for(int i = 0; i < cols.length; i++)
	//		{
	//		Toast.makeText(this, cols[i], Toast.LENGTH_LONG).show();
	//		}
	
	try {
	     ObjectOutputStream  objectOut = null; 
	     File file = new File("appconf.txt");
	      if(!file.exists() && file.mkdir())
	   {
	       file = new File(file.toString());
	       if(!file.exists())
	        file.createNewFile();
	   } 
	      FileOutputStream stream = new FileOutputStream(file.toString());
	      objectOut = new ObjectOutputStream(new BufferedOutputStream(stream));
	      objectOut.writeObject(cols); 
    } catch (IOException e) {
    	e.printStackTrace(); 

    }
    try {
    	FileInputStream FIn = new FileInputStream("appconf.txt"); 
    	  BufferedInputStream bis = new BufferedInputStream(FIn); 
    	  DataInputStream dis = new DataInputStream(bis);
        String str;
        while ((str = dis.readLine()) != null) {
        	Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        }
        dis.close();
    } catch (IOException e) {
    	e.printStackTrace(); 
    }



	}

	
	
	
	private String[] displayRecords() {

// put the uri in the content:// format in the managedQuery
	Cursor cur = managedQuery(Uri.parse("content://call_log/calls"), null, 

	null, 

	null, 

	null 

	);
	String[] cols = null;

	if (cur.moveToFirst()) {

		
	do {

			 cols = cur.getColumnNames();
			
			
		//for(int i = 0; i < cols.length; i++) { Log.d(TAG, "Column: " + cols[i]); } while(c.moveToNext()) { Log.d(TAG, c.getString(c.getColumnIndex("address")) + ":" + c.getString(c.getColumnIndex("person")) + ":" +c.getString(c.getColumnIndex("date")) + ":" +c.getString(c.getColumnIndex("body"))); } 
	//	for(int i = 0; i < cols.length; i++)
	//	{
	//	Toast.makeText(this, cols[i], Toast.LENGTH_LONG).show();
	//	}

	} while (cur.moveToNext());

	}
	return cols;

	}
}