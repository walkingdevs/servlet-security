package walkingdevs.umbrella.fake;

import walkingdevs.umbrella.api.AuthResult;
import walkingdevs.umbrella.spi.AuthProvider;

class FakeAuthProvider implements AuthProvider {
    public AuthResult get(String username, String password) {
        if (!"top-secret".equals(password)) {
            return AuthResult.notOk("You don't know nothing " + username);
        }
        return AuthResult.ok();
    }
}