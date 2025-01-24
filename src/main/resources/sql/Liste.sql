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

DROP TABLE liste_noel.objet CASCADE;

CREATE TABLE liste_noel.objet
(
    id_objet SERIAL NOT NULL,
    id_liste SERIAL NOT NULL,
    titre character varying(1000) NOT NULL,
    description character varying(1000),
    url character varying(1000),
    est_prit boolean NOT NULL,
    detenteur character varying(320),
    PRIMARY KEY (id_objet),
    CONSTRAINT id_liste FOREIGN KEY (id_liste) REFERENCES liste_noel.liste (id_liste),
    CONSTRAINT detenteur FOREIGN KEY (detenteur) REFERENCES liste_noel.compte (email)
);

ALTER TABLE liste_noel.objet ADD COLUMN pseudo_detenteur character varying(320);

CREATE TABLE liste_noel.ref_priorite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    value INT NOT NULL,
    libelle VARCHAR(100) NOT NULL
);

ALTER TABLE liste_noel.ref_priorite
ADD CONSTRAINT unique_value UNIQUE (value);

INSERT INTO liste_noel.ref_priorite (id, value, libelle) VALUES
(1,1, '❤️❤️❤️❤️❤️'),
(2,2, '❤️❤️❤️❤️'),
(3,3, '❤️❤️❤️'),
(4,4, '❤️❤️'),
(5,5, '❤️');

ALTER TABLE liste_noel.objet
ADD COLUMN priorite INT,
ADD CONSTRAINT fk_priorite FOREIGN KEY (priorite) REFERENCES liste_noel.ref_priorite(value);
