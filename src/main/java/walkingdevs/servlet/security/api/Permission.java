package walkingdevs.servlet.security.api;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;
import walkingdevs.val.Val;

public interface Permission {
    String authority();

    Path<String> path();

    Method method();

    static Permission mk(String authority, Path<String> path, Method method) {
        return new PermissionImpl(
            Val.isBlank(authority, "authority").get(),
            Val.isNull(path, "path").get(),
            Val.isNull(method, "method").get()
        );
    }
}