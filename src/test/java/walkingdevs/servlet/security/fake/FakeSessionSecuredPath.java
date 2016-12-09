package walkingdevs.servlet.security.fake;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;
import walkingdevs.iter.Iter;
import walkingdevs.servlet.security.api.Permissions;
import walkingdevs.servlet.security.api.Type;
import walkingdevs.servlet.security.spi.SecuredPath;

public class FakeSessionSecuredPath implements SecuredPath {
    public Path<String> path() {
        return Path.mk();
    }

    public Type type() {
        return Type.Session;
    }

    public Permissions permissions() {
        return Permissions.mk(
            "anonymous",
            Path.mk("/hello-my-anonymous-friend"),
            Method.GET
        ).add(Permissions.mk(
            "admin",
            Path.mk("/admin"),
            Iter.mk(Method.GET)
        ));
    }
}