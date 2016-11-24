/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.controller.util;

import android.util.Log;

/**
 * Class takes place of the Android.util.Log class. It logs data both to the Android.util.Log class
 * as well as to our DB so that users can see what is going on inside Omnidroid and debug possible
 * problems.
 * 
 */
public final class Logger {
  // Priority constant for the println method.
  public static final int ASSERT = 1;
  // Priority constant for the println method; use Log.d.
  public static final int DEBUG = 2;
  // Priority constant for the println method; use Log.e.
  public static final int ERROR = 3;
  // Priority constant for the println method; use Log.i.
  public static final int INFO = 4;
  // Priority constant for the println method; use Log.v.
  public static final int VERBOSE = 5;
  // Priority constant for the println method; use Log.w.
  public static final int WARN = 6;

  /**
   * Send a DEBUG log message and log the exception.
   * 
   * @return
   */
  public static int d(String tag, String msg, Throwable tr) {
    // TODO: (acase) Log to DB
    return Log.d(tag, msg, tr);
  }

  /**
   * Send a DEBUG log message.
   */
  public static int d(String tag, String msg) {
    // TODO: (acase) Log to DB
    return Log.d(tag, msg);
  }

  /**
   * Send an ERROR log message.
   */
  public static int e(String tag, String msg) {
    // TODO: (acase) Log to DB
    return Log.e(tag, msg);
  }

  /**
   * Send a ERROR log message and log the exception.
   */
  public static int e(String tag, String msg, Throwable tr) {
    // TODO: (acase) Log to DB
    return Log.e(tag, msg, tr);
  }

  /**
   * Handy function to get a loggable stack trace from a Throwable
   */
  public static String getStackTraceString(Throwable tr) {
    // TODO: (acase) Log to DB
    return Log.getStackTraceString(tr);
  }

  /**
   * Send a INFO log message and log the exception.
   */
  public static int i(String tag, String msg, Throwable tr) {
    // TODO: (acase) Log to DB
    return Log.i(tag, msg, tr);
  }

  /**
   * Send an INFO log message.
   */
  public static int i(String tag, String msg) {
    // TODO: (acase) Log to DB
    return Log.i(tag, msg);
  }

  /**
   * Checks to see whether or not a log for the specified tag is loggable at the specified level.
   */
  public static boolean isLoggable(String tag, int level) {
    // TODO: (acase) Log to DB
    return Log.isLoggable(tag, level);
  }

  /**
   * Low-level logging call.
   */
  public static int println(int priority, String tag, String msg) {
    // TODO: (acase) Log to DB
    return Log.println(priority, tag, msg);
  }

  /**
   * Send a VERBOSE log message and log the exception.
   */
  public static int v(String tag, String msg, Throwable tr) {
    // TODO: (acase) Log to DB
    return Log.v(tag, msg, tr);
  }

  /**
   * Send a VERBOSE log message.
   */
  public static int v(String tag, String msg) {
    // TODO: (acase) Log to DB
    return Log.v(tag, msg);
  }

  /**
   * Send a WARN log message.
   */
  public static int w(String tag, String msg) {
    // TODO: (acase) Log to DB
    return Log.w(tag, msg);
  }

  /**
   * Send a WARN exception.
   */
  public static int w(String tag, Throwable tr) {
    // TODO: (acase) Log to DB
    return Log.w(tag, tr);
  }

  /**
   * Send a WARN log message and log the exception.
   */
  public static int w(String tag, String msg, Throwable tr) {
    // TODO: (acase) Log to DB
    return Log.w(tag, msg, tr);
  }
}
