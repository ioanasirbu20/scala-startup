# --- !Ups

CREATE SEQUENCE actor_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "actor" (
  "id" integer NOT NULL DEFAULT nextval('actor_id_seq'::regclass),
  "firstName" varchar not null,
  "lastName" varchar not null,
  "lastUpdate" timestamp,
   CONSTRAINT actor_pkey PRIMARY KEY (id)
);

# --- !Downs

drop table  if exists "actor";

drop sequence  if exists "actor_id_seq";
