# --- !Ups

CREATE SEQUENCE film_actor_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 600
  CACHE 1;

create table "film_actor" (
  "actorId" integer NOT NULL,
  "filmId" integer NOT NULL
);

# --- !Downs

drop table  if exists "film_actor";

drop sequence  if exists "film_actor_id_seq";
