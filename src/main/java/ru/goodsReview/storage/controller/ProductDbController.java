package ru.goodsReview.storage.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import ru.goodsReview.core.db.controller.ProductController;
import ru.goodsReview.core.db.exception.StorageException;
import ru.goodsReview.core.model.Product;
import ru.goodsReview.storage.mapper.ProductMapper;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/*
 *  Date: 17.10.11
 *   Time: 19:04
 *   Author:
 *      Artemij Chugreev
 *      artemij.chugreev@gmail.com
 */
public class ProductDbController implements ProductController {
    private SimpleJdbcTemplate simpleJdbcTemplate;
    private ProductMapper productMapper = new ProductMapper();
    private static final Logger log = Logger.getLogger(ProductDbController.class);

    @Required
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate){
        this.simpleJdbcTemplate = jdbcTemplate;
    }

    public ProductDbController(SimpleJdbcTemplate simpleJdbcTemplate1){
        this.simpleJdbcTemplate = simpleJdbcTemplate1;
    }

    public long addProduct(Product product) throws StorageException {
        try {
            simpleJdbcTemplate.getJdbcOperations().update(
                    "INSERT INTO product (category_id, name, description, popularity) VALUES(?,?,?,?)",
                    new Object[]{product.getCategoryId(), product.getName(), product.getDescription(), product.getPopularity()},
                    new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER});
            long lastId = simpleJdbcTemplate.getJdbcOperations().queryForLong("SELECT LAST_INSERT_ID()");
            return lastId;
        } catch (DataAccessException e) {
            log.error("Error while inserting product (probably not enough permissions): " + product, e);
            throw new StorageException();
        }
    }

    public List<Long> addProductList(List<Product> productList) throws StorageException {
        List<Long> ids = new ArrayList<Long>();
        for (Product product : productList) {
            ids.add(addProduct(product));
        }
        return ids;
    }

    public List<Product> getAllProducts() {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query("SELECT * FROM product", productMapper);
        return products;
    }

    /**
     * Returns specified number of most popular products.
     * @param n Number of products needed.
     * @return Specified number of most popular products.
     */
    public List<Product> getPopularProducts(int n) {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query(
                "SELECT * FROM product ORDER BY popularity DESC LIMIT ?", new Object[]{n}, new int[]{Types.INTEGER},
                productMapper);
        return products;
    }

    /**
     * get n products, ordered by count of reviews on it
     * @param n
     * @return
     */
    public List<Product> getProductsMostComments(int n) {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query(
                "select * from product where id in ( select product_id from review group by product_id order by count(*)) LIMIT ?", new Object[]{n}, new int[]{Types.INTEGER},
                productMapper);
        return products;
    }

    /**
     * get all products, ordered by number of comments
     * @return
     */
    public List<Product> getProductsOrderByCommentsNum() {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query(
                "select * from product where id in ( select product_id from review group by product_id order by count(*))",
                productMapper);
        return products;
    }

    public Product getProductById(long product_id) {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query("SELECT * FROM product WHERE id = ?",
                                                                              new Object[]{product_id},
                                                                              new int[]{Types.INTEGER}, productMapper);
        if (products.size() > 0) {
            return products.get(0);
        }
        return null;
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        List<Product> products = new ArrayList<Product>();
        for (Long id : ids) {
            products.add(getProductById(id));
        }
        return products;
    }

    public Product getProductByName(String product_name) {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query("SELECT * FROM product WHERE name = ?",
                                                                              new Object[]{product_name},
                                                                              new int[]{Types.VARCHAR}, productMapper);
        if (products.size() > 0) {
            return products.get(0);
        }
        return null;
    }

    public List<Product> getProductsByName(String product_name) {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query("SELECT * FROM product WHERE name = ?",
                                                                              new Object[]{product_name},
                                                                              new int[]{Types.VARCHAR}, productMapper);
        return products;
    }

    public List<Product> getProductsByCategoryId(long category_id) {
        List<Product> products = simpleJdbcTemplate.getJdbcOperations().query(
                "SELECT * FROM product WHERE category_id = ?", new Object[]{category_id}, new int[]{Types.INTEGER},
                productMapper);
        return products;
    }
}
