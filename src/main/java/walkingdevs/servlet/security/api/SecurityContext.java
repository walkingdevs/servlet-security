package walkingdevs.servlet.security.api;

public interface SecurityContext {
    String user();

    boolean isIn();

    void logIn(String username, String password);
}