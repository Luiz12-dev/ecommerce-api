CREATE TABLE product (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    category_id UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category (id)
);
