create table application (
    application_id bigserial
    , client_id bigserial not null
    , credit_id bigserial not null
    , status varchar(40) not null
    , creation_date timestamp not null
    , applied_offer jsonb not null
    , sign_date timestamp not null
    , ses_code integer
    , status_history jsonb not null
);

GO

alter table application
add constraint pk_application_id primary key(application_id);

GO

alter table application
add constraint fk_application_client foreign key(client_id)
    references client(client_id);

GO

alter table application
add constraint fk_application_credit foreign key(credit_id)
    references credit(credit_id);

GO

create index i_application_id on application(application_id);

GO