package be.fgov.caamihziv.services.quickmon.domain.assertions;

import java.util.Optional;

/**
 * Created by gs on 24.04.17.
 */
public interface Assertion<T> {

    AssertionBuilder toBbuilder();

    public class Result {
        
        private boolean success;
        private Optional<String> message;
        private boolean critical;

        protected Result(boolean value, boolean critical, Optional<String> message) {
            this.success = value;
            this.message = message;
            this.critical = critical;
        }

        public boolean isSuccess() {
            return success;
        }

        public boolean isCritical() {
            return critical;
        }

        public String getMessage() {
            return message.orElse("no message");
        }

        @Override
        public String toString() {
            return "Result{" +
                    "success=" + success +
                    ", message=" + message +
                    ", critical=" + critical +
                    '}';
        }
    }
    
    boolean isCritical();
    Result execute(T target);

}
