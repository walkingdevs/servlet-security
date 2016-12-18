package walkingdevs.umbrella.api;

import java.util.Iterator;
import java.util.Set;

class PermsImpl implements Perms {
    public boolean has(Perm perm) {
        return perms.contains(perm);
    }

    @Override
    public Iterator<Perm> iterator() {
        return perms.iterator();
    }

    PermsImpl(Set<Perm> perms) {
        this.perms = perms;
    }

    private final Set<Perm> perms;
}