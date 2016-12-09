package walkingdevs.servlet.security.spi;

import walkingdevs.data.Path;
import walkingdevs.servlet.security.api.Permissions;
import walkingdevs.servlet.security.api.Type;

public interface SecuredPath {
    Path<String> path();

    Type type();

    Permissions permissions();
}