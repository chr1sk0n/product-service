package com.digitalriver.catalog.api.service;

import com.digitalriver.catalog.api.exception.ProductException;

import java.util.Map;

public interface ProductService {

    Map<String, ?> get(String productID, String locale) throws ProductException;

}