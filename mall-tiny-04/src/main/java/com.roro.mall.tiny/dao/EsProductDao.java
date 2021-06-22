package com.roro.mall.tiny.dao;

import com.roro.mall.tiny.nosql.elasticsearch.document.EsProduct;

import java.util.List;

public interface EsProductDao {

    List<EsProduct> getAllEsProductList(Object o);
}
