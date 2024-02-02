ALTER TABLE credit
add column version bigint not null default(0);

GO