package com.roro.mall.tiny.dao;

import com.roro.mall.tiny.nosql.elasticsearch.document.EsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsProductDao {

    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
