# --- !Ups

CREATE SEQUENCE login_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "login" (
  "id" integer NOT NULL DEFAULT nextval('login_id_seq'::regclass),
  "username" varchar not null,
  "password" varchar not null,
   CONSTRAINT login_pkey PRIMARY KEY (id)
);

# --- !Downs

drop table  if exists "login";

drop sequence  if exists "login_id_seq";
