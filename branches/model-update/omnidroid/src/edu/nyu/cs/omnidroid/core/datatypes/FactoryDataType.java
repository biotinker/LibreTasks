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
package edu.nyu.cs.omnidroid.core.datatypes;

import java.lang.reflect.Constructor;

/**
 * The class acts as a factory for instantiation of the Omnidroid data types.
 * 
 */
public class FactoryDataType {
	
	/* TODO: The package name is temporary measure, as requested by other developers, once all the packages are settled and defined, it should be removed.
	 *       The complete package name and class should be stored in the database.
	*/       
	private static final String packageName = "edu.nyu.cs.omnidroid.core.datatypes";

	/**
	 * Factory method that creates the object of className type and initialized with provided value
	 * @param className - string representing Omnidroid class name. Must implement DataType.
	 * @param value - the string value to initialize object with. Note: if value is not valid, no object will be created and null will be returned.
	 * @return the DataType object if object was successfully created, or null if failed.
	 */
	public static DataType createObject(String className, String value) {
        try {
			Class<?> theClass  = Class.forName(packageName + "." + className);
			
			Class<?>[] constructorParameters = new Class[1];
			constructorParameters[0] = Class.forName("java.lang.String");
			
			Constructor<?> classConstructor = theClass.getConstructor(constructorParameters);
			 
			return (DataType) classConstructor.newInstance(value);
        }
        catch (Exception ex ){
        }
    
		return null;
	}
}