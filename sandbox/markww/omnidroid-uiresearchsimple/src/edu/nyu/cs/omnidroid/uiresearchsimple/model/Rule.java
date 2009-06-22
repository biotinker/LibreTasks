package edu.nyu.cs.omnidroid.uiresearchsimple.model;

import java.util.ArrayList;


/**
 * A rule is a collection of item nodes, representing
 * the different ways in which users can cluster filter
 * chains together. 
 */
public class Rule
{
	private RuleNode mNode;
	
	
	public Rule() {
	}
	
	public void setRootEvent(ModelEvent event) {
		if (mNode == null) {
			mNode = new RuleNode(null, event);
		}
		else {
			mNode.setItem(event);
		}
	}
	
	public RuleNode getRootEvent() {
		return mNode;
	}
	
	public String getNaturalLanguageString() {
		// TODO: Generate natural language string for current rule.
		return "Not yet implemented!";
	}
	
	
	/**
	 * Represents a single entry in our ListView. 
	 * Each node acts like a tree, but we display
	 * it in the ListView as a flat list for speed.
	 */
	public static class RuleNode
	{
		private int mDepth;
		private ModelItem mItem;
		private RuleNode mParent;
		private ArrayList<RuleNode> mChildren;
		
		
		public RuleNode(RuleNode parent, ModelItem item) {
			mParent = parent;
			mItem = item;
			mChildren = new ArrayList<RuleNode>();
		}
		
		public RuleNode getParent() {
			return mParent;
		}

		public int getDepth() {
			return mDepth;
		}
		
		public void setDepth(int depth) {
			mDepth = depth;
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
		
		public void addChild(ModelItem item) {
			RuleNode it = new RuleNode(this, item);
			mChildren.add(it);
		}
		
		public void removeChild(RuleNode child) {
			mChildren.remove(child);
		}
	}
}