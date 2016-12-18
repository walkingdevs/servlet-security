## Simple Session and Basic Auth security for the ~~fucking~~ Servlet API

    <dependency>
        <groupId>walkingdevs</groupId>
        <artifactId>umbrella</artifactId>
        <version>0.1</version>
    </dependency>
    
## How to use?

**Implement SPI-s**

    class FakeAuthProvider implements AuthProvider {
        public AuthResult get(String username, String password) {
            if (!"top-secret".equals(password)) {
                return AuthResult.notOk("You don't know nothing " + username);
            }
            return AuthResult.ok();
        }
    }

    class FakeAuthoritiesProvider implements AuthoritiesProvider {
        public Authorities get(String username) {
            if (username.equals("batman")) {
                return Authorities.mk(
                    "superhero",
                    "bat",
                    "man"
                );
            }
            if (username.equals("superman")) {
                return Authorities.mk(
                    "superhero",
                    "super",
                    "man"
                );
            }
            if (username.equals("rocky")) {
                return Authorities.mk(
                    "crazy",
                    "army-hater",
                    "man"
                );
            }
            return Authorities.mk();
        }
    }

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
    
**LogIn**

For Session, somewhere in code:

    umbrella.logIn(
        "username",
        "password"
    );

For Basic, just pass Username and Password headers with request:

    curl http://host:port/resource -H "Username: username" -H "Password: password"

Not super-secure? Well, Basic security is not for security at all.