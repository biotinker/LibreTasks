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

import edu.nyu.cs.omnidroid.ui.simple.model.ModelItem;

/**
 * This is a singleton class shared between <code>ActivityChooseFilters</code> and
 * <code>DlgFilterInput</code> so constructed filters can be passed between them.
 */
public class DlgItemBuilderStore {

  private static DlgItemBuilderStore mInstance;
  private ModelItem mItem;

  private DlgItemBuilderStore() {
    reset();
  }

  public static DlgItemBuilderStore instance() {
    if (mInstance == null) {
      mInstance = new DlgItemBuilderStore();
    }
    return mInstance;
  }

  public void reset() {
	  setBuiltItem(null);
  }

  public ModelItem getBuiltItem() {
    return mItem;
  }

  public void setBuiltItem(ModelItem item) {
	  mItem = item;
  }
}