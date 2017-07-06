package be.fgov.caamihziv.services.quickmon.domain.samplers.ssh;

/**
 * Created by gs on 10.05.17.
 */
public class ShellResult {

    public static final class Builder {
        private String out;
        private String err;
        private int exitStatus;

        private Builder() {
        }

        public Builder out(String val, boolean append) {
            if (append) {
                if (out == null) {
                    out = "";
                }
                out += val;
            } else {
                out = val;
            }

            return this;
        }

        public Builder err(String val) {
            err = val;
            return this;
        }

        public Builder exitStatus(int val) {
            exitStatus = val;
            return this;
        }

        public ShellResult build() {
            return new ShellResult(this);
        }
    }


    private String out;
    private String err;
    private int exitStatus;

    private ShellResult(Builder builder) {
        out = builder.out;
        err = builder.err;
        exitStatus = builder.exitStatus;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getOut() {
        return out;
    }

    public String getErr() {
        return err;
    }

    public int getExitStatus() {
        return exitStatus;
    }

    @Override
    public String toString() {
        return "ShellResult{" +
                "out='" + out + '\'' +
                ", err='" + err + '\'' +
                ", exitStatus=" + exitStatus +
                '}';
    }
}
