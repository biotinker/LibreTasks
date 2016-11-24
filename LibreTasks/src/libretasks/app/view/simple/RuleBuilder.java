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
package libretasks.app.view.simple;

import java.util.ArrayList;

import libretasks.app.controller.datatypes.DataType;
import libretasks.app.view.simple.model.ModelAction;
import libretasks.app.view.simple.model.ModelApplication;
import libretasks.app.view.simple.model.ModelAttribute;
import libretasks.app.view.simple.model.ModelEvent;
import libretasks.app.view.simple.model.ModelFilter;
import libretasks.app.view.simple.model.ModelRuleAction;
import libretasks.app.view.simple.model.ModelRuleFilter;
import libretasks.app.view.simple.model.Rule;

/**
 * This is a singleton class used to store elements of a rule between different activities
 * as the user either creates a new rule, or edits an existing rule. The Android team recommends
 * using singletons for passing this sort of data between activities.
 * 
 * The user can create a new rule starting in {@link ActivityChooseRootEvent}. In this case,
 * {@link resetWithRootEventForNewRuleEditing} should be called to wipe the internal rule 
 * representation and start with a brand new empty rule.
 * 
 * The user can edit an existing rule starting in {@link ActivitySavedRules}. In this case,
 * {@link resetWithSavedRuleForEditing} should be called to set the internal rule to a saved rule
 * instance as loaded from the database.
 * 
 * In both cases, the user can then go about viewing and modifying the rule inside the activity
 * {@link ActivityChooseRootEvent}.
 * 
 * TODO: (markww) Split this class into three separate classes, one for storage of the rule being 
 * created, the second for storage of a filter being created, and the third for storage of an 
 * action being created.
 */
public class RuleBuilder {

  private static RuleBuilder instance;
  private Rule rule;

  private ModelAttribute attribute;
  private ModelFilter modelFilter;
  private ModelRuleFilter ruleFilter;
  private DataType ruleFilterDataOld;
  
  private ModelApplication application;
  private ModelAction modelAction;
  private ModelRuleAction ruleAction;
  private ArrayList<DataType> ruleActionDataOld;


  private RuleBuilder() {
  }

  public static RuleBuilder instance() {
    if (instance == null) {
      instance = new RuleBuilder();
    }
    return instance;
  }

  /** 
   * Initialize with a root event. This should be used when the user wants to create a brand
   * new rule.
   */
  public void resetForNewRuleEditing(ModelEvent rootEvent) {
    rule = new Rule();
    rule.setRootEvent(rootEvent);
    resetFilterPath();
    resetActionPath();
  }
  
  /**
   * Initialize with an existing rule. This should be used when the user wants to edit an
   * existing rule.
   */
  public void resetForEditing(Rule savedRule) {
    rule = savedRule;
    resetFilterPath();
    resetActionPath();
  }

  public Rule getRule() {
    return rule;
  }
  
  public ModelEvent getChosenEvent() {
    return (ModelEvent)rule.getRootNode().getItem();
  }
  
  public ModelAttribute getChosenAttribute() {
    return attribute;
  }
  
  public void setChosenAttribute(ModelAttribute attribute) {
    this.attribute = attribute;
  }
  
  public ModelFilter getChosenModelFilter() {
    return modelFilter;
  }
  
  public void setChosenModelFilter(ModelFilter modelFilter) {
    this.modelFilter = modelFilter;
  }
  
  public ModelRuleFilter getChosenRuleFilter() {
    return ruleFilter;
  }
  
  public void setChosenRuleFilter(ModelRuleFilter ruleFilter) {
    this.ruleFilter = ruleFilter;
  }
  
  public DataType getChosenRuleFilterDataOld() {
    return ruleFilterDataOld;
  }
  
  public void setChosenRuleFilterDataOld(DataType ruleFilterDataOld) {
    this.ruleFilterDataOld = ruleFilterDataOld;
  }
  
  public ModelApplication getChosenApplication() {
    return application;
  }
  
  public void setChosenApplication(ModelApplication application) {
    this.application = application;
  }
  
  public ModelAction getChosenModelAction() {
    return modelAction;
  }
  
  public void setChosenModelAction(ModelAction modelAction) {
    this.modelAction = modelAction;
  }
	  
  public ModelRuleAction getChosenRuleAction() {
    return ruleAction;
  }
  
  public void setChosenRuleAction(ModelRuleAction ruleAction) {
    this.ruleAction = ruleAction;
  }
  
  public ArrayList<DataType> getChosenRuleActionDataOld() {
	return ruleActionDataOld;
  }
  
  public void setChosenRuleActionDataOld(ArrayList<DataType> ruleActionDataOld) {
    this.ruleActionDataOld = ruleActionDataOld;
  }
  
  /**
   * Reset all elements used for creating a new filter.
   */
  public void resetFilterPath() {
    attribute = null;
    modelFilter = null;
    ruleFilter = null;
    ruleFilterDataOld = null;
  }
  
  /**
   * Reset all elements used for creating a new action.
   */
  public void resetActionPath() {
    application = null;
    modelAction = null;
    ruleAction = null;
    ruleActionDataOld = null;
  }
}
