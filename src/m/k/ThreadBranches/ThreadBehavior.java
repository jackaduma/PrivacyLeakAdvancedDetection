package m.k.ThreadBranches;

import java.util.LinkedList;

import m.k.callgraph.GraphNode;

/**
 * @author kun
 *
 */
public class ThreadBehavior {
	
	private String threadName;
	private int threadIndex;
	
	private LinkedList<GraphNode> callGraph = new LinkedList<GraphNode> ();
	
	public ThreadBehavior(String name, int index) {
		this.threadName = name;
		this.threadIndex = index;
	}
	
	public ThreadBehavior(GraphNode gn, int index) {
		this.threadName = gn.owner;
		this.threadIndex = index;
	}
	
	public String getThreadName() {
		return this.threadName;
	}
	
	public int getThreadIndex() {
		return this.threadIndex;
	}
}
