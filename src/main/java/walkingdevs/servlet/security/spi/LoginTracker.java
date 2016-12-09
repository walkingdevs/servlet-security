package walkingdevs.servlet.security.spi;

public interface LoginTracker {
    void loggedIn(String username);

    void loggedOut(String username);

    void authenticationFailed(String username, String password, Throwable source);

    void loginFailed(String username, Throwable source);
}