package walkingdevs.umbrella.api;

import walkingdevs.val.Val;

public interface PermChecker {
    boolean check(Perm perm);

    static PermChecker mk(Perms perms) {
        return new PermCheckerImpl(
            Val.isNull(
                perms, "perms"
            ).get()
        );
    }
}