package walkingdevs.umbrella.api;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;
import walkingdevs.val.Val;

// Do not pronounce as "sperm"
public interface Perm {
    Path<String> path();

    AuthMethod authenticationMethod();

    String authority();

    Method method();

    static Perm mk(String path, AuthMethod authMethod, String authority, Method method) {
        return new PermImpl(
            Path.mkHttp(
                Val.isNull(path, "path").get()
            ),
            Val.isNull(authMethod, "authMethod").get(),
            Val.isBlank(authority, "authority").get(),
            Val.isNull(method, "method").get()
        );
    }

    static Perm mkSession(String path, String authority, Method method) {
        return mk(
            path,
            AuthMethod.Session,
            authority,
            method
        );
    }

    static Perm mkBasic(String path, String authority, Method method) {
        return mk(
            path,
            AuthMethod.Basic,
            authority,
            method
        );
    }
}