package de.quoss.citrus.purge.jms.queues;

import org.apache.activemq.artemis.jms.client.ActiveMQQueueConnectionFactory;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.context.TestContext;
import org.citrusframework.exceptions.CitrusRuntimeException;
import org.citrusframework.jms.endpoint.JmsEndpoint;
import org.citrusframework.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.citrusframework.jms.actions.PurgeJmsQueuesAction.Builder.purgeQueues;
import static org.citrusframework.actions.ReceiveMessageAction.Builder.receive;

@ComponentScan
public class AppIT extends TestNGCitrusSpringSupport {

    private static final ActiveMQQueueConnectionFactory CONNECTION_FACTORY = new ActiveMQQueueConnectionFactory();

    @Autowired
    JmsEndpoint endpoint;

    @Autowired
    TestContext context;

    @CitrusTest
    @Test
    public void testApp() {
        run(receive(endpoint).validate((message, context) -> {
            if (!"Hello World".equals(message.getPayload(String.class))) {
                throw new CitrusRuntimeException("Error validating message payload.");
            }
        }));
    }

    @AfterMethod
    public void tearDown() {
        run(purgeQueues().queue("queue").connectionFactory(CONNECTION_FACTORY));
    }

}
