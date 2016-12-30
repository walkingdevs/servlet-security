package walkingdevs.umbrella.api;

import walkingdevs.http11.Method;
import walkingdevs.val.Val;

public interface PermsComposer {
    PermsComposer path(String path);

    PermsComposer authority(String authority);

    PermsComposer method(Method method);

    PermsComposer anyMethod();

    Perms compose();

    static PermsComposer mk(String rootPath, AuthMethod authMethod) {
        return new PermsComposerImpl(
            Val.isBlank(
                rootPath, "rootPath"
            ).get(),
            authMethod
        );
    }

    static PermsComposer session(String rootPath) {
        return mk(
            rootPath,
            AuthMethod.Session
        );
    }

    static PermsComposer basic(String rootPath) {
        return mk(
            rootPath,
            AuthMethod.Basic
        );
    }
}