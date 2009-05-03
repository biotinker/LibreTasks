/***
	Copyright (c) 2008-2009 CommonsWare, LLC
  Copyright (c) 2009 Andrew I. Case
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package edu.nyu.cs.omnidroid.tests;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CheckListView extends ListView {
  public CheckListView(Context context) {
    super(context);
  }

  public CheckListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CheckListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setAdapter(ListAdapter adapter) {
    super.setAdapter(new CheckableWrapper(getContext(), adapter));
  }

  public List<Integer> getCheckedPositions() {
    return (((CheckableWrapper) getAdapter()).getCheckedPositions());
  }

  public List<Integer> getCheckedObjects() {
    return (((CheckableWrapper) getAdapter()).getCheckedObjects());
  }
}