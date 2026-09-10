CREATE TABLE fcm_tokens (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            token VARCHAR(500) NOT NULL,
                            created_at DATETIME NOT NULL,
                            updated_at DATETIME NOT NULL,

                            PRIMARY KEY (id),
                            UNIQUE KEY uk_fcm_tokens_token (token)
);