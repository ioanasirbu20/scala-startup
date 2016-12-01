# --- !Ups

CREATE SEQUENCE language_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "language" (
  "id" integer NOT NULL DEFAULT nextval('language_id_seq'::regclass),
  "name" varchar not null,
   CONSTRAINT language_pkey PRIMARY KEY (id)
);

# --- !Downs
--- unde ai DAO?

drop table  if exists "language";

drop sequence  if exists "language_id_seq";
