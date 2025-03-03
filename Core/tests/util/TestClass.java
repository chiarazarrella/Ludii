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
	
}
