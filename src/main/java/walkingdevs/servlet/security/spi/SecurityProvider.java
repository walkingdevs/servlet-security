package walkingdevs.servlet.security.spi;

import java.util.Set;

public interface SecurityProvider {
    // Throw exception if something is wrong
    void authenticate(String username, String password);

    // Check that username can be logged in
    // Throw exception if something is wrong
    void login(String username);

    Set<String> authorities(String username);
}