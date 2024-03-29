DROP SCHEMA IF EXISTS demo_ds;
DROP SCHEMA IF EXISTS demo_ds_0;
DROP SCHEMA IF EXISTS demo_ds_1;

DROP SCHEMA IF EXISTS demo_write_ds;
DROP SCHEMA IF EXISTS demo_read_ds_0;
DROP SCHEMA IF EXISTS demo_read_ds_1;

DROP SCHEMA IF EXISTS demo_write_ds_0;
DROP SCHEMA IF EXISTS demo_write_ds_0_read_0;
DROP SCHEMA IF EXISTS demo_write_ds_0_read_1;
DROP SCHEMA IF EXISTS demo_write_ds_1;
DROP SCHEMA IF EXISTS demo_write_ds_1_read_0;
DROP SCHEMA IF EXISTS demo_write_ds_1_read_1;

DROP SCHEMA IF EXISTS shadow_demo_ds;
DROP SCHEMA IF EXISTS shadow_demo_ds_0;
DROP SCHEMA IF EXISTS shadow_demo_ds_1;

DROP SCHEMA IF EXISTS demo_shadow_write_ds;
DROP SCHEMA IF EXISTS demo_shadow_read_ds;
DROP SCHEMA IF EXISTS demo_read_ds;

CREATE SCHEMA IF NOT EXISTS demo_ds;
CREATE SCHEMA IF NOT EXISTS demo_ds_0;
CREATE SCHEMA IF NOT EXISTS demo_ds_1;

CREATE SCHEMA IF NOT EXISTS demo_write_ds;
CREATE SCHEMA IF NOT EXISTS demo_read_ds_0;
CREATE SCHEMA IF NOT EXISTS demo_read_ds_1;

CREATE SCHEMA IF NOT EXISTS demo_write_ds_0;
CREATE SCHEMA IF NOT EXISTS demo_write_ds_0_read_0;
CREATE SCHEMA IF NOT EXISTS demo_write_ds_0_read_1;
CREATE SCHEMA IF NOT EXISTS demo_write_ds_1;
CREATE SCHEMA IF NOT EXISTS demo_write_ds_1_read_0;
CREATE SCHEMA IF NOT EXISTS demo_write_ds_1_read_1;

CREATE SCHEMA IF NOT EXISTS shadow_demo_ds;
CREATE SCHEMA IF NOT EXISTS shadow_demo_ds_0;
CREATE SCHEMA IF NOT EXISTS shadow_demo_ds_1;

CREATE SCHEMA IF NOT EXISTS demo_shadow_write_ds;
CREATE SCHEMA IF NOT EXISTS demo_shadow_read_ds;
CREATE SCHEMA IF NOT EXISTS demo_read_ds;

-- Should sync from master-salve automatically
-- CREATE TABLE IF NOT EXISTS demo_read_ds_0.t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_read_ds_1.t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_read_ds_0.t_order_item (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_read_ds_1.t_order_item (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));

-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_0.t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_0.t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_1.t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_1.t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_0.t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_0.t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_1.t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_1.t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_0.t_order_item_0 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_0.t_order_item_1 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_1.t_order_item_0 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_0_read_1.t_order_item_1 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_0.t_order_item_0 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_0.t_order_item_1 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_1.t_order_item_0 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
-- CREATE TABLE IF NOT EXISTS demo_write_ds_1_read_1.t_order_item_1 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));

-- CREATE TABLE IF NOT EXISTS demo_shadow_read_ds.t_user (user_id INT NOT NULL AUTO_INCREMENT, user_name VARCHAR(200), user_name_plain VARCHAR(200), pwd VARCHAR(200), assisted_query_pwd VARCHAR(200), PRIMARY KEY (user_id));
-- CREATE TABLE IF NOT EXISTS demo_read_ds.t_user (user_id INT NOT NULL AUTO_INCREMENT, user_name VARCHAR(200), user_name_plain VARCHAR(200), pwd VARCHAR(200), assisted_query_pwd VARCHAR(200), PRIMARY KEY (user_id));
