/*
Project 1 - SQL
CS 687, Spring 2022
Generate tables and data for MySQL.
*/

CREATE DATABASE IF NOT EXISTS forestry;

USE forestry;

/* Family table */
CREATE TABLE family (
    family_name varchar(20) NOT NULL,
    total_species int,
    climate varchar(10),
    family_order varchar(20),
    PRIMARY KEY (family_name)
);

INSERT INTO family
VALUES
('Rosaceae', 2000, 'temperate', 'Rosales'),
('Juglandaceae', 50, 'temperate','Fagales' ),
('Ulmaceae', 200, 'temperate', 'Rosales'),
('Pinaceae', 200, 'temperate', 'Pinales');

/* Tree table */
CREATE TABLE tree (
    tree_id int NOT NULL,
    genus varchar(20) NOT NULL,
    species varchar(20) NOT NULL,
    shape varchar(20),
    leaf_type varchar(20),
    min_height int,
    max_height int,
    family_name varchar(20),
    PRIMARY KEY (tree_id),
    UNIQUE (genus, species),
    FOREIGN KEY (family_name) REFERENCES family(family_name)
);

INSERT INTO tree
VALUES
(1, 'Larix', 'laricina', 'conical', 'needles', 12, 24, 'Pinales'),
(2, 'Carya', 'cordiformis', 'rounded', 'pinnately compound', 18, 24, 'Juglandaceae'),
(3, 'Ulmus', 'americana', 'vase', 'elliptical', 24, 42, 'Ulmaceae'),
(4, 'Malus', 'ioensis', 'broad', 'elliptical', 3, 9, 'Rosaceae');

/* Common name table */
CREATE TABLE common_name (
    tree_id int NOT NULL,
    tree_name varchar(20) NOT NULL,
    PRIMARY KEY (tree_id),
    FOREIGN KEY (tree_id) REFERENCES tree(tree_id)
);

INSERT INTO common_name
VALUES
(18, 'Tamarack'),
(332, 'Bitternut Hickory'),
(162, 'American Elm'),
(130, 'Prairie Crab Apple');

/* Flower table */
CREATE TABLE flower (
    tree_id int NOT NULL,
    color varchar(10) NOT NULL,
    shape varchar(10),
    petals int,
    PRIMARY KEY (tree_id, color),
    FOREIGN KEY (tree_id) REFERENCES tree(tree_id)
);

INSERT INTO flower
VALUES
(332, 'green', 'catkins', NULL),
(162, 'green', 'clustered', NULL),
(130, 'pink', 'rounded', 5),
(130, 'white', 'rounded', 5);

/* Habitat table */
CREATE TABLE habitat (
    habitat_id int NOT NULL,
    soil_moisture varchar(10),
    soil_type varchar(10),
    habitat_type varchar(20),
    PRIMARY KEY (habitat_id)
);

INSERT INTO habitat
VALUES
(1, 'wet', 'peaty', 'bog'),
(2, 'moist', null, 'valley'),
(3, 'moist', 'clay', 'stream'),
(4, 'dry', null, 'upland');

/* Grows_in table */
CREATE TABLE grows_in (
    tree_id int NOT NULL,
    habitat_id int NOT NULL,
    PRIMARY KEY (tree_id, habitat_id),
    FOREIGN KEY (tree_id) REFERENCES tree(tree_id),
    FOREIGN KEY (habitat_id) REFERENCES habitat(habitat_id)
);

INSERT INTO grows_in
VALUES
(332, 2),
(332, 3),
(162, 3),
(130, 4);

/* Sighting table */
CREATE TABLE sighting (
    sighting_id int NOT NULL,
    tree_id int NOT NULL,
    sighting_date Date,
    latitude float,
    longitude float,
    altitude int
);

INSERT INTO sighting
VALUES
(101, 18, '2016-06-15', 45.7, -93.1, 280),
(25, 162, '2010-01-19', 43.8, 70.2, 22),
(7005, 130, '2012-12-01', 41.3, -91.1, 166),
(7006, 162, '2012-12-01', 46.4, -62.2, 53);