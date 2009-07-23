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

import edu.nyu.cs.omnidroid.core.datatypes.DataType;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.Rule;

/**
 * This is a singleton class used to store elements of a new rule between
 * <code>ActivityChooseRootEvent</code> and <code>ActivityChooseFilters</code>. Using singletons is
 * how the Android team recommends passing this type of data between activities.
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
  private ModelRuleAction action;


  private RuleBuilder() {
  }

  public static RuleBuilder instance() {
    if (instance == null) {
      instance = new RuleBuilder();
    }
    return instance;
  }

  public void reset(ModelEvent event) {
    rule = new Rule(-1);
    rule.setRootEvent(event);
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
  
  public void setModelApplication(ModelApplication application) {
    this.application = application;
  }
  
  public ModelRuleAction getChosenAction() {
    return action;
  }
  
  public void setModelAction(ModelRuleAction action) {
    this.action = action;
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
    action = null;
  }
}