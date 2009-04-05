package edu.nyu.cs.omnidroid.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import android.content.Context;

public class UGParser
{
	/**
	 * deletes the entire userConfig Except the Enabled Field.
	 *  
	 * @param Context
	 *          Application Context
	 */ 
public void delete_all(Context context)
{
	try
	{
	String Enabled=readLine(context,"Enabled");
	if(Enabled.equals(null)) Enabled="True";
	
	String LineString = new String("Enabled"+":"+Enabled+"\n"); 
	FileOutputStream fOut = context.openFileOutput("UserConfig.txt",2); 
	OutputStreamWriter osw = new OutputStreamWriter(fOut);  
	osw.write(LineString);
	osw.flush(); 
	osw.close();
	}
	catch(Exception e)
	{
	OmLogger.write(context,"Could not delete Instances");	
	}
}

/**
 * deletes the Record from userConfig.
 *  
 * @param Context
 *          Application Context
 * @param HM
 *         HashMap of the record to be deleted.
 */ 
public void deleteRecord(Context context,HashMap<String,String> HM)
{
try
{
ArrayList<HashMap <String,String>> UCRecords=readRecords(context);
ArrayList<HashMap <String,String>> UCRecords_New=readRecords(context);

Iterator<HashMap<String,String>> i=UCRecords.iterator();
while(i.hasNext())
{
HashMap<String,String> HM1=i.next();
if(HM1.equals(HM))
continue;
UCRecords_New.add(HM1);
}
delete_all(context);

Iterator<HashMap<String,String>> i1=UCRecords_New.iterator();
while(i1.hasNext())
{
	HashMap<String,String> HM1=i.next();
    writeRecord(context, HM1);
}
}
catch(Exception e)
{
OmLogger.write(context,"Could not delete Instance Record");	
}
}


/**
 * Writes a Key Value into the UserConfig as Key:Value
 *  
 * @param Context
 *          Application Context
 * @param Key
 *          Specify the Key to be written
 * @param Value
 *          Specify the Value to be written
 * @return Returns 1 if successful
 */ 
public int write(Context context,String key,String val)
  {
  try
  {
  final String LineString = new String(key+":"+val+"\n"); 
  FileOutputStream fOut = context.openFileOutput("UserConfig.txt",32768); 
  OutputStreamWriter osw = new OutputStreamWriter(fOut);  
  osw.write(LineString);
  osw.flush(); 
  osw.close();
  return 1;
  }catch(Exception e)
  {
	OmLogger.write(context, "Unable to write line in User Config");  
  return 0;
  }
  }
  
  public String readLine(Context context,String key)
  {
  String col2="";
  try{
  FileInputStream FIn = context.openFileInput("UserConfig.txt"); 
  BufferedInputStream bis = new BufferedInputStream(FIn); 
  DataInputStream dis = new DataInputStream(bis);
  String line;
  
  while((line=dis.readLine())!=null)
  {                
  	String[] parts=line.split(":");
  	if(parts[0].toString().equalsIgnoreCase(key))
  	{
  		col2=parts[1].toString();
  		break;
  	}
  		
  }
  return col2;
  }catch(Exception e)
  {
	  OmLogger.write(context,"Unable to read Line from User Config");
	  return col2;
  }
  }

  /**
   * Reads values from the UserConfig based on the Key
   *  
   * @param Context
   *          Application Context
   * @param Key
   *          Specify the Key to be read. example ActionName, EventName
   * @return Returns values as ArrayList of Strings
   */ 
  public ArrayList<String> readLines(Context context,String key)
  {
  ArrayList<String> cols2=new ArrayList<String>();
  
  String val;
  try{
  FileInputStream FIn = context.openFileInput("UserConfig.txt"); 
  BufferedInputStream bis = new BufferedInputStream(FIn); 
  DataInputStream dis = new DataInputStream(bis);
  String line;
  
  while((line=dis.readLine())!=null)
  {                
  	String[] parts=line.split(":");
  	if(parts[0].toString().equalsIgnoreCase(key))
  	{
  		val=parts[1].toString();
  		cols2.add(val);
  	}
  		
  }
  return cols2;
  }catch(Exception e)
  {
	  OmLogger.write(context,"Unable to read Line from User Config");
	  return cols2;
  }
  }
  
