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

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.core.datatypes.DataType;
import edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.core.datatypes.OmniText;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;

/**
 * Static factory class for setting up a dynamic UI for every filter type that should be supported.
 */
public class FactoryFilterToUI {

  private FactoryFilterToUI() {
  }

  /**
   * The <code>DlgFilterInput</code> instance will have a member instance of this interface. When
   * the user dismisses the input dialog, the dialog can call this handler to have the dynamic UI
   * elements try to create a filter from the user's input. The logic for this is setup inside of
   * <code>buildUIForFilter</code>. If successful, it will return a complete
   * <code>ModelFilter</code> instance, otherwise will throw an exception with a meaningful error
   * message.
   */
  public interface IFilterInputDone {
    public ModelFilter onFilterInputDone() throws Exception;
  }

  /**
   * The <code>DlgFilterInput</code> instance will have to support UI state preservation between
   * orientation changes, but the class won't know what its UI elements are. The
   * <code>buildUIForFilter</code> will instead set an instance of this handler in the dialog, and
   * the dialog can call these two methods when needed to save and load UI state.
   */
  public interface IDlgPreserveState {
    public void saveState(SharedPreferences state);
    public void loadState(SharedPreferences state);
  }

  /**
   * This interface must be implemented by <code>DlgFilterInput</code> so we can support adding
   * dynamic UI elements to it based on the filter type chosen by the end user.
   */
  public interface IDlgFilterInput {
    public Context getContext();
    public void setTitle(CharSequence title);
    public void addDynamicLayout(LinearLayout ll);
    public void setHandlerOnFilterInputDone(IFilterInputDone handler);
    public void setHandlerPreserveState(IDlgPreserveState handler);
  }

  /**
   * For now we hard-code all the filters into this factory class. Eventually this should be moved
   * into the database or something besides a huge switch statement.
   * 
   * The passed filter may already be a fully constructed instance, for example, if the user is
   * editing a constructed filter instance in the UI. In that case, its data can be queried and used
   * to pre-populate controls for end-user convenience.
   * 
   * @param dlg
   *          Dialog interface
   * @param attribute
   *          The model attribute parent of the filter.
   * @param filter
   *          The model filter we want to try to fill.
   * @param dataOld
   *          If we're modifying an existing filter, then this is the data the user had last set for
   *          it.
   */
  public static void buildUIForFilter(IDlgFilterInput dlg, 
                                      final ModelAttribute attribute,
                                      final ModelFilter filter, 
                                      DataType dataOld) 
  {
    dlg.setTitle(attribute.getDescriptionShort() + " " + filter.getTypeName());

    LinearLayout ll = new LinearLayout(dlg.getContext());
    ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    ll.setOrientation(LinearLayout.VERTICAL);

    if (filter.getDatabaseId() == DbInterfaceUI.FILTER_ID_PHONENUMBER_EQUALS) {

      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter a phone number to match below:");
      ll.addView(tvInstructions);

      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniPhoneNumber) dataOld).getValue());
      }
      ll.addView(edit);

      // Add the handler for when the user is done with their input.
      dlg.setHandlerOnFilterInputDone(new IFilterInputDone() {
        public ModelFilter onFilterInputDone() throws Exception {
          // Try to construct an OmniPhoneNumber from what the user
          // entered in the edit field. If no good, let its exception
          // be thrown up.
          OmniPhoneNumber data = new OmniPhoneNumber(edit.getText().toString());

          // The entered text was valid, return a completely constructed
          // filter, with its associated data.
          return new ModelFilter(filter.getDatabaseId(), filter.getTypeName(), filter
              .getDescription(), filter.getIconResId(), attribute, data);
        }
      });
      // Also implement the UI state preservation handlers.
      dlg.setHandlerPreserveState(new IDlgPreserveState() {
        public void saveState(SharedPreferences state) {
          SharedPreferences.Editor prefsEditor = state.edit();
          String phoneNumber = edit.getText().toString();
          prefsEditor.putString("phoneNumber", phoneNumber);
        }

        public void loadState(SharedPreferences state) {
          if (state.contains("phoneNumber")) {
            edit.setText(state.getString("phoneNumber", ""));
          }
        }
      });
    } else if (filter.getDatabaseId() == DbInterfaceUI.FILTER_ID_TEXT_EQUALS) {

      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter an exact text string to match below:");
      ll.addView(tvInstructions);

      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniText) dataOld).getValue());
      }
      ll.addView(edit);

      // Add the handler for when the user is done with their input.
      dlg.setHandlerOnFilterInputDone(new IFilterInputDone() {
        public ModelFilter onFilterInputDone() throws Exception {
          // Try to construct an OmniText from what the user
          // entered in the edit field. If no good, let its
          // exception be thrown up.
          OmniText data = new OmniText(edit.getText().toString());

          // The entered text was valid, return a completely constructed
          // filter, with its associated data.
          return new ModelFilter(filter.getDatabaseId(), filter.getTypeName(), filter
              .getDescription(), filter.getIconResId(), attribute, data);
        }
      });
      // Also implement the UI state preservation handlers.
      dlg.setHandlerPreserveState(new IDlgPreserveState() {
        public void saveState(SharedPreferences state) {
          SharedPreferences.Editor prefsEditor = state.edit();
          prefsEditor.putString("smstext", edit.getText().toString());
        }

        public void loadState(SharedPreferences state) {
          if (state.contains("smstext")) {
            edit.setText(state.getString("smstext", ""));
          }
        }
      });
    } else if (filter.getDatabaseId() == DbInterfaceUI.FILTER_ID_TEXT_CONTAINS) {

      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter a phrase in the text body to match below:");
      ll.addView(tvInstructions);

      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniText) dataOld).getValue());
      }
      ll.addView(edit);

      dlg.setHandlerOnFilterInputDone(new IFilterInputDone() {
        public ModelFilter onFilterInputDone() throws Exception {
          OmniText data = new OmniText(edit.getText().toString());
          return new ModelFilter(filter.getDatabaseId(), filter.getTypeName(), filter
              .getDescription(), filter.getIconResId(), attribute, data);
        }
      });
      dlg.setHandlerPreserveState(new IDlgPreserveState() {
        public void saveState(SharedPreferences state) {
          SharedPreferences.Editor prefsEditor = state.edit();
          prefsEditor.putString("smsphrase", edit.getText().toString());
        }

        public void loadState(SharedPreferences state) {
          if (state.contains("smsphrase")) {
            edit.setText(state.getString("smsphrase", ""));
          }
        }
      });
    }
    else {
    	System.out.println("Filter ID: " + filter.getDatabaseId());
    	throw new IllegalArgumentException("Unknown filter ID[" + filter.getDatabaseId() + "] passed to FactoryFilterToUI::buildUIForFilter()!");
    }
    dlg.addDynamicLayout(ll);
  }
}