package walkingdevs.umbrella.api;

public interface AuthResult {
    boolean isOk();

    String message();

    static AuthResult mk(boolean ok, String message) {
        return new AuthResult() {
            public boolean isOk() {
                return ok;
            }
            public String message() {
                return message;
            }
        };
    }

    static AuthResult ok() {
        return mk(true, "");
    }

    static AuthResult notOk(String message) {
        return mk(false, message);
    }
}