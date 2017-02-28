# --- !Ups

CREATE SEQUENCE identity_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "identity" (
  "id" integer NOT NULL DEFAULT nextval('identity_id_seq'::regclass),
  "first_name" varchar not null,
  "last_name" varchar not null,
  "email" varchar not null,
   CONSTRAINT identity_pkey PRIMARY KEY (id)
);

# --- !Downs

drop table  if exists "identity";

drop sequence  if exists "identity_id_seq";
