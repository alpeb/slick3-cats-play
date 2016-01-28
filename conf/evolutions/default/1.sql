# --- !Ups

CREATE TABLE USER (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO USER (email, name) VALUES('foo@example.org', 'Alejandro');

# --- !Downs

DROP TABLE User;
