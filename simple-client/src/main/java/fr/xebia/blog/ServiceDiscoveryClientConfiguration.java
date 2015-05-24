package fr.xebia.blog;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceDiscoveryClientConfiguration {

    @Bean(initMethod = "start", destroyMethod = "close")
    public ServiceDiscovery<String> discovery() {
        JsonInstanceSerializer<String> serializer =
                new JsonInstanceSerializer<String>(String.class);

        return ServiceDiscoveryBuilder.builder(String.class)
                .client(curator())
                .basePath("services")
                .serializer(serializer)
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curator() {
        return CuratorFrameworkFactory.newClient("localhost", new ExponentialBackoffRetry(1000, 3));
    }

}
