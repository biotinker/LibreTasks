/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package edu.nyu.cs.omnidroid.app.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import android.content.Context;
import android.util.Log;

public class OmLogger {

  public static void write(Context context, String s) {
    try {
      /*
       * FileHandler handler = new FileHandler(context.getFilesDir().getAbsolutePath()+"/droid.log",
       * 1000, 2); Logger logger= Logger.getLogger("Omni"); logger.addHandler(handler); LogRecord
       * record = new LogRecord(Level.INFO, s); logger.log(record); handler.close();
       * Toast.makeText(context, "2.Write to log", 5).show();
       * Log.i("Succees","inserted into Omni.log");
       */
      // Context context=edu.nyu.cs.omnidroid.bkgservice.OmniBR.class.cast(context);
      FileOutputStream fOut = context.openFileOutput("Omni.log", 32768);
      OutputStreamWriter osw = new OutputStreamWriter(fOut);
      osw.write(s + "\n");
      osw.flush();
      osw.close();

    } catch (Exception e) {
      Log.i("Omlogger error", e.toString());
    }

  }

  public static String read(Context context) {

    String total = "";
    try {
      FileInputStream FIn1 = context.openFileInput("Omni.log");
      BufferedInputStream bis1 = new BufferedInputStream(FIn1);
      DataInputStream dis1 = new DataInputStream(bis1);
      String line1;

      while ((line1 = dis1.readLine()) != null) {
        total = total.concat(line1);
      }
      FIn1.close();
    } catch (Exception e) {
      Log.i("Omlogger Read error", e.toString());
    }
    return total;
  }
}