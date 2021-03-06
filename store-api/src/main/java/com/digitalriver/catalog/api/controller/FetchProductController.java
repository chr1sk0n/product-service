package com.digitalriver.catalog.api.controller;

import com.digitalriver.catalog.api.exception.ProductException;
import com.digitalriver.catalog.api.service.FetchProductServiceImpl;
import groovy.json.JsonBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/pd")
public class FetchProductController {

    @Resource
    protected FetchProductServiceImpl service;

    @RequestMapping(method = RequestMethod.GET, value = "/{locale}/{productID}", produces = "application/json")
    public String get(@PathVariable String productID, @PathVariable String locale) throws ProductException {
        return new JsonBuilder(service.get(productID, locale)).toPrettyString();
    }

}
