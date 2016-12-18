package walkingdevs.umbrella.api;

import walkingdevs.val.Val;

import java.util.Set;

public interface Perms extends Iterable<Perm> {
    boolean has(Perm perm);

    static Perms mk(Set<Perm> perms) {
        return new PermsImpl(
            Val.isNull(
                perms, "perms"
            ).get()
        );
    }
}