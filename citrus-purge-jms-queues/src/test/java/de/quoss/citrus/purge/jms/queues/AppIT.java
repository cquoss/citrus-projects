package de.quoss.citrus.purge.jms.queues;

import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.testng.spring.TestNGCitrusSpringSupport;
import org.testng.annotations.Test;

public class AppIT extends TestNGCitrusSpringSupport {

    @CitrusTest
    @Test
    public void testApp() {
        run
    }

}
