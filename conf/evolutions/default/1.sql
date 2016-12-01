# --- !Ups

CREATE SEQUENCE film_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "film" (
  "id" integer NOT NULL DEFAULT nextval('film_id_seq'::regclass),
  "title" varchar not null,
  "description" varchar not null,
  "releaseYear" int not NULL,
  "languageId" int not null,
  "rating" varchar,
   CONSTRAINT film_pkey PRIMARY KEY (id)
);

# --- !Downs

drop table  if exists "film";

drop sequence  if exists "film_id_seq";
