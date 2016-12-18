package walkingdevs.umbrella.api;

import walkingdevs.val.Val;

import java.util.HashSet;
import java.util.Set;

class PermsBuilderImpl implements PermsBuilder {
    public PermsBuilder add(Perm perm) {
        perms.add(
            Val.isNull(perm, "perm").get()
        );
        return this;
    }

    public PermsBuilder add(Perms other) {
        for (Perm perm : Val.isNull(other, "other").get()) {
            add(perm);
        }
        return this;
    }


    public Perms build() {
        return Perms.mk(perms);
    }

    private final Set<Perm> perms = new HashSet<>();
}