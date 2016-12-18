package walkingdevs.umbrella;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.net.URL;

@RunWith(Arquillian.class)
abstract class AbstractTest extends Assert {
    @ArquillianResource
    URL url;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackages(true, "walkingdevs.umbrella")
            .addAsResource("beans.xml", "META-INF/beans.xml");
    }
}