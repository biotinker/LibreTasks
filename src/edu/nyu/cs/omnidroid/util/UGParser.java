//Author: Pradeep Varma
package edu.nyu.cs.omnidroid.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;

/**
 * Used to Specify the User Config schema
 * 
 * @param ParserType
 *          Specify UC for UserConfig and AC for Application Config
 */
public class UGParser {
  private ArrayList<String> Schema;
  private FileOutputStream fout;
  private OutputStreamWriter osw;
  private FileInputStream FIn;
  private BufferedInputStream bis;
  private DataInputStream dis;
  private Context context;
  private static final String KEY_InstanceName = "InstanceName";
  private static final String KEY_EventName = "EventName";
  private static final String KEY_EventApp = "EventApp";
  private static final String KEY_FilterType = "FilterType";
  private static final String KEY_FilterData = "FilterData";
  private static final String KEY_ActionApp = "ActionApp";
  private static final String KEY_ActionName = "ActionName";
  private static final String KEY_ActionData = "ActionData";
  private static final String KEY_EnableInstance = "EnableInstance";
  private static final int MODE_WRITE = android.content.Context.MODE_WORLD_WRITEABLE;
  private static final int MODE_APPEND = android.content.Context.MODE_APPEND;
  
  /**
   * Initializes the parser to be a User Config or App Config based on the parameter
   * 
   * @param context
   *          Specify the context of the Application
   */
  public UGParser(Context context) {
    // Defining the User Config Schema in ArrayList
    Schema = new ArrayList<String>();
    Schema.add("InstanceName");
    Schema.add("EventName");
    Schema.add("EventApp");
    Schema.add("FilterType");
    Schema.add("FilterData");
    Schema.add("ActionApp");
    Schema.add("ActionName");
    Schema.add("ActionData");
    Schema.add("EnableInstance");
    this.context = context;
  }

  /**
   * Opens User Config for writing
   * 
   */
  public void OpenFileWrite(int mode) {
    try {
      fout = context.openFileOutput("UserConfig.txt", mode);
      osw = new OutputStreamWriter(fout);
    } catch (FileNotFoundException e) {
      OmLogger.write(context, "Unable to Open User Config to write");
    }
  }

  /**
   * Opens User Config for reading
   * 
   */
  public void OpenFileRead() {
    try {
      FIn = context.openFileInput("UserConfig.txt");
      bis = new BufferedInputStream(FIn);
      dis = new DataInputStream(bis);
    } catch (FileNotFoundException e) {
      OmLogger.write(context, "Unable to Open User Config to write");
    }
  }

  /**
   * deletes the entire userConfig except the Enabled Field.
   * 
   */
  public void delete_all() {
    try {
      String Enabled = readLine("Enabled");
      if (Enabled.equals(null))
        Enabled = "True";
      String LineString = new String("Enabled" + ":" + Enabled + "\n");
      OpenFileWrite(MODE_WRITE);
      osw.write(LineString);
      osw.flush();
      osw.close();
    } catch (Exception e) {
      OmLogger.write(context, "Could not delete Instances");
    }
  }

  /**
   * deletes the Record from userConfig.
   * 
   * @param HM
   *          HashMap of the record to be deleted.
   */
  public boolean deleteRecord(HashMap<String, String> HM) {
    try {
      ArrayList<HashMap<String, String>> UCRecords = readRecords();
      ArrayList<HashMap<String, String>> UCRecords_New = readRecords();

      Iterator<HashMap<String, String>> i = UCRecords.iterator();
      while (i.hasNext()) {
        HashMap<String, String> HM1 = i.next();
        if (HM1.equals(HM))
          continue;
        UCRecords_New.add(HM1);
      }
      delete_all();

      Iterator<HashMap<String, String>> i1 = UCRecords_New.iterator();
      while (i1.hasNext()) {
        HashMap<String, String> HM1 = i.next();
        writeRecord(HM1);
      }
      return true;
    } catch (Exception e) {
      OmLogger.write(context, "Could not delete Instance Record");
      return false;
    }
  }

