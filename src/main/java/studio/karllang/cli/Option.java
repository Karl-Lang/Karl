package studio.karllang.cli;

import java.util.Arrays;

public class Option {
    private final Options type;
    private final String value;

    public Option(String value, String strType) throws Exception {
        this.value = value;
        this.type = this.getType(strType);
    }

    public Options getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    private Options getType(String strType) throws Exception {
        for (Options opt : Options.values()) {
            if (Arrays.asList(opt.name).contains(strType)) {
                return opt;
            }
        }

        throw new Exception("Incorrect option: " + strType);
    }
}
