package ir.vasl.chatkitlight.utils.globalEnums;

import androidx.room.TypeConverter;

public enum FileType {

    NONE("NONE", -1),
    DOCUMENT("DOCUMENT", 10),
    VIDEO("VIDEO", 100),
    IMAGE("IMAGE", 1000),
    AUDIO("AUDIO", 10000),
    ;

    private String valueStr;
    private Integer value;

    FileType(String valueStr, Integer value) {
        this.valueStr = valueStr;
        this.value = value;
    }

    @TypeConverter
    public static FileType getFileType(Integer numeral){
        for(FileType ds : values()){
            if(ds.getValue().equals(numeral)){
                return ds;
            }
        }
        return null;
    }

    @TypeConverter
    public static Integer getFileTypeInt(FileType status){

        if(status != null)
            return status.getValue();

        return  null;
    }

    public static FileType get(String value) {
        if (value == null) {
            return NONE;
        }

        FileType[] arr$ = values();
        for (FileType val : arr$) {
            if (val.valueStr.equalsIgnoreCase(value.trim())) {
                return val;
            }
        }

        return NONE;
    }

    public static FileType get(Integer value) {

        if (value == null) {
            return NONE;
        }

        FileType[] arr$ = values();
        for (FileType val : arr$) {
            if (val.value.equals(value)) {
                return val;
            }
        }

        return NONE;
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
