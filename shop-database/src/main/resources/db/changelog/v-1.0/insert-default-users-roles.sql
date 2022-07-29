INSERT INTO `users` (`id`, `email`, `firstname`, `lastname`, `password`)
    VALUE   (0xB6630045683441799572C6E747B39F34, 'admin@mail.ru', 'admin', 'admin', '$2a$10$85IcFNsYe7moM6A/VX6tvOliKzBhiOXbCK4K16JTewt1nQQm9VEuu'),
        (0x0FEE77071F02417E8C3146EE47762851, 'user@mail.ru', 'user', 'user', '$2a$10$3cnZ/G8fRIG.K68CTbP3yuWM8.7BT2IMkAJf3qLRSvNOGsCy0Tbla');
GO

INSERT INTO `roles` (`id`, `role`)
VALUE (0xB6630045683441799572C6E747B39F34, 'ROLE_ADMIN'), (0x0FEE77071F02417E8C3146EE47762851, 'ROLE_USER');
GO

INSERT INTO `users_roles`(`user_id`, `role_id`)
SELECT (SELECT id FROM `users` WHERE `email` = 'admin@mail.ru'), (SELECT id FROM `roles` WHERE `role` = 'ROLE_ADMIN')
UNION ALL
SELECT (SELECT id FROM `users` WHERE `email` = 'user@mail.ru'), (SELECT id FROM `roles` WHERE `role` = 'ROLE_USER');
GO

INSERT INTO `categories` (`id`, `title`)
    VALUE   
    	(0xB6630045683441799572C6E747B39F34, 'Desktop'), 
    	(0x0FEE77071F02417E8C3146EE47762851, 'Laptop'), 
    	(0x9ACDE4A18AE440D3B2EED531ED04B6DC, 'Monitor'), 
    	(0x7156085BC17A4CF09AC81B360E5E98FC, 'Printer');
GO

INSERT INTO `brands` (`id`, `title`)
	VALUE 
		(0xB6630045683441799572C6E747B39F34, 'DEXP'), 
		(0x0FEE77071F02417E8C3146EE47762851, 'ASUS'), 
		(0x9ACDE4A18AE440D3B2EED531ED04B6DC, 'HP'), 
		(0x7156085BC17A4CF09AC81B360E5E98FC, 'Logitech');
GO

INSERT INTO `products`(`id`, `title`, `cost`, `category_id`, `brand_id`)
VALUE (
	0xB6630045683441799572C6E747B39F34,
    'DEXP Aquilon O251 [Intel Celeron J4005, 4 ГБ DDR4, SSD 120 ГБ]', 110,
    (SELECT id FROM `categories` WHERE `title` = 'Desktop'),
    (SELECT id FROM `brands` WHERE `title` = 'DEXP')
),
(
	0x0FEE77071F02417E8C3146EE47762851,
    '15.6" ASUS 15 D543MA-DM1328T', 325,
    (SELECT id FROM `categories` WHERE `title` = 'Laptop'),
    (SELECT id FROM `brands` WHERE `title` = 'ASUS')
),
(
	0x9ACDE4A18AE440D3B2EED531ED04B6DC,
    '19.5" HP V20 [1600x900, TN, 5 мс, 600:1, 90°/65°, HDMI, VGA (D-sub)]', 105,
    (SELECT id FROM `categories` WHERE `title` = 'Monitor'),
    (SELECT id FROM `brands` WHERE `title` = 'HP')
),
(
	0x7156085BC17A4CF09AC81B360E5E98FC,
    'HP Laser 107a', 15,
    (SELECT id FROM `categories` WHERE `title` = 'Printer'),
    (SELECT id FROM `brands` WHERE `title` = 'HP')
);
GO