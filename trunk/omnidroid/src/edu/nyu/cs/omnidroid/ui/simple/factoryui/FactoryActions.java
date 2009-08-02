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
package edu.nyu.cs.omnidroid.ui.simple.factoryui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.core.datatypes.DataType;
import edu.nyu.cs.omnidroid.core.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.core.datatypes.OmniText;
import edu.nyu.cs.omnidroid.model.DataTypeIDLookup;
import edu.nyu.cs.omnidroid.ui.simple.UIDbHelperStore;
import edu.nyu.cs.omnidroid.ui.simple.UtilUI;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelParameter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleAction;

/**
 * Static factory class for setting up a dynamic UI for every filter/action type.
 */
public class FactoryActions {

  private static BuilderOmniPhoneNumber vbOmniPhoneNumber;
  private static BuilderOmniText vbOmniText;
  private static BuilderOmniArea vbOmniArea;
  private static HashMap<Long, ViewBuilder> viewBuilders;

  static {
    // Initialize each of our omni data type builders.
    DataTypeIDLookup lookup = UIDbHelperStore.instance().getDatatypeLookup();
    vbOmniPhoneNumber = new BuilderOmniPhoneNumber(lookup
        .getDataTypeID(BuilderOmniPhoneNumber.NAME));
    vbOmniText = new BuilderOmniText(lookup.getDataTypeID(BuilderOmniText.NAME));
    vbOmniArea = new BuilderOmniArea(lookup.getDataTypeID(BuilderOmniArea.NAME));
    // TODO: (markww) Add builders for rest of omni data types.

    viewBuilders = new HashMap<Long, ViewBuilder>();
    viewBuilders.put(vbOmniPhoneNumber.getDatatypeId(), vbOmniPhoneNumber);
    viewBuilders.put(vbOmniText.getDatatypeId(), vbOmniText);
    viewBuilders.put(vbOmniArea.getDatatypeId(), vbOmniArea);
  }

  private FactoryActions() {
  }

  /**
   * Given an action, build a UI for it based on its parameters and their datatypes.
   * 
   * @return The fully constructed UI which should be appended to the caller parent.
   */
  public static LinearLayout buildUIFromAction(ModelAction modelAction,
      ArrayList<DataType> datasOld, Context context) {

    if (datasOld != null && datasOld.size() != modelAction.getParameters().size()) {
      throw new IllegalStateException(
          "Old action parameter data array does not much parameter size!");
    }

    LinearLayout ll = new LinearLayout(context);
    ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    ll.setOrientation(LinearLayout.VERTICAL);

    int numParameters = modelAction.getParameters().size();
    for (int i = 0; i < numParameters; i++) {
      ModelParameter parameter = modelAction.getParameters().get(i);

      // Always add a TextView showing the parameter name.
      addParameterName(parameter, ll);

      // Point to the correct builder for the current parameter.
      ViewBuilder viewBuilder = viewBuilders.get(parameter.getDatatype());
      if (viewBuilder == null) {
        throw new IllegalArgumentException("Unsupported datatype encountered!");
      }

      // Now append the UI elements required for the type.
      viewBuilder.buildUI(ll, datasOld != null ? datasOld.get(i) : null);
    }

    return ll;
  }

  /**
   * Given a layout we previously constructed and an action, extract all user supplied data from it
   * and construct a ModelRuleAction.
   * 
   * @return A ModelRuleAction constructed from the user-supplied data.
   * @throws Exception
   *           If the user-supplied data is invalid or the supplied layout was not created by
   *           buildUIFromAction().
   */
  public static ModelRuleAction buildActionFromUI(ModelAction modelAction, ViewGroup layout)
      throws Exception {

    ArrayList<DataType> datas = new ArrayList<DataType>();

    // Extract all user-supplied information from the UI.
    int childViewPosition = 0;
    int numParameters = modelAction.getParameters().size();
    for (int i = 0; i < numParameters; i++) {
      ModelParameter parameter = modelAction.getParameters().get(i);
      
      // Skip over the parameter-name textview.
      childViewPosition++;

      // Point to the correct builder for the current parameter.
      ViewBuilder viewBuilder = viewBuilders.get(parameter.getDatatype());
      if (viewBuilder == null) {
        throw new IllegalArgumentException("Unsupported datatype encountered!");
      }

      // Add the user-supplied data finally.
      datas.add(viewBuilder.getDataFromView(childViewPosition, layout));

      // Advance past the number of views required for this datatype.
      childViewPosition += viewBuilder.getNumControls();
    }

    return new ModelRuleAction(-1, modelAction, datas);
  }

