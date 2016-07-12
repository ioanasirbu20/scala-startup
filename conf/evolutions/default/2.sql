# --- !Ups

CREATE SEQUENCE product_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table "product" (
  "id" integer NOT NULL DEFAULT nextval('product_id_seq'::regclass),
  "ean" int not null,
  "name" varchar not null,
   CONSTRAINT product_pkey PRIMARY KEY (id)
);

# --- !Downs

drop table  if exists "product";

drop sequence  if exists "product_id_seq";
