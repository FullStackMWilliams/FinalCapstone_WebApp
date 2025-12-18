package org.yearup.data.mysql;

import org.springframework.stereotype.Repository;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
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
    public void addProduct(int userId, int productId)
    {
        String updateSql =
                "UPDATE shopping_cart " +
                        "SET quantity = quantity + 1 " +
                        "WHERE user_id = ? AND product_id = ?";

        String insertSql =
                "INSERT INTO shopping_cart (user_id, product_id, quantity) " +
                        "VALUES (?, ?, 1)";

        try (Connection conn = getConnection())
        {
            // 1) Try to increment existing row
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql))
            {
                updateStmt.setInt(1, userId);
                updateStmt.setInt(2, productId);

                int rowsUpdated = updateStmt.executeUpdate();

                // 2) If nothing was updated, insert new row
                if (rowsUpdated == 0)
                {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql))
                    {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, productId);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error adding product to cart userId=" + userId + " productId=" + productId, e);
        }
    }


    @Override
    public void updateProduct(int userId, int productId, int quantity) {

    }

    @Override
    public void clearCart(int userId) {

    }
}