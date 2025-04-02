CREATE SCHEMA IF NOT EXISTS app;

SET search_path TO app;

drop table if exists app.season_pass_holder;
drop table if exists app.season_pass_type;
drop table if exists app.season_pass_offer;
drop table if exists app.ticket_purchase;
drop table if exists app.ticket_type;
drop table if exists app.ticket_offer;
drop table if exists app.club_admin;
drop table if exists app.subscription;
drop table if exists app.match;
drop table if exists app.stand;
drop table if exists app.stadium;
drop table if exists app.club;
drop table if exists app.user_role;
drop table if exists app.user;
drop table if exists app.role;
drop table if exists app.person;
drop table if exists app.identity_document;



CREATE TABLE app.identity_document
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(100)                            NOT NULL UNIQUE,
    abbreviation VARCHAR(4)                              NOT NULL UNIQUE,
    CONSTRAINT pk_identity_document PRIMARY KEY (id)
);


CREATE TABLE app.person
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    last_name   VARCHAR(255)                            NOT NULL,
    doc_type_id BIGINT                                  NOT NULL,
    doc_number  VARCHAR(25)                             NOT NULL,
    birthday    date                                    NOT NULL,
    gender      VARCHAR(1)                              NOT NULL,
    email       VARCHAR(250)                            NOT NULL UNIQUE,
    phone       VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_person PRIMARY KEY (id),
    CONSTRAINT fk_person_doc_type FOREIGN KEY (doc_type_id) REFERENCES app.identity_document (id)
);


CREATE TABLE app.role
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(100)                            NOT NULL UNIQUE,
    CONSTRAINT pk_role PRIMARY KEY (id)
);


CREATE TABLE app."user"
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    person_id BIGINT UNIQUE,
    username  VARCHAR(100)                            NOT NULL UNIQUE,
    password  VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT FK_USER_ON_PERSON FOREIGN KEY (person_id) REFERENCES app.person (id)
);


CREATE TABLE app.user_role
(
    role_id   BIGINT  NOT NULL,
    user_id   BIGINT  NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES app.role (id),
    CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES app."user" (id)
);


CREATE TABLE app.club
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name           VARCHAR(250)                            NOT NULL,
    crest_image_id VARCHAR(250),
    CONSTRAINT pk_club PRIMARY KEY (id)
);


CREATE TABLE app.stadium
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    club_id BIGINT,
    name    VARCHAR(250)                            NOT NULL,
    CONSTRAINT pk_stadium PRIMARY KEY (id),
    CONSTRAINT FK_STADIUM_ON_CLUB FOREIGN KEY (club_id) REFERENCES app.club (id)
);


CREATE TABLE app.stand
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    stadium_id BIGINT,
    name       VARCHAR(250)                            NOT NULL,
    capacity   INTEGER                                 NOT NULL,
    CONSTRAINT pk_stand PRIMARY KEY (id),
    CONSTRAINT FK_STAND_ON_STADIUM FOREIGN KEY (stadium_id) REFERENCES app.stadium (id)
);


CREATE TABLE app.match
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    home_team_id BIGINT                                  NOT NULL,
    away_team_id BIGINT                                  NOT NULL,
    year         INTEGER                                 NOT NULL,
    season       INTEGER                                 NOT NULL,
    stadium_id   BIGINT                                  NOT NULL,
    start_time   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_match PRIMARY KEY (id),
    CONSTRAINT FK_MATCH_ON_AWAY_TEAM FOREIGN KEY (away_team_id) REFERENCES app.club (id),
    CONSTRAINT FK_MATCH_ON_HOME_TEAM FOREIGN KEY (home_team_id) REFERENCES app.club (id),
    CONSTRAINT FK_MATCH_ON_STADIUM FOREIGN KEY (stadium_id) REFERENCES app.stadium (id)
);


CREATE TABLE app.subscription
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT                                  NOT NULL,
    club_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_subscription PRIMARY KEY (id),
    CONSTRAINT FK_SUBSCRIPTION_ON_CLUB FOREIGN KEY (club_id) REFERENCES app.club (id),
    CONSTRAINT FK_SUBSCRIPTION_ON_USER FOREIGN KEY (user_id) REFERENCES app."user" (id)
);



