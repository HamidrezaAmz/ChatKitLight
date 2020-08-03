package ir.vasl.chatkitlight.utils.globalEnums;

public enum ChatStyleEnum {
    DEFAULT(0),
    ARMAN_VARZESH(1),
    LAWONE(2);

    private int ordinal;

    ChatStyleEnum(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }
}
