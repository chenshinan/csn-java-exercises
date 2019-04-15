package com.chenshinan.exercises.modelmapper;

/**
 * @author shinan.chen
 * @since 2019/4/15
 */
public class BDTO {
    private String value;
    private String defaultValue;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
