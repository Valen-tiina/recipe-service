set foreign_key_checks = 0;
create database if not exists recipe_service character set utf8mb4 collate utf8mb4_general_ci;
use recipe_service;

create table difficulty(
ID int auto_increment primary key,
name_difficulty varchar(6) not null);

create table licenses_recipe(
ID int auto_increment primary key,
name_license varchar(150)not null,
url_recipe varchar(150) not null);

create table recipes (
ID int auto_increment primary key,
title varchar(100) not null,
short_desc varchar(255) not null,
total_time_min int,
difficulty_id int,
servings int not null,
license_recipe_id int,
constraint fk_recipes_difficulty foreign key (difficulty_id) references difficulty(ID) on delete cascade on update cascade,
constraint fk_recipes_license foreign key (license_recipe_id) references licenses_recipe(ID)on delete cascade on update cascade
);

create table ingredients (
    ID int auto_increment primary key,
    name_ing varchar (100) not null,
    image_url varchar(150) not null,
    measurement_id int not null,
    constraint fk_ing_measure foreign key (measurement_id) 
        references measurement(ID) 
        on delete cascade on update cascade
);

create table measurement(
ID int auto_increment primary key,
name_measurement varchar(20));

create table units(
ID int auto_increment primary key,
measurement_id int not null,
name_unit varchar(50) not null,
constraint fk_units_measurement foreign key (measurement_id) references measurement(ID) on delete cascade on update cascade);

create table categories(
ID int auto_increment primary key,
name_category varchar(9) not null);

create table flavors (
ID int auto_increment primary key,
name_flavor varchar(10));

create table licenses_img(
ID int auto_increment primary key,
name_license varchar(100) not null,
url_recipe varchar(150) not null);

create table steps (
ID int auto_increment primary key,
recipe_id int not null,
step_template_id int,
step_order int not null,
description_step varchar(255),
time_seconds int,
image_url varchar(150),
constraint fk_step_recipe foreign key (recipe_id) references recipes(ID)on delete cascade on update cascade,
FOREIGN KEY (step_template_id) REFERENCES step_templates(id)
)ENGINE=InnoDB;

/*TABLAS PUENTE (RELACIONES MUCHOS A MUCHOS)*/
create table recipe_categories(
ID int auto_increment primary key,
recipe_id int,
category_id int,
UNIQUE KEY uk_recipe_category (recipe_id, category_id),
constraint fk_rc_recipe foreign key (recipe_id) references recipes(ID) on delete cascade on update cascade,
constraint fk_rc_category foreign key (category_id) references categories(ID) on delete cascade on update cascade
)ENGINE=InnoDB;

create table recipe_flavors(
ID int auto_increment primary key,
recipe_id int,
flavor_id int,
UNIQUE KEY uk_recipe_flavor (recipe_id, flavor_id), 
constraint fk_rf_recipe foreign key (recipe_id) references recipes(ID) on delete cascade on update cascade,
constraint fk_rf_flavor foreign key (flavor_id) references flavors(ID)on delete cascade on update cascade
)ENGINE=InnoDB;

create table recipe_images (
ID int auto_increment primary key,
recipe_id int not null,
image_url varchar(500) not null,
alt_text varchar(100),
position int default 0,
license_id int not null,
constraint fk_ri_recipe foreign key (recipe_id) references recipes(ID) on delete cascade on update cascade,
constraint fk_ri_license foreign key (license_id) references licenses_img(ID) on delete cascade on update cascade
)ENGINE=InnoDB;

create table recipe_ingredients (
ID int auto_increment primary key,
recipe_id int not null,
ingredient_id int not null,
quantity decimal(10,2) not null,
unit_id int not null,
constraint fk_ri_recipe2 foreign key (recipe_id) references recipes(ID)on delete cascade on update cascade,
constraint fk_ri_ingredient foreign key (ingredient_id) references ingredients(ID) on delete cascade on update cascade,
constraint fk_ri_unit foreign key (unit_id) references units(ID)on delete cascade on update cascade
)ENGINE=InnoDB;


set foreign_key_checks = 1;

    /* INSERCIONES */
insert into difficulty(name_difficulty)values
	('Baja'),
    ('Media'),
    ('Alta');
    
insert into measurement(name_measurement)values
	('Volumen'),
    ('Masa');

insert into units(measurement_id, name_unit)values
	(1,'Mililitro'),
    (1,'Litro'),
    (1,'Cucharadita'),
    (1,'Cucharada'),
    (1,'Onza líquida'),
    (1,'Taza'),
    (2,'Libra'),
    (2,'Onza'),
    (2,'Gramo');
    
insert into categories(name_category)values
	('Desayuno'),
    ('Almuerzo'),
    ('Cena'),
    ('Snack');
    
insert into flavors(name_flavor)values
	('Salado'),
    ('Dulce'),
    ('Picante'),
    ('Agridulce'),
    ('Ácido');
    
select * from ingredients;
