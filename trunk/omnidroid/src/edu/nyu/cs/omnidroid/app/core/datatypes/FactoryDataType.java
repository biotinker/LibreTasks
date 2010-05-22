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
package edu.nyu.cs.omnidroid.app.core.datatypes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * The class acts as a factory for instantiation of the Omnidroid data types.
 * 
 */
public class FactoryDataType {

  /**
   * Private constructor to make sure that the class cannot be instantiated.
   */
  private FactoryDataType() {

  }

  /**
   * Factory method that creates the object of className type and initialized with provided value
   * 
   * @param className
   *          - string representing Omnidroid class name. Must implement DataType.
   * @param value
   *          - the string value to initialize object with. Note: if value is not valid, no object
   *          will be created and null will be returned.
   * @return the DataType object if object was successfully created, or null if failed.
   */
  public static DataType createObject(String className, String value) {
    try {
      Class<?> theClass = Class.forName(className);

      Class<?>[] constructorParameters = new Class[1];
      constructorParameters[0] = Class.forName("java.lang.String");

      Constructor<?> classConstructor = theClass.getConstructor(constructorParameters);

      return (DataType) classConstructor.newInstance(value);
    } catch (Exception ex) {
      Log.e("createObject", "Can't create class " + className + " with value: " + value);
    }

    return null;
  }

  /**
   * Factory method that creates the object of Filter type given the omni data type that & name.
   * 
   * @param className
   *          Omni data type that the filter belongs to.
   * @param filter
   * @return filter object represented by the classname & filter name if found.  Null otherwise.
   */
  public static DataType.Filter getFilterFromString(String className, String filter) {
    Class<?> theClass;
    try {
      theClass = Class.forName(className);
      Method method = theClass.getMethod("getFilterFromString", String.class);
      return (DataType.Filter) method.invoke(null, filter);
    } catch (ClassNotFoundException e) {
      Log.e("getFilterFromString", "ClassNotFoundException");
      return null;
    } catch (SecurityException e) {
      Log.e("getFilterFromString", "SecurityException");
      return null;
    } catch (NoSuchMethodException e) {
      Log.e("getFilterFromString", "NoSuchMethodException");
      return null;
    } catch (IllegalArgumentException e) {
      Log.e("getFilterFromString", "IllegalArgumentException");
      return null;
    } catch (IllegalAccessException e) {
      Log.e("getFilterFromString", "IllegalAccessException");
      return null;
    } catch (InvocationTargetException e) {
      Log.e("getFilterFromString", "InvocationTargetException");
      return null;
    }
  }
}