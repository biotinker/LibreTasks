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

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/**
 * This is a utility class that implements some basic I/O operations.
 */
public class IOUtil {

  /**
   * Close an I/O stream. Do nothing if closeable is null.
   * 
   * @param closeable
   *          is the I/O stream to be closed.
   * 
   * @throws RuntimeException
   *           when IOException is thrown.
   */
  public static void close(Closeable closeable) {
    if (closeable == null) {
      return;
    }
    try {
      closeable.close();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * @param in
   *          is the input stream
   * 
   * @return the ASCII code of the next character from the input stream
   * 
   * @throws RuntimeException
   *           when IOException is thrown.
   * 
   * @throws IllegalArgumentException
   *           if input stream is null.
   */
  public static int read(InputStream in) {
    if (in == null) {
      throw new IllegalArgumentException("input stream null.");
    }
    try {
      return in.read();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Write a ASCII code to the output stream
   * 
   * @param out
   *          is the output stream to write to
   *          
   * @param c
   *          is the ASCII code to write
   * 
   * @throws RuntimeException
   *           when IOException is thrown.
   * 
   * @throws IllegalArgumentException
   *           if output stream is null.
   */
  public static void write(OutputStream out, int c) {
    if (out == null) {
      throw new IllegalArgumentException("output stream null.");
    }
    try {
      out.write(c);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Copy file from source path to the target file path
   * 
   * @param source
   *          is the source file path. Takes the same input as FileInputStream(string)
   *          
   * @param target
   *          is the target file path. Takes the same input as FileInputStream(string)
   * 
   * @throws RuntimeException
   *           when source or target file is failed to be located.
   */
  public static void copy(String source, String target) {
    InputStream in = null;
    OutputStream out = null;

    try {
      in = new BufferedInputStream(new FileInputStream(source));
      out = new BufferedOutputStream(new FileOutputStream(target));
      for (int c = read(in); c != -1; c = read(in)) {
        write(out, c);
      }

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);

    } finally {
      close(in);
      close(out);
    }

    close(in);
    close(out);
  }

  /**
   * Move file from source path to the target file path
   * 
   * @param source
   *          is the source file path. Takes the same input as File(string)
   *          
   * @param target
   *          is the target file path. Takes the same input as File(string)
   */
  public static void move(String source, String target) {
    new File(source).renameTo(new File(target));
  }

  /**
   * Remove a file
   * 
   * @param file
   *          is name of the file to be removed. Takes the same input as File(string)
   *          
   * @return true to file if success, or false if failed
   */
  public static boolean remove(String file) {
    File f = new File(file);
    if (!f.exists()) {
      return true;
    }
    return f.delete();
  }

  /**
   * @param file
   *          is the file name. Takes the same input as File(string)
   *          
   * @return whether the file is existing.
   */
  public static boolean exist(String file) {
    return new File(file).exists();
  }

}
