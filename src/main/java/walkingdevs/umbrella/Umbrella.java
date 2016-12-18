package walkingdevs.umbrella;

import walkingdevs.umbrella.api.Authorities;

public interface Umbrella {
    String user();

    boolean isIn();

    LogInResult logIn(String username, String password);

    Authorities authorities(String user);
}