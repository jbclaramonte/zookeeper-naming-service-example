package fr.xebia.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/taxapi")
public class TaxRest {

    private static final double TVA = 0.2;

    @RequestMapping(value = "/ttc", method = RequestMethod.GET)
    public double add(@RequestParam("ht") double ht) {
        return ht * (1 + TVA);
    }

}
