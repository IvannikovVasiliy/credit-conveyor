create table credit (
    credit_id bigserial
    , amount numeric(100, 10) not null
    , term integer not null
    , monthly_payment numeric(100, 10) not null
    , rate numeric(15, 10) not null
    , psk numeric(100, 10) not null
    , payment_schedule jsonb not null
    , insurance_enable boolean not null
    , salary_client boolean not null
    , credit_status varchar(15)
);

GO

alter table credit
add constraint pk_credit_id primary key (credit_id);

GO