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

/**
 * Base exception for Omnidroid specific exceptions.
 */
public class OmnidroidException extends Exception {

  /**
   * Generated SerialID
   */
  private static final long serialVersionUID = -4127262962368059843L;

  // The code for an exception instance.
  private int code;

  /**
   * Create an exception with a code and a message.
   * 
   * @param code
   *          The code for this exception.
   * @param message
   *          The message for this exception.
   */
  public OmnidroidException(int code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Create an exception with a code, a message and a causal <code>Throwable</code>.
   * 
   * @param code
   *          The code for this exception.
   * @param message
   *          The message for this exception.
   * @param cause
   *          A <code>Throwable</code> that caused this exception.
   */
  public OmnidroidException(int code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Get the code this exception is thrown with.
   * 
   * @return The code for this exception.
   */
  public int getCode() {
    return code;
  }

}
