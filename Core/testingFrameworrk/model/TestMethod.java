package model;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.lang.reflect.Method;

import annotation.DefaultParameter;
import org.junit.jupiter.api.Tag;

public class TestMethod {
	
	private int id;
	private String name;
	private Map<String, TestParameter> parameters;
	private final Map<String, TestParameter> defaultParameters;
	private boolean isChecked;
	private boolean isPassed;
	private boolean isStatic;
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
		this.defaultParameters = new HashMap<String, TestParameter>();
		
		
		this.isStatic = Optional.ofNullable(method.getAnnotation(Tag.class))
                .map(Tag::value)
                .map("Static"::equals)
                .orElse(false);
		
					
		for(Parameter p: method.getParameters()) {
						
			String value = null;
			
			// DEFAULT PARAMETER
			if(p.isAnnotationPresent(DefaultParameter.class)) {
				
				value = p.getAnnotation(DefaultParameter.class).value();
			}
			
			parameters.put(p.getName(), new TestParameter(p.getType(), value)); 
			defaultParameters.put(p.getName(), new TestParameter(p.getType(), value));

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
	
	public boolean isStatic() {
		return isStatic;
	}
	
	// used when the user wants to change the default parameter
	public void setValue(String parameter, String value) {
		this.parameters.get(parameter).setValue(value);
	}
	
	public boolean hasDefaultParameters() {
		return defaultParameters.size() > 1;
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
	
	public void reset() {
		
		setDuration(null);
		setFailureMessage(null);
		setPassed(false);
		setChecked(false);
		this.parameters.clear();
		for(Map.Entry<String, TestParameter> entry: defaultParameters.entrySet()){
			parameters.put(entry.getKey(), new TestParameter(entry.getValue().getType(), entry.getValue().getValue()));
		}
	}
	
}
