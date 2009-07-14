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
package edu.nyu.cs.omnidroid.ui.simple.model;

import java.util.ArrayList;

/**
 * A rule is a collection of <code>RuleNode</code> nodes. Each RuleNode instance has a single
 * associated <code>ModelItem</code> and a list of zero or more child <code>RuleNode</code> items.
 * This structure allows us to nest filters within other filters etc.
 */
public class Rule {

  /**
   * The root node will always represent the root event, and will turn out to be a tree looking like
   * this: 
   * RootEvent 
   *   |- Filter1 
   *   |- Filter2 
   *   |    |- Filter3 
   *   |    |- Filter4 
   *   |- Action1 
   *   |- Action2
   * 
   * I thought about making Actions a separate array on their own, but in the future maybe we want
   * to support nesting actions so that a branch is performed only if all parent nodes were executed
   * OK, instead of treating it as a straight list of tasks to be performed.
   */
  private RuleNode mNode;
  

  public Rule() {
  }
  
  public void setRootEvent(ModelEvent event) {
    if (mNode == null) {
      mNode = new RuleNode(null, event);
    } else {
      mNode.setItem(event);
    }
  }

  public RuleNode getRootNode() {
    return mNode;
  }

  public boolean getHasAnyFilters() {
    return mNode.getChildren().size() > 0;
  }

  public ArrayList<RuleNode> getFilterBranches() {
    ArrayList<RuleNode> filters = new ArrayList<RuleNode>();
    
    int indexAction = getFirstActionPosition();
    for (int i = 0; i < getRootNode().getChildren().size(); i++) {
    	if (i >= indexAction) {
    		break;
    	}
    	filters.add(getRootNode().getChildren().get(i));
    }
    
    return filters;
  }
  
  /**
   * Extract all <code>ModelAction</code> instances out of the tree form and add them to the a list.
   * 
   * @return All actions that are part of the rule.
   */
  public ArrayList<ModelAction> getActions() {
    ArrayList<ModelAction> actions = new ArrayList<ModelAction>();
    for (int i = getFirstActionPosition(); i < mNode.getChildren().size(); i++) {
      actions.add((ModelAction) mNode.getChildren().get(i).getItem());
    }
    return actions;
  }

  public int getFirstActionPosition() {
    // Actions are stored as siblings under the root event.
    // This is enforced by the user interface.
    for (int i = mNode.getChildren().size() - 1; i > -1; i--) {
      if (mNode.getChildren().get(i).getItem() instanceof ModelAction) {
        return i;
      }
    }
    return -1;
  }

  public String getNaturalLanguageString() {
    StringBuilder sb = new StringBuilder();
    buildNaturalLanguageString(mNode, sb);
    return sb.toString();
  }

  private void buildNaturalLanguageString(RuleNode node, StringBuilder sb) {
    sb.append(node.getItem().getDescriptionShort());
    sb.append(", ");
    for (int i = 0; i < node.getChildren().size(); i++) {
      buildNaturalLanguageString(node.getChildren().get(i), sb);
    }
  }
}