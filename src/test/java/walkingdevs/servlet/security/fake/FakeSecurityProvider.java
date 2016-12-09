package walkingdevs.servlet.security.fake;

import walkingdevs.servlet.security.spi.SecurityProvider;

import java.util.HashSet;
import java.util.Set;

class FakeSecurityProvider implements SecurityProvider {
    public void authenticate(String username, String password) {
        if (!"fake".equals(password)) {
            throw new RuntimeException("WTF?");
        }
    }

    public void login(String username) {
        // Do nothing
    }

    public Set<String> authorities(String username) {
        if (username.equals("batman")) {
            return new HashSet<>();
        }
        Set<String> res = new HashSet<>();
        res.add("admin");
        return res;
    }
}