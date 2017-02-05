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
package libretasks.app.controller.util;

import java.util.ArrayList;

/**
 * A Tree data structure which consists of nodes containing a data item, a list of child nodes, and
 * a pointer to its parent.
 */
public class Tree<E> {

  /** The associated filter item for this node. */
  private E item;

  /** Reference to our parent node, if any. */
  private Tree<E> parent;

  /** Our child nodes, may be empty. */
  private ArrayList<Tree<E>> children;

  /**
   * Create a new node with a data item and link to its parent node.
   * 
   * @param parent
   *          the parent node, null if no parent
   * @param item
   *          the data item at this node
   */
  public Tree(Tree<E> parent, E item) {
    this.parent = parent;
    this.item = item;
    this.children = new ArrayList<Tree<E>>();
  }

  /**
   * Returns the parent of this node
   * 
   * @return this node's parent
   */
  public Tree<E> getParent() {
    return parent;
  }

  /**
   * Returns the data item at this node
   * 
   * @return this node's data item
   */
  public E getItem() {
    return item;
  }

  /**
   * Sets the data item at this node
   * 
   * @param item
   *          the data to be stored in this node
   */
  public void setItem(E item) {
    this.item = item;
  }

  /**
   * Gets a list of the child {@link Tree} nodes of this node
   * 
   * @return list of this node's child nodes
   */
  public ArrayList<Tree<E>> getChildren() {
    return children;
  }

  /**
   * Adds a subtree as a child to this node and sets its parent pointer to this node
   * 
   * @param node
   *          the new child node to be inserted
   */
  public void addSubTree(Tree<E> node) {
    node.parent = this;
    children.add(node);
  }

  /**
   * Adds a data item as a new child node of this node
   * 
   * @param item
   *          the data item to be inserted
   * @return a pointer to the new node
   */
  public Tree<E> addChild(E item) {
    Tree<E> it = new Tree<E>(this, item);
    children.add(it);
    return it;
  }

  /**
   * Adds a child node at a specific index in the list of children of this node
   * 
   * @param item
   *          the data item to be inserted
   * @param insertionIndex
   *          the index in the list of child nodes where the data item should be inserted
   * @return a pointer to the new node
   * @throws IndexOutOfBoundsException
   */
  public Tree<E> addChild(E item, int insertionIndex) throws IndexOutOfBoundsException {
    Tree<E> it = new Tree<E>(this, item);
    children.add(insertionIndex, it);
    return it;
  }

  /**
   * Removes a child of this node its sub nodes from the Tree
   * 
   * @param child
   *          the node to be removed
   * @returns true if the child is successfully removed, false otherwise
   */
  public boolean removeChild(Tree<E> child) {
    return children.remove(child);
  }

  /**
   * Removes all children of this node
   */
  public void removeAllChildren() {
    children.clear();
  }

  /**
   * Checks if this is a leaf node of the tree
   * 
   * @return true if this is the last node on a branch, false otherwise
   */
  public boolean isLeafNode() {
    return (children.size() == 0);
  }

  /**
   * Checks if the given node is the last child in the list of this node's children
   * 
   * @param node
   *          the node to check if it is the last child
   * @return true if the given node is the last child, false otherwise
   */
  public boolean getIsLastChild(Tree<E> node) {
    return (children.size() > 0 && children.get(children.size() - 1) == node);
  }

  @Override
  /** Pre-order traversal string representation */
  public String toString() {
    StringBuffer treeString = new StringBuffer();
    getNodeString(this, treeString);
    return treeString.toString();
  }

  /**
   * Appends the string value of a node's item (or "null" if it is null) to the given StringBuffer
   * 
   * @param node
   *          the top node, recursively appends the values from this node
   * @param treeString
   *          a StringBuffer that will be appended with the node's string value
   */
  private void getNodeString(Tree<E> node, StringBuffer treeString) {
    treeString.append((node.getItem() == null) ? "null" : node.item);
    for (Tree<E> childNode : node.children) {
      treeString.append(", ");
      getNodeString(childNode, treeString);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Tree)) {
      return false;
    }
    
    Tree<E> that = (Tree<E>) o;
    return nodeEquals(this, that);
  }

  /**
   * Recursively compares tree nodes for equality
   * 
   * @param thisNode
   *          this Tree node
   * @param thatNode
   *          the node to compare to this Tree node
   * @return true if the nodes and sub nodes are equal, false otherwise
   */
  private boolean nodeEquals(Tree<E> thisNode, Tree<E> thatNode) {
    int nodeCount = thisNode.children.size();
    if (!(nodeCount == thatNode.children.size())) {
      return false;
    }
    if (!(thisNode.item == null ? thatNode.item == null : thisNode.item.equals(thatNode.item))) {
      return false;
    }
    boolean equals = true;
    int i = 0;
    while (equals && i < nodeCount) {
      equals = nodeEquals(thisNode.children.get(i), thatNode.children.get(i));
      i++;
    }
    return equals;
  }

  @Override
  public int hashCode() {
    int result = 17;
    return getNodeHashCode(this, result);
  }

  /**
   * Recursively generate a hash code from the data items in each node
   * 
   * @param node
   *          the node to generate a hash code for
   * @param result
   *          the hash code for the current node and its sub nodes
   * @return
   */
  private int getNodeHashCode(Tree<E> node, int result) {
    result = 37 * result + (node.item == null ? 0 : node.item.hashCode());
    for (Tree<E> childNode : node.children) {
      result += getNodeHashCode(childNode, result);
    }
    return result;
  }
}
