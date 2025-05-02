package model;

public class TestParameter {
	
    private final Class<?> type;
    private String value;
    private final boolean isDefault;
    private final String defaultValue;
    
    public TestParameter(Class<?> type, String defaultValue, boolean isDefault) {
        this.type = type;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.isDefault = isDefault;
    }

    public Class<?> getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
    public boolean isDefault() {
    	return this.isDefault;
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
