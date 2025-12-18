package org.yearup.data.mysql;

import org.springframework.stereotype.Repository;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.mysql.MySqlDaoBase;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class MySqlShoppingCartDao extends MySqlDaoBase
        implements ShoppingCartDao {
    private ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();

        String sql = "SELECT product_id, quantity FROM shopping_cart WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");

                    Product product = productDao.getById(productId);
                    if (product == null) continue;

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    // discountPercent stays default 0

                    cart.add(item);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error loading cart for userId=" + userId, e);
        }

        return cart;
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