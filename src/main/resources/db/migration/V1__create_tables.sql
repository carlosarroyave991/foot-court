-- Create categories table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(250) NOT NULL
);

-- Create restaurants table
CREATE TABLE restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address TEXT NOT NULL,
    owner_id BIGINT NOT NULL,
    phone VARCHAR(13) NOT NULL,
    logo_url TEXT NOT NULL,
    nit VARCHAR(50) NOT NULL
);

-- Create dishes table
CREATE TABLE dishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    description VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    url_image VARCHAR(150) NOT NULL,
    active BOOLEAN NOT NULL,
    category_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- Create orders table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    status VARCHAR(20),
    chef_id BIGINT,
    client_id BIGINT,
    restaurant_id BIGINT NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- Create orders_dishes table
CREATE TABLE orders_dishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INTEGER NOT NULL,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (dish_id) REFERENCES dishes(id)
);