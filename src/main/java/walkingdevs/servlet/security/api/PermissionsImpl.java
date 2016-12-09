package walkingdevs.servlet.security.api;

import walkingdevs.val.Val;

import java.util.*;

class PermissionsImpl implements Permissions {
    public Permissions add(Permission permission) {
        permissions.add(
            Val.isNull(permission, "permission").get()
        );
        return this;
    }

    public Permissions add(Permissions other) {
        for (Permission permission : Val.isNull(other, "other").get()) {
            add(permission);
        }
        return this;
    }

    @Override
    public Iterator<Permission> iterator() {
        return permissions.iterator();
    }

    private final Set<Permission> permissions = new HashSet<>();
}