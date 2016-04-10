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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import libretasks.app.R;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniText;
import libretasks.app.view.simple.factoryui.ActionParameterViewFactory;
import libretasks.app.view.simple.model.ModelAction;
import libretasks.app.view.simple.model.ModelAttribute;
import libretasks.app.view.simple.model.ModelRuleAction;
import libretasks.app.view.simple.viewitem.ViewItem;
import libretasks.app.view.simple.viewitem.ViewItemGroup;

/**
 * This dialog is a shell to contain UI elements specific to different actions. Given an action ID,
 * we can construct the inner UI elements using {@link ActionParameterViewFactory}.
 */
public class ActivityDlgActionInput extends Activity {
  private static final String TAG = ActivityDlgActionInput.class.getSimpleName();
  /** Layout dynamically generated on our action type by FactoryActions. */
  private LinearLayout llContent;

  // Container for the dynamically created view
  private ViewItemGroup viewItems;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI(savedInstanceState);
  }

  @Override
  protected void onSaveInstanceState(Bundle bundle) {
    viewItems.saveState(bundle);
  }

  private void initializeUI(Bundle bundle) {
    setContentView(R.layout.activity_dlg_action_input);

    Button btnOk = (Button) findViewById(R.id.activity_dlg_action_input_btnOk);
    btnOk.setOnClickListener(listenerBtnClickOk);

    Button btnAttributes = (Button) findViewById(R.id.activity_dlg_action_input_btnAttributes);
    btnAttributes.setOnClickListener(listenerBtnClickAttributes);

    Button btnHelp = (Button) findViewById(R.id.activity_dlg_action_input_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickHelp);

    llContent = (LinearLayout) findViewById(R.id.activity_dlg_action_input_llDynamicContent);

    // Add dynamic content now based on our action type.
    ModelAction modelAction = RuleBuilder.instance().getChosenModelAction();
    ArrayList<DataType> ruleActionDataOld = RuleBuilder.instance().getChosenRuleActionDataOld();

    viewItems = ActionParameterViewFactory.buildUIFromAction(modelAction, ruleActionDataOld, this);
    llContent.addView(viewItems.getLayout());

    ArrayList<View> textEdits = llContent.getFocusables(View.FOCUS_FORWARD);
    for (View t : textEdits) {
      t.setOnFocusChangeListener(editTextFocusChangeListener);
    }

    try {
      viewItems.loadState(bundle);
    } catch (Exception e) {
      Log.e(TAG, "Failed during loadState", e);
    }

    setTitle(modelAction.getTypeName());
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      // Have the listener try to construct a full ModelRuleAction for us now
      // based on our dynamic UI content.
      ModelRuleAction action;
      try {
        action = ActionParameterViewFactory.buildActionFromUI(RuleBuilder.instance()
            .getChosenModelAction(), viewItems);
      } catch (Exception ex) {
        // TODO: (markww) Make sure DataType classes are providing meaningful error output, then
        // remove the static string below and only use the contents of the exception.
        Resources resource = v.getContext().getResources();
        UtilUI.showAlert(v.getContext(), resource.getString(R.string.sorry), resource
            .getString(R.string.bad_data_format)
            + ex.getMessage());
        return;
      }

      // Set our constructed action so the parent activity can pick it up.
      RuleBuilder.instance().setChosenRuleAction(action);
      finish();
    }
  };

  private View.OnClickListener listenerBtnClickAttributes = new View.OnClickListener() {
    public void onClick(View v) {
      /*
       * For the selected control, try to pop up a list of attribute parameters that can be used in
       * the action based on the data type. For example, if the user has an OmniText UI control
       * selected, and they hit this button, pop up a list of all attributes from the root event
       * that are also OmniText.
       */
      ViewItem focusedItem = getFocusedItem();
      if (focusedItem != null) {
        showDialogAttributes(focusedItem);
      } else {
        Resources resource = v.getContext().getResources();
        UtilUI.showAlert(v.getContext(), resource.getString(R.string.sorry), resource
            .getString(R.string.select_control_for_param_alert_inst));
      }
    }
  };

  private View.OnClickListener listenerBtnClickHelp = new View.OnClickListener() {
    public void onClick(View v) {
      Builder help = new AlertDialog.Builder(v.getContext());
      help.setIcon(android.R.drawable.ic_menu_help);
      help.setTitle(R.string.help);
      help.setMessage(Html.fromHtml(getString(R.string.help_dlgactioninput)));
      help.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        }
      });
      help.show();
    }
  };

  /**
   * Get the current item on focus. This method can only get the focused item if it was created
   * dynamically by the factory.
   * 
   * @return the {@link ViewItem} instance that is currently being focused. null otherwise
   */
  private ViewItem getFocusedItem() {
    View viewFocused = viewItems.getLayout().getFocusedChild();

    if (viewFocused == null) {
      return null;
    } else {
      return viewItems.get(viewFocused.getId());
    }
  }

  /**
   * For a selected event, retrieve any attributes that could be useful for the action
   * 
   * @param viewItem
   *          -the selected view item
   * @return List of attributes applicable to the view item
   */
  private List<ModelAttribute> getAttributes(final ViewItem viewItem) {
    long datatypeId = viewItem.getDataTypeDbID();

    // Get all attributes that have the same data type ID.
    List<ModelAttribute> attributes = UIDbHelperStore.instance().db().getAttributesForEvent(
        RuleBuilder.instance().getChosenEvent());
    List<ModelAttribute> attributesValid;
    if (datatypeId != UIDbHelperStore.instance().getDatatypeLookup()
        .getDataTypeID(OmniText.DB_NAME)) {
      attributesValid = new ArrayList<ModelAttribute>();
      for (int i = 0; i < attributes.size(); i++) {
        ModelAttribute attribute = attributes.get(i);
        if (attribute.getDatatype() == datatypeId) {
          attributesValid.add(attribute);
        }
      }
    } else {
      attributesValid = attributes;
    }
    return attributesValid;
  }

  /**
   * Check whether the selected {@code viewItem} has any attributes
   * 
   * @param viewItem
   *          the view item in question
   * @return true if there are attributes related to the view item
   */
  private boolean checkForAttributes(final ViewItem viewItem) {
    return !getAttributes(viewItem).isEmpty();
  }

  /**
   * If an input field gains focus, enable or disable the attributes button if there are any
   * attributes from the original event.
   */
  private View.OnFocusChangeListener editTextFocusChangeListener = new View.OnFocusChangeListener() {

    public void onFocusChange(View v, boolean hasFocus) {
      Button btnAttributes = (Button) findViewById(R.id.activity_dlg_action_input_btnAttributes);
      if (hasFocus) {
        btnAttributes.setEnabled(checkForAttributes(getFocusedItem()));
      }
    }
  };

  /**
   * Show the attributes dialog for the specified {@code viewItem} if applicable
   * 
   * @param viewItem
   *          the item chosen to show the attributes
   */
  private void showDialogAttributes(final ViewItem viewItem) {

    ArrayList<ModelAttribute> attributesValid = (ArrayList<ModelAttribute>) getAttributes(viewItem);
    // Show the dialog finally if they have any choice.
    if (!attributesValid.isEmpty()) {
      DlgAttributes dlg = new DlgAttributes(this, attributesValid);
      dlg.setOnDismissListener(new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
          // Fetch the attribute they chose, if any.
          ModelAttribute modelAttribute = ((DlgAttributes) dialog).getSelectedAttribute();

          if (modelAttribute != null) {
            viewItem.insertAttribute(modelAttribute);
          }
        }
      });
      dlg.show();
    } else {
      // Shouldn't happen but leaving in for safety
      UtilUI.showAlert(this, "Sorry!",
          "There are no matching parameters for the selected attribute type!");
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    viewItems.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * Shows attributes for the root event that can work as parameters for the action.
   */
  private static class DlgAttributes extends Dialog {
    private ListView listView;
    private int selectedIndex;
    private AdapterAttributes adapterAttributes;

    public DlgAttributes(Context context, ArrayList<ModelAttribute> attributes) {
      super(context);
      
      selectedIndex = -1;
      setContentView(R.layout.dlg_attributes_for_action);
      setTitle("Attributes");

      adapterAttributes = new AdapterAttributes(getContext(), attributes);

      listView = (ListView) findViewById(R.id.dlg_attributes_for_action_listview);
      listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      listView.setAdapter(adapterAttributes);
      
      listView.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
          selectedIndex = position;
          dismiss();
        }
      });

      UtilUI.inflateDialog((LinearLayout) findViewById(R.id.dlg_attributes_for_action_ll_main));
    }

    public ModelAttribute getSelectedAttribute() {
      return adapterAttributes.getItem(selectedIndex);
    }

    public class AdapterAttributes extends BaseAdapter {
      private Context context;
      private final List<ModelAttribute> attributes;

      public AdapterAttributes(Context context, List<ModelAttribute> attributes) {
        this.context = context;
        this.attributes = attributes;
      }

      public int getCount() {
        return attributes.size();
      }

      public ModelAttribute getItem(int position) {
        ModelAttribute item;
        
        try {
          item = attributes.get(position);
        } catch (IndexOutOfBoundsException ex) {
          item = null;
        }
        
        return item; 
      }

      public long getItemId(int position) {
        return position;
      }

      public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT));
        ll.setMinimumHeight(50);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);

        ImageView iv = new ImageView(context);
        iv.setImageResource(attributes.get(position).getIconResId());
        iv.setAdjustViewBounds(true);
        iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT));
        if (listView.getCheckedItemPosition() == position) {
          iv.setBackgroundResource(R.drawable.icon_hilight);
        }

        TextView tv = new TextView(context);
        tv.setText(attributes.get(position).getDescriptionShort());
        tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT));
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(10, 0, 0, 0);
        tv.setTextSize(14.0f);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setTextColor(0xFFFFFFFF);
        tv.setMinHeight(46);

        ll.addView(iv);
        ll.addView(tv);

        return ll;
      }
    }
  }
}
