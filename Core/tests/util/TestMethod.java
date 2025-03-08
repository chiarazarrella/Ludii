package util;

import java.util.List;

public class TestMethod {
	
	private static int id = -1;
	private String name;
	private List<Pair<String, Class<?>>> paramTypes;
	private List<Pair<String, Object>> paramValue;
	private boolean isChecked;
	private boolean isPassed;
	
	@SuppressWarnings("javadoc")
	public TestMethod(String name, List<Pair<String, Class<?>>> paramTypes, List<Pair<String, Object>> paramValue) {
		TestMethod.id = id + 1;
		this.name = name;
		this.paramTypes = paramTypes;
		this.paramValue = paramValue;
		this.isChecked = false;
		this.isPassed = false;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Pair<String, Class<?>>> getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(List<Pair<String, Class<?>>> paramTypes) {
		this.paramTypes = paramTypes;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isPassed() {
		return isPassed;
	}

	public void setPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}

	
	
	
	
}
