package model;

import java.util.HashMap;


/**
 * 
 */
public class TestClass {
	
	private final String packageName;
	private HashMap<Integer, TestMethod> methods;
	private final String name;
	
	
	
	public TestClass(String packageName, String name) {
		this.packageName = packageName;
		this.methods = new HashMap<Integer, TestMethod>();
		this.name = name;
	}

	public String getPackageName() {
		return this.packageName;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public TestMethod getMethod(int id) {
		return methods.get(id);
	}
	
	public void addMethod(TestMethod method) {
		methods.put(TestMethod.getId(method.getName()), method);
	}
	
	
	
	public HashMap<Integer, TestMethod> getMethods(){
		return this.methods;
	}
	
	// if the module organization is to be change, then this need to be modified !!!!!!
	// retrieval example: board.BoardTest
	public String getFullyQualifiedName() {
		
		return packageName.toLowerCase() + "." + name;
	}
	
	public boolean hasAtLeastOneMethodChecked() {
		
		for(TestMethod method: methods.values()) {
			
			if(method.isSelected())
				return true;
		}
		
		return false;
		
	}
	
	public String getFullyQualifiedNameForMethod(int id) {
		
		TestMethod method = this.getMethod(id);
		return this.getFullyQualifiedName() + "#" + method.getQualifiedName();
	}
	
	public void reset() {
		for(TestMethod m: this.methods.values()) {
			m.reset();
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TestClass: ").append(this.packageName).append("\n");
		sb.append("Methods:\n");

		for (TestMethod m : this.methods.values()) {
			sb.append(m.toString()).append("\n");
		}

		return sb.toString();
	}
	
	public boolean hasStaticTests(String category) {
		
		for (TestMethod m : this.methods.values()) {
			if (m.isStatic() && packageName.equals(category))
				return true;
		}
		
		return false;
	}
	
	public boolean hasDynamicTests(String category) {

		for (TestMethod m : this.methods.values()) {
			if (!m.isStatic() && packageName.equals(category))
				return true;
		}

		return false;
	}

}
