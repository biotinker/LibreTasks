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
package edu.nyu.cs.omnidroid.ui.simple;

import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.Rule;



/**
 * This is a singleton class used to store elements of a new rule
 * between <code>ActivityChooseRootEvent</code> and <code>ActivityChooseFilters</code>.
 * Using singletons is how the Android team recommends passing this type
 * of data between activities.
 */
public class RuleBuilder {
	
	private static RuleBuilder mInstance;
	private Rule mRule;
	
	
	private RuleBuilder() {
	}
	
	public static RuleBuilder instance() {
		if (mInstance == null) {
			mInstance = new RuleBuilder();
		}
		return mInstance;
	}
	
	public void reset(ModelEvent event) {
		mRule = null;
		mRule = new Rule();
		mRule.setRootEvent(event);
	}
	
	public Rule getRule() {
		return mRule;
	}
}