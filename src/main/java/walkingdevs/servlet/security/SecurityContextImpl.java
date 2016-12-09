package walkingdevs.servlet.security;

import walkingdevs.data.Kv;
import walkingdevs.data.Kvs;
import walkingdevs.servlet.security.api.SecurityContext;
import walkingdevs.servlet.security.spi.LoginTracker;
import walkingdevs.servlet.security.spi.SecurityProvider;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SecurityContextImpl implements SecurityContext {
    @Inject
    private SecurityProvider securityProvider;

    @Inject @Any
    private Instance<LoginTracker> loginTrackers;

    public String user() {
        return user(sessionId);
    }

    public boolean isIn() {
        return users.has(sessionId);
    }

    public void logIn(String username, String password) {
        try {
            securityProvider.authenticate(username, password);
        } catch (Exception fail) {
            for (LoginTracker loginTracker : loginTrackers) {
                loginTracker.authenticationFailed(username, password, fail);
            }
            throw fail;
        }
        try {
            securityProvider.login(username);
        } catch (Exception fail) {
            for (LoginTracker loginTracker : loginTrackers) {
                loginTracker.loginFailed(username, fail);
            }
            throw fail;
        }
        users.add(
            Kv.mk(sessionId, username)
        );
        for (LoginTracker loginTracker : loginTrackers) {
            loginTracker.loggedIn(username);
        }
    }

    String sessionId() {
        return sessionId;
    }

    void sessionChanged(String newSessionId) {
        sessionId = newSessionId;
    }

    void sessionDestroyed(String destroyedSessionId) {
        users.del(destroyedSessionId);
        for (LoginTracker loginTracker : loginTrackers) {
            loginTracker.loggedOut(
                user(destroyedSessionId)
            );
        }
        if (sessionId.equals(destroyedSessionId)) {
            sessionChanged("");
        }
    }

    private String sessionId = "";
    private final Kvs<String, String> users = Kvs.mk();

    private String user(String sessionId) {
        if (users.get(sessionId).isEmpty()) {
            return "";
        }
        return users.get(sessionId).val();
    }
}