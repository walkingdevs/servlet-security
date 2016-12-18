package walkingdevs.umbrella;

import walkingdevs.Problems;
import walkingdevs.data.Kv;
import walkingdevs.data.Kvs;
import walkingdevs.str.Str;
import walkingdevs.umbrella.api.AuthResult;
import walkingdevs.umbrella.api.Authorities;
import walkingdevs.umbrella.spi.AuthProvider;
import walkingdevs.umbrella.spi.AuthoritiesProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class UmbrellaImpl implements Umbrella {
    @Inject
    private AuthProvider authProvider;

    @Inject
    private AuthoritiesProvider authoritiesProvider;

    public String user() {
        return user(sessionId);
    }

    public boolean isIn() {
        return users.has(sessionId);
    }

    public LogInResult logIn(String username, String password) {
        AuthResult authResult = authProvider.get(username, password);
        if (!authResult.isOk()) {
            return LogInResult.notOk(authResult.message());
        }
        if (Str.mk(sessionId).isBlank()) {
            throw Problems.WTF("For reasons unknown sessionId is blank");
        }
        users.add(
            Kv.mk(sessionId, username)
        );
        return LogInResult.ok();
    }

    public Authorities authorities(String user) {
        return authoritiesProvider.get(user);
    }

    String sessionId() {
        return sessionId;
    }

    void sessionChanged(String newSessionId) {
        sessionId = newSessionId;
    }

    void sessionDestroyed(String destroyedSessionId) {
        users.del(destroyedSessionId);
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