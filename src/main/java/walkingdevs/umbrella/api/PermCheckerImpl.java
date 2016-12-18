package walkingdevs.umbrella.api;

class PermCheckerImpl implements PermChecker {
    public boolean check(Perm perm) {
        return perms.has(perm);
    }

    PermCheckerImpl(Perms perms) {
        this.perms = perms;
    }

    private final Perms perms;
}