-- TODO Task 3
drop database if exists cartdb;

create database cartdb;

use cartdb;

create table orders (
   order_id varchar(64) not null,
   date timestamp default current_timestamp,
   name varchar(64) not null,
   address varchar(256) not null,
   priority boolean not null,
   comments varchar(256),

   primary key(order_id)
);

create table line_item (
   id int auto_increment, 
   prod_id varchar(64) not null,
   name varchar(256) not null,
   quantity int not null,
   order_id varchar(64) not null,

   primary key(id),
   constraint fk_order_id foreign key(order_id) references orders(order_id)
);

grant all privileges on cartdb.* to fred@'%';

flush privileges;