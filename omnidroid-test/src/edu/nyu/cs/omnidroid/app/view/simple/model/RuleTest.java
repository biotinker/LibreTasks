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
package edu.nyu.cs.omnidroid.app.view.simple.model;

import java.util.ArrayList;

import junit.framework.TestCase;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelParameter;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleAction;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleFilter;
import edu.nyu.cs.omnidroid.app.view.simple.model.Rule;
import edu.nyu.cs.omnidroid.app.view.simple.model.RuleNode;

public class RuleTest extends TestCase {
  private Rule dummyRule;
  private ModelEvent dummyEvent;
  private ModelRuleFilter dummyFilter1;
  private ModelRuleFilter dummyFilter2;
  private ModelRuleAction dummyAction1;
  private ModelRuleAction dummyAction2;
  
  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp() throws Exception {
    // Setup a dummy root event.
    dummyEvent = new ModelEvent(-1, "DummyEvent", "", -1);
    
    // Setup a dummy model filter.
    ModelFilter modelFilter = new ModelFilter("DummyFilter", "", -1, -1, 
      new ModelAttribute(-1, -1, -1, "DummyAttribute", "", -1));
    
    // We can create a dummy ModelRuleFilter now using the dummy model filter.
    dummyFilter1 = new ModelRuleFilter(-1, modelFilter, new OmniPhoneNumber("111-222-3333"));
    dummyFilter2 = new ModelRuleFilter(-1, modelFilter, new OmniPhoneNumber("444-555-6666"));
    
    // Setup a dummy model action.
    ModelAction modelAction = new ModelAction("DummyAction", "", -1, -1, 
      new ModelApplication("DummyApplication", "", -1, -1), new ArrayList<ModelParameter>());
    
    // Make some dummy data for it.
    ArrayList<DataType> datas = new ArrayList<DataType>();
    datas.add(new OmniPhoneNumber("111-222-3333"));
    
    // We can create a dummy ModelRuleAction now using the dummy model action.
    dummyAction1 = new ModelRuleAction(-1, modelAction, datas);
    dummyAction2 = new ModelRuleAction(-2, modelAction, datas);

    // Finally the dummy rule to which we'll add different combinations of the above.
    dummyRule = new Rule(-1);
  }
  
