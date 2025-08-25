-- Sample data for development and testing

INSERT INTO products (id, name, description, price, currency, stock_quantity, status, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'MacBook Pro 16"', 'Apple MacBook Pro with M2 chip, 16-inch display', 2999.99, 'USD', 50, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440002', 'iPhone 15 Pro', 'Latest iPhone with advanced camera system', 1199.99, 'USD', 100, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440003', 'Samsung Galaxy S24', 'Android flagship smartphone', 899.99, 'USD', 75, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440004', 'Dell XPS 13', 'Ultrabook with Intel Core i7', 1299.99, 'USD', 0, 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440005', 'AirPods Pro', 'Wireless earbuds with noise cancellation', 249.99, 'USD', 200, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);