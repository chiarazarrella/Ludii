package model;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Tag;

import util.DefaultParameter;

public class TestMethod {
	
	private int id;
	private final String name;
	private List<TestParameter> parameters;
	private boolean isSelected;
	private boolean isPassed;
	private boolean isStatic;
	private String failureMessage;
	private String duration;
	
	
	
	public TestMethod(Method method) {
		
		this.name = method.getName();
		this.id = name.hashCode();
		this.isSelected = false;
		this.isPassed = false;
		this.failureMessage = null;
		this.duration = null;
		this.parameters = new LinkedList<TestParameter>();
		
		this.isStatic = Optional.ofNullable(method.getAnnotation(Tag.class))
                .map(Tag::value)
                .map("Static"::equals)
                .orElse(false);
		
					
		for(Parameter p: method.getParameters()) {
		
			if(p.isAnnotationPresent(DefaultParameter.class)) {
				
				String value = p.getAnnotation(DefaultParameter.class).value();
				parameters.add(new TestParameter(p.getName(), p.getType(), value));
				continue;
				
			}
			
			parameters.add(new TestParameter(p.getName(), p.getType(), null));

		}
		
		
	}
	
	
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
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
	
	
	public void setValue(String parameter, String value) {
		for(TestParameter p: parameters) {
			if(p.getName().equals(parameter)){
				p.setValue(value);
				break;
			}
		}
	}
	
	public boolean hasDefaultParameters() {
		
		for(TestParameter parameter : parameters) {
			if(parameter.isDefault()) {
				return true;
			}
		}
		
		return false;
	}
	
	public List<TestParameter> getParameters(){
		return parameters;
	}
	
	
	public String getQualifiedName() {
	    StringBuilder qName = new StringBuilder(getName());

	    if (!parameters.isEmpty()) {
	        qName.append("(");
	        for (TestParameter parameter : parameters) {
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
		setSelected(false);
		
		
		for(TestParameter parameter : parameters) {
			parameter.reset();
		}
		
	}

	
	public String parameterWithoutValue() {
		for(TestParameter param: parameters) {
			if(param.getValue() == null)
				return param.getName();
		}
		return null;
	}
	
}
