package m.k.callgraph.classgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kun
 * 
 */
public class ClassGraphNode {

	public String superName;
	public String name;
	public List<String> interfacesList = new ArrayList<String>();
	
	public Set<ClassGraphNode> parents = new HashSet<ClassGraphNode>();
	public Set<ClassGraphNode> children = new HashSet<ClassGraphNode>();

	public ClassGraphNode(String name, String superName,
			List<String> interfacesList) {
		this.name = name;
		this.superName = superName;
		this.interfacesList = interfacesList;
	}

//	public ClassGraphNode(String name) {
//		this.name = name;
//	}
//
//	public ClassGraphNode(String name, String superName) {
//		this.name = name;
//		this.superName = superName;
//	}
//
//	public ClassGraphNode(String name, List<String> interfacesList) {
//		this.name = name;
//		this.interfacesList = interfacesList;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (null == obj || this.getClass() != obj.getClass()) {
			return false;
		}

		final ClassGraphNode other = (ClassGraphNode) obj;
		if (other.superName.equals(this.superName)
				&& other.name.equals(this.name)
				&& other.interfacesList.equals(this.interfacesList)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.Format().hashCode();
	}

	@Override
	public String toString() {
		return this.Format();
	}
	
	/*
	 * ClassName extends SuperName implements interface1, interface2, ...
	 */
	public String Format() {
		if (null != this.name) {
			if (null != this.superName) {
				if (null != this.interfacesList && !this.interfacesList.isEmpty()) {
					String interfaces = "";
					for (String interfaceName : this.interfacesList) {
						interfaces = String.format("%s, %s", interfaces, interfaceName);
					}
					return String.format("%s extends %s implements %s", this.name, this.superName, interfaces);
				} else {
					return String.format("%s extends %s", this.name, this.superName);
				}
			} else {
				if (null != this.interfacesList && !this.interfacesList.isEmpty()) {
					String interfaces = "";
					for (String interfaceName : this.interfacesList) {
						interfaces = String.format("%s, %s", interfaces, interfaceName);
					}
					return String.format("%s implements %s", this.name, interfaces);
				} else {
					return this.name;
				}
			}
		} else {
			return null;
		}
	}
}