  private void resetRule() {
    dummyRule.setRootEvent(dummyEvent);
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.view.simple.model.Rule#setRootEvent()}.
   */
  public void testResetRule() {
      Rule rule = new Rule(-1);
      rule.setRootEvent(dummyEvent);
      assertEquals(0, rule.getRootNode().getChildren().size());
      
      rule.getRootNode().addChild(dummyFilter1);
      assertEquals(1, rule.getRootNode().getChildren().size());
      rule.setRootEvent(dummyEvent);
      assertEquals(0, rule.getRootNode().getChildren().size());
  }
  
  public void testEmptyRule() {
    resetRule();
    
    // The first action position should be -1 since there are no action.
    assertEquals(dummyRule.getFirstActionPosition(), -1);
    // We should get a list with zero filter branches.
    ArrayList<RuleNode> filterBranches = dummyRule.getFilterBranches();
    assertEquals(0, filterBranches.size());
    // We should get a list of zero actions.
    ArrayList<ModelRuleAction> actions = dummyRule.getActions();
    assertEquals(0, actions.size());
  }
  
  public void testOneFilter() {
    resetRule();
    dummyRule.getRootNode().addChild(dummyFilter1);
    
    // The first action position should be -1 since there are no action.
    assertEquals(-1, dummyRule.getFirstActionPosition());
    // We should get a list with one filter branch.
    ArrayList<RuleNode> filterBranches = dummyRule.getFilterBranches();
    assertEquals(1, filterBranches.size());
    // We should get a list of zero actions.
    ArrayList<ModelRuleAction> actions = dummyRule.getActions();
    assertEquals(0, actions.size());
    
    // Check that the filter is the same one we added.
    assertEquals(dummyFilter1, filterBranches.get(0).getItem());
  }
  
  public void testOneAction() {
    resetRule();
    dummyRule.getRootNode().addChild(dummyAction1);
    
    // The first action position should be 0 since we have one action and zero filters.
    assertEquals(0, dummyRule.getFirstActionPosition());
    // We should get a list with zero filter branches.
    ArrayList<RuleNode> filterBranches = dummyRule.getFilterBranches();
    assertEquals(0, filterBranches.size());
    // We should get a list of one action.
    ArrayList<ModelRuleAction> actions = dummyRule.getActions();
    assertEquals(1, actions.size());

    // Check that the action is the same one we added.
    assertEquals(dummyAction1, actions.get(0));
  }
  
  public void testOneActionOneFilter() {
    resetRule();
    dummyRule.getRootNode().addChild(dummyFilter1);
    dummyRule.getRootNode().addChild(dummyAction1);
    
    // The first action position should be 1 since we have one filter and one action.
    assertEquals(dummyRule.getFirstActionPosition(), 1);
    // We should get a list with one filter branches.
    ArrayList<RuleNode> filterBranches = dummyRule.getFilterBranches();
    assertEquals(1, filterBranches.size());
    // We should get a list of one action.
    ArrayList<ModelRuleAction> actions = dummyRule.getActions();
    assertEquals(1, actions.size());

    // Check that the filter and action are the same ones we added.
    assertEquals(dummyFilter1, filterBranches.get(0).getItem());
    assertEquals(dummyAction1, actions.get(0));
  }
  
  public void testTwoFilterTwoAction() {
    resetRule();
    dummyRule.getRootNode().addChild(dummyFilter1);
    dummyRule.getRootNode().addChild(dummyFilter2);
    dummyRule.getRootNode().addChild(dummyAction1);
    dummyRule.getRootNode().addChild(dummyAction2);
    
    // The first action position should be 2 since we have two filters and at least one action.
    assertEquals(2, dummyRule.getFirstActionPosition());
    // We should get a list with two filter branches.
    ArrayList<RuleNode> filterBranches = dummyRule.getFilterBranches();
    assertEquals(2, filterBranches.size());
    // We should get a list of two actions.
    ArrayList<ModelRuleAction> actions = dummyRule.getActions();
    assertEquals(2, actions.size());
    
    // Check that the actual element objects are the same as we added.
    assertEquals(filterBranches.get(0).getItem(), dummyFilter1);
    assertEquals(filterBranches.get(1).getItem(), dummyFilter2);
    assertEquals(actions.get(0), dummyAction1);
    assertEquals(actions.get(1), dummyAction2);
  }
  
  public void testBranchedFiltersTwoAction() {
    resetRule();
    dummyRule.getRootNode().addChild(dummyFilter1);
    dummyRule.getRootNode().getChildren().get(0).addChild(dummyFilter2);
    dummyRule.getRootNode().addChild(dummyAction1);
    dummyRule.getRootNode().addChild(dummyAction2);
    
    // The first action position should be 1 since we have one filter branch and at least one 
    // action.
    assertEquals(1, dummyRule.getFirstActionPosition());
    // We should get a list with one filter branches.
    ArrayList<RuleNode> filterBranches = dummyRule.getFilterBranches();
    assertEquals(1, filterBranches.size());
    // The one filter branch should have one child.
    assertEquals(1, filterBranches.get(0).getChildren().size());
    // It should have no children.
    assertEquals(0, filterBranches.get(0).getChildren().get(0).getChildren().size());
    // We should get a list of two actions.
    ArrayList<ModelRuleAction> actions = dummyRule.getActions();
    assertEquals(2, actions.size());
    
    // Check that the actual element objects are the same as we added.
    assertEquals(dummyFilter1, filterBranches.get(0).getItem());
    assertEquals(dummyFilter2, filterBranches.get(0).getChildren().get(0).getItem());
    assertEquals(dummyAction1, actions.get(0));
    assertEquals(dummyAction2, actions.get(1));
  }
}