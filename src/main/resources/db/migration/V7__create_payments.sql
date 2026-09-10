CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

                          order_id BIGINT NOT NULL,

                          razorpay_order_id VARCHAR(100) NOT NULL UNIQUE,

                          razorpay_payment_id VARCHAR(100),

                          razorpay_signature VARCHAR(255),

                          amount DECIMAL(10,2) NOT NULL,

                          currency VARCHAR(10) NOT NULL DEFAULT 'INR',

                          status VARCHAR(30) NOT NULL DEFAULT 'CREATED',

                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP,

                          CONSTRAINT fk_payment_order
                              FOREIGN KEY (order_id)
                                  REFERENCES orders(id)
);