package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{


    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }


    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT category_id, name, description FROM categories ORDER BY category_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet row = statement.executeQuery())
        {
            while (row.next())
            {
                Category c = new Category();
                c.setCategoryId(row.getInt("category_id"));
                c.setName(row.getString("name"));
                c.setDescription(row.getString("description"));
                categories.add(c);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting categories", e);
        }

        return categories;
    }


    @Override
    public Category getById(int categoryId) {
        String sql = "SELECT category_id, name, description FROM categories WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);

            try (ResultSet row = statement.executeQuery()) {
                if (!row.next()) return null;

                Category c = new Category();
                c.setCategoryId(row.getInt("category_id"));
                c.setName(row.getString("name"));
                c.setDescription(row.getString("description"));
                return c;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting category id=" + categoryId, e);
        }
    }


    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) category.setCategoryId(keys.getInt(1));
            }

            return category;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating category", e);
        }
    }


    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}