package ir.vasl.chatkitlight.utils.globalEnums;

import androidx.room.TypeConverter;

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

    @TypeConverter
    public static ConversationType getConversationStatus(Integer numeral){
        for(ConversationType ds : values()){
            if(ds.getValue().equals(numeral)){
                return ds;
            }
        }
        return null;
    }

    @TypeConverter
    public static Integer getConversationStatusInt(ConversationType status){

        if(status != null)
            return status.getValue();

        return  null;
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


