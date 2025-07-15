CREATE DATABASE "orderdb"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conéctate a la base de datos recién creada
\connect orderdb;

--CREATE TABLE public.order_shop (
--    id int GENERATED ALWAYS AS IDENTITY NOT NULL,
--    code_product varchar NULL,
--    date_order varchar NULL,
--    status_order varchar NULL,
--    CONSTRAINT order_shop_pk PRIMARY KEY (id)
--);