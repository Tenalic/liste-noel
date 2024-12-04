DROP TABLE liste_noel.liste CASCADE;

CREATE TABLE liste_noel.liste
(
    id_liste SERIAL NOT NULL,
    email character varying(320) NOT NULL,
    nom_liste character varying(1000) NOT NULL,
    PRIMARY KEY (id_liste),
    CONSTRAINT email FOREIGN KEY (email)
            REFERENCES liste_noel.compte (email)
);


CREATE TABLE liste_noel.objet
(
    id_objet SERIAL NOT NULL,
    id_liste SERIAL NOT NULL,
    titre character varying(1000) NOT NULL,
    description character varying(1000),
    url character varying(1000),
    estPrit boolean NOT NULL,
    detenteur character varying(320),
    PRIMARY KEY (id_objet),
    CONSTRAINT id_liste FOREIGN KEY (id_liste) REFERENCES liste_noel.liste (id_liste),
    CONSTRAINT detenteur FOREIGN KEY (detenteur) REFERENCES liste_noel.compte (email)
);


