create table credit (
    credit_id bigserial
    , amount double precision not null
    , term integer not null
    , monthly_payment double precision not null
    , rate double precision not null
    , psk double precision not null
    , payment_schedule jsonb not null
    , insurance_enable boolean not null
    , salary_client boolean not null
    , credit_status varchar(15)
);

GO

alter table credit
add constraint pk_credit_id primary key (credit_id);

GO