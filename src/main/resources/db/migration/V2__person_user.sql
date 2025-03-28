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