package m.k.callgraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import m.k.callgraph.classgraph.ClassGraphNode;

/**
 * @author kun_ma
 * 
 */
public class GraphNode {

	public Set<GraphNode> parents = new HashSet<GraphNode>();
	public Set<GraphNode> children = new HashSet<GraphNode>();

	public ClassGraphNode classGraphNode;

	public String owner;
	public String name;
	public String desc;

	public void setClassGraphNode(ClassGraphNode classGraphNode) {
		this.classGraphNode = classGraphNode;
	}

	public GraphNode GetUnAccessChild() {
		Iterator<GraphNode> it = this.children.iterator();
		while (it.hasNext()) {
			GraphNode n = it.next();
			if (0 == n.accessFlag) {
				return n;
			}
		}
		return null;
	}

	public GraphNode GetUnAccessParent() {
		Iterator<GraphNode> it = this.parents.iterator();
		while (it.hasNext()) {
			GraphNode n = it.next();
			if (0 == n.accessFlag) {
				return n;
			}
		}

		return null;
	}

	/*
	 * default, not access yet set to 0, access and not infect set to 1, access
	 * and infect set to 2
	 */
	public int accessFlag;

	public GraphNode() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (null == obj || !this.getClass().equals(obj.getClass())) {
			return false;
		}

		final GraphNode other = (GraphNode) obj;
		if (this.owner.equals(other.owner) && this.name.equals(other.name)
				&& this.desc.equals(other.desc)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.Format2Pattern().hashCode();
	}

	@Override
	public String toString() {
		return this.Format2Pattern();
	}

	public String GetParentString() {
		if (null == this.parents) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (GraphNode n : this.parents) {
			sb.append(String.format("[%s]", n.toString()));
		}

		return sb.toString();

	}

	public String GetChildrenString() {
		if (null == this.parents) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (GraphNode n : this.children) {
			sb.append(String.format("[%s]", n.toString()));
		}

		return sb.toString();

	}

	public GraphNode(String o, String n, String d) {
		this.owner = o;
		this.name = n;
		this.desc = d;
	}

	public GraphNode(String o, String n, String d, ClassGraphNode c) {
		this.owner = o;
		this.name = n;
		this.desc = d;
		this.classGraphNode = c;
	}

	public String Format2Pattern() {
		char retValue = this.desc.charAt(0);
		String params = this.desc.substring(1);
		return String.format("%s->%s(%s)%s", this.owner, this.name, params,
				retValue);
	}
}
