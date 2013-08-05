package m.k;

import java.util.Iterator;
import java.util.LinkedList;

import m.k.callgraph.Builder;
import m.k.callgraph.CG;
import m.k.callgraph.GraphNode;
import m.k.callgraph.Pattern;

/**
 * @author kun_ma
 * 
 */
public class Detector {

	public Builder builder;

	public Detector(String dexPath) {
		this.builder = new Builder(dexPath);
	}

	public GraphNode TaggingSource() {
		for (String source : Pattern.getPatternList(2)) {
			for (GraphNode n : this.builder.graph) {
				if (source.equals(n.toString())) {
					return n;
				}
			}
		}
		return null;
	}

	public GraphNode TaggingSink() {
		for (String sink : Pattern.getPatternList(3)) {
			for (GraphNode n : this.builder.graph) {
				if (sink.equals(n.toString())) {
					return n;
				}
			}
		}
		return null;
	}

	public GraphNode TaggingAction() {
		for (String action : Pattern.getPatternList(1)) {
			for (GraphNode n : this.builder.graph) {
				if (action.equals(n.toString())) {
					return n;
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String dexPath = "/home/kun/AndroidSamples/virus/SendSMS/00040FC1FD2A77B8BC31C1A540AFED67CBD38F1B_dex/classes.dex";
		Detector d = new Detector(dexPath);

		System.out.println("Graph Node Count: " + d.builder.graph.size());

		CG cg = new CG(d.builder.graph, d.builder.graph.size());
		cg.Backward(d.TaggingAction());
		
		System.out.println("*******************Final Result:************************");
		Iterator<LinkedList<GraphNode>> sIt = cg.infectionChainSet.iterator();
		while (sIt.hasNext()) {
			LinkedList<GraphNode> lIt = sIt.next();
			System.out.println(lIt.toString());
		}

	}
}