  /**
   * deletes the Record from userConfig.
   * 
   * @param InstanceName
   *          InstanceName of the record to be deleted.
   */
  public boolean deleteRecordbyInstance(String InstanceName) {
    try {
      ArrayList<HashMap<String, String>> UCRecords = readRecords();
      ArrayList<HashMap<String, String>> UCRecords_New = readRecords();

      Iterator<HashMap<String, String>> i = UCRecords.iterator();
      while (i.hasNext()) {
        HashMap<String, String> HM1 = i.next();
        if (HM1.get(KEY_InstanceName).equals(InstanceName))
          continue;
        UCRecords_New.add(HM1);
      }
      delete_all();

      Iterator<HashMap<String, String>> i1 = UCRecords_New.iterator();
      while (i1.hasNext()) {
        HashMap<String, String> HM1 = i.next();
        writeRecord(HM1);
      }
      return true;
    } catch (Exception e) {
      OmLogger.write(context, "Could not delete Instance Record");
      return false;
    }
  }

  /**
   * Writes a Key Value into the UserConfig as Key:Value
   * 
   * @param Key
   *          Specify the Key to be written
   * @param Value
   *          Specify the Value to be written
   * @return Returns true if successful
   */
  public boolean write(String key, String val) {
    try {
      final String LineString = new String(key + ":" + val + "\n");
      OpenFileWrite(MODE_APPEND);
      osw.write(LineString);
      osw.flush();
      osw.close();
      return true;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to write line in User Config");
      return false;
    }
  }

  /**
   * Updates a Key Value with the val passed
   * 
   * @param Key
   *          Specify the Key to be updated
   * @param Value
   *          Specify the Value to be written
   * @return Returns true if successful
   */
  public boolean update(Context context, String key, String val) {
    try {
      OpenFileRead();
      String line;
      ArrayList<String> lines = new ArrayList<String>();
      while ((line = dis.readLine()) != null) {
        String[] parts = line.split(":");
        if (!parts[0].toString().equalsIgnoreCase(key)) {
          lines.add(line);
        }

      }
      OpenFileWrite(MODE_WRITE);
      String LineString = new String(key + ":" + val + "\n");
      osw.write(LineString);
      Iterator<String> i = lines.iterator();
      while (i.hasNext()) {
        osw.write(i.next());
      }
      osw.flush();
      osw.close();
      return true;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to write line in User Config");
      return false;
    }
  }

  /**
   * Reads the value of the key
   * 
   * @param Key
   *          Specify the key
   * @return Returns value of the key
   */
  public String readLine(String key) {
    String col2 = "";
    try {
      OpenFileRead();
      String line;

      while ((line = dis.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts[0].toString().equalsIgnoreCase(key)) {
          col2 = parts[1].toString();
          break;
        }
      }
      dis.close();
      bis.close();
      return col2;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to read Line from User Config");
      e.printStackTrace();
      return col2;
    }
  }

  /**
   * Reads values from the UserConfig based on the Key
   * 
   * @param Key
   *          Specify the Key to be read. example ActionName, EventName
   * @return Returns values as ArrayList of Strings
   */
  public ArrayList<String> readLines(String key) {
    ArrayList<String> cols2 = new ArrayList<String>();

    String val;
    try {
      OpenFileRead();
      String line;

      while ((line = dis.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts[0].toString().equalsIgnoreCase(key)) {
          val = parts[1].toString();
          cols2.add(val);
        }
      }
      return cols2;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to read Line from User Config");
      return cols2;
    }
  }

