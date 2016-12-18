package walkingdevs.umbrella;

public interface LogInResult {
    boolean isOk();

    String message();

    static LogInResult mk(boolean ok, String message) {
        return new LogInResult() {
            public boolean isOk() {
                return ok;
            }
            public String message() {
                return message;
            }
        };
    }

    static LogInResult ok() {
        return mk(true, "");
    }

    static LogInResult notOk(String message) {
        return mk(false, message);
    }
}