package edu.nyu.cs.omnidroid.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;

public class UGParser
{

public void write(Context context,String col1,String col2)
  {
  try
  {
  final String LineString = new String(col1+":"+col2+"\n"); 
  FileOutputStream fOut = context.openFileOutput("UserConfig.txt",32768); 
  OutputStreamWriter osw = new OutputStreamWriter(fOut);  
  osw.write(LineString);
  osw.flush(); 
  osw.close();
  }catch(Exception e)
  {
	OmLogger.write(context, "Unable to write line in User Config");  
  }
  }
  
  public String readLine(Context context,String col1)
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
  	if(parts[0].toString().equalsIgnoreCase(col1))
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
 
  public ArrayList<String> readLines(Context context,String col1)
  {
  ArrayList<String> cols2=new ArrayList<String>();
  
  String col2;
  try{
  FileInputStream FIn = context.openFileInput("UserConfig.txt"); 
  BufferedInputStream bis = new BufferedInputStream(FIn); 
  DataInputStream dis = new DataInputStream(bis);
  String line;
  
  while((line=dis.readLine())!=null)
  {                
  	String[] parts=line.split(":");
  	if(parts[0].toString().equalsIgnoreCase(col1))
  	{
  		col2=parts[1].toString();
  		cols2.add(col2);
  	}
  		
  }
  return cols2;
  }catch(Exception e)
  {
	  OmLogger.write(context,"Unable to read Line from User Config");
	  return cols2;
  }
  }
  
}