  /**
   * Reads Instance Records from the UserConfig
   * 
   * @return Returns Array List of HashMaps. HashMaps have the keys as EventName, EventApp,
   *         FilterType, FilterData, ActionName, ActionApp, AppData,EnableInstance
   */
  public ArrayList<HashMap<String, String>> readRecords() {
    ArrayList<HashMap<String, String>> UCRecords = new ArrayList<HashMap<String, String>>();

    try {
      OpenFileRead();
      String line = "";

      while ((line = dis.readLine()) != null) {
        HashMap<String, String> HM = new HashMap<String, String>();
        String[] parts = line.split(":");
        if (parts[0].toString().equalsIgnoreCase("InstanceName")) {

          Iterator<String> i = Schema.iterator();
          String key;
          while (i.hasNext()) {
            key = i.next();
            HM.put(key, line.split(":")[1].toString());
            if (!line.split(":")[0].toString().equalsIgnoreCase("EnableInstance"))
              line = dis.readLine();
          }
          UCRecords.add(HM);
        }

      }
      return UCRecords;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to read Line from User Config");
      return UCRecords;
    }
  }

  /**
   * Reads Instance Records from the UserConfig based on the InstanceName passed
   * 
   * @param Key
   *          InstanceName to be passed.
   * @return Returns HashMap.
   * */
  public HashMap<String, String> readRecord(String Key) {
    HashMap<String, String> HM = new HashMap<String, String>();

    try {
      OpenFileRead();
      String line = "";

      while ((line = dis.readLine()) != null) {

        String[] parts = line.split(":");
        if (parts[0].toString().equalsIgnoreCase("InstanceName")
            && parts[1].toString().equalsIgnoreCase(Key)) {
          Iterator<String> i = Schema.iterator();
          String key;
          while (i.hasNext()) {
            key = i.next();
            HM.put(key, line.split(":")[1].toString());
            if (!line.split(":")[0].toString().equalsIgnoreCase("EnableInstance"))
              line = dis.readLine();
          }
        }

      }
      return HM;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to read record from User Config");
      return HM;
    }
  }

  /**
   * Writes an Instance in the UserConfig File
   * 
   * @param HM
   *          HashMap containing EventName, EventApp, FilterType, FilterData, ActionName, ActionApp,
   *          AppData,EnableInstance
   * @return Returns 1 if successful else 0.
   */
  public int writeRecord(HashMap<String, String> HM) {
    try {
      Iterator<String> i = Schema.iterator();
      String key;
      while (i.hasNext()) {
        key = i.next();
        write(key, HM.get(key).toString());
      }
      return 1;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to write record t Usoer Config");
      return 0;
    }
  }

  /**
   * Updates the Instance Record
   * 
   * @param HM
   *          HashMap containing EventName, EventApp, FilterType, FilterData, ActionName, ActionApp,
   *          AppData,EnableInstance
   * @return Returns true if successful else false.
   */
  public boolean updateRecord(Context context, HashMap<String, String> HM) {
    try {
      deleteRecord(HM);
      writeRecord(HM);
      return true;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to write record from User Config");
      return false;
    }
  }

  /**
   * Reads Instance Records from the UserConfig based on the EventName passed
   * 
   * @param Key
   *          EventName to be passed.
   * @return Returns ArrayList.
   * */
  public ArrayList<HashMap<String, String>> readbyEventName(Context context, String Key) {
    ArrayList<HashMap<String, String>> UCRecords = new ArrayList<HashMap<String, String>>();

    try {
      OpenFileRead();
      String line = "";

      while ((line = dis.readLine()) != null) {
        HashMap<String, String> HM = new HashMap<String, String>();
        String[] parts = line.split(":");
        if (parts[0].toString().equalsIgnoreCase("EventName")
            && parts[1].toString().equalsIgnoreCase(Key)) {
          Iterator<String> i = Schema.iterator();
          i.next();// Ignoring Instance Name
          String key;
          while (i.hasNext()) {
            key = i.next();
            HM.put(key, line.split(":")[1].toString());
            if (!line.split(":")[0].toString().equalsIgnoreCase("EnableInstance"))
              line = dis.readLine();
          }
        }
      }
      return UCRecords;
    } catch (Exception e) {
      OmLogger.write(context, "Unable to read Line from User Config");
      return UCRecords;
    }
  }

}