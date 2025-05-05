package model;

public class TestParameter {
	
    private final Class<?> type;
    private String value;
    private final String defaultValue;
    private final String name;
    
    public TestParameter(String name, Class<?> type, String value) {
    	
    	this.name = name;
        this.type = type;
        this.value = value;
        this.defaultValue = value;

    }

    public Class<?> getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public boolean isDefault() {
    	return defaultValue != null;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getQualifiedName() {
    	return this.type.getName();
    }
    
    public void reset() {
    	this.value = this.defaultValue;
    }
    
}
