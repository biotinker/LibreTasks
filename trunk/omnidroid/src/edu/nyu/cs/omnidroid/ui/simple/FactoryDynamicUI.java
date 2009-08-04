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
import edu.nyu.cs.omnidroid.core.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.core.datatypes.OmniText;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelItem;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleFilter;

/**
 * Static factory class for setting up a dynamic UI for every filter/action type.
 * 
 * TODO: (markww) Refactor internal builder classes to use parent class for common tasks.
 */
public class FactoryDynamicUI {

  private FactoryDynamicUI() {
  }

  /**
   * The <code>DlgFilterInput</code> instance will have a member instance of this interface. When
   * the user dismisses the input dialog, the dialog can call this handler to have the dynamic UI
   * elements try to create a filter from the user's input. The logic for this is setup inside of
   * <code>buildUIForFilter</code>. If successful, it will return a complete
   * <code>ModelFilter</code> instance, otherwise will throw an exception with a meaningful error
   * message.
   */
  public interface InputDone {
    public ModelItem onInputDone(Context context) throws Exception;
  }

  /**
   * The <code>DlgFilterInput</code> instance will have to support UI state preservation between
   * orientation changes, but the class won't know what its UI elements are. The
   * <code>buildUIForFilter</code> will instead set an instance of this handler in the dialog, and
   * the dialog can call these two methods when needed to save and load UI state.
   */
  public interface DlgPreserveState {
    /** Called when the dynamic dialog should save its state. */
    public void saveState(SharedPreferences.Editor prefsEditor);

    /** Called to restore the dynamic dialog to its saved state. */
    public void loadState(SharedPreferences state);
  }

  /**
   * This interface must be implemented by <code>DlgFilterInput</code> so we can support adding
   * dynamic UI elements to it based on the filter type chosen by the end user.
   */
  public interface DlgDynamicInput {
    /** Get the dialog's context member. */
    public Context getContext();

    /** Set the title of the dialog, will depend on the chosen filter/action. */
    public void setTitle(CharSequence title);

    /** Add the constructed dynamic controls to the dialog. */
    public void addDynamicLayout(LinearLayout ll);

    /** Set the handler to be called when user is done supplying information. */
    public void setHandlerOnFilterInputDone(InputDone handler);

    /** Set the handler to be called for state preservation. */
    public void setHandlerPreserveState(DlgPreserveState handler);
  }
  
  /**
   * Interface to make a filter builder. This is used to pull UI creation code out of the main
   * buildUIForFilter function.
   */
  private interface BuildFilterUI {
    public void build(DlgDynamicInput dlg, LinearLayout ll, final ModelFilter modelFilter,
        DataType dataOld);
  }

  /**
   * For now we hard-code all the filters into this factory class. Eventually this should be changed
   * to use some information from the database about filter attribute types to generate the required
   * UI elements, instead of a large hard-coded if statement.
   * 
   * The passed filter may already be a fully constructed instance, for example, if the user is
   * editing a constructed filter instance in the UI. In that case, its data can be queried and used
   * to pre-populate controls for end-user convenience.
   * 
   * @param dlg
   *          Dialog interface
   * @param modelFilter
   *          The model filter we want to try to fill.
   * @param dataOld
   *          If we're modifying an existing filter, then this is the data the user had last set for
   *          it.
   */
  public static void buildUIForFilter(DlgDynamicInput dlg, final ModelFilter modelFilter,
      final DataType dataOld) {
    dlg
        .setTitle(modelFilter.getAttribute().getDescriptionShort() + " "
            + modelFilter.getTypeName());

    LinearLayout ll = new LinearLayout(dlg.getContext());
    ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    ll.setOrientation(LinearLayout.VERTICAL);

    UIDbHelperStore dbi = UIDbHelperStore.instance();
    if (modelFilter.getDatabaseId() == dbi.getFilterLookup().getDataFilterID(
        OmniPhoneNumber.DB_NAME, OmniPhoneNumber.Filter.EQUALS.toString())) {
      BuildPhonenumberEquals.build(dlg, ll, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == dbi.getFilterLookup().getDataFilterID(
        OmniText.DB_NAME, OmniText.Filter.EQUALS.toString())) {
      BuildTextEquals.build(dlg, ll, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == dbi.getFilterLookup().getDataFilterID(
        OmniText.DB_NAME, OmniText.Filter.CONTAINS.toString())) {
      BuildTextContains.build(dlg, ll, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == dbi.getFilterLookup().getDataFilterID(
        OmniArea.DB_NAME, OmniArea.Filter.AWAY.toString())) {
      BuildAreaAwayOrNear.build(dlg, ll, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == dbi.getFilterLookup().getDataFilterID(
        OmniArea.DB_NAME, OmniArea.Filter.NEAR.toString())) {
      BuildAreaAwayOrNear.build(dlg, ll, modelFilter, dataOld);
    } else {
      throw new IllegalArgumentException("Unknown filter ID[" + modelFilter.getDatabaseId()
          + "] passed to FactoryDynamicUI::buildUIForFilter()!");
    }
    dlg.addDynamicLayout(ll);
  }

  private static BuildFilterUI BuildPhonenumberEquals = new BuildFilterUI() {
    private static final String UISTATE_PHONENUMBER = "phonenumber";
    
    public void build(DlgDynamicInput dlg, LinearLayout ll, final ModelFilter modelFilter,
        DataType dataOld) {
      // This is a Phonenumber Equals filter, so generate a specific UI for it.
      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter a phone number to match below:");
      ll.addView(tvInstructions);

      // If we had data previously entered by the user, reset it for them.
      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniPhoneNumber) dataOld).getValue());
      }
      ll.addView(edit);

      // Add the handler for when the user signals they are done with their input.
      dlg.setHandlerOnFilterInputDone(new InputDone() {
        public ModelItem onInputDone(Context context) throws Exception {
          // Try to construct an OmniPhoneNumber from what the user
          // entered in the edit field. If no good, let its exception
          // be thrown up.
          OmniPhoneNumber data = new OmniPhoneNumber(edit.getText().toString());

          // The entered text was valid, return a completely constructed
          // rule filter with an invalid database ID, along with the new
          // associated omni data.
          return new ModelRuleFilter(-1, modelFilter, data);
        }
      });
      
      // Also implement the UI state preservation handlers, again specific to the
      // UI created for this filter.
      dlg.setHandlerPreserveState(new DlgPreserveState() {
        public void saveState(SharedPreferences.Editor prefsEditor) {
          String phoneNumber = edit.getText().toString();
          prefsEditor.putString(UISTATE_PHONENUMBER, phoneNumber);
        }

        public void loadState(SharedPreferences state) {
          if (state.contains(UISTATE_PHONENUMBER)) {
            edit.setText(state.getString(UISTATE_PHONENUMBER, ""));
          }
        }
      });
    }
  };

  private static BuildFilterUI BuildTextEquals = new BuildFilterUI() {
    private static final String UISTATE_TEXT = "text";
    
    public void build(DlgDynamicInput dlg, LinearLayout ll, final ModelFilter modelFilter,
        DataType dataOld) {
      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter an exact text string to match below:");
      ll.addView(tvInstructions);

      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniText) dataOld).getValue());
      }
      ll.addView(edit);

      // Add the handler for when the user is done with their input.
      dlg.setHandlerOnFilterInputDone(new InputDone() {
        public ModelItem onInputDone(Context context) throws Exception {
          OmniText data = new OmniText(edit.getText().toString());
          return new ModelRuleFilter(-1, modelFilter, data);
        }
      });
      // Also implement the UI state preservation handlers.
      dlg.setHandlerPreserveState(new DlgPreserveState() {
        public void saveState(SharedPreferences.Editor prefsEditor) {
          prefsEditor.putString(UISTATE_TEXT, edit.getText().toString());
        }

        public void loadState(SharedPreferences state) {
          if (state.contains(UISTATE_TEXT)) {
            edit.setText(state.getString(UISTATE_TEXT, ""));
          }
        }
      });
    }
  };

  private static BuildFilterUI BuildTextContains = new BuildFilterUI() {
    private static final String UISTATE_TEXT = "text";
    
    public void build(DlgDynamicInput dlg, LinearLayout ll, final ModelFilter modelFilter,
        DataType dataOld) {
      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter a phrase in the text body to match below:");
      ll.addView(tvInstructions);

      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniText) dataOld).getValue());
      }
      ll.addView(edit);

      dlg.setHandlerOnFilterInputDone(new InputDone() {
        public ModelItem onInputDone(Context context) throws Exception {
          OmniText data = new OmniText(edit.getText().toString());
          return new ModelRuleFilter(-1, modelFilter, data);
        }
      });
      dlg.setHandlerPreserveState(new DlgPreserveState() {
        public void saveState(SharedPreferences.Editor prefsEditor) {
          prefsEditor.putString(UISTATE_TEXT, edit.getText().toString());
        }

        public void loadState(SharedPreferences state) {
          if (state.contains(UISTATE_TEXT)) {
            edit.setText(state.getString(UISTATE_TEXT, ""));
          }
        }
      });
    }
  };
  
  private static BuildFilterUI BuildAreaAwayOrNear = new BuildFilterUI() {
    private final static String UISTATE_ADDRESS = "address";
    
    private final static String UISTATE_DISTANCE = "distance";
    
    public void build(DlgDynamicInput dlg, LinearLayout ll, final ModelFilter modelFilter,
        DataType dataOld) {
      TextView tvAddress = new TextView(dlg.getContext());
      tvAddress.setText("Address:");
      ll.addView(tvAddress);

      final EditText etAddress = new EditText(dlg.getContext());
      ll.addView(etAddress);
      
      TextView tvDistance = new TextView(dlg.getContext());
      tvDistance.setText("Distance (in miles):");
      ll.addView(tvDistance);

      final EditText etDistance = new EditText(dlg.getContext());
      ll.addView(etDistance);
      
      if (dataOld != null) {
        OmniArea area = (OmniArea)dataOld;
        etAddress.setText(area.getUserInput());
        etDistance.setText(Double.toString(area.getProximityDistance()));
      }
      
      dlg.setHandlerOnFilterInputDone(new InputDone() {
        public ModelItem onInputDone(Context context) throws Exception {
          String address = etAddress.getText().toString();
          double distance = 0;
          try {
            Double.parseDouble(etDistance.getText().toString());
          } catch (NumberFormatException ex) {
            throw new Exception("Please enter a distance in miles.");
          }
          
          OmniArea data = new OmniArea(OmniArea.getOmniArea(
            context, address, distance));
          
          return new ModelRuleFilter(-1, modelFilter, data);
        }
      });
      dlg.setHandlerPreserveState(new DlgPreserveState() {
        public void saveState(SharedPreferences.Editor prefsEditor) {
          prefsEditor.putString(UISTATE_ADDRESS, etAddress.getText().toString());
          prefsEditor.putString(UISTATE_DISTANCE, etDistance.getText().toString());
        }

        public void loadState(SharedPreferences state) {
          if (state.contains(UISTATE_ADDRESS)) {
            etAddress.setText(state.getString(UISTATE_ADDRESS, ""));
          }
          if (state.contains(UISTATE_DISTANCE)) {
            etDistance.setText(state.getString(UISTATE_DISTANCE, ""));
          }
        }
      });
    }
  };
}