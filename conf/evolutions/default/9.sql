# --- !Ups

CREATE SEQUENCE identity_login_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 600
  CACHE 1;

create table "identity_login" (
  "identity_id" integer NOT NULL REFERENCES identity(id),
  "login_id" integer NOT NULL REFERENCES login(id)
);

# --- !Downs

drop table  if exists "identity_login";

drop sequence  if exists "identity_login_id_seq";
