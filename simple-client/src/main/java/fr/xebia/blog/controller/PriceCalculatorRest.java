package fr.xebia.blog.controller;

import fr.xebia.blog.model.Article;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/ecommerce/api")
public class PriceCalculatorRest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ServiceDiscovery<String> discovery;

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/total", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public double totalPanier(@RequestBody List<Article> articles) throws Exception {
        double totalHT = 0;
        double totalTTC = 0;

        for (Article article : articles) {
            totalHT += article.getPriceHT();
        }

        Collection<ServiceInstance<String>> services = discovery.queryForInstances("simple-tax-api");

        logger.debug("Le service 'simple-tax-api' est fourni par {} instance(s)", services.size());

        if (services.iterator().hasNext()) {
            // Nous utilisons par défaut le premier dans la liste
            ServiceInstance<String> serviceInstance = services.iterator().next();

            logger.debug("version du service: {}", serviceInstance.getPayload());

            String serviceUrl = serviceInstance.buildUriSpec() + "/ttc?ht={ht}";
            Map<String,Double> params = new HashMap<String, Double>();
            params.put("ht", totalHT);
            totalTTC = restTemplate.getForObject(serviceUrl, Double.class, params);
        }

        return totalTTC;
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public void oups() {
        logger.error("error");
    }

}
