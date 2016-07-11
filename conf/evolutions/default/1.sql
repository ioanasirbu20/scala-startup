# --- !Ups

CREATE SEQUENCE people_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 600
  CACHE 1;

create table "people" (
  "id" integer NOT NULL DEFAULT nextval('people_id_seq'::regclass),
  "name" varchar not null,
  "age" int not null,
   CONSTRAINT people_pkey PRIMARY KEY (id)
);

# --- !Downs

drop table  if exists "people";

drop sequence  if exists "people_id_seq";
