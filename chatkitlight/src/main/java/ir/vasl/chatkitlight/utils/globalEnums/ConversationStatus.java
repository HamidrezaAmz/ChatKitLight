package ir.vasl.chatkitlight.utils.globalEnums;

public enum ConversationStatus {

    UNDEFINE("UNDEFINED", -1),
    SENDING("SENDING", 0),
    FAILED("FAILED", 1),
    SENT("SENT", 2),
    DELIVERED("DELIVERED", 3),
    SEEN("SEEN", 4),
    ;

    private String valueStr;
    private Integer value;

    ConversationStatus(String valueStr, Integer value) {
        this.valueStr = valueStr;
        this.value = value;
    }

    public static ConversationStatus get(String value) {
        if (value == null) {
            return UNDEFINE;
        }

        ConversationStatus[] arr$ = values();
        for (ConversationStatus val : arr$) {
            if (val.valueStr.equalsIgnoreCase(value.trim())) {
                return val;
            }
        }

        return UNDEFINE;
    }

    public static ConversationStatus get(Integer value) {

        if (value == null) {
            return UNDEFINE;
        }

        ConversationStatus[] arr$ = values();
        for (ConversationStatus val : arr$) {
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
