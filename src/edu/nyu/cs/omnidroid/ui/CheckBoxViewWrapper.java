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

package edu.nyu.cs.omnidroid.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

class CheckBoxViewWrapper {
  ViewGroup base;
  View guts = null;
  CheckBox cb = null;

  CheckBoxViewWrapper(ViewGroup base) {
    this.base = base;
  }

  CheckBox getCheckBox() {
    if (cb == null) {
      cb = (CheckBox) base.getChildAt(0);
    }

    return (cb);
  }

  void setCheckBox(CheckBox cb) {
    this.cb = cb;
  }

  View getGuts() {
    if (guts == null) {
      guts = base.getChildAt(1);
    }

    return (guts);
  }

  void setGuts(View guts) {
    this.guts = guts;
  }
}
