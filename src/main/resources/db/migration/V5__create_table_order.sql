CREATE TABLE order_table (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    address_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT fk_order_address FOREIGN KEY (address_id) REFERENCES address (id)
);
