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
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid
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
package libretasks.app.view.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import libretasks.app.R;
import libretasks.app.view.simple.factoryui.ActionParameterViewFactory;
import libretasks.app.view.simple.model.ModelApplication;
import libretasks.app.view.simple.viewitem.ViewItemGroup;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * This dialog is a shell to contain UI elements specific to creating a login UI. Given an
 * application, we can construct the inner UI elements.
 */
public class ActivityDlgApplicationLoginInput extends Activity {

  /** Layout dynamically generated on our action type by FactoryActions. */
  private ListView lv;

  /** Main layout to which we append the dynamically generated layout. */
  private ViewItemGroup viewItems;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    show_custom_chooser();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  private void initializeUI() {

    //~ Button btnHelp = (Button) findViewById(R.id.activity_dlg_action_login_input_btnHelp);
    //~ btnHelp.setOnClickListener(listenerBtnClickHelp);
//~ 
    //~ llContent = (LinearLayout) findViewById(R.id.activity_dlg_action_input_llDynamicContent);
    show_custom_chooser();

    // Add dynamic content from the application
    //~ ModelApplication modelApp = UIDbHelperStore.instance().db().getApplication(
        //~ RuleBuilder.instance().getChosenApplication().getDatabaseId());
//~ 
    //~ viewItems = ActionParameterViewFactory.buildLoginUI(modelApp, this);
//~ 
    //~ llContent.addView(viewItems.getLayout());
  }

  public void show_custom_chooser() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(ActivityDlgApplicationLoginInput.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.about_dialog);
		dialog.setCancelable(true);
		ListView lv=(ListView)dialog.findViewById(R.id.listView1);
		PackageManager pm=getPackageManager();
		final Intent email = new Intent(Intent.ACTION_SEND);
		email.setType("text/plain");
		List<ResolveInfo> launchables=pm.queryIntentActivities(email, 0);
	    
	    Collections.sort(launchables,
	                     new ResolveInfo.DisplayNameComparator(pm)); 
	    dialog.show();
	    final AppAdapter adapter=new AppAdapter(pm, launchables);
	    lv.setAdapter(adapter);	
	    lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ResolveInfo launchable=adapter.getItem(position);
			    ActivityInfo activity=launchable.activityInfo;
			    ComponentName name=new ComponentName(activity.applicationInfo.packageName,
			                                         activity.name);
				email.addCategory(Intent.CATEGORY_LAUNCHER);
				email.setComponent(name);
			    //~ startActivity(email);
			    dialog.dismiss();
			    setResult(RESULT_OK, getIntent());
				finish();
			}
		});	
	}
	class AppAdapter extends ArrayAdapter<ResolveInfo> {
    private PackageManager pm=null;
    
    AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
      super(ActivityDlgApplicationLoginInput.this, R.layout.row, apps);
      this.pm=pm;
    }
    
    @Override
    public View getView(int position, View convertView,
                          ViewGroup parent) {
      if (convertView==null) {
        convertView=newView(parent);
      }
      
      bindView(position, convertView);
      
      return(convertView);
    }
    
    private View newView(ViewGroup parent) {
      return(ActivityDlgApplicationLoginInput.this.getLayoutInflater().inflate(R.layout.row, parent, false));
    }
    
    private void bindView(int position, View row) {
      TextView label=(TextView)row.findViewById(R.id.label);
      
      label.setText(getItem(position).loadLabel(pm));
      
      ImageView icon=(ImageView)row.findViewById(R.id.icon);
      
      icon.setImageDrawable(getItem(position).loadIcon(pm));
    }
  }
}