CREATE TABLE app.club_admin
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id    BIGINT                                  NOT NULL,
    club_id    BIGINT                                  NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_club_admin PRIMARY KEY (id),
    CONSTRAINT FK_CLUB_ADMIN_ON_CLUB FOREIGN KEY (club_id) REFERENCES app.club (id),
    CONSTRAINT FK_CLUB_ADMIN_ON_USER FOREIGN KEY (user_id) REFERENCES app."user" (id)
);


CREATE TABLE app.ticket_offer
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    posted_by  BIGINT                                  NOT NULL,
    match_id   BIGINT                                  NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    image_id   VARCHAR(255),
    CONSTRAINT pk_ticket_offer PRIMARY KEY (id),
    CONSTRAINT FK_TICKET_OFFER_ON_MATCH FOREIGN KEY (match_id) REFERENCES app.match (id),
    CONSTRAINT FK_TICKET_OFFER_ON_CLUB_ADMIN FOREIGN KEY (posted_by) REFERENCES app.club_admin (id)
);


CREATE TABLE app.ticket_type
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ticket_offer_id BIGINT,
    stand_id        BIGINT                                  NOT NULL,
    price           DECIMAL                                 NOT NULL,
    CONSTRAINT pk_ticket_type PRIMARY KEY (id),
    CONSTRAINT FK_TICKET_TYPE_ON_STAND FOREIGN KEY (stand_id) REFERENCES app.stand (id),
    CONSTRAINT FK_TICKET_TYPE_ON_TICKET_OFFER FOREIGN KEY (ticket_offer_id) REFERENCES app.ticket_offer (id)
);


CREATE TABLE app.ticket_purchase
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ticket_type_id BIGINT                                  NOT NULL,
    user_id        BIGINT                                  NOT NULL,
    payment_id     VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_ticket_purchase PRIMARY KEY (id),
    CONSTRAINT FK_TICKET_PURCHASE_ON_TICKET_TYPE FOREIGN KEY (ticket_type_id) REFERENCES app.ticket_type (id),
    CONSTRAINT FK_TICKET_PURCHASE_ON_USER FOREIGN KEY (user_id) REFERENCES app."user" (id)
);


CREATE TABLE app.season_pass_offer
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    posted_by  BIGINT                                  NOT NULL,
    year       INTEGER                                 NOT NULL,
    season     INTEGER                                 NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    image_id   VARCHAR(255),
    CONSTRAINT pk_season_pass_offer PRIMARY KEY (id),
    CONSTRAINT FK_SEASON_PASS_OFFER_ON_CLUB_ADMIN FOREIGN KEY (posted_by) REFERENCES app.club_admin (id)
);


CREATE TABLE app.season_pass_type
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    season_pass_offer_id BIGINT                                  NOT NULL,
    stand_id            BIGINT                                  NOT NULL,
    price               DECIMAL                                 NOT NULL,
    CONSTRAINT pk_season_pass_type PRIMARY KEY (id),
    CONSTRAINT FK_SEASON_PASS_TYPE_ON_MEMBERSHIP_OFFER FOREIGN KEY (season_pass_offer_id) REFERENCES app.season_pass_offer (id),
    CONSTRAINT FK_SEASON_PASS_TYPE_ON_STAND FOREIGN KEY (stand_id) REFERENCES app.stand (id)
);


CREATE TABLE app.season_pass_holder
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id             BIGINT                                  NOT NULL,
    season_pass_type_id BIGINT                                  NOT NULL,
    payment_id          VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_season_pass_holder PRIMARY KEY (id),
    CONSTRAINT FK_SEASON_PASS_HOLDER_ON_SEASON_PASS_TYPE FOREIGN KEY (season_pass_type_id) REFERENCES app.season_pass_type (id),
    CONSTRAINT FK_SEASON_PASS_HOLDER_ON_USER FOREIGN KEY (user_id) REFERENCES app."user" (id)
);