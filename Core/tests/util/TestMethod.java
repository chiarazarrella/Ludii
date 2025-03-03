package util;

import java.util.List;

public class TestMethod {
	
	private static int id = -1;
	private String name;
	private List<String> parameters;
	private boolean isChecked;
	private boolean isPassed;
	
	public TestMethod(String name, List<String> parameters) {
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

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
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
