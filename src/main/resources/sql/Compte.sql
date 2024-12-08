DROP TABLE liste_noel.compte CASCADE;

CREATE TABLE liste_noel.compte
(
    email character varying(320) NOT NULL,
	password text not null,
    PRIMARY KEY (email)
);

ALTER TABLE liste_noel.compte ADD COLUMN nb_connexion bigint;
ALTER TABLE liste_noel.compte ADD COLUMN date_derniere_connexion timestamp ;
ALTER TABLE liste_noel.compte ADD COLUMN nb_deconnexion bigint;
ALTER TABLE liste_noel.compte ADD COLUMN date_derniere_deconnexion timestamp ;
ALTER TABLE liste_noel.compte ADD COLUMN nb_modification_mdp bigint;
ALTER TABLE liste_noel.compte ADD COLUMN date_derniere_modification_mdp timestamp ;

update liste_noel.compte set nb_connexion = 0 where nb_connexion is null;
update liste_noel.compte set nb_deconnexion = 0 where nb_deconnexion is null;
update liste_noel.compte set nb_modification_mdp = 0 where nb_modification_mdp is null;

ALTER TABLE liste_noel.compte ADD COLUMN cgu_accepted boolean;

ALTER TABLE liste_noel.compte ADD COLUMN pseudo character varying(320) NOT NULL;