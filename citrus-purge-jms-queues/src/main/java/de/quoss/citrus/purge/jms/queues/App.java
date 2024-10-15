package de.quoss.citrus.purge.jms.queues;

import jakarta.jms.*;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.jms.client.ActiveMQQueueConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.IllegalStateException;
import java.util.Enumeration;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        EmbeddedActiveMQ embeddedActiveMQ = new EmbeddedActiveMQ();
        try {
            LOGGER.info("Start embedded broker.");
            embeddedActiveMQ.start();
        } catch (final Exception e) {
            throw new IllegalStateException("Error starting embedded active-mq: " + e.getMessage(), e);
        }
        LOGGER.info("Create queue connection factory.");
        try (ActiveMQQueueConnectionFactory connectionFactory = new ActiveMQQueueConnectionFactory();
             final JMSContext context = connectionFactory.createContext("admin", "admin")) {
            LOGGER.info("Write text message to queue 'queue'.");
            final TextMessage message = context.createTextMessage();
            message.setText("Hello World");
            final JMSProducer producer = context.createProducer();
            final Queue queue = context.createQueue("queue");
            producer.send(queue, message);
            try (final QueueBrowser browser = context.createBrowser(queue)) {
                LOGGER.info("Peek into queue until message is gone.");
                Enumeration<Message> messages = browser.getEnumeration();
                LOGGER.info("Looping ");
                while (messages.hasMoreElements()) {
                    messages = browser.getEnumeration();
                    Thread.sleep(5000L);
                }
                LOGGER.info("Message was consumed.");
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (final JMSException e) {
            throw new IllegalStateException("Error doing stuff: " + e.getMessage(), e);
        }
        try {
            LOGGER.info("Stop embedded broker.");
            embeddedActiveMQ.stop();
        } catch (final Exception e) {
            throw new IllegalStateException("Error stopping embedded active-mq: " + e.getMessage(), e);
        }
        LOGGER.info("Stop application.");
    }
}
