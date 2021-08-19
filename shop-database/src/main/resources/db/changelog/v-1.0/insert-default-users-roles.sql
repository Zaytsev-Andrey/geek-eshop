INSERT INTO `users` (`email`, `firstname`, `lastname`, `password`)
    VALUE   ('admin@mail.ru', 'admin', 'admin', '$2a$10$85IcFNsYe7moM6A/VX6tvOliKzBhiOXbCK4K16JTewt1nQQm9VEuu'),
        ('user@mail.ru', 'user', 'user', '$2a$10$3cnZ/G8fRIG.K68CTbP3yuWM8.7BT2IMkAJf3qLRSvNOGsCy0Tbla');
GO

INSERT INTO `roles` (`role`)
VALUE ('ROLE_ADMIN'), ('ROLE_USER');
GO

INSERT INTO `users_roles`(`user_id`, `role_id`)
SELECT (SELECT id FROM `users` WHERE `email` = 'admin@mail.ru'), (SELECT id FROM `roles` WHERE `role` = 'ROLE_ADMIN')
UNION ALL
SELECT (SELECT id FROM `users` WHERE `email` = 'user@mail.ru'), (SELECT id FROM `roles` WHERE `role` = 'ROLE_USER');
GO