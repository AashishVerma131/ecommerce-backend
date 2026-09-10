-- Convert all application-owned numeric IDs to prefixed VARCHAR IDs.
-- Existing relationships are preserved by first creating and populating temporary columns.

ALTER TABLE users
    ADD COLUMN id_new VARCHAR(50);

ALTER TABLE products
    ADD COLUMN id_new VARCHAR(50);

ALTER TABLE carts
    ADD COLUMN id_new VARCHAR(50),
    ADD COLUMN user_id_new VARCHAR(50);

ALTER TABLE cart_items
    ADD COLUMN id_new VARCHAR(50),
    ADD COLUMN cart_id_new VARCHAR(50),
    ADD COLUMN product_id_new VARCHAR(50);

ALTER TABLE orders
    ADD COLUMN id_new VARCHAR(50),
    ADD COLUMN user_id_new VARCHAR(50);

ALTER TABLE order_items
    ADD COLUMN id_new VARCHAR(50),
    ADD COLUMN order_id_new VARCHAR(50),
    ADD COLUMN product_id_new VARCHAR(50);

ALTER TABLE payments
    ADD COLUMN id_new VARCHAR(50),
    ADD COLUMN order_id_new VARCHAR(50);

-- Build new IDs from the existing numeric IDs so existing data remains valid.
UPDATE users
SET id_new = CONCAT('usr_', LPAD(id, 2, '0'));

UPDATE products
SET id_new = CONCAT('prod_', LPAD(id, 2, '0'));

UPDATE carts
SET id_new = CONCAT('cart_', LPAD(id, 2, '0'));

UPDATE carts c
JOIN users u ON c.user_id = u.id
SET c.user_id_new = u.id_new;

UPDATE cart_items ci
JOIN carts c ON ci.cart_id = c.id
JOIN products p ON ci.product_id = p.id
SET ci.id_new = CONCAT('item_', LPAD(ci.id, 2, '0')),
    ci.cart_id_new = c.id_new,
    ci.product_id_new = p.id_new;

UPDATE orders o
JOIN users u ON o.user_id = u.id
SET o.id_new = CONCAT('ord_', LPAD(o.id, 2, '0')),
    o.user_id_new = u.id_new;

UPDATE order_items oi
JOIN orders o ON oi.order_id = o.id
JOIN products p ON oi.product_id = p.id
SET oi.id_new = CONCAT('orditem_', LPAD(oi.id, 2, '0')),
    oi.order_id_new = o.id_new,
    oi.product_id_new = p.id_new;

UPDATE payments p
JOIN orders o ON p.order_id = o.id
SET p.id_new = CONCAT('pay_', LPAD(p.id, 2, '0')),
    p.order_id_new = o.id_new;

-- Remove foreign keys before replacing the referenced columns.
ALTER TABLE cart_items
    DROP FOREIGN KEY fk_cart_item_cart,
    DROP FOREIGN KEY fk_cart_item_product;

ALTER TABLE carts
    DROP FOREIGN KEY fk_cart_user;

ALTER TABLE order_items
    DROP FOREIGN KEY fk_order_item_order,
    DROP FOREIGN KEY fk_order_item_product;

ALTER TABLE orders
    DROP FOREIGN KEY fk_order_user;

ALTER TABLE payments
    DROP FOREIGN KEY fk_payment_order;

-- Replace users.id.
ALTER TABLE users
    DROP PRIMARY KEY,
    DROP COLUMN id,
    CHANGE COLUMN id_new id VARCHAR(50) NOT NULL,
    ADD PRIMARY KEY (id);

-- Replace products.id.
ALTER TABLE products
    DROP PRIMARY KEY,
    DROP COLUMN id,
    CHANGE COLUMN id_new id VARCHAR(50) NOT NULL,
    ADD PRIMARY KEY (id);

-- Replace carts.id and carts.user_id.
ALTER TABLE carts
    DROP PRIMARY KEY,
    DROP COLUMN id,
    DROP COLUMN user_id,
    CHANGE COLUMN id_new id VARCHAR(50) NOT NULL,
    CHANGE COLUMN user_id_new user_id VARCHAR(50) NOT NULL,
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uk_carts_user_id (user_id);

-- Replace cart_items.id, cart_id and product_id.
ALTER TABLE cart_items
    DROP PRIMARY KEY,
    DROP COLUMN id,
    DROP COLUMN cart_id,
    DROP COLUMN product_id,
    CHANGE COLUMN id_new id VARCHAR(50) NOT NULL,
    CHANGE COLUMN cart_id_new cart_id VARCHAR(50) NOT NULL,
    CHANGE COLUMN product_id_new product_id VARCHAR(50) NOT NULL,
    ADD PRIMARY KEY (id);

-- Replace orders.id and orders.user_id.
ALTER TABLE orders
    DROP PRIMARY KEY,
    DROP COLUMN id,
    DROP COLUMN user_id,
    CHANGE COLUMN id_new id VARCHAR(50) NOT NULL,
    CHANGE COLUMN user_id_new user_id VARCHAR(50) NOT NULL,
    ADD PRIMARY KEY (id);

-- Replace order_items.id, order_id and product_id.
ALTER TABLE order_items
    DROP PRIMARY KEY,
    DROP COLUMN id,
    DROP COLUMN order_id,
    DROP COLUMN product_id,
    CHANGE COLUMN id_new id VARCHAR(50) NOT NULL,
    CHANGE COLUMN order_id_new order_id VARCHAR(50) NOT NULL,
    CHANGE COLUMN product_id_new product_id VARCHAR(50) NOT NULL,
    ADD PRIMARY KEY (id);

-- Replace payments.id and payments.order_id.
ALTER TABLE payments
    DROP PRIMARY KEY,
    DROP COLUMN id,
    DROP COLUMN order_id,
    CHANGE COLUMN id_new id VARCHAR(50) NOT NULL,
    CHANGE COLUMN order_id_new order_id VARCHAR(50) NOT NULL,
    ADD PRIMARY KEY (id);

-- Re-create foreign keys using the new string IDs.
ALTER TABLE carts
    ADD CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id)
        REFERENCES users(id);

ALTER TABLE cart_items
    ADD CONSTRAINT fk_cart_item_cart
        FOREIGN KEY (cart_id)
        REFERENCES carts(id),
    ADD CONSTRAINT fk_cart_item_product
        FOREIGN KEY (product_id)
        REFERENCES products(id);

ALTER TABLE orders
    ADD CONSTRAINT fk_order_user
        FOREIGN KEY (user_id)
        REFERENCES users(id);

ALTER TABLE order_items
    ADD CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id),
    ADD CONSTRAINT fk_order_item_product
        FOREIGN KEY (product_id)
        REFERENCES products(id);

ALTER TABLE payments
    ADD CONSTRAINT fk_payment_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id);
