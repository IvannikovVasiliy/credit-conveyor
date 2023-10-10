create table document (
    application_id bigserial
    , loan_agreement_name varchar(256) not null unique
    , questionnaire_name varchar(256) not null unique
    , payment_schedule_name varchar(256) not null unique
);

GO

ALTER TABLE document
    add constraint pk_document_id primary key(application_id);

GO