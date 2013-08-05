package m.k.callgraph;

import java.util.List;

/**
 * @author kun_ma
 *
 */
public class Binder {
	
	public GraphNode ThreadBinder(GraphNode startMethod, GraphNode runMethod) {		
		startMethod.children.add(runMethod);
		runMethod.parents.add(startMethod);
		
		return startMethod;
	}
	
	public GraphNode ExtendBinder(GraphNode node, GraphNode superNode) {
		superNode.children.add(node);
		node.parents.add(superNode);
		
		return node;		
	}
	
	public GraphNode InterfaceBinder(GraphNode node, List<GraphNode> interfaceNodes) {
		for (GraphNode interfaceNode : interfaceNodes) {
			interfaceNode.children.add(node);
			node.parents.add(interfaceNode);
		}		
		return node;
	}
}
