package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 
 */
public class TestClass {
	
	private final String packageName;
	//private HashMap<Integer, TestMethod> methods;
	private List<TestMethod> methods;
	private final String name;
	
	
	
	public TestClass(String packageName, String name) {
		this.packageName = packageName;
		//this.methods = new HashMap<Integer, TestMethod>();
		this.methods = new ArrayList<>();
		this.name = name;
	}

	public String getPackageName() {
		return this.packageName;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public TestMethod getMethod(int id) {
		
		for(TestMethod method: methods) {
			if(method.getId() == id) {
				return method;
			}
		}
		
		return null;
	}
	
	public void addMethod(TestMethod method) {
		//methods.put(TestMethod.getId(method.getName()), method);
		methods.add(method);
	}
	
	public List<TestMethod> getMethods(){
		return this.methods;
	}
	
	/*public HashMap<Integer, TestMethod> getMethods(){
		return this.methods;
	}*/
	
	
	public String getFullyQualifiedName() {
		
		return packageName.toLowerCase() + "." + name;
	}
	
	public boolean hasAtLeastOneMethodSelected() {
		
		for(TestMethod method: methods) {
			
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
		for(TestMethod m: this.methods) {
			m.reset();
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TestClass: ").append(this.packageName).append("\n");
		sb.append("Methods:\n");

		for (TestMethod m : this.methods) {
			sb.append(m.toString()).append("\n");
		}

		return sb.toString();
	}
	
	public boolean hasStaticTests(String category) {
		
		for (TestMethod m : this.methods) {
			if (m.isStatic() && packageName.equals(category))
				return true;
		}
		
		return false;
	}
	
	public boolean hasDynamicTests(String category) {

		for (TestMethod m : this.methods) {
			if (!m.isStatic() && packageName.equals(category))
				return true;
		}

		return false;
	}

}
