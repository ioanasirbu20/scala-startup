# --- !Ups

CREATE SEQUENCE filmActor_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 600
  CACHE 1;

create table "filmActor" (
  "actorId" integer NOT NULL,
  "filmId" integer NOT NULL,
  "lastUpdate" timestamp

);

# --- !Downs

drop table  if exists "filmActor";

drop sequence  if exists "filmActor_id_seq";
