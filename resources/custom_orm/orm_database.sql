DROP DATABASE IF EXISTS orm ;
CREATE DATABASE IF NOT EXISTS orm ;

CREATE TABLE IF NOT EXISTS orm.user (
    user_id MEDIUMINT AUTO_INCREMENT,
    full_name CHAR(100) NOT NULL,
    login CHAR(30) NOT NULL UNIQUE,
    password CHAR(30) NOT NULL,
    PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS orm.item (
    item_id MEDIUMINT AUTO_INCREMENT,
    seller_id MEDIUMINT NOT NULL,
    title CHAR(100) NOT NULL,
    description CHAR(200) NOT NULL,
    price DECIMAL(5,2) NOT NULL,
    PRIMARY KEY(item_id),
    FOREIGN KEY (seller_id)
        REFERENCES user(user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orm.purchase (
    purchase_id INT AUTO_INCREMENT,
    purchaser_id MEDIUMINT NOT NULL,
    item_id MEDIUMINT NOT NULL,
    amount SMALLINT NOT NULL,
    PRIMARY KEY(purchase_id),
    FOREIGN KEY (purchaser_id)
        REFERENCES user(user_id)
        ON DELETE CASCADE,
	FOREIGN KEY (item_id)
        REFERENCES item(item_id)
        ON DELETE CASCADE
);
