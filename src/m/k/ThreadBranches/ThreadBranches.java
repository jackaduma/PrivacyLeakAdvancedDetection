package m.k.ThreadBranches;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import m.k.callgraph.CG;
import m.k.callgraph.GraphNode;
import m.k.callgraph.Pattern;
import m.k.callgraph.classgraph.ClassGraphNode;

import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.tree.AbstractInsnNode;
import org.ow2.asmdex.tree.ApplicationNode;
import org.ow2.asmdex.tree.ClassNode;
import org.ow2.asmdex.tree.MethodInsnNode;
import org.ow2.asmdex.tree.MethodNode;

/**
 * @author kun
 * 
 */
public class ThreadBranches {
	private String dexPath;

	private List<ThreadBehavior> branches = new ArrayList<ThreadBehavior>();

	private String threadClassName = "Ljava/lang/Thread;";
	private String runnableInterfaceName = "Ljava/lang/Runnable;";
	private String threadRunMethodName = "run";

	private Set<GraphNode> graph = new HashSet<GraphNode>();
	private Set<ClassGraphNode> classGraph = new HashSet<ClassGraphNode>();

	public GraphNode GetGraphNodeByName(String name) {
		Iterator<GraphNode> it = this.graph.iterator();
		while (it.hasNext()) {
			GraphNode gn = it.next();
			if (gn.toString().equals(name)) {
				return gn;
			}
		}
		return null;
	}

	private List<GraphNode> TaggingStart() {
		List<GraphNode> list = new ArrayList<GraphNode>();
		for (GraphNode n : this.graph) {
			try {
				if (null!=n.classGraphNode) {
					if ((null != n.classGraphNode.superName && this.threadClassName.equals(n.classGraphNode.superName))) {
						if (n.name.equals(this.threadRunMethodName)) {
							list.add(n);
						}
					} 
					else if ((null != n.classGraphNode.interfacesList && !n.classGraphNode.interfacesList.isEmpty() 
							&& n.classGraphNode.interfacesList.contains(this.runnableInterfaceName))) {
						if (n.name.equals(this.threadRunMethodName)) {
							list.add(n);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public ThreadBranches(String dexPath) {
		this.dexPath = dexPath;
	}

	public List<ThreadBehavior> getBranches() {
		return this.branches;
	}

	public void BuildClassGraph(ApplicationNode an) {
		try {
			for (ClassNode cn : an.classes) {
				String owner = cn.name;
				String superName = cn.superName;
				List<String> interfacesList = cn.interfaces;

				ClassGraphNode classGraphNode = new ClassGraphNode(owner,
						superName, interfacesList);

				this.classGraph.add(classGraphNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ClassGraphNode GetClassGraphNodeByName(String name) {
		for (ClassGraphNode cgn : this.classGraph) {
			if (name.equals(cgn.name)) {
				return cgn;
			}
		}
		return null;
	}

	public static int api = Opcodes.ASM4;

	public void Build() {
		ApplicationReader ar = null;
		try {
			ar = new ApplicationReader(api, this.dexPath);
			ApplicationNode an = new ApplicationNode(api);
			ar.accept(an, 0);
			System.out.println("[class count]: " + an.classes.size());
			int index = 0;

			// Build class graph
			this.BuildClassGraph(an);

			for (ClassNode cn : an.classes) {
				String owner = cn.name;
				// String superName = cn.superName;
				// List<String> interfacesList = cn.interfaces;

				System.out.println("*["+(++index)+"]*" + cn.name + "******");

				for (MethodNode mn : cn.methods) {
					String name = mn.name;
					String desc = mn.desc;
					// System.out.println(mn.name);
					GraphNode gn = new GraphNode(owner, name, desc,
							this.GetClassGraphNodeByName(owner));
					if (this.graph.contains(gn)) {
						gn = this.GetGraphNodeByName(gn.toString());
					} else {
						this.graph.add(gn);
					}

					if (null == mn.instructions || 0 == mn.instructions.size()) {
						continue;
					}
					ListIterator<AbstractInsnNode> it = mn.instructions
							.iterator();
					while (it.hasNext()) {
						AbstractInsnNode ain = it.next();

						int opCode = ain.getOpcode();
						if (opCode == Opcodes.INSN_INVOKE_DIRECT
								|| opCode == Opcodes.INSN_INVOKE_DIRECT_RANGE
								|| opCode == Opcodes.INSN_INVOKE_INTERFACE
								|| opCode == Opcodes.INSN_INVOKE_INTERFACE_RANGE
								|| opCode == Opcodes.INSN_INVOKE_STATIC
								|| opCode == Opcodes.INSN_INVOKE_STATIC_RANGE
								|| opCode == Opcodes.INSN_INVOKE_SUPER
								|| opCode == Opcodes.INSN_INVOKE_SUPER_RANGE
								|| opCode == Opcodes.INSN_INVOKE_VIRTUAL
								|| opCode == Opcodes.INSN_INVOKE_VIRTUAL_RANGE) {
							MethodInsnNode min = (MethodInsnNode) ain;
							GraphNode ngn = new GraphNode(min.owner, min.name,
									min.desc,
									this.GetClassGraphNodeByName(min.owner));
							if (this.graph.contains(ngn)) {
								ngn = this.GetGraphNodeByName(ngn.toString());
							} else {
								this.graph.add(ngn);
							}
							gn.children.add(ngn);
							ngn.parents.add(gn);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		String dirPath = "/home/kun/AndroidSamples/virus/SendSMS/";
		File fDir = new File(dirPath);
		File[] subFiles = fDir.listFiles();
		for (int i=0; i<subFiles.length; i++) {
			if (!subFiles[i].isDirectory()) {
				continue;
			}
			
			if (!subFiles[i].getName().contains("dex")) {
				continue;
			}
			
			String dexPath = subFiles[i].getAbsolutePath() + "/classes.dex";
			
			ThreadBranches threadBranches = new ThreadBranches(dexPath);
			threadBranches.Build();

			CG cg = new CG(threadBranches.graph, threadBranches.graph.size());

			for (GraphNode gn : threadBranches.TaggingStart()) {
				cg.Forward(gn);
			}
			
			for (LinkedList<GraphNode> graphNodeList : cg.infectionChainSet) {
				System.out.println("****************");
				for (GraphNode node : graphNodeList) {
					System.out.println(node.toString()+"-->");
				}
			}
			
		}
//		String dexPath = "/home/kun/AndroidSamples/virus/PrivacyLeak/0024968320569EFCD6DDEF7232FF13A9F25CF32D_dex/classes.dex";

	}

}
