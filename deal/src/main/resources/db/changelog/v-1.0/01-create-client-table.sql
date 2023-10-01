create table client (
    client_id bigserial
    , last_name varchar(50) not null
    , first_name varchar(50) not null
    , middle_name varchar(50)
    , birth_date date not null
    , email varchar(64)
    , gender varchar(50)
    , martial_status varchar(25) not null
    , dependence_amount integer not null
    , passport_id jsonb not null
    , employment_id jsonb not null
    , account varchar(50)
);

GO

ALTER TABLE client
add constraint pk_client_id primary key(client_id);

GO