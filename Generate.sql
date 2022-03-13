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
    tree_id int NOT NULL AUTO_INCREMENT,
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

INSERT INTO tree(genus, species, shape, leaf_type, min_height, max_height, family_name) 
VALUES
('Larix', 'laricina', 'conical', 'needles', 12, 24, 'Pinaceae'),
('Carya', 'cordiformis', 'rounded', 'pinnately compound', 18, 24, 'Juglandaceae'),
('Ulmus', 'americana', 'vase', 'elliptical', 24, 42, 'Ulmaceae'),
('Malus', 'ioensis', 'broad', 'elliptical', 3, 9, 'Rosaceae');

/* Common name table */
CREATE TABLE common_name (
    tree_id int NOT NULL,
    tree_name varchar(20) NOT NULL,
    PRIMARY KEY (tree_id, tree_name),
    FOREIGN KEY (tree_id) REFERENCES tree(tree_id)
);

INSERT INTO common_name
VALUES
(1, 'Tamarack'),
(2, 'Bitternut Hickory'),
(3, 'American Elm'),
(4, 'Prairie Crab Apple');

/* Flower table */
CREATE TABLE flower (
    tree_id int NOT NULL,
    color varchar(10) NOT NULL,
    flower_shape varchar(10),
    petals int,
    PRIMARY KEY (tree_id, color),
    FOREIGN KEY (tree_id) REFERENCES tree(tree_id)
);

INSERT INTO flower
VALUES
(2, 'green', 'catkins', NULL),
(3, 'green', 'clustered', NULL),
(4, 'pink', 'rounded', 5),
(4, 'white', 'rounded', 5);

/* Habitat table */
CREATE TABLE habitat (
    habitat_id int NOT NULL AUTO_INCREMENT,
    soil_moisture varchar(10),
    soil_type varchar(10),
    habitat_type varchar(20),
    PRIMARY KEY (habitat_id)
);

INSERT INTO habitat(soil_moisture, soil_type, habitat_type) 
VALUES
('wet', 'peaty', 'bog'),
('moist', null, 'valley'),
('moist', 'clay', 'stream'),
('dry', null, 'upland');

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
(2, 2),
(2, 4),
(3, 2),
(4, 3);

/* Sighting table */
CREATE TABLE sighting (
    sighting_id int NOT NULL AUTO_INCREMENT,
    tree_id int NOT NULL,
    sighting_date Date,
    latitude float NOT NULL,
    longitude float NOT NULL,
    altitude int,
    PRIMARY KEY (sighting_id),
    FOREIGN KEY (tree_id) REFERENCES tree(tree_id)
);

INSERT INTO sighting(tree_id, sighting_date, latitude, longitude, altitude) 
VALUES
(1, '2016-06-15', 45.7, -93.1, 280),
(3, '2010-01-19', 43.8, -70.2, 22),
(4, '2012-12-01', 41.3, -91.1, 166),
(3, '2012-12-01', 46.4, -62.2, 53);