package com.pdf.parser.amqp;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Author: puyangsky
 * Date: 17/4/1
 * RabbitMQ初始化配置
 */
@Configuration
@PropertySource(value = "classpath:rabbit.properties", ignoreResourceNotFound = true)
public class AmqpConfig {

    @Value("${rabbitmq.host}")
    private String host;
    @Value("${rabbitmq.username}")
    private String username;
    @Value("${rabbitmq.password}")
    private String password;
    @Value("${rabbitmq.port}")
    private int port;
    @Value("${rabbitmq.virtualHost}")
    private String virtualHost;


    public static final String EXCHANGE   = "spring-boot-exchange";
    public static final String ROUTINGKEY = "spring-boot-routingKey";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }

}
