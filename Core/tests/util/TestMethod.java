package util;

import java.util.List;

public class TestMethod {
	
	private static int id = -1;
	private String name;
	private List<Pair<String, Class<?>>> parameters;
	private boolean isChecked;
	private boolean isPassed;
	
	@SuppressWarnings("javadoc")
	public TestMethod(String name, List<Pair<String, Class<?>>> parameters) {
		TestMethod.id = id + 1;
		this.name = name;
		this.parameters = parameters;
		this.isChecked = false;
		this.isPassed = false;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Pair<String, Class<?>>> getParameters() {
		return parameters;
	}

	public void setParameters(List<Pair<String, Class<?>>> parameters) {
		this.parameters = parameters;
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
