package com.OmniBR;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Logger{

static void write(String str)
{
	try
	{
	FileOutputStream fOut = new FileOutputStream("/Log.txt"); 
    OutputStreamWriter osw = new OutputStreamWriter(fOut);  
    osw.write(str); 
    osw.flush(); 
    osw.close();
	}catch(Exception e){}
} 
static String read()
{
try
{
FileInputStream FIn = new FileInputStream("/Log.txt"); 
BufferedInputStream bis = new BufferedInputStream(FIn); 
DataInputStream dis = new DataInputStream(bis);
String str =dis.readLine();
return str;
}catch(Exception e){return e.getMessage();}
}
}
