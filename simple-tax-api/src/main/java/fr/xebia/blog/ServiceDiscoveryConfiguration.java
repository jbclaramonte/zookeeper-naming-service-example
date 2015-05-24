package fr.xebia.blog;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceDiscoveryConfiguration implements CommandLineRunner{

    @Autowired
    ServiceDiscovery<String> discovery;

    /**
     * serverPort est fourni au démarrage en ligne de commande
     */
    @Value("${server.port}")
    private int serverPort;

    public void run(String... args) throws Exception {

        ServiceInstance<String> instance =
                ServiceInstance.<String>builder()
                        .name("simple-tax-api")
                        .payload("1.0")
                        .address("localhost")
                        .port(serverPort)
                        .uriSpec(new UriSpec("{scheme}://{address}:{port}/taxapi"))
                        .build();

        discovery.registerService(instance);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curator() {
        return CuratorFrameworkFactory.newClient("localhost", new ExponentialBackoffRetry(1000, 3));
    }

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
}
