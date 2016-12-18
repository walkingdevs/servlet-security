package walkingdevs.umbrella.api;

public interface PermsBuilder {
    PermsBuilder add(Perm perm);

    PermsBuilder add(Perms other);

    Perms build();

    static PermsBuilder mk() {
        return new PermsBuilderImpl();
    }
}