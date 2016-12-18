package walkingdevs.umbrella.api;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;

class PermImpl implements Perm {
    public Path<String> path() {
        return path;
    }

    public AuthMethod authenticationMethod() {
        return authMethod;
    }

    public String authority() {
        return authority;
    }

    public Method method() {
        return method;
    }

    @Override
    public String toString() {
        return "PermImpl{" +
            "path=" + path +
            ", authMethod=" + authMethod +
            ", authority='" + authority + '\'' +
            ", method=" + method +
            '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Perm)) {
            return false;
        }
        Perm other = (Perm) obj;
        return path().equals(other.path())
            && authenticationMethod().equals(other.authenticationMethod())
            && authority().equals(other.authority())
            && method().equals(other.method());
    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + authority.hashCode();
        result = 31 * result + authMethod.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }

    PermImpl(Path<String> path, AuthMethod authMethod, String authority, Method method) {
        this.path = path;
        this.authMethod = authMethod;
        this.authority = authority;
        this.method = method;
    }

    private final Path<String> path;
    private final AuthMethod authMethod;
    private final String authority;
    private final Method method;
}