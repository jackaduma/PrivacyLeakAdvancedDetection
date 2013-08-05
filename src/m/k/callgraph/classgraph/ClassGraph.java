package m.k.callgraph.classgraph;

import java.util.HashSet;
import java.util.Set;

/**
 * @author kun
 * 
 */
public class ClassGraph {
	public Set<ClassGraphNode> graph = new HashSet<ClassGraphNode>();

	public void Merge() {
		if (null == this.graph || this.graph.isEmpty()) {
			return;
		}

		for (ClassGraphNode node : this.graph) {
		}
	}
}
