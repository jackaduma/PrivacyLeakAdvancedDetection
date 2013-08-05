package m.k.callgraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author kun
 *
 */
public class CG {
	
	public Set<GraphNode> graph = new HashSet<GraphNode>();

	
	private int maxStack;
	private GraphNode[] stack;
	private int top;
	
	public Set<LinkedList<GraphNode>> infectionChainSet = new HashSet<LinkedList<GraphNode>>();
	
	public CG(Set<GraphNode> g, int max) {
		this.graph = g;
		this.maxStack = max;
		
		// init stack
		this.stack = new GraphNode[this.maxStack];
		this.top = -1;
	}
	
	public void PopStack() {
		if (this.top <= -1) {
			return;
		}
		
		this.stack[this.top] = null;
		this.top -= 1;
	}
	
	/*
	 * push node into stack
	 * set access flag
	 */
	public void PushStack(GraphNode n) {
		if (this.top >= this.maxStack) {
			System.out.println("Stack Overflow!");
			return;
		}
		n.accessFlag = 1;  // set access flag
		this.top += 1;
		this.stack[this.top] = n;		
	}
	
	public void PrintStack(String sep) {
		System.out.println("**************************************");
		
		LinkedList<GraphNode> infectionChain = new LinkedList<GraphNode>();
		for (int i=0; i<= this.top; i++) {
			System.out.println(this.stack[i] + sep);
			infectionChain.add(this.stack[i]);
		}
		System.out.println("**************************************");
		
		// Remove Duplicates		
		if (!this.ExistChain(infectionChain)) {
			this.infectionChainSet.add(infectionChain);
		}
	}
	
	public boolean ExistChain(LinkedList<GraphNode> newNode) {
		if (null == this.infectionChainSet) {
			return false;
		}
		
		for (LinkedList<GraphNode> oldNode : this.infectionChainSet) {
			if (newNode.equals(oldNode)) {
				return true;
			}
			
			if (oldNode.containsAll(newNode)) {
				return this.IsSubChain(oldNode, newNode);
			}
			else if (newNode.containsAll(oldNode)) {
				return this.IsSubChain(newNode, oldNode);
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	public boolean IsSubChain(LinkedList<GraphNode> parent, LinkedList<GraphNode> child) {
		Iterator<GraphNode> pIt = parent.iterator();
		Iterator<GraphNode> cIt = child.iterator();
		while (cIt.hasNext()) {
			GraphNode cE = cIt.next();
			GraphNode pE = pIt.next();
			if (!cE.equals(pE)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * DFS
	 */
	public void Forward(GraphNode p) {
		PushStack(p);
		while (true) {
			if (-1 == this.top) {
				break;
			}			
			
			p = p.GetUnAccessChild();
			if (null == p) {
				PrintStack(" --> ");
				PopStack();
				if (-1 == this.top) {
					break;
				}
				p = this.stack[this.top];
			}
			else {
				PushStack(p);
			}
		}
	}
	
	public void Backward(GraphNode p) {
		PushStack(p);
		while (true) {
			if (-1 == this.top) {
				break;
			}			
			
			p = p.GetUnAccessParent();
			if (null == p) {
				PrintStack(" <-- ");
				PopStack();
				if (-1 == this.top) {
					break;
				}
				p = this.stack[this.top];
			}
			else {
				PushStack(p);
			}
		}
	}
	
}
