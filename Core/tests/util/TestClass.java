package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class TestClass {
	
	private String name;
	private HashMap<Integer, TestMethod> methods;
	
	
	
	public TestClass(String name) {
		this.name = name;
		this.methods = new HashMap<Integer, TestMethod>();
	}

	public String getName() {
		return this.name;
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
	    String name = getName();
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

}
