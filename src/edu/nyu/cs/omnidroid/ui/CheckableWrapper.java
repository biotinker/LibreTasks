/***
  Copyright (c) 2008-2009 CommonsWare, LLC
  Copyright (c) 2009 Andrew Case

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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.CheckBox;

public class CheckableWrapper extends AdapterWrapper {
  Context ctxt=null;
  boolean[] boxes=null;
  
  public CheckableWrapper(Context ctxt, ListAdapter delegate) {
    super(delegate);
    
    this.ctxt=ctxt;
    this.boxes=new boolean[delegate.getCount()];
    
    for (int i=0;i<delegate.getCount();i++) {
      this.boxes[i]=true;
    }
  }
  
  public View getView(int position, View convertView,
                      ViewGroup parent) {
    CheckBoxViewWrapper wrap=null;
    View row=convertView;
                            
    if (convertView==null) {
      LinearLayout layout=new LinearLayout(ctxt);
      CheckBox cb=new CheckBox(ctxt);
      
      //cb.setNumStars(3);
      //cb.setStepSize(1.0f);
      
      View guts=delegate.getView(position, null, parent);
    
      layout.setOrientation(LinearLayout.HORIZONTAL); 
          
      cb.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.FILL_PARENT));
      guts.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.FILL_PARENT));
      
      CheckBox.OnCheckedChangeListener l=
                    new CheckBox.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton btn, boolean rating) {
          boxes[(Integer)btn.getTag()]=rating;
        }
      };
      
      cb.setOnCheckedChangeListener(l);
          
      layout.addView(cb);           
      layout.addView(guts);
      
      wrap=new CheckBoxViewWrapper(layout);
      wrap.setGuts(guts);
      layout.setTag(wrap);
      
      cb.setTag(new Integer(position));
      cb.setChecked(boxes[position]);
        
      row=layout;       
    }
    else {
      wrap=(CheckBoxViewWrapper)convertView.getTag();
      wrap.setGuts(delegate.getView(position, wrap.getGuts(),
                                    parent));
      wrap.getCheckBox().setTag(new Integer(position));
      wrap.getCheckBox().setChecked(boxes[position]);
    }
    
    return(row);
  }   
}