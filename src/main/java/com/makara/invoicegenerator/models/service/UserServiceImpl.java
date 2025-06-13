package com.makara.invoicegenerator.models.service;
import com.makara.invoicegenerator.models.entity.User;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.jdbc.core.BeanPropertyRowMapper;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.stereotype.Service;import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;@Override

    public void save(User user) {
        String sql = "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEnabled());
    }

    @Override
    public void assignRole(String username, String role) {
        String sql = "INSERT INTO authorities (user_id, authority) " +
                "SELECT id, ? FROM users WHERE username = ?";
        jdbcTemplate.update(sql, role, username);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET username = ?, enabled = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getEnabled(), user.getId());
    }

    @Override
    public void delete(Long id) {
        // Delete related rows in the authorities table
        String deleteAuthoritiesSql = "DELETE FROM authorities WHERE user_id = ?";
        jdbcTemplate.update(deleteAuthoritiesSql, id);
        // Delete the user from the users table
        String deleteUserSql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(deleteUserSql, id);
    }
    @Override
    public List<String> findAllRoles() {
        String sql = "SELECT DISTINCT authority FROM authorities";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    @Override
    public void updateRoles(Long userId, List<String> roles) {
        // Delete existing roles
        String deleteRolesSql = "DELETE FROM authorities WHERE user_id = ?";
        jdbcTemplate.update(deleteRolesSql, userId);
        // Add new roles
        String insertRoleSql = "INSERT INTO authorities (user_id, authority) VALUES (?, ?)";
        for (String role : roles) {
            jdbcTemplate.update(insertRoleSql, userId, role);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
    }

}