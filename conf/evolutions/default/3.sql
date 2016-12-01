# --- !Ups

CREATE SEQUENCE category_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "category" (
  "id" integer NOT NULL DEFAULT nextval('category_id_seq'::regclass),
  "name" varchar not null,
   CONSTRAINT category_pkey PRIMARY KEY (id)
);

# --- !Downs

drop table  if exists "category";

drop sequence  if exists "category_id_seq";
