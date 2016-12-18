package walkingdevs.umbrella.fake;

import walkingdevs.http11.Method;
import walkingdevs.umbrella.api.Perms;
import walkingdevs.umbrella.api.PermsBuilder;
import walkingdevs.umbrella.api.PermsComposer;
import walkingdevs.umbrella.spi.PermsProvider;

class FakePermsProvider implements PermsProvider {
    public Perms get() {
        return PermsBuilder.mk()
            .add(PermsComposer.session("/")
                .authority("anonymous")
                .path("/anonymous")
                .method(Method.GET)
                .compose()
            )
            .add(PermsComposer.session("/superheroes-only")
                .authority("superhero")
                .path("/")
                .method(Method.GET)
                .method(Method.POST)
                .compose()
            )
            .add(PermsComposer.session("/superman")
                .authority("super")
                .path("/")
                .method(Method.GET)
                .compose()
            )
            // Basic
            .add(PermsComposer.basic("/basic/")
                .authority("anonymous")
                .path("/anonymous")
                .method(Method.GET)
                .compose()
            )
            .add(PermsComposer.basic("/basic/superheroes-only")
                .authority("superhero")
                .path("/")
                .method(Method.GET)
                .method(Method.POST)
                .compose()
            )
            .add(PermsComposer.basic("/basic/superman")
                .authority("super")
                .path("/")
                .method(Method.GET)
                .compose()
            ).build();
    }
}