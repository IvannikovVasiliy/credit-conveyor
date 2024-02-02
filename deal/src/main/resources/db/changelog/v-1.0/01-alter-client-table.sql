ALTER TABLE client
add column version bigint not null default(0);

GO