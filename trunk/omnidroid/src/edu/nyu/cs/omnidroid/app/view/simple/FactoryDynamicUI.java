/*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
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

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniText;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniTimePeriod;
import edu.nyu.cs.omnidroid.app.controller.util.DataTypeValidationException;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelItem;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleFilter;

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

    /** Called to restore the dynamic dialog to its saved state. 
     * @throws DataTypeValidationException */
    public void loadState(SharedPreferences state) throws DataTypeValidationException;
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

    LinearLayout linearLayout = new LinearLayout(dlg.getContext());
    linearLayout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    //pick UI builder according to the filter
    AllFilterID filterId = new AllFilterID();
    if (modelFilter.getDatabaseId() == filterId.PHONENUMBER_EQUALS) {
      BuildPhonenumberEquals.build(dlg, linearLayout, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == filterId.TEXT_EQUALS) {
      BuildTextEquals.build(dlg, linearLayout, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == filterId.TEXT_CONTAINS) {
      BuildTextContains.build(dlg, linearLayout, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == filterId.AREA_AWAY || 
        modelFilter.getDatabaseId() == filterId.AREA_NEAR) {
      BuildAreaAwayOrNear.build(dlg, linearLayout, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == filterId.DATE_BEFORE_EVERYDAY ||
        modelFilter.getDatabaseId() == filterId.DATE_IS_EVERYDAY ||
        modelFilter.getDatabaseId() == filterId.DATE_IS_NOT_EVERYDAY ||
        modelFilter.getDatabaseId() == filterId.DATE_AFTER_EVERYDAY) {
      BuildDateBeforeOrAfter.build(dlg, linearLayout, modelFilter, dataOld);
    } else if (modelFilter.getDatabaseId() == filterId.DATE_DURING_EVERYDAY ||
        modelFilter.getDatabaseId() == filterId.DATE_EXCEPT_EVERYDAY ||
        modelFilter.getDatabaseId() == filterId.TIMEPERIOD_DURING_EVERYDAY || 
        modelFilter.getDatabaseId() == filterId.TIMEPERIOD_EXCEPT_EVERYDAY) {
      BuildGetTimePeriod.build(dlg, linearLayout, modelFilter, dataOld);
    } else {
      throw new IllegalArgumentException("Unknown filter ID[" + modelFilter.getDatabaseId()
          + "] passed to FactoryDynamicUI::buildUIForFilter()!");
    }
    dlg.addDynamicLayout(linearLayout);
  }

  /**
   * Build UI for a Omnidate to OmniTimePeriod filter
   */
  private static BuildFilterUI BuildGetTimePeriod = new BuildFilterUI() {
    private static final String UISTATE_TIME_PERIOD_START = "time period start";
    private static final String UISTATE_TIME_PERIOD_END = "time period end";

    public void build(final DlgDynamicInput dlg, LinearLayout linearLayout, 
        final ModelFilter modelFilter, DataType dataOld) {
      Log.d("BuildDateDuringOrExcept", "start to build the ui...");
      // This is a Time period Before or After filter, so generate a specific UI for it.
      TextView tvInstructionsStart = new TextView(dlg.getContext());
      tvInstructionsStart.setText(dlg.getContext().getString(
          R.string.pick_start_time));
      linearLayout.addView(tvInstructionsStart);

      final TimePicker startTimePicker = new TimePicker(dlg.getContext());
      startTimePicker.setIs24HourView(false);
      // If we had data previously entered by the user, reset it for them.
      if (dataOld != null) {
        startTimePicker.setCurrentHour(((OmniTimePeriod) dataOld).getStartHour());
        startTimePicker.setCurrentMinute(((OmniTimePeriod) dataOld).getStartMinute());
      }
      linearLayout.addView(startTimePicker);

      TextView tvInstructionsEnd = new TextView(dlg.getContext());
      tvInstructionsEnd.setText(dlg.getContext().getString(
          R.string.pick_end_time));
      linearLayout.addView(tvInstructionsEnd);

      final TimePicker endTimePicker = new TimePicker(dlg.getContext());
      endTimePicker.setIs24HourView(false);
      // If we had data previously entered by the user, reset it for them.
      if (dataOld != null) {
        endTimePicker.setCurrentHour(((OmniTimePeriod) dataOld).getEndHour());
        endTimePicker.setCurrentMinute(((OmniTimePeriod) dataOld).getEndMinute());
      }
      linearLayout.addView(endTimePicker);

      // Add the handler for when the user signals they are done with their input.
      dlg.setHandlerOnFilterInputDone(new InputDone() {
        public ModelItem onInputDone(Context context) throws Exception {
          // Try to construct an OmniTimePeriod from what the user
          // entered. If no good, let its exception be thrown up. As we don't use
          // year, month, day, so set them to be anything.
          OmniTimePeriod data = new OmniTimePeriod(
              OmniTimePeriod.buildTimeString(1, 1, 1, startTimePicker.getCurrentHour(), 
                  startTimePicker.getCurrentMinute(), 0),
                  OmniTimePeriod.buildTimeString(1, 1, 1, endTimePicker.getCurrentHour(), 
                      endTimePicker.getCurrentMinute(), 0));

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
          prefsEditor.putString(UISTATE_TIME_PERIOD_START, 
              OmniTimePeriod.buildTimeString(0, 0, 0, startTimePicker.getCurrentHour(), 
                  startTimePicker.getCurrentMinute(), 0));
          prefsEditor.putString(UISTATE_TIME_PERIOD_END, 
              OmniTimePeriod.buildTimeString(0, 0, 0, endTimePicker.getCurrentHour(), 
                  endTimePicker.getCurrentMinute(), 0));
        }

        public void loadState(SharedPreferences state) throws DataTypeValidationException {
          if (state.contains(OmniTimePeriod.buildTimeString(1, 1, 1,
              startTimePicker.getCurrentHour(), 
              startTimePicker.getCurrentMinute(), 0))) {
            Date date = OmniTimePeriod.getDate(state.getString(UISTATE_TIME_PERIOD_START, ""));
            startTimePicker.setCurrentHour(date.getHours());
            startTimePicker.setCurrentMinute(date.getMinutes());

          }
          if (state.contains(OmniTimePeriod.buildTimeString(1, 1, 1, endTimePicker.getCurrentHour(), 
              endTimePicker.getCurrentMinute(), 0))) {
            Date date = OmniTimePeriod.getDate(state.getString(UISTATE_TIME_PERIOD_END, ""));
            endTimePicker.setCurrentHour(date.getHours());
            endTimePicker.setCurrentMinute(date.getMinutes());
          }
        }
      });
    }
  };

  /**
   * UI for before or after filter for OmniDate
   */
  private static BuildFilterUI BuildDateBeforeOrAfter = new BuildFilterUI() {
    private static final String UISTATE_DATE = "time";

    public void build(DlgDynamicInput dlg, LinearLayout linearLayout, final ModelFilter modelFilter,
        DataType dataOld) {
      // This is a Before or After filter, so generate a specific UI for it.
      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText(dlg.getContext().getString(
          R.string.pick_time));
      linearLayout.addView(tvInstructions);

      final TimePicker timePicker = new TimePicker(dlg.getContext());
      timePicker.setIs24HourView(false);
      // If we had data previously entered by the user, reset it for them.
      if (dataOld != null) {
        timePicker.setCurrentHour(((OmniDate) dataOld).getDate().getHours());
        timePicker.setCurrentMinute(((OmniDate) dataOld).getDate().getMinutes());
      }
      linearLayout.addView(timePicker);

      // Add the handler for when the user signals they are done with their input.
      dlg.setHandlerOnFilterInputDone(new InputDone() {
        public ModelItem onInputDone(Context context) throws Exception {
          // Try to construct an OmniDate from what the user
          // entered in the edit field. If no good, let its exception
          // be thrown up.
          OmniDate data = new OmniDate(
              OmniTimePeriod.buildTimeString(1, 1, 1, timePicker.getCurrentHour(), 
              timePicker.getCurrentMinute(), 0));

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
          String date = OmniTimePeriod.buildTimeString(1, 1, 1, timePicker.getCurrentHour(), 
              timePicker.getCurrentMinute(), 0);
          prefsEditor.putString(UISTATE_DATE, date);
        }

        public void loadState(SharedPreferences state) throws DataTypeValidationException {
          if (state.contains(UISTATE_DATE)) {
            Date date = OmniTimePeriod.getDate(state.getString(UISTATE_DATE, ""));
            timePicker.setCurrentHour(date.getHours());
            timePicker.setCurrentMinute(date.getMinutes());
          }
        }
      });
    }
  };

  /**
   * UI for equal filter for PhoneNumber
   */
  private static BuildFilterUI BuildPhonenumberEquals = new BuildFilterUI() {
    private static final String UISTATE_PHONENUMBER = "phonenumber";

    public void build(DlgDynamicInput dlg, LinearLayout linearLayout, final ModelFilter modelFilter,
        DataType dataOld) {
      // This is a Phonenumber Equals filter, so generate a specific UI for it.
      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter a phone number to match below:");
      linearLayout.addView(tvInstructions);

      // If we had data previously entered by the user, reset it for them.
      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniPhoneNumber) dataOld).getValue());
      }
      linearLayout.addView(edit);

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

  /**
   * UI for equal filter between OmniText
   */
  private static BuildFilterUI BuildTextEquals = new BuildFilterUI() {
    private static final String UISTATE_TEXT = "text";

    public void build(DlgDynamicInput dlg, LinearLayout linearLayout, final ModelFilter modelFilter,
        DataType dataOld) {
      TextView tvInstructions = new TextView(dlg.getContext());
      tvInstructions.setText("Enter an exact text string to match below:");
      linearLayout.addView(tvInstructions);

      final EditText edit = new EditText(dlg.getContext());
      if (dataOld != null) {
        edit.setText(((OmniText) dataOld).getValue());
      }
      linearLayout.addView(edit);

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

  /**
   * UI for contains filter between OmniText
   */
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
            distance = Double.parseDouble(etDistance.getText().toString());
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

  /**
   * static class to load all filter id for later use.
   */
  @SuppressWarnings("unused")
  private static class AllFilterID{
    public final long PHONENUMBER_EQUALS = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniPhoneNumber.DB_NAME, OmniPhoneNumber.Filter.EQUALS.toString());
    public final long TEXT_EQUALS = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniText.DB_NAME, OmniText.Filter.EQUALS.toString());
    public final long TEXT_CONTAINS = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniText.DB_NAME, OmniText.Filter.CONTAINS.toString());
    public final long AREA_AWAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniArea.DB_NAME, OmniArea.Filter.AWAY.toString());
    public final long AREA_NEAR = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniArea.DB_NAME, OmniArea.Filter.NEAR.toString());
    public final long DATE_IS_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.IS_EVERYDAY.toString());
    public final long DATE_IS_NOT_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.IS_NOT_EVERYDAY.toString());
    public final long DATE_BEFORE = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.BEFORE.toString());
    public final long DATE_AFTER = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.AFTER.toString());
    public final long DATE_AFTER_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.AFTER_EVERYDAY.toString());
    public final long DATE_BEFORE_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.BEFORE_EVERYDAY.toString());
    public final long DATE_DURING = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME, OmniDate.Filter.DURING.toString());
    public final long DATE_DURING_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME, 
        OmniDate.Filter.DURING_EVERYDAY.toString());
    public final long DATE_EXCEPT = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME, OmniDate.Filter.EXCEPT.toString());
    public final long DATE_EXCEPT_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME, 
        OmniDate.Filter.EXCEPT_EVERYDAY.toString());
    public final long TIMEPERIOD_DURING = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniTimePeriod.DB_NAME,OmniDate.DB_NAME, 
        OmniTimePeriod.Filter.DURING.toString());
    public final long TIMEPERIOD_DURING_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniTimePeriod.DB_NAME,OmniDate.DB_NAME, 
        OmniTimePeriod.Filter.DURING_EVERYDAY.toString());
    public final long TIMEPERIOD_EXCEPT = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniTimePeriod.DB_NAME,OmniDate.DB_NAME, 
        OmniTimePeriod.Filter.EXCEPT.toString());
    public final long TIMEPERIOD_EXCEPT_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
    .getDataFilterID(OmniTimePeriod.DB_NAME,OmniDate.DB_NAME, 
        OmniTimePeriod.Filter.EXCEPT_EVERYDAY.toString());
  }
}