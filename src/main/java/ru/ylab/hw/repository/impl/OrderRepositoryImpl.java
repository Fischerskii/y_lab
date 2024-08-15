package ru.ylab.hw.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.ylab.hw.config.DatabaseConfig;
import ru.ylab.hw.dto.Order;
import ru.ylab.hw.enums.OrderStatus;
import ru.ylab.hw.exception.DataAccessException;
import ru.ylab.hw.repository.OrderRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public void save(Order order) {
        String query = "INSERT INTO entity_schema.order (id, username, car_vin_number, status, order_creation_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection()){
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1, order.getId());
                statement.setString(2, order.getUserName());
                statement.setString(3, order.getCarVinNumber());
                statement.setString(4, String.valueOf(order.getStatus()));
                statement.setTimestamp(5, Timestamp.valueOf(order.getOrderCreationDate()));

                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                log.error("Error creating order with id: {}", order.getId(), e);
                throw new DataAccessException("Error creating order with id: " + order.getId(), e);
            }
        } catch (SQLException e) {
            log.error("Failed to save order", e);
        }
    }

    @Override
    public void edit(UUID id, OrderStatus orderNewStatus) {
        String query = "UPDATE entity_schema.order SET status = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, String.valueOf(orderNewStatus));
                statement.setObject(2, id);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated == 0) {
                    log.warn("Not found order with id: {}", id);
                } else {
                    log.info("Order with id: {} was changed status to: {}", id, orderNewStatus);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                log.error("Error updating order with id: {}", id, e);
                throw new DataAccessException("Error updating order with id: " + id, e);
            }
        } catch (SQLException e) {
            log.error("Failed to edit order", e);
        }
    }

    public List<Order> findAll() {
        String query = "SELECT id, username, car_vin_number, status, order_creation_date FROM entity_schema.order";
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Order order = mapRowToOrder(resultSet);

                orders.add(order);
            }
        } catch (SQLException e) {
            log.error("Error finding orders", e);
        }

        return orders;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        String query = "SELECT * FROM entity_schema.order WHERE id = ?";

        Order order = null;
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, String.valueOf(id));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order = mapRowToOrder(resultSet);
            }

        } catch (SQLException e) {
            log.error("Error finding order with id: {}", id, e);
        }

        return Optional.ofNullable(order);
    }

    /**
     * Maps a {@link ResultSet} row to an {@link Order} object.
     *
     * @param resultSet the ResultSet to be mapped
     * @return the corresponding Order object
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    private Order mapRowToOrder(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String userName = resultSet.getString("username");
        String carVinNumber = resultSet.getString("car_vin_number");
        OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));
        LocalDateTime orderCreationDate = resultSet.getTimestamp("order_creation_date").toLocalDateTime();

        return Order.builder()
                .id(UUID.fromString(id))
                .userName(userName)
                .carVinNumber(carVinNumber)
                .status(status)
                .orderCreationDate(orderCreationDate)
                .build();
    }
}