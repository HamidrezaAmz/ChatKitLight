package ir.vasl.chatkitlight.utils.globalEnums;

public enum ConversationType {

    UNDEFINE("UNDEFINED", -1),
    CLIENT("CLIENT", 1),
    SERVER("SERVER", 2),
    EMPTY("EMPTY", 3),
    ;

    private String valueStr;
    private Integer value;

    ConversationType(String valueStr, Integer value) {
        this.valueStr = valueStr;
        this.value = value;
    }

    public static ConversationType get(String value) {
        if (value == null) {
            return UNDEFINE;
        }

        ConversationType[] arr$ = values();
        for (ConversationType val : arr$) {
            if (val.valueStr.equalsIgnoreCase(value.trim())) {
                return val;
            }
        }

        return UNDEFINE;
    }

    public static ConversationType get(Integer value) {

        if (value == null) {
            return UNDEFINE;
        }

        ConversationType[] arr$ = values();
        for (ConversationType val : arr$) {
            if (val.value.equals(value)) {
                return val;
            }
        }

        return UNDEFINE;
    }

    public String getValueStr() {
        return valueStr;
    }

    public Integer getValue() {
        return value;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }
}


