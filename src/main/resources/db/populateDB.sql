DELETE FROM votes;
DELETE FROM menu_items;
DELETE FROM restaurants;
DELETE FROM dishes;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, role) VALUES
  ('admin_1','ROLE_ADMIN'),
  ('admin_2','ROLE_ADMIN'),
  ('user_1','ROLE_USER'),
  ('user_2','ROLE_USER'),
  ('user_3','ROLE_USER');

INSERT INTO dishes (description, price) VALUES
  ('dish_01', 100),
  ('dish_02', 200),
  ('dish_03', 50),
  ('dish_04', 70),
  ('dish_05', 150),
  ('dish_06', 210),
  ('dish_07', 90),
  ('dish_08', 120),
  ('dish_09', 1000),
  ('dish_10', 10);

INSERT INTO restaurants (name) VALUES
  ('restaurant_1'),
  ('restaurant_2'),
  ('restaurant_3'),
  ('restaurant_4'),
  ('restaurant_5');

INSERT INTO menu_items (dish_id, restaurant_id, added) VALUES
  (100005, 100015, '2001-01-01'),
  (100006, 100015, '2001-01-01'),
  (100005, 100017, '2001-01-01'),
  (100008, 100017, '2001-01-01'),
  (100005, 100015, '2001-01-02'),
  (100006, 100015, '2001-01-02'),
  (100007, 100017, '2001-01-02'),
  (100008, 100017, '2001-01-02');

INSERT INTO votes (user_id, restaurant_id, taken) VALUES
  (100002, 100017, '2001-01-01 01:01:01'),
  (100003, 100016, '2001-01-01 02:02:02'),
  (100002, 100017, '2001-01-02 01:01:01'),
  (100003, 100018, '2001-01-02 02:02:01'),
  (100002, 100017, '2001-01-03 01:01:01'),
  (100003, 100017, '2001-01-03 02:02:01');