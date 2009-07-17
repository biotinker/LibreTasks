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
 * A node that has an associated <code>ModelItem</code> and a list 
 * of zero or more <code>RuleNodes</code> children.
 */
public class RuleNode {

  /** The associated rule item for this node. */
  private ModelItem mItem;
  
  /** Reference to our parent node, if any. */
  private RuleNode mParent;
  
  /** Our child nodes, may be empty. */
  private ArrayList<RuleNode> mChildren;

  
  public RuleNode(RuleNode parent, ModelItem item) {
    mParent = parent;
    mItem = item;
    mChildren = new ArrayList<RuleNode>();
  }

  public RuleNode getParent() {
    return mParent;
  }

  public ModelItem getItem() {
    return mItem;
  }

  public void setItem(ModelItem item) {
    mItem = item;
  }

  public ArrayList<RuleNode> getChildren() {
    return mChildren;
  }

  public RuleNode addChild(ModelItem item) {
    RuleNode it = new RuleNode(this, item);
    mChildren.add(it);
    return it;
  }

  public RuleNode addChild(ModelItem item, int insertionIndex) throws IndexOutOfBoundsException {
    RuleNode it = new RuleNode(this, item);
    mChildren.add(insertionIndex, it);
    return it;
  }

  public void removeChild(RuleNode child) {
    mChildren.remove(child);
  }

  public void removeAllChildren() {
    mChildren.clear();
  }

  public boolean getIsLastChild(RuleNode node) {
    if (mChildren.size() > 0 && mChildren.get(mChildren.size() - 1) == node) {
      return true;
    }
    return false;
  }
}