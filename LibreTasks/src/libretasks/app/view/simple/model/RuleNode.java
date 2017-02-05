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
package libretasks.app.view.simple.model;

import java.util.ArrayList;

/**
 * A node that has an associated <code>ModelItem</code> and a list of zero or more
 * <code>RuleNodes</code> children.
 */
public class RuleNode {

  /** The associated rule item for this node. */
  private ModelItem item;

  /** Reference to our parent node, if any. */
  private RuleNode parent;

  /** Our child nodes, may be empty. */
  private ArrayList<RuleNode> children;

  public RuleNode(RuleNode parent, ModelItem item) {
    this.parent = parent;
    this.item = item;
    this.children = new ArrayList<RuleNode>();
  }

  public RuleNode getParent() {
    return parent;
  }

  public ModelItem getItem() {
    return item;
  }

  public void setItem(ModelItem item) {
    this.item = item;
  }

  public ArrayList<RuleNode> getChildren() {
    return children;
  }

  public RuleNode addChild(ModelItem item) {
    RuleNode it = new RuleNode(this, item);
    children.add(it);
    return it;
  }

  public RuleNode addChild(ModelItem item, int insertionIndex) throws IndexOutOfBoundsException {
    RuleNode it = new RuleNode(this, item);
    children.add(insertionIndex, it);
    return it;
  }

  public void removeChild(RuleNode child) {
    children.remove(child);
  }

  public void removeAllChildren() {
    children.clear();
  }

  public boolean getIsLastChild(RuleNode node) {
    if (children.size() > 0 && children.get(children.size() - 1) == node) {
      return true;
    }
    return false;
  }
}
