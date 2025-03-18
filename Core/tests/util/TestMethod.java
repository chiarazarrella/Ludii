package util;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.reflect.Method;

import annotation.DefaultParameter;

public class TestMethod {
	
	private int id;
	private String name;
	private final Map<String, TestParameter> parameters;
	private boolean isChecked;
	private boolean isPassed;
	private String failureMessage;
	private String duration;
	
	public TestMethod(Method method) {
		
		this.name = method.getName();
		this.id = name.hashCode();
		this.isChecked = false;
		this.isPassed = false;
		this.failureMessage = null;
		this.duration = null;
		this.parameters = new HashMap<String, TestParameter>();
		
		for(Parameter p: method.getParameters()) {
						
			String value = null;
			
			if(p.isAnnotationPresent(DefaultParameter.class)) {
				
				value = p.getAnnotation(DefaultParameter.class).value();
				
			}
			
			parameters.put(p.getName(), new TestParameter(p.getType(), value));
			
			
		}
		
		
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
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
	
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		return this.duration;
	}
	
	public void setFailureMessage(String message) {
		this.failureMessage = message;
	}
	
	public String getFailureMessage() {
		return this.failureMessage;
	}
	
	// used when the user wants to change the default parameter
	public void setValue(String parameter, String value) {
		parameters.get(parameter).setValue(value);
	}
	
	public boolean hasDefaultParameters() {
		return parameters.size() > 1;
	}
	
	public Map<String, TestParameter> getParameters(){
		return parameters;
	}
	
	public String getQualifiedName() {
	    StringBuilder qName = new StringBuilder(getName());

	    if (!parameters.isEmpty()) {
	        qName.append("(");
	        for (TestParameter parameter : parameters.values()) {
	            qName.append(parameter.getQualifiedName()).append(",");
	        }
	        qName.setLength(qName.length() - 1); // Remove last ","
	        qName.append(")");
	    }

	    return qName.toString();
	}
	
}
