package util;

public class TestParameter {
	
    private final Class<?> type;
    private String value;

    public TestParameter(Class<?> type, String defaultValue) {
        this.type = type;
        this.value = defaultValue;
    }

    public Class<?> getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getQualifiedName() {
    	return this.type.getName();
    }
    
}
