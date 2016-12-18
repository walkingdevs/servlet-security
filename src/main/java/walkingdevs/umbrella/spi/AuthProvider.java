package walkingdevs.umbrella.spi;

import walkingdevs.umbrella.api.AuthResult;

public interface AuthProvider {
    AuthResult get(String username, String password);
}