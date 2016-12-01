# --- !Ups

CREATE SEQUENCE film_category_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "film_category" (
  "film_id" integer NOT NULL REFERENCES film(id),
  "category_id" integer NOT NULL REFERENCES category(id)
);

# --- !Downs

drop table  if exists "film_category";

drop sequence  if exists "film_category_id_seq";
