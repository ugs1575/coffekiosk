drop table if exists items cascade;
create table items (
   id bigint generated by default as identity,
    item_type varchar(255),
    last_modified_date_time timestamp(6),
    name varchar(255),
    price integer not null,
    primary key (id)
)