package walkingdevs.umbrella.api;

import walkingdevs.http11.Method;

import java.util.HashSet;
import java.util.Set;

class PermsComposerImpl implements PermsComposer {
    public PermsComposer path(String path) {
        paths.add(path);
        return this;
    }

    public PermsComposer authority(String authority) {
        authorities.add(authority);
        return this;
    }

    public PermsComposer method(Method method) {
        methods.add(method);
        return this;
    }

    public PermsComposer anyMethod() {
        for (Method method : Method.values()) {
            method(method);
        }
        return this;
    }

    public Perms compose() {
        PermsBuilder permsBuilder = PermsBuilder.mk();
        for (String authority : authorities) {
            for (Method method : methods) {
                for (String path : paths) {
                    permsBuilder.add(Perm.mk(
                        rootPath + path,
                        authMethod,
                        authority,
                        method
                    ));
                }
            }
        }
        return permsBuilder.build();
    }

    PermsComposerImpl(String rootPath, AuthMethod authMethod) {
        this.rootPath = rootPath;
        this.authMethod = authMethod;
    }

    private final String rootPath;
    private final AuthMethod authMethod;
    private Set<String> paths = new HashSet<>();
    private Set<String> authorities = new HashSet<>();
    private Set<Method> methods = new HashSet<>();
}