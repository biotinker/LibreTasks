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
package libretasks.app.controller;

import java.util.ArrayList;

import libretasks.app.controller.util.Tree;
import libretasks.app.model.CoreActionsDbHelper;

/**
 * This class represents a user defined rule. It consists of the name of the rule (for logging
 * purposes), the filters that apply to the event, and the actions that will be performed if an
 * event matches the rule.
 */
public class Rule {

  /** Rule parameters */
  public final String ruleName;
  Tree<Filter> filterTree;

  /** Package private for pre-populating database tests */
  long ruleID;

  /** An event to check against this rule */
  private Event event;
  
  /** Shows whether notification service is on or off for this rule*/
  private Boolean showNotification;
  
  /**
   * Constructs a rule from all rule parameters
   * 
   * @param ruleName
   *          user-defined name of the rule
   * @param filters
   *          a tree of filters on the event attributes which captures the and/or relationships
   *          between the filters, can be null if there are no filters defined for this rule
   * @throws IllegalArgumentException
   *           if required parameters are null
   */
  public Rule(String ruleName, long ruleID, Tree<Filter> filterTree, Boolean showNotification) {
    if (ruleName == null) {
      throw new IllegalArgumentException("ruleName cannot be null");
    }
    this.ruleName = ruleName;
    this.ruleID = ruleID;
    this.filterTree = filterTree;
    this.showNotification = showNotification;
  }

  /**
   * Matches the {@link Event} to all {@link Filter}s associated with this rule
   * 
   * @param event
   *          the event that triggered this rule
   * @return true if this event passes all rule filters (or if there are no filters), false
   *         otherwise
   */
  public boolean passesFilters(Event event) {
    if (filterTree == null) {
      return true;
    }

    this.event = event;
    return isFilterBranchTrue(filterTree);
  }

  /**
   * Returns the actions associated with this rule. Populates the action parameter fields, which may
   * require retrieving them from the event
   * 
   * @param coreActionsDbHelper
   *          The helper class to get actions data from database
   * @param event
   *          the event which triggered this rule and may contain data for the action parameters
   * @return the action(s) fired by this rule
   */
  public ArrayList<Action> getActions(CoreActionsDbHelper coreActionsDbHelper, Event event) {
    // Get actions arraylist for this rule
    ArrayList<Action> actionsList = coreActionsDbHelper.getActions(ruleID, ruleName, event);
    for (Action action : actionsList) {
      action.setNotification(showNotification);
    }
    return actionsList;
  }

  /**
   * Recursively descends down the tree looking for a branch which returns true at the leaf level,
   * which represents an "and" relationship between the filters
   * 
   * @param node
   *          the root of the tree on which to check the filters
   * @return true if this is a leaf, or if at least one branch is true to the leaf level, false
   *         otherwise
   */
  private boolean isFilterBranchTrue(Tree<Filter> node) {
    if (node.isLeafNode()) {
      return node.getItem().match(event);
    }

    for (Tree<Filter> currentNode : node.getChildren()) {
      if (currentNode.getItem().match(event)) {
        return isFilterBranchTrue(currentNode);
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Rule)) {
      return false;
    }
    Rule that = (Rule) o;
    return that.ruleName.equals(ruleName)
        && (filterTree == null ? that.filterTree == null : filterTree.equals(that.filterTree));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 37 * result + ruleName.hashCode();
    result = 37 * result + (filterTree == null ? 0 : filterTree.hashCode());
    return result;
  }
}
