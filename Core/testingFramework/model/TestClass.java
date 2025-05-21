package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 
 */
public class TestClass {
	
	private final String packageName;
	private List<TestMethod> methods;
	private final String name;
	
	
	
	public TestClass(String packageName, String name) {
		this.packageName = packageName;
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
		methods.add(method);
	}
	
	public List<TestMethod> getMethods(){
		return this.methods;
	}
	
	public List<TestMethod> getSelectedMethods() {
	    return methods.stream()
	                  .filter(TestMethod::isSelected)
	                  .collect(Collectors.toList());
	}

	public String getFullyQualifiedName() {
		
		return packageName.toLowerCase() + "." + name;
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
