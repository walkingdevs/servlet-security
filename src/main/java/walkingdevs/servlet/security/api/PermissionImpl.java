package walkingdevs.servlet.security.api;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;

class PermissionImpl implements Permission {
    public String authority() {
        return authority;
    }

    public Path<String> path() {
        return path;
    }

    public Method method() {
        return method;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Permission)) {
            return false;
        }
        Permission other = (Permission) obj;
        return authority().equals(other.authority())
            && path().equals(other.path())
            && method().equals(other.method());
    }

    @Override
    public int hashCode() {
        int result = authority.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }

    PermissionImpl(String authority, Path<String> path, Method method) {
        this.authority = authority;
        this.path = path;
        this.method = method;
    }

    private final String authority;
    private final Path<String> path;
    private final Method method;
}