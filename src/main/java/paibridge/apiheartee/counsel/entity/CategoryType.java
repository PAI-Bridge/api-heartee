package paibridge.apiheartee.counsel.entity;

public enum CategoryType {
    GL(Values.GL), DT(Values.DT), BU(Values.BU);

    private CategoryType(String value) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(value)) {
            throw new IllegalArgumentException("Incorrect use of ELanguage");
        }
    }

    public static Boolean validateDtype(String dtype) {
        if (dtype.equals(Values.GL) || dtype.equals(Values.DT) || dtype.equals(Values.BU)) {
            return true;
        }
        return false;
    }

    public static class Values {

        public static final String GL = "GL";
        public static final String DT = "DT";
        public static final String BU = "BU";
    }
}
