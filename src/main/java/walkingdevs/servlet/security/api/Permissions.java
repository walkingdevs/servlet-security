package walkingdevs.servlet.security.api;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;
import walkingdevs.iter.Iter;

public interface Permissions extends Iterable<Permission> {
    Permissions add(Permission permission);

    Permissions add(Permissions other);

    static Permissions mk() {
        return new PermissionsImpl();
    }

    static Permissions mk(String authority, Path<String> path, Method method) {
        return mk(
            Iter.mk(authority),
            Iter.mk(path),
            Iter.mk(method)
        );
    }

    static Permissions mk(String authority, Path<String> path, Iterable<Method> methods) {
        return mk(
            Iter.mk(authority),
            Iter.mk(path),
            methods
        );
    }

    static Permissions mk(String authority, Iterable<Path<String>> paths, Method method) {
        return mk(
            Iter.mk(authority),
            paths,
            Iter.mk(method)
        );
    }

    static Permissions mk(String authority, Iterable<Path<String>> paths, Iterable<Method> methods) {
        return mk(
            Iter.mk(authority),
            paths,
            methods
        );
    }

    static Permissions mk(Iterable<String> authorities, Path<String> path, Iterable<Method> methods) {
        return mk(
            authorities,
            Iter.mk(path),
            methods
        );
    }

    static Permissions mk(Iterable<String> authorities, Iterable<Path<String>> paths, Method method) {
        return mk(
            authorities,
            paths,
            Iter.mk(method)
        );
    }

    static Permissions mk(Iterable<String> authorities, Iterable<Path<String>> paths, Iterable<Method> methods) {
        Permissions ret = mk();
        for (String authority : authorities) {
            for (Path<String> path : paths) {
                for (Method method : methods) {
                    ret.add(Permission.mk(
                        authority,
                        path,
                        method
                    ));
                }
            }
        }
        return ret;
    }
}