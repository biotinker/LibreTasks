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
 *
 */
public class FactoryDataType {
	
	private final String packageName = "edu.nyu.cs.omnidroid.core.datatypes";

	/**
	 * 
	 * @param className
	 * @param value
	 * @return the DataType object if object was successfully created, or null if failed.
	 */
	public DataType createObject(String className, String value) {
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