  /**
   * Given a layout created by buildUIFromAction(), store all of its current data inside the
   * supplied prefsEditor.
   */
  public static void uiStateSave(ModelAction modelAction, ViewGroup layout,
      SharedPreferences.Editor prefsEditor) {

    int childViewPosition = 0;
    int numParameters = modelAction.getParameters().size();
    for (int i = 0; i < numParameters; i++) {
      ModelParameter parameter = modelAction.getParameters().get(i);
      
      // Skip over the parameter-name textview.
      childViewPosition++;

      // Point to the correct builder for the current parameter.
      ViewBuilder viewBuilder = viewBuilders.get(parameter.getDatatype());
      if (viewBuilder == null) {
        throw new IllegalArgumentException("Unsupported datatype encountered!");
      }

      // Finally save the state of this parameter.
      viewBuilder.saveState(prefsEditor, childViewPosition, layout);

      // Advance past the number of views required for this datatype.
      childViewPosition += viewBuilder.getNumControls();
    }
  }

  /**
   * Given a layout created by buildUIFromAction() and data stored by uiStateSave(), load all UI
   * data back into UI elements.
   */
  public static void uiStateLoad(ModelAction modelAction, ViewGroup layout, 
    SharedPreferences state) {

    int childViewPosition = 0;
    int numParameters = modelAction.getParameters().size();
    for (int i = 0; i < numParameters; i++) {
      ModelParameter parameter = modelAction.getParameters().get(i);
      
      // Skip over the parameter-name textview.
      childViewPosition++;

      // Point to the correct builder for the current parameter.
      ViewBuilder viewBuilder = viewBuilders.get(parameter.getDatatype());
      if (viewBuilder == null) {
        throw new IllegalArgumentException("Unsupported datatype encountered!");
      }

      // Finally restore this control.
      viewBuilder.loadState(state, childViewPosition, layout);

      // Advance past the number of views required for this datatype.
      childViewPosition += viewBuilder.getNumControls();
    }
  }
  
  /**
   * Given a control position, return the omni data type it's associated with.
   * @return Omni data type control at specified position belongs to.
   */
  public static long getDatatypeIdForControlAtPosition(
      ModelAction modelAction, ViewGroup layout, int position) {

    int childViewPosition = 0;
    int numParameters = modelAction.getParameters().size();
    for (int i = 0; i < numParameters; i++) {
      
      ModelParameter parameter = modelAction.getParameters().get(i);
      ViewBuilder viewBuilder = viewBuilders.get(parameter.getDatatype());
      if (viewBuilder == null) {
        throw new IllegalArgumentException("Unsupported datatype encountered!");
      }

      // Skip text label.
      childViewPosition++;
      
      int maxChildPosition = childViewPosition + viewBuilder.getNumControls();
      if (position < maxChildPosition) {
        return viewBuilder.getDatatypeId();
      }

      // Advance past the number of views required for this datatype.
      childViewPosition += viewBuilder.getNumControls();
    }
      
    return -1;
  }
  