  /**
   * Reads Instance Records from the UserConfig
   *  
   * @param Context
   *          Application Context
   * @return Returns Array List of HashMaps. HashMaps have the keys as EventName, EventApp, FilterType, FilterData, ActionName, ActionApp, AppData,EnableInstance
   */
  public ArrayList<HashMap<String,String>> readRecords(Context context)
  {
	  ArrayList<HashMap<String,String>> UCRecords=new ArrayList<HashMap<String,String>>();
	  
	  try{
		  FileInputStream FIn = context.openFileInput("UserConfig.txt"); 
		  BufferedInputStream bis = new BufferedInputStream(FIn); 
		  DataInputStream dis = new DataInputStream(bis);
		  String line="";
		    
		  while((line=dis.readLine())!=null)
		  { 
			  HashMap<String,String> HM=new HashMap<String,String>();
			  String[] parts=line.split(":");
			  	if(parts[0].toString().equalsIgnoreCase("EventName"))
			  			{
			  		HM.put("EventName",line.split(":")[1].toString());
		  			line=dis.readLine();
		  			HM.put("EventApp",line.split(":")[1].toString());
		  			line=dis.readLine();
		  			HM.put("FilterType",line.split(":")[1].toString());
		  			line=dis.readLine();
		  			HM.put("FilterData",line.split(":")[1].toString());
		  			line=dis.readLine();
		  			HM.put("ActionName",line.split(":")[1].toString());
		  			line=dis.readLine();
		  			HM.put("ActionApp",line.split(":")[1].toString());
		  			line=dis.readLine();
		  			HM.put("ActionData",line.split(":")[1].toString());
		  			line=dis.readLine();
		  			HM.put("EnableInstance",line.split(":")[1].toString());
		  			line=dis.readLine();
			  			}
			  	UCRecords.add(HM);
		  }
		  return UCRecords;
	  }catch(Exception e)
	  {
		  OmLogger.write(context,"Unable to read Line from User Config");
		  return UCRecords;
	  }
  }
 
  /**
   * Reads Instance Records from the UserConfig based on the EventName passed
   *  
   * @param Context
   *          Application Context
   * @param Key
   *          EventName to be passed.
   * @return Returns Array List of HashMaps. HashMaps have the keys as EventName, EventApp, FilterType, FilterData, ActionName, ActionApp, AppData,EnableInstance
   */
  public ArrayList<HashMap<String,String>> readRecord(Context context,String Key)
  {
	  ArrayList<HashMap<String,String>> UCRecords=new ArrayList<HashMap<String,String>>();
	  
	  try{
		  FileInputStream FIn = context.openFileInput("UserConfig.txt"); 
		  BufferedInputStream bis = new BufferedInputStream(FIn); 
		  DataInputStream dis = new DataInputStream(bis);
		  String line="";
		 
		  while((line=dis.readLine())!=null)
		  { 
			  HashMap<String,String> HM=new HashMap<String,String>();
			  
			  String[] parts=line.split(":");
			  	if(parts[0].toString().equalsIgnoreCase("EventName") && parts[1].toString().equalsIgnoreCase(Key) )
			  			{
			  			HM.put("EventName",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			HM.put("EventApp",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			HM.put("FilterType",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			HM.put("FilterData",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			HM.put("ActionName",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			HM.put("ActionApp",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			HM.put("ActionData",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			HM.put("EnableInstance",line.split(":")[1].toString());
			  			line=dis.readLine();
			  			UCRecords.add(HM);
			  			}
			  	
		 }
		  return UCRecords;
	  }catch(Exception e)
	  {
		  OmLogger.write(context,"Unable to read record from User Config");
		  return UCRecords;
	  }
  }
  
  /**
   * Writes an Instance in the UserConfig File
   * 
   * @param Context
   *          Application Context
   * @param HM
   *          HashMap containing EventName, EventApp, FilterType, FilterData, ActionName, ActionApp, AppData,EnableInstance
   * @return Returns 1 if successful else 0.
   */
  public int writeRecord(Context context,HashMap<String,String> HM)
  {
	  try{
	  write(context,"EventName",HM.get("EventName").toString());
	  write(context,"EventApp",HM.get("EventApp").toString());
	  write(context,"FilterType",HM.get("FilterType").toString());
	  write(context,"FilterData",HM.get("FilterData").toString());
	  write(context,"ActionName",HM.get("ActionName").toString());
	  write(context,"ActionApp",HM.get("ActionApp").toString());
	  write(context,"ActionData",HM.get("ActionData").toString());
	  write(context,"EnableInstance",HM.get("EnableInstance").toString());
	  return 1;
	  }catch(Exception e)
	  {
		  OmLogger.write(context,"Unable to write record from User Config");
		  return 0;
	  }
  }
  
  
}