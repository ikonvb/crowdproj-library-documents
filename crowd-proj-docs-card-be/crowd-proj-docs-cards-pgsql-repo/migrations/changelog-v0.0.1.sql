--liquibase formatted sql

--changeset bulygink:1 labels:v0.0.1
CREATE TYPE "doc_types_type" AS ENUM ('pdf', 'png', 'jpeg', 'ms_word');
CREATE TYPE "doc_visibilities_type" AS ENUM ('public', 'owner', 'group');

CREATE TABLE "doc_cards"
(
    "id"          text primary key
        constraint doc_cards_id_length_ctr check (length("id") < 64),
    "title"       text
        constraint doc_cards_title_length_ctr check (length(title) < 128),
    "description" text
        constraint doc_cards_description_length_ctr check (length(title) < 4096),
    "doc_type"    doc_types_type        not null,
    "visibility"  doc_visibilities_type not null,
    "owner_id"    text                  not null
        constraint doc_cards_owner_id_length_ctr check (length(id) < 64),
    "product_id"  text
        constraint doc_cards_product_id_length_ctr check (length(id) < 64),
    "lock"        text                  not null
        constraint doc_cards_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX doc_cards_owner_id_idx on "doc_cards" using hash ("owner_id");

CREATE INDEX doc_cards_product_id_idx on "doc_cards" using hash ("product_id");

CREATE INDEX doc_cards_doc_card_type_idx on "doc_cards" using hash ("doc_type");

CREATE INDEX doc_cards_visibility_idx on "doc_cards" using hash ("visibility");
