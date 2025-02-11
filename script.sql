DROP TABLE IF EXISTS hotel_booking;
DROP TABLE IF EXISTS hotel;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL
);

CREATE TABLE hotel (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       prices DOUBLE NOT NULL,
                       latitude DOUBLE NOT NULL,
                       longitude DOUBLE NOT NULL,
                       image_url VARCHAR(500),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       agent_id BIGINT NOT NULL,
                       FOREIGN KEY (agent_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE hotel_booking (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               booking_start TIMESTAMP NOT NULL,
                               booking_end TIMESTAMP NOT NULL,
                               status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL,
                               hotel_id BIGINT NOT NULL,
                               agent_id BIGINT NOT NULL,
                               FOREIGN KEY (hotel_id) REFERENCES hotel(id) ON DELETE CASCADE,
                               FOREIGN KEY (agent_id) REFERENCES user(id) ON DELETE CASCADE
);
