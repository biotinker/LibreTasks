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
package edu.nyu.cs.omnidroid.app.view.simple;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.view.simple.factoryui.FactoryActions;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleAction;
import edu.nyu.cs.omnidroid.app.view.simple.viewitem.ViewItem;
import edu.nyu.cs.omnidroid.app.view.simple.viewitem.ViewItemGroup;

/**
 * This dialog is a shell to contain UI elements specific to different actions. Given an action ID,
 * we can construct the inner UI elements using {@link FactoryActions}.
 */
public class ActivityDlgActionInput extends Activity {
  // Request code for opening the create account screen
  public static final int SETUP_ACCOUNT_REQUEST = 0;

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

    // TODO(acase): Only display if applicable
    Button btnAttributes = (Button) findViewById(R.id.activity_dlg_action_input_btnAttributes);
    btnAttributes.setOnClickListener(listenerBtnClickAttributes);

    Button btnHelp = (Button) findViewById(R.id.activity_dlg_action_input_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickInfo);

    llContent = (LinearLayout) findViewById(R.id.activity_dlg_action_input_llDynamicContent);

    // Add dynamic content now based on our action type.
    ModelAction modelAction = RuleBuilder.instance().getChosenModelAction();
    ArrayList<DataType> ruleActionDataOld = RuleBuilder.instance().getChosenRuleActionDataOld();

    viewItems = FactoryActions.buildUIFromAction(modelAction, ruleActionDataOld, this);
    llContent.addView(viewItems.getLayout());

    viewItems.loadState(bundle);

    setTitle(modelAction.getTypeName());
  }

  private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
    public void onClick(View v) {
      // Have the listener try to construct a full ModelRuleAction for us now
      // based on our dynamic UI content.
      ModelRuleAction action;
      try {
        action = FactoryActions.buildActionFromUI(RuleBuilder.instance().getChosenModelAction(),
            viewItems);
      } catch (Exception ex) {
        // TODO: (markww) Make sure DataType classes are providing meaningful error output, then
        // remove the static string below and only use the contents of the exception.
        UtilUI.showAlert(v.getContext(), "Sorry!",
            "There was an error creating your action, your input was probably bad!:\n"
                + ex.toString());
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

  private View.OnClickListener listenerBtnClickInfo = new View.OnClickListener() {
    public void onClick(View v) {
      // TODO: (markww) Add help info about action.
      UtilUI.showAlert(v.getContext(), getString(R.string.sorry), getString(R.string.coming_soon));
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
    return viewItems.get(viewFocused.getId());
  }

  /**
   * Show the attributes dialog for the specified {@code viewItem} if applicable
   * 
   * @param viewItem
   *          the item chosen to show the attributes
   */
  private void showDialogAttributes(final ViewItem viewItem) {
    long datatypeId = viewItem.getDataTypeDbID();

    // Get all attributes that have the same data type ID.
    ArrayList<ModelAttribute> attributes = UIDbHelperStore.instance().db().getAttributesForEvent(
        RuleBuilder.instance().getChosenEvent());
    ArrayList<ModelAttribute> attributesValid;
    if (datatypeId != UIDbHelperStore.instance().getDatatypeLookup().getDataTypeID("Text")) {
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

    // Show the dialog finally if they have any choice.
    if (attributesValid.size() > 0) {
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
      UtilUI.showAlert(this, "Sorry!",
          "There are no matching parameters for the selected attribute type!");
    }
  }

  /**
   * Shows attributes for the root event that can work as parameters for the action.
   */
  private static class DlgAttributes extends Dialog {

    private ListView listView;
    private AdapterAttributes adapterAttributes;

    public DlgAttributes(Context context, ArrayList<ModelAttribute> attributes) {
      super(context);
      setContentView(R.layout.dlg_attributes_for_action);
      setTitle("Attributes");

      adapterAttributes = new AdapterAttributes(getContext(), attributes);

      listView = (ListView) findViewById(R.id.dlg_attributes_for_action_listview);
      listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      listView.setAdapter(adapterAttributes);

      Button btnOk = (Button) findViewById(R.id.dlg_attributes_for_action_btnOk);
      btnOk.setOnClickListener(listenerBtnClickOk);

      UtilUI.inflateDialog((LinearLayout) findViewById(R.id.dlg_attributes_for_action_ll_main));
    }

    private View.OnClickListener listenerBtnClickOk = new View.OnClickListener() {
      public void onClick(View v) {
        // The user has chosen an attribute, now get a list of filters associated
        // with that attribute, from the database.
        int position = listView.getCheckedItemPosition();
        if (position < 0) {
          Resources resource = v.getContext().getResources();
          UtilUI.showAlert(v.getContext(), resource.getString(R.string.sorry), resource
              .getString(R.string.select_attribute_alert_inst));
          return;

        }

        // The parent activity will pick up our selected attribute from the list.
        dismiss();
      }
    };

    public ModelAttribute getSelectedAttribute() {
      return adapterAttributes.getItem(listView.getCheckedItemPosition());
    }

    public class AdapterAttributes extends BaseAdapter {
      private Context context;
      private ArrayList<ModelAttribute> attributes;

      public AdapterAttributes(Context context, ArrayList<ModelAttribute> attributes) {
        this.context = context;
        this.attributes = attributes;
      }

      public int getCount() {
        return attributes.size();
      }

      public ModelAttribute getItem(int position) {
        if (position > -1 && position < attributes.size()) {
          return attributes.get(position);
        }
        return null;
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