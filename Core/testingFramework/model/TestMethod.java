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

/**
 * Represents a single test method with metadata such as parameters,
 * execution status, and result information.
 * 
 * @author Chiara E. Zarrella
 */
public class TestMethod {
	
	private int id;
	private final String name;
	private List<TestParameter> parameters;
	private boolean isSelected;
	private boolean isPassed;
	private boolean isStatic;
	private String failureMessage;
	private String duration;
	
	
	/**
	 * Constructs a TestMethod instance based on a Java reflection Method.
	 * Extracts parameters, static/dynamic tag, and default values (if annotated).
	 * 
	 * @param method The reflected method.
	 */
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
	
	
	/**
	 * Returns the unique identifier of the test method.
	 * 
	 * @return ID of the method.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the method's name.
	 * 
	 * @return Method name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks whether this method has been selected for execution.
	 * 
	 * @return {@code true} if selected, {@code false} otherwise.
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * Sets whether the method is selected for execution.
	 * 
	 * @param isSelected Selection state.
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * Checks whether this method has passed during execution.
	 * 
	 * @return {@code true} if passed, {@code false} otherwise.
	 */
	public boolean isPassed() {
		return isPassed;
	}

	/**
	 * Sets whether the method passed.
	 * 
	 * @param isPassed Result of execution.
	 */
	public void setPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}
	
	/**
	 * Sets the execution duration.
	 * 
	 * @param duration Duration as string.
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * Returns the execution duration.
	 * 
	 * @return Duration string.
	 */
	public String getDuration() {
		return this.duration;
	}
	
	/**
	 * Sets the failure message after a failed execution.
	 * 
	 * @param message Failure message.
	 */
	public void setFailureMessage(String message) {
		this.failureMessage = message;
	}
	
	/**
	 * Returns the failure message if the method failed.
	 * 
	 * @return Failure message or {@code null}.
	 */
	public String getFailureMessage() {
		return this.failureMessage;
	}
	
	/**
	 * Checks whether this method is tagged as static.
	 * 
	 * @return {@code true} if static, {@code false} otherwise.
	 */
	public boolean isStatic() {
		return isStatic;
	}
	
	/**
	 * Assigns a specific value to a named parameter.
	 * 
	 * @param parameter Name of the parameter.
	 * @param value     Value to assign.
	 */
	public void setValue(String parameter, String value) {
		for(TestParameter p: parameters) {
			if(p.getName().equals(parameter)){
				p.setValue(value);
				break;
			}
		}
	}
	
	/**
	 * Checks whether any parameter has a default value.
	 * 
	 * @return {@code true} if at least one parameter has a default value.
	 */
	public boolean hasDefaultParameters() {
		
		for(TestParameter parameter : parameters) {
			if(parameter.isDefault()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the list of parameters of this method.
	 * 
	 * @return List of {@link TestParameter}.
	 */
	public List<TestParameter> getParameters(){
		return parameters;
	}
	
	/**
	 * Returns the fully qualified method name including parameters.
	 * Example: methodName(java.lang.String,int)
	 * 
	 * @return Fully qualified method signature.
	 */
	public String getFullyQualifiedName() {
	    StringBuilder qName = new StringBuilder(getName());

	    if (!parameters.isEmpty()) {
	        qName.append("(");
	        for (TestParameter parameter : parameters) {
	            qName.append(parameter.getFullyQualifiedName()).append(",");
	        }
	        qName.setLength(qName.length() - 1); // Remove last ","
	        qName.append(")");
	    }

	    return qName.toString();
	}
	
	/**
	 * Resets the method's state to default (e.g., deselected, no result, etc.).
	 */
	public void reset() {
		
		setDuration(null);
		setFailureMessage(null);
		setPassed(false);
		setSelected(false);
		
		for(TestParameter parameter : parameters) {
			parameter.reset();
		}
		
	}

	/**
	 * Returns the name of the first parameter without a value, or {@code null} if all are filled.
	 * 
	 * @return Parameter name or {@code null}.
	 */
	public String parameterWithoutValue() {
		for(TestParameter param: parameters) {
		
			if(param.getValue() == null)
				return param.getName();
		}
		return null;
	}
	
}
