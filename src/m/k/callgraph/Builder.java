package m.k.callgraph;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.tree.AbstractInsnNode;
import org.ow2.asmdex.tree.ApplicationNode;
import org.ow2.asmdex.tree.ClassNode;
import org.ow2.asmdex.tree.MethodInsnNode;
import org.ow2.asmdex.tree.MethodNode;

/**
 * @author kun_ma
 * 
 */
public class Builder {
	
	public static int api = Opcodes.ASM4;
	
	public Set<GraphNode> graph = new HashSet<GraphNode>();	

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
	
	public Builder(String dexPath) {
		
		ApplicationReader ar;

		try {
			ar = new ApplicationReader(api, dexPath);
			ApplicationNode an = new ApplicationNode(api);
			ar.accept(an, 0);
			System.out.println("[class count]: "+an.classes.size());

			for (ClassNode cn : an.classes) {
				String owner = cn.name;
				System.out.println("*****"+cn.name+"******");
				System.out.println("[method count]: "+cn.methods.size());
				for (MethodNode mn : cn.methods) {
					String name = mn.name;
					String desc = mn.desc;
					System.out.println(mn.name);
					GraphNode gn = new GraphNode(owner, name, desc);
					if (this.graph.contains(gn)) {
						gn = this.GetGraphNodeByName(gn.toString());
					} else {
						this.graph.add(gn);
					}
					
					if (null==mn.instructions || 0==mn.instructions.size()){
						continue;
					}
					ListIterator<AbstractInsnNode> it = mn.instructions.iterator();
					while (it.hasNext()) {
						AbstractInsnNode ain = it.next();
						
						int opcode = ain.getOpcode();
						if (opcode==Opcodes.INSN_INVOKE_DIRECT
								|| opcode==Opcodes.INSN_INVOKE_INTERFACE
								|| opcode==Opcodes.INSN_INVOKE_STATIC
								|| opcode==Opcodes.INSN_INVOKE_SUPER
								|| opcode==Opcodes.INSN_INVOKE_VIRTUAL
								|| opcode==Opcodes.INSN_INVOKE_DIRECT_RANGE
								|| opcode==Opcodes.INSN_INVOKE_INTERFACE_RANGE
								|| opcode==Opcodes.INSN_INVOKE_STATIC_RANGE
								|| opcode==Opcodes.INSN_INVOKE_SUPER_RANGE
								|| opcode==Opcodes.INSN_INVOKE_VIRTUAL_RANGE) {
							MethodInsnNode min = (MethodInsnNode) ain;
							GraphNode ngn = new GraphNode(min.owner, min.name, min.desc);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String dexPath = "/home/kun/AndroidSamples/virus/SendSMS/00040FC1FD2A77B8BC31C1A540AFED67CBD38F1B_dex/classes.dex";
		Builder b = new Builder(dexPath);

		System.out.println("Graph Node Count: " + b.graph.size());
		for (GraphNode gn : b.graph) {
			if (null == gn) {
				continue;
			}
//			System.out.println(gn.Format2Pattern());
			
			if (gn.toString().equals("Lcom/depositmobi/Main;->activate()V")) {
				System.out.println(gn.GetParentString());
				System.out.println(gn.GetChildrenString());
			}
		}
		
	}

}