  public static void insertAttributeForControlAtPosition(
    ModelAction modelAction, ModelAttribute modelAttribute, ViewGroup layout, int position) {

    int childViewPosition = 0;
    int numParameters = modelAction.getParameters().size();
    for (int i = 0; i < numParameters; i++) {
      
      ModelParameter parameter = modelAction.getParameters().get(i);
      ViewBuilder viewBuilder = viewBuilders.get(parameter.getDatatype());
      if (viewBuilder == null) {
        throw new IllegalArgumentException("Unsupported datatype encountered!");
      }

      // Skip text label.
      childViewPosition++;
      
      int maxChildPosition = childViewPosition + viewBuilder.getNumControls();
      if (position < maxChildPosition) {
        viewBuilder.insertAttributeAtControl(childViewPosition, layout, modelAttribute);
        return;
      }

      // Advance past the number of views required for this datatype.
      childViewPosition += viewBuilder.getNumControls();
    }
  }

  /**
   * Appends a TextView with the name of the supplied parameter to the layout.
   * 
   * @param parameter
   * @param ll
   */
  private static void addParameterName(ModelParameter parameter, ViewGroup layout) {
    TextView tv = new TextView(layout.getContext());
    tv.setText(parameter.getTypeName() + ":");
    layout.addView(tv);
  }
  
  private static String getAttributeInsertName(ModelAttribute modelAttribute) {
    return "<" + modelAttribute.getTypeName() + ">";
  }

  /**
   * Interface for UI data type builders. Each data type is responsible for being able to generate a
   * UI for itself, generate a ModelRuleAction, and save and load the UI state.
   */
  interface ViewBuilder {
    public long getDatatypeId();

    /** Returns number of controls being built */
    public int getNumControls();

    public void buildUI(ViewGroup view, DataType dataOld);

    public DataType getDataFromView(int childViewPosition, ViewGroup view) throws Exception;

    public void saveState(SharedPreferences.Editor prefsEditor, int childViewPosition,
        ViewGroup view);

    public void loadState(SharedPreferences state, int childViewPosition, ViewGroup view);
    
    /** Builders should be able to insert an attribute parameter tag directly into
     *  their child view specified by childViewPosition.
     */
    public void insertAttributeAtControl(int childViewPosition, ViewGroup view, 
      ModelAttribute attribute);
  }

  /**
   * Builder for OmniPhoneNumber.
   */
  private static class BuilderOmniPhoneNumber implements ViewBuilder {
    public static final String NAME = "PhoneNumber";
    private long datatypeId;

    public BuilderOmniPhoneNumber(long datatypeId) {
      this.datatypeId = datatypeId;
    }

    public int getNumControls() {
      return 1;
    }

    public long getDatatypeId() {
      return datatypeId;
    }

    public void buildUI(ViewGroup view, DataType dataOld) {
      EditText et = new EditText(view.getContext());
      if (dataOld != null) {
        et.setText(dataOld.getValue());
      }
      view.addView(et);
    }

    public DataType getDataFromView(int childViewPosition, ViewGroup view) throws Exception {
      EditText et = (EditText) view.getChildAt(childViewPosition);
      return new OmniPhoneNumber(et.getText().toString());
    }

    public void saveState(SharedPreferences.Editor prefsEditor, int childViewPosition,
        ViewGroup view) {
      EditText et = (EditText) view.getChildAt(childViewPosition);
      prefsEditor.putString(childViewPosition + "", et.getText().toString());
    }

    public void loadState(SharedPreferences state, int childViewPosition, ViewGroup view) {
      if (state.contains(childViewPosition + "")) {
        EditText et = (EditText) view.getChildAt(childViewPosition);
        et.setText(state.getString(childViewPosition + "", ""));
      }
    }
    
    public void insertAttributeAtControl(int childViewPosition, ViewGroup view, 
        ModelAttribute attribute) {
      EditText et = (EditText) view.getChildAt(childViewPosition);
      UtilUI.replaceEditText(et, getAttributeInsertName(attribute));
    }
  }

  /**
   * Builder for OmniText.
   */
  private static class BuilderOmniText implements ViewBuilder {
    public static final String NAME = "Text";
    private long datatypeId;

    public BuilderOmniText(long datatypeId) {
      this.datatypeId = datatypeId;
    }

    public int getNumControls() {
      return 1;
    }

    public long getDatatypeId() {
      return datatypeId;
    }

