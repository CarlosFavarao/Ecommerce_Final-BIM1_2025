CREATE TABLE IF NOT EXISTS products (
    uuid UUID PRIMARY KEY,       -- ID único para cada produto
    name VARCHAR(255) NOT NULL,  -- Nome do produto
    price NUMERIC(10, 2) NOT NULL -- Preço do produto com até 10 dígitos e 2 casas decimais
);

CREATE TABLE IF NOT EXISTS users (
    uuid UUID PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL
);

-- Table: sales
CREATE TABLE sales (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    payment_method TEXT NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(uuid)
);

-- Table: sale_products
CREATE TABLE sale_products (
    sale_id UUID NOT NULL,
    product_id UUID NOT NULL,
    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (product_id) REFERENCES products(uuid)
);