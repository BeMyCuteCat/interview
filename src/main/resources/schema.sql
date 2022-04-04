drop table if exists t_car;
drop table if exists t_user;

create table t_car (
    id bigint auto_increment,
    model varchar(255),
    total int,
    remain int,
    primary key (id),
    unique key (model)
);

create table t_user (
    id bigint auto_increment,
    name varchar(255),
    model varchar(255),
    num int,
    create_time datetime,
    primary key (id)
);