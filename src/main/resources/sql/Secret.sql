DROP TABLE liste_noel.compte CASCADE;

CREATE TABLE liste_noel.secret
(
    nom_application character varying(50) NOT NULL,
	secret character varying(20) not null,
    PRIMARY KEY (nom_application)
);