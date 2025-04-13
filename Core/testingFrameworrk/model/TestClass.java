package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class TestClass {
	
	private String packageName;
	private HashMap<Integer, TestMethod> methods;
	
	
	
	public TestClass(String name) {
		this.packageName = name;
		this.methods = new HashMap<Integer, TestMethod>();
	}

	public String getPackageName() {
		return this.packageName;
	}
	
	
	public TestMethod getMethod(int id) {
		return methods.get(id);
	}
	
	public void addMethod(TestMethod method) {
		methods.put(method.getId(), method);
	}
	
	
	
	public HashMap<Integer, TestMethod> getMethods(){
		return this.methods;
	}
	
	// if the module organization is to be change, then this need to be modified !!!!!!
	public String getClassName() {
	    String name = getPackageName();
	    String formattedName = Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
	    return name.toLowerCase() + "." + formattedName + "Test";
	}
	
	public boolean hasAtLeastOneMethodChecked() {
		
		for(TestMethod method: methods.values()) {
			
			if(method.isChecked())
				return true;
		}
		
		return false;
		
	}
	
	public String getFullyQualifiedNameForMethod(int id) {
		
		TestMethod method = this.getMethod(id);
		return this.getClassName() + "#" + method.getQualifiedName();
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
	
	public boolean hasStaticTest(String category) {
		for (TestMethod m : this.methods.values()) {
			if (m.isStatic() && packageName.equals(category))
				return true;
		}
		
		return false;
	}
	
	public boolean hasDynamicTest(String category) {
		for (TestMethod m : this.methods.values()) {
			if (!m.isStatic() && packageName.equals(category))
				return true;
		}
		return false;
	}

}
