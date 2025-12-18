package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import javax.sql.DataSource;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase
        implements ShoppingCartDao {
    private ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        return null;
    }

    @Override
    public void addProduct(int userId, int productId) {

    }

    @Override
    public void updateProduct(int userId, int productId, int quantity) {

    }

    @Override
    public void clearCart(int userId) {

    }
}