insert into USER (LAST_NAME,NAME, EMAIL)
values ('Nunez', 'Raul', 'rnunez@eemail.com'),
       ('Ortega', 'Esteban', 'oesteban@email.com');

insert into TRANSACTION (USER_ID, AMOUNT, DESCRIPTION, DATE)
values (1, 40.56, 'Description 2', '2020-02-15'),
       (1, 34.56, 'Description 1', '2020-01-02'),
       (1, 345.55, 'Description 3', '2019-02-16'),
       (1, 231.53, 'Description 4', '2020-03-17'),
       (1, 34.67, 'Description 5', '2018-01-01'),
       (2, 31.34, 'Description 6', '2016-01-02'),
       (2, 876.34, 'Description 7', '2020-02-10'),
       (2, 315.70, 'Description 8', '2019-02-11'),
       (2, 16.12, 'Description 9', '2018-03-11'),
       (2, 45.65, 'Description 10', '2020-03-15');