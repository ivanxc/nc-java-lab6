CREATE TABLE public.customers (
    customer_id bigserial PRIMARY KEY,
    customer_surname varchar(60),
    customer_district varchar(60),
    customer_discount smallint CHECK (customer_discount <= 100 AND customer_discount >= 0)
);

CREATE TABLE public.shops (
    shop_id bigserial PRIMARY KEY,
    shop_name varchar(100),
    shop_district varchar(100),
    shop_commission smallint CHECK(shop_commission <= 100 AND shop_commission >= 0)
);

CREATE TABLE public.books (
    book_id bigserial PRIMARY KEY ,
    book_title text NOT NULL,
    book_price integer CHECK(book_price > 0),
    book_repo text NOT NULL,
    book_quantity integer NOT NULL CHECK(book_quantity >= 0)
);

CREATE TABLE public.purchases (
    purchase_id bigserial PRIMARY KEY,
    purchase_date timestamp NOT NULL,
    shop_id bigint REFERENCES shops(shop_id),
    customer_id bigint REFERENCES customers(customer_id),
    book_id bigint REFERENCES books(book_id),
    quantity integer NOT NULL CHECK(quantity > 0),
    total_price real NOT NULL CHECK(total_price > 0)
);