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

import java.util.HashMap;
import java.util.Map;

/**
 * Maps exception error codes to exception messages. <br>
 * <br>
 * 
 * The code structure is as follows:
 * 
 * <ul>
 * <li>The error code is a six digit number</li>
 * <li>The first two digits represent the error type</li>
 * <li>The last four digits represent the specific error in that error type</li>
 * </ul>
 * 
 * Note: the last four digits may be divided into subtypes. In such case, the explanation of the
 * format will be included in the error type class descriptions below.<br>
 * <br>
 * 
 * The error types are:
 * 
 * <ul>
 * <li>00 -- Standard Java exceptions caused by malformed or invalid method parameters</li>
 * <li>02 -- All other standard Java exceptions</li>
 * <li>10 -- General {@code OmniException}s from the UI</li>
 * <li>11 -- General {@code OmniException}s from the Event Catcher</li>
 * <li>12 -- General {@code OmniException}s from the Action Thrower</li>
 * <li>13 -- General {@code OmniException}s from the Application API</li>
 * <li>20 -- IO related {@code OmniException}s</li>
 * </ul>
 * 
 * How to use the codes and messages:
 * 
 * <ul>
 * <li>All exceptions thrown from within Omnidroid should contain a code and a message, even
 * exceptions thrown using standard Java exceptions.</li>
 * <li>When creating a new exception code and message, start the message with the code like so:
 * "000001: "</li>
 * <li>When throwing an <code>OmniException</code>, the code and the message must be supplied to the
 * constructor.</li>
 * <li>When throwing a standard Java exception (ex. <code>NullPointerException</code>), just supply
 * the message since the code is already included in the message.</li>
 * <li>The message retrieved from the <code>ExceptionMessageMap</code> can be treated like a base
 * message. If additional information is required in the message, append that information to the
 * base message.</li>
 * <li>If the value of a variable must be included in the message, list the variable names and their
 * values at the end of the message in the following format: "{ varName1=[varVal1],
 * varName2=[varVal2], ... }"</li>
 * </ul>
 * 
 * TODO (acase): Load the exception message mapping from a properties bundle.
 */
public class ExceptionMessageMap {

  private static Map<String, String> MESSAGE_MAP;

  static {
    MESSAGE_MAP = new HashMap<String, String>();

    MESSAGE_MAP.put("000001", "000001: A String argument was not specified but is required.");
    MESSAGE_MAP.put("000002", "000002: A List<String> argument was not specified but is required.");
    MESSAGE_MAP.put("000003", "000003: An Object argument was not specified but is required.");
    MESSAGE_MAP.put("000004", "000004: A MessageType argument was not specified but is required.");
    MESSAGE_MAP.put("000005",
        "000005: The int argument is outside the allowable range (start <= index < end).");

    MESSAGE_MAP.put("020000", "020000: Error serializing message.");
    MESSAGE_MAP.put("020001", "020001: Error deserializing message.");
    MESSAGE_MAP.put("020002", "020002: Configuration file does not exist.");

    MESSAGE_MAP.put("100000", "100000: Error starting UI.");

    MESSAGE_MAP.put("110000", "110000: Error starting EventCatcher.");

    MESSAGE_MAP.put("120000", "120000: Error starting ActionThrower.");
    MESSAGE_MAP.put("120001", "120001: Illegal execution method for action.");
    MESSAGE_MAP.put("120002", "120002: Action parameters not found.");
    MESSAGE_MAP.put("120003", "120003: Action not supported.");
    MESSAGE_MAP.put("120004", "120004: Action parameters mal format.");
    
    MESSAGE_MAP.put("130000", "130000: Error starting Application API.");

    MESSAGE_MAP.put("140000", "140000: Application/Event name cannot be null.");
    MESSAGE_MAP.put("140001", "140001: Error retrieving event from database.");
    
    MESSAGE_MAP.put("200000", "200000: When reading bytes from an InputStream, the EndOfFile "
        + "was reached before expected.");
    MESSAGE_MAP.put("200001", "200001: When reading chars from a Reader, the EndOfFile was "
        + "reached before expected.");
    MESSAGE_MAP.put("200002", "200002: Error reading the bytes of a message from an InputStream.");
    MESSAGE_MAP.put("200003", "200003: Error reading the chars of a message from an InputStream.");

    MESSAGE_MAP.put("200500", "200500: Error writing message bytes to an output stream.");

  }

  /**
   * Gets the error message for the specified code.
   * 
   * @param code
   *          The error code for which to get the associated error message.
   * @return The error message for the specified code.
   */
  public static String getMessage(String code) {
    String str = MESSAGE_MAP.get(code);
    if (null == str) {
      return "NO MESSAGE FOR ERROR CODE. { code=[" + code + "] }";
    }
    return str;
  }

}
