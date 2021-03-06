package com.digitalriver.catalog.api.mapper;

import com.digitalriver.catalog.api.mapper.type.ClobXMLTypeHandler;
import com.digitalriver.catalog.api.mapper.type.VarCharXMLTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public interface ProductMapper {

    @Select({"WITH pid AS (",
             "  SELECT product_id FROM cat_product WHERE product_id = #{productID} and parent_product_id is null",
             "  UNION",
             "  SELECT parent_product_id as product_id FROM cat_product WHERE product_id = #{productID}",
             ")",
             "SELECT product_id,",
             "       external_reference_id,",
             "       parent_product_id,",
             "       states ",
             "  FROM CAT_PRODUCT ",
             " WHERE product_id in (select product_id from pid)"})
    @ResultType(HashMap.class)
    @Results({
        @Result(property = "STATES", column = "STATES", javaType = Map.class, jdbcType = JdbcType.VARCHAR, typeHandler = VarCharXMLTypeHandler.class)
    })
    Map<String, ?> getMetadata(@Param("productID") String productID);

    @Select("WITH pid AS (" +
            "  SELECT #{productID} as product_id FROM dual " +
            "  UNION" +
            "  SELECT product_id as product_id FROM cat_product WHERE parent_product_id = #{productID}" +
            ")" +
            "SELECT PD.product_id, " +
            "       PD.product_data_id, " +
            "       PD.locale, " +
            "       PD.display_name, " +
            "       NVL(PI.PARENT_PRODUCT_ID, '') BASE_PRODUCT_ID, " +
            "       (CASE WHEN PI.parent_product_id is null THEN '0' ELSE '1' END) IS_VARIATION, " +
            "       PD.short_description, " +
            "       TO_CHAR(PD.long_description) as long_description, " +
            "       PD.thumbnail, " +
            "       PD.mfr_partnumber, " +
            "       PD.sku, " +
            "       PD.detail_image, " +
            "       PD.is_orderable, " +
            "       PD.keywords, " +
            "       XMLTYPE(PD.extended_attributes) as extended_attributes " +
            "  FROM CAT_PRODUCT_DATA PD JOIN CAT_PRODUCT PI ON PD.PRODUCT_ID = PI.PRODUCT_ID" +
            " WHERE PD.product_id in (SELECT product_id FROM pid)" +
            "   and PD.version = #{version} " +
            "   and PD.locale = #{locale}")
    @ResultType(HashMap.class)
    @Results({
        @Result(property = "IS_VARIATION", column = "IS_VARIATION", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "IS_ORDERABLE", column = "IS_ORDERABLE", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "EXTENDED_ATTRIBUTES", column = "EXTENDED_ATTRIBUTES", javaType = Map.class, jdbcType = JdbcType.CLOB, typeHandler = ClobXMLTypeHandler.class)
    })
    List<Map<String, ?>> getDisplayData(@Param("productID") String productID, @Param("version") Integer version, @Param("locale") String locale);

    @Select("WITH pid AS (" +
            "  SELECT #{productID} as product_id FROM dual " +
            "  UNION" +
            "  SELECT product_id as product_id FROM cat_product WHERE parent_product_id = #{productID}" +
            ")" +
            "SELECT PD.product_id, " +
            "       PD.product_data_id, " +
            "       PD.locale, " +
            "       PD.display_name, " +
            "       NVL(PI.PARENT_PRODUCT_ID, '') BASE_PRODUCT_ID, " +
            "       (CASE WHEN PI.parent_product_id is null THEN '0' ELSE '1' END) IS_VARIATION, " +
            "       PD.short_description, " +
            "       TO_CHAR(PD.long_description) as long_description, " +
            "       PD.thumbnail, " +
            "       PD.mfr_partnumber, " +
            "       PD.sku, " +
            "       PD.detail_image, " +
            "       PD.is_orderable, " +
            "       PD.keywords, " +
            "       XMLTYPE(PD.extended_attributes) as extended_attributes " +
            "  FROM CAT_PRODUCT_DATA PD JOIN CAT_PRODUCT PI ON PD.PRODUCT_ID = PI.PRODUCT_ID" +
            " WHERE PD.product_id in (SELECT product_id FROM pid)" +
            "   and PD.version = #{version} ")
    @ResultType(HashMap.class)
    @Results({
        @Result(property = "IS_VARIATION", column = "IS_VARIATION", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "IS_ORDERABLE", column = "IS_ORDERABLE", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "EXTENDED_ATTRIBUTES", column = "EXTENDED_ATTRIBUTES", javaType = Map.class, jdbcType = JdbcType.CLOB, typeHandler = ClobXMLTypeHandler.class)
    })
    List<Map<String, ?>> getAllLocaleDisplayData(@Param("productID") String productID, @Param("version") Integer version);

    @Select({"SELECT PD.product_id, ",
            "       PD.product_data_id, ",
            "       PD.locale, ",
            "       PD.display_name, ",
            "       NVL(PI.PARENT_PRODUCT_ID, '') BASE_PRODUCT_ID, ",
            "       (CASE WHEN PI.parent_product_id is null THEN '0' ELSE '1' END) IS_VARIATION, ",
            "       PD.short_description, ",
            "       TO_CHAR(PD.long_description) as long_description, ",
            "       PD.thumbnail, ",
            "       PD.mfr_partnumber, ",
            "       PD.sku, ",
            "       PD.detail_image, ",
            "       PD.is_orderable, ",
            "       PD.keywords, ",
            "       XMLTYPE(PD.extended_attributes) as extended_attributes ",
            "  FROM CAT_PRODUCT_DATA PD JOIN CAT_PRODUCT PI ON PD.PRODUCT_ID = PI.PRODUCT_ID",
            " WHERE PD.product_data_id = #{productDataID}"})
    @ResultType(HashMap.class)
    @Results({
        @Result(property = "IS_VARIATION", column = "IS_VARIATION", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "IS_ORDERABLE", column = "IS_ORDERABLE", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "EXTENDED_ATTRIBUTES", column = "EXTENDED_ATTRIBUTES", javaType = Map.class, jdbcType = JdbcType.CLOB, typeHandler = ClobXMLTypeHandler.class)
    })
    Map<String, ?> getDisplayDataByID(@Param("productDataID") String productDataID);

    @Select({"with base_pid as (",
            "    select p.product_id",
            "      from cat_product p left join cat_catalog_product c on p.product_id = c.product_id and c.catalog_id = #{catalogID}",
            "     where c.catalog_id is not null",
            "       and p.parent_product_id is null",
            "),",
            "deployed as (",
            "    select v.product_id, ",
            "           v.version",
            "      from cat_product_version v",
            "     where v.product_id in (select product_id from base_pid) and v.state_id = '53'",
            ") ",
            "select d.product_data_id, ",
            "       #{catalogID} as catalog_id ",
            " from cat_product_data d left join cat_product p on d.product_id = p.product_id, ",
            "      deployed y ",
            "where p.product_id = y.product_id ",
            "  AND d.version = y.version ",
            "union ",
            "select d.product_data_id, ",
            "       #{catalogID} as catalog_id ",
            "  from cat_product_data d left join cat_product p on d.product_id = p.product_id, ",
            "       deployed y ",
            " where p.parent_product_id = y.product_id ",
            "   AND d.version = y.version "})
    List<String> getProductDataIDByCatalog(@Param("catalogID") String catalogID);

    @Select({"with base_pid as (",
             "    select p.product_id",
             "      from cat_product p left join cat_catalog_product c on p.product_id = c.product_id and c.catalog_id = #{catalogID}",
             "     where c.catalog_id is not null",
             "       and p.parent_product_id is null",
             "),",
             "deployed as (",
             "    select v.product_id, ",
             "           v.version",
             "      from cat_product_version v",
             "     where v.product_id in (select product_id from base_pid) and v.state_id = '53'",
             "),",
             "alldata as (",
             "    select d.product_data_id,",
             "           p.parent_product_id",
             "      from cat_product_data d left join cat_product p on d.product_id = p.product_id,",
             "           deployed y",
             "     where p.product_id = y.product_id",
             "       AND d.version = y.version",
             "     union",
             "    select d.product_data_id,",
             "           p.parent_product_id",
             "      from cat_product_data d left join cat_product p on d.product_id = p.product_id,",
             "           deployed y",
             "     where p.parent_product_id = y.product_id ",
             "       AND d.version = y.version",
             ") ",
             "select D.product_id,",
             "       D.product_data_id,",
             "       D.version,",
             "       #{catalogID} as catalog_id ",
             "       D.locale,",
             "       D.display_name,",
             "       NVL(a.parent_product_id, '') BASE_PRODUCT_ID,",
             "       (CASE WHEN a.parent_product_id is null THEN '0' ELSE '1' END) IS_VARIATION,",
             "       D.short_description,",
             "       TO_CHAR(D.long_description) as long_description,",
             "       D.thumbnail,",
             "       D.mfr_partnumber,",
             "       D.sku,",
             "       D.detail_image,",
             "       D.is_orderable,",
             "       D.keywords,",
             "       XMLTYPE(D.extended_attributes) as extended_attributes " +
             "  from alldata a left join cat_product_data d on d.product_data_id = a.product_data_id"})
    @ResultType(HashMap.class)
    @Results({
        @Result(property = "IS_VARIATION", column = "IS_VARIATION", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "IS_ORDERABLE", column = "IS_ORDERABLE", javaType = Boolean.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "EXTENDED_ATTRIBUTES", column = "EXTENDED_ATTRIBUTES", javaType = Map.class, jdbcType = JdbcType.CLOB, typeHandler = ClobXMLTypeHandler.class)
    })
    List<Map<String, ?>> getProductDataByCatalog(@Param("catalogID") String catalogID);

}
