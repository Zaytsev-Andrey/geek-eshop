INSERT INTO `categories` (`title`)
    VALUE   ('Desktop'), ('Laptop'), ('Monitor'), ('Printer'), ('Keyboard'), ('Mouse'), ('Soft');
GO

INSERT INTO `brands` (`id`, `title`)
VALUE ('b214ad04-10a5-4fb7-bbb2-8b9256d69682', 'DEXP'), 
('1c4b8ccb-2db1-430b-8c6e-b815c973ac3c', 'ASUS'), 
('5ecef127-1cac-4b65-90ac-6580ee452e17', 'HP'), 
('19d87ddf-0761-488d-b090-efba6e58b9ac', 'Logitech');
GO

INSERT INTO `products`(`title`, `cost`, `category_id`, `brand_id`)
VALUE (
    'DEXP Aquilon O251 [Intel Celeron J4005, 4 ГБ DDR4, SSD 120 ГБ]', 110,
    (SELECT id FROM `categories` WHERE `title` = 'Desktop'),
    (SELECT id FROM `brands` WHERE `title` = 'DEXP')
),
(
    '15.6" ASUS 15 D543MA-DM1328T', 325,
    (SELECT id FROM `categories` WHERE `title` = 'Laptop'),
    (SELECT id FROM `brands` WHERE `title` = 'ASUS')
),
(
    '19.5" HP V20 [1600x900, TN, 5 мс, 600:1, 90°/65°, HDMI, VGA (D-sub)]', 105,
    (SELECT id FROM `categories` WHERE `title` = 'Monitor'),
    (SELECT id FROM `brands` WHERE `title` = 'HP')
),
(
    'Logitech K120', 20,
    (SELECT id FROM `categories` WHERE `title` = 'Keyboard'),
    (SELECT id FROM `brands` WHERE `title` = 'Logitech')
),
(
    'Logitech M90', 15,
    (SELECT id FROM `categories` WHERE `title` = 'Mouse'),
    (SELECT id FROM `brands` WHERE `title` = 'Logitech')
),
(
    'HP Laser 107a', 15,
    (SELECT id FROM `categories` WHERE `title` = 'Printer'),
    (SELECT id FROM `brands` WHERE `title` = 'HP')
);
GO