    public void buildUI(ViewGroup view, DataType dataOld) {
      EditText et = new EditText(view.getContext());
      if (dataOld != null) {
        et.setText(dataOld.getValue());
      }
      view.addView(et);
    }

    public DataType getDataFromView(int childViewPosition, ViewGroup view) throws Exception {
      EditText et = (EditText) view.getChildAt(childViewPosition);
      return new OmniText(et.getText().toString());
    }

    public void saveState(SharedPreferences.Editor prefsEditor, int childViewPosition,
        ViewGroup view) {
      EditText et = (EditText) view.getChildAt(childViewPosition);
      prefsEditor.putString(childViewPosition + "", et.getText().toString());
    }

    public void loadState(SharedPreferences state, int childViewPosition, ViewGroup view) {
      if (state.contains(childViewPosition + "")) {
        EditText et = (EditText) view.getChildAt(childViewPosition);
        et.setText(state.getString(childViewPosition + "", ""));
      }
    }
    
    public void insertAttributeAtControl(int childViewPosition, ViewGroup view, 
        ModelAttribute attribute) {
      EditText et = (EditText) view.getChildAt(childViewPosition);
      UtilUI.replaceEditText(et, getAttributeInsertName(attribute));
    }
  }

  /**
   * Builder for OmniArea.
   */
  private static class BuilderOmniArea implements ViewBuilder {
    public static final String NAME = "Area";
    private long datatypeId;

    public BuilderOmniArea(long datatypeId) {
      this.datatypeId = datatypeId;
    }

    public int getNumControls() {
      return 4;
    }

    public long getDatatypeId() {
      return datatypeId;
    }

    public void buildUI(ViewGroup view, DataType dataOld) {
      EditText etAddress = new EditText(view.getContext());
      EditText etDistance = new EditText(view.getContext());
      TextView tvAddress = new TextView(view.getContext());
      TextView tvDistance = new TextView(view.getContext());

      if (dataOld != null) {
        OmniArea area = (OmniArea) dataOld;
        etAddress.setText(area.getUserInput());
        etDistance.setText(Double.toString(area.getProximityDistance()));
      }

      tvAddress.setText("Address:");
      tvAddress.setText("Distance (in miles):");

      view.addView(tvAddress);
      view.addView(etAddress);
      view.addView(tvDistance);
      view.addView(etDistance);
    }

    public DataType getDataFromView(int childViewPosition, ViewGroup view) throws Exception {
      childViewPosition++; // Skip TextView
      EditText etAddress = (EditText) view.getChildAt(childViewPosition++);
      childViewPosition++; // Skip TextView
      EditText etDistance = (EditText) view.getChildAt(childViewPosition);
      double distance = Double.parseDouble(etDistance.getText().toString());
      return new OmniArea(OmniArea.getOmniArea(view.getContext(), etAddress.getText().toString(),
          distance));
    }

    public void saveState(SharedPreferences.Editor prefsEditor, int childViewPosition,
        ViewGroup view) {
      childViewPosition++; // Skip TextView
      EditText etAddress = (EditText) view.getChildAt(childViewPosition++);
      prefsEditor.putString("Address", etAddress.getText().toString());
      childViewPosition++; // Skip TextView
      EditText etDistance = (EditText) view.getChildAt(childViewPosition);
      prefsEditor.putString("Distance", etDistance.getText().toString());
    }

    public void loadState(SharedPreferences state, int childViewPosition, ViewGroup view) {
      childViewPosition++;
      if (state.contains("Address")) {
        EditText et = (EditText) view.getChildAt(childViewPosition);
        et.setText(state.getString("Address", ""));
      }
      childViewPosition++;
      childViewPosition++;
      if (state.contains("Distance")) {
        EditText et = (EditText) view.getChildAt(childViewPosition);
        et.setText(state.getString("Distance", ""));
      }
    }
    
    public void insertAttributeAtControl(int childViewPosition, ViewGroup view, 
        ModelAttribute attribute) {
      // TODO: (markww) figure out if BuilderOmniArea should allow copy parameter arguments.
    }
  }
}