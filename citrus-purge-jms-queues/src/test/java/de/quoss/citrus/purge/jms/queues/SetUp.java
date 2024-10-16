package de.quoss.citrus.purge.jms.queues;

import org.citrusframework.jms.endpoint.JmsEndpoint;
import org.citrusframework.jms.endpoint.JmsEndpointBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SetUp {

    @Bean
    public JmsEndpoint jmsEndpoint() {
        return new JmsEndpointBuilder()
                .destination("queue")
                .build();
    }

}
