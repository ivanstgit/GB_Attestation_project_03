DROP SCHEMA IF EXISTS `HUMAN_FRIENDS`;
CREATE SCHEMA `HUMAN_FRIENDS`;

USE `HUMAN_FRIENDS`;


/* 
 * домашние животные и вьючные животные, в составы которых в случае домашних
 * животных войдут классы: собаки, кошки, хомяки, а в класс вьючные животные
 * войдут: Лошади, верблюды и ослы
 * 7. В подключенном MySQL репозитории создать базу данных “Друзья человека” 
 * 8. Создать таблицы с иерархией из диаграммы в БД
 * 9. Заполнить низкоуровневые таблицы именами(животных), командами
 * которые они выполняют и датами рождения
*/
-- не понятно, насколько адекватен человек, написавший это задание, но делаем по ТЗ
DROP TABLE IF EXISTS dogs;
CREATE TABLE dogs (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения'
);
INSERT INTO dogs (id, name, birth_date) VALUES 
(1, 'Dog 1', '2021-01-01'),
(2, 'Dog 2', '2021-01-01');
DROP TABLE IF EXISTS dogs_commands;
CREATE TABLE dogs_commands (
	id SERIAL PRIMARY KEY, 
	dog_id BIGINT UNSIGNED,
    command VARCHAR(50) COMMENT 'Команда',
    FOREIGN KEY (dog_id) REFERENCES dogs(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO dogs_commands (id, dog_id, command) VALUES 
(1, '1', 'сидеть'),
(2, '1', 'лежать'),
(3, '2', 'прыгать');

DROP TABLE IF EXISTS cats;
CREATE TABLE cats (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения'
);
INSERT INTO cats (id, name, birth_date) VALUES 
(1, 'Cat 1', '2021-01-01'),
(2, 'Cat 2', '2021-01-01');
DROP TABLE IF EXISTS cats_commands;
CREATE TABLE cats_commands (
	id SERIAL PRIMARY KEY, 
	cat_id BIGINT UNSIGNED,
    command VARCHAR(50) COMMENT 'Команда',
    FOREIGN KEY (cat_id) REFERENCES cats(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO cats_commands (id, cat_id, command) VALUES 
(1, '1', 'сидеть'),
(2, '1', 'лежать'),
(3, '2', 'прыгать');

DROP TABLE IF EXISTS hamsters;
CREATE TABLE hamsters (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения'
);
INSERT INTO hamsters (id, name, birth_date) VALUES 
(1, 'Hamster 1', '2021-01-01'),
(2, 'Hamster 2', '2021-01-01');
DROP TABLE IF EXISTS hamsters_commands;
CREATE TABLE hamsters_commands (
	id SERIAL PRIMARY KEY, 
	hamster_id BIGINT UNSIGNED,
    command VARCHAR(50) COMMENT 'Команда',
    FOREIGN KEY (hamster_id) REFERENCES hamsters(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO hamsters_commands (id, hamster_id, command) VALUES 
(1, '1', 'сидеть'),
(2, '1', 'лежать'),
(3, '2', 'прыгать');

DROP TABLE IF EXISTS horses;
CREATE TABLE horses (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения'
);
INSERT INTO horses (id, name, birth_date) VALUES 
(1, 'Horse 1', '2021-01-01'),
(2, 'Horse 2', '2021-01-01');
DROP TABLE IF EXISTS horses_commands;
CREATE TABLE horses_commands (
	id SERIAL PRIMARY KEY, 
	horse_id BIGINT UNSIGNED,
    command VARCHAR(50) COMMENT 'Команда',
    FOREIGN KEY (horse_id) REFERENCES horses(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO horses_commands (id, horse_id, command) VALUES 
(1, '1', 'сидеть'),
(2, '1', 'лежать'),
(3, '2', 'прыгать');

DROP TABLE IF EXISTS camels;
CREATE TABLE camels (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения'
);
INSERT INTO camels (id, name, birth_date) VALUES 
(1, 'Camel 1', '2021-01-01'),
(2, 'Camel 2', '2021-01-01');
DROP TABLE IF EXISTS camels_commands;
CREATE TABLE camels_commands (
	id SERIAL PRIMARY KEY, 
	camel_id BIGINT UNSIGNED,
    command VARCHAR(50) COMMENT 'Команда',
    FOREIGN KEY (camel_id) REFERENCES camels(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO camels_commands (id, camel_id, command) VALUES 
(1, '1', 'сидеть'),
(2, '1', 'лежать'),
(3, '2', 'прыгать');

DROP TABLE IF EXISTS donkeys;
CREATE TABLE donkeys (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения'
);
INSERT INTO donkeys (id, name, birth_date) VALUES 
(1, 'Donkey 1', '2021-01-01'),
(2, 'Donkey 2', '2021-01-01');
DROP TABLE IF EXISTS donkeys_commands;
CREATE TABLE donkeys_commands (
	id SERIAL PRIMARY KEY, 
	donkey_id BIGINT UNSIGNED,
    command VARCHAR(50) COMMENT 'Команда',
    FOREIGN KEY (donkey_id) REFERENCES donkeys(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO donkeys_commands (id, donkey_id, command) VALUES 
(1, '1', 'сидеть'),
(2, '1', 'лежать'),
(3, '2', 'прыгать');


-- * 10. Удалив из таблицы верблюдов, т.к. верблюдов решили перевезти в другой
-- * питомник на зимовку. Объединить таблицы лошади, и ослы в одну таблицу.

DELETE FROM camels_commands;
DELETE FROM camels ;

DROP TABLE IF EXISTS pack_animals;
CREATE TABLE pack_animals (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения'
);

INSERT INTO pack_animals (name, birth_date)
SELECT name, birth_date FROM horses
UNION ALL 
SELECT name, birth_date FROM donkeys;

--  * 11. Создать новую таблицу “молодые животные” в которую попадут все
--  * животные старше 1 года, но младше 3 лет и в отдельном столбце с точностью
--  * до месяца подсчитать возраст животных в новой таблице
DROP TABLE IF EXISTS young_animals;
CREATE TABLE young_animals (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения',
    age INTEGER
);

INSERT INTO young_animals (name, birth_date, age)
SELECT 
	a.name, 
	a.birth_date,
	TIMESTAMPDIFF(MONTH, a.birth_date, CURRENT_DATE()) AS age
FROM ( 
	SELECT name, birth_date FROM horses
	UNION ALL 
	SELECT name, birth_date FROM donkeys
	UNION ALL 
	SELECT name, birth_date FROM cats
	UNION ALL 
	SELECT name, birth_date FROM dogs
	UNION ALL 
	SELECT name, birth_date FROM hamsters
 ) a
WHERE TIMESTAMPDIFF(MONTH, a.birth_date, CURRENT_DATE()) < 36
  AND TIMESTAMPDIFF(MONTH, a.birth_date, CURRENT_DATE()) > 12
;

--  * 12. Объединить все таблицы в одну, при этом сохраняя поля, указывающие на
--  * прошлую принадлежность к старым таблицам.
DROP TABLE IF EXISTS animals_commands;
DROP TABLE IF EXISTS animals;
DROP TABLE IF EXISTS animal_types;
CREATE TABLE animal_types (
	id VARCHAR(20) PRIMARY KEY, 
    name TEXT COMMENT 'Имя'
    );
   
INSERT INTO animal_types (id, name) VALUES 
('cat', 'Кошка'),
('dog', 'Собака'),
('hamster', 'Хомяк'),
('horse', 'Лошадь'),
('donkey', 'Осел');

CREATE TABLE animals (
	id SERIAL PRIMARY KEY, 
    name VARCHAR(50) COMMENT 'Имя',
    birth_date DATE COMMENT 'Дата рождения',
    animal_type VARCHAR(20),
    src_tab VARCHAR(50),
    src_id BIGINT UNSIGNED,
    FOREIGN KEY (animal_type) REFERENCES animal_types(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO animals (name, birth_date, animal_type, src_tab, src_id)
	SELECT name, birth_date, 'horse', 'horses', id FROM horses
	UNION ALL 
	SELECT name, birth_date, 'donkey', 'donkeys', id FROM donkeys
	UNION ALL 
	SELECT name, birth_date, 'cat', 'cats', id FROM cats
	UNION ALL 
	SELECT name, birth_date, 'dog', 'dogs', id FROM dogs
	UNION ALL 
	SELECT name, birth_date, 'hamster', 'hamsters', id FROM hamsters;

CREATE TABLE animals_commands(
	id SERIAL PRIMARY KEY, 
	animal_id BIGINT UNSIGNED,
    command VARCHAR(50) COMMENT 'Команда',
    src_tab VARCHAR(50),
    src_id BIGINT UNSIGNED,
    FOREIGN KEY (animal_id) REFERENCES animals(id) ON UPDATE CASCADE ON DELETE CASCADE
);
INSERT INTO animals_commands (animal_id, command, src_tab, src_id)
	SELECT 
	a.id, ac.command, ac.src_tab, ac.src_id
	FROM
	(
		SELECT command, 'horses' AS src_tab, id AS src_id, horse_id AS animal_id FROM horses_commands
		UNION ALL 
		SELECT command, 'donkeys' AS src_tab, id AS src_id, donkey_id AS animal_id FROM donkeys_commands 
		UNION ALL 
		SELECT command, 'cats' AS src_tab, id AS src_id, cat_id AS animal_id FROM cats_commands 
		UNION ALL 
		SELECT command, 'dogs' AS src_tab, id AS src_id, dog_id AS animal_id FROM dogs_commands 
		UNION ALL 
		SELECT command, 'hamsters' AS src_tab, id AS src_id, hamster_id AS animal_id FROM hamsters_commands 
	) ac
	JOIN animals a ON ac.animal_id = a.src_id
;



