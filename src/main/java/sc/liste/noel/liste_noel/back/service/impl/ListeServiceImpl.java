package sc.liste.noel.liste_noel.back.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.back.ListeMapper;
import sc.liste.noel.liste_noel.back.ObjetMapper;
import sc.liste.noel.liste_noel.back.db.entity.FavorisEntity;
import sc.liste.noel.liste_noel.back.db.entity.ListeEntity;
import sc.liste.noel.liste_noel.back.db.entity.ObjetEntity;
import sc.liste.noel.liste_noel.back.db.repo.CompteRepo;
import sc.liste.noel.liste_noel.back.db.repo.FavorisRepo;
import sc.liste.noel.liste_noel.back.db.repo.ListeRepo;
import sc.liste.noel.liste_noel.back.db.repo.ObjetRepo;
import sc.liste.noel.liste_noel.back.service.ListeServiceInterface;
import sc.liste.noel.liste_noel.common.dto.ListeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListeServiceImpl implements ListeServiceInterface {
    @Autowired
    private ListeRepo listeRepo;

    @Autowired
    private ObjetRepo objetRepo;

    @Autowired
    private FavorisRepo favorisRepo;

    @Autowired
    private CompteRepo compteRepo;

    @Value("${base_url}")
    private String baseUrl;

    @Autowired
    private MailService mailService;

    @Override
    public ListeDto creerListe(String proprietaire, String nomListe) {

        ListeEntity listeEntity = new ListeEntity();
        listeEntity.setNomListe(nomListe);
        listeEntity.setProprietaire(proprietaire);

        try {
            listeRepo.save(listeEntity);
            return new ListeDto();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ListeDto> getListesOfEmail(String email) {
        List<ListeEntity> listeEntityList = listeRepo.findByProprietaire(email);
        return ListeMapper.entitiesToDtos(listeEntityList);
    }

    @Override
    public ListeDto getListeById(Long id) {
        ListeEntity listeEntity = listeRepo.findByIdListe(id);
        ListeDto listeDto = ListeMapper.entityToDto(listeEntity);
        listeDto.setUrlPartage(ListeMapper.buildUrlPartage(baseUrl, id));
        return listeDto;
    }

    @Override
    public void ajouterObjetDansUneListe(String titre, String url, String description, String idListe, String proprietaire, int priorite) {
        ObjetEntity objetEntity = new ObjetEntity();
        objetEntity.setDescription(description);
        objetEntity.setIdListe(Long.valueOf(idListe));
        objetEntity.setTitre(titre);
        objetEntity.setEstPrit(false);
        objetEntity.setUrl(url);
        objetEntity.setPrioriteValue(priorite);
        objetRepo.save(objetEntity);
    }

    @Override
    @Transactional
    public void prendreUnObjet(String idListe, String idObjet, String personne, String pseudo) {
        ObjetEntity objetEntity = objetRepo.findByIdObjet(Long.valueOf(idObjet));
        objetEntity.setDetenteur(personne);
        objetEntity.setPseudoDetenteur(pseudo);
        objetEntity.setEstPrit(true);
        objetRepo.save(objetEntity);
    }

    @Override
    @Transactional
    public void nePlusPrendreUnObjet(String idObjet) {
        ObjetEntity objetEntity = objetRepo.findByIdObjet(Long.valueOf(idObjet));
        objetEntity.setDetenteur(null);
        objetEntity.setPseudoDetenteur(null);
        objetEntity.setEstPrit(false);
        objetRepo.save(objetEntity);
    }


    public List<ListeDto> getListeFavorisOfEmail(String email) {
        List<FavorisEntity> favorisEntityList = favorisRepo.findByEmail(email);

        if (favorisEntityList == null) {
            return null;
        }
        List<ListeEntity> list = new ArrayList<>();

        for (FavorisEntity favorisEntity : favorisEntityList) {
            ListeEntity listeEntity = listeRepo.findByIdListe(favorisEntity.getIdListe());
            if (listeEntity != null) {
                list.add(listeEntity);
            }
        }

        return transcoEmailToPPseudo(ListeMapper.entitiesToDtos(list));
    }

    public boolean checkifListeInFavoris(Long idListe, String email) {
        return favorisRepo.findByEmailAndIdListe(email, idListe) != null;
    }


    private List<ListeDto> transcoEmailToPPseudo(List<ListeDto> list) {
        for (ListeDto listeDto : list) {
            listeDto.setProprietaire(compteRepo.findByEmail(listeDto.getProprietaire()).getPseudo());
        }
        return list;
    }

    @Transactional
    @Override
    public void ajouterFavori(Long idListe, String email) {
        FavorisEntity favorisEntityList = favorisRepo.findByEmailAndIdListe(email, idListe);
        if (favorisEntityList == null) {
            FavorisEntity favorisEntity = new FavorisEntity();
            favorisEntity.setEmail(email);
            favorisEntity.setIdListe(idListe);
            favorisRepo.save(favorisEntity);
        }
    }

    @Transactional
    @Override
    public void supprimerFavori(Long idListe, String email) {
        FavorisEntity favorisEntityList = favorisRepo.findByEmailAndIdListe(email, idListe);
        if (favorisEntityList != null) {
            favorisRepo.delete(favorisEntityList);
        }
    }

    @Transactional
    @Override
    public void supprimerObjet(Long idObjet, String email) {

        ObjetEntity objetEntity = objetRepo.findByIdObjet(idObjet);

        if (objetEntity != null) {

            ListeEntity listeEntity = listeRepo.findByIdListe(objetEntity.getIdListe());

            String bodyEmail = "L'objet " + objetEntity.getTitre() + " : " + objetEntity.getDescription() + " " + objetEntity.getUrl()
                    + " a été supprimé de la liste " + listeEntity.getNomListe()
                    + " qui fait partie de vos favoris" + " consulter la liste : \n\n"
                    + ListeMapper.buildUrlPartage(baseUrl, listeEntity.getIdListe());;
            String sujetEmail = "Objet supprimé de la liste : " + listeEntity.getNomListe();

            List<FavorisEntity> favorisEntityList = favorisRepo.findByIdListe(listeEntity.getIdListe());

            envoyerEmailToListe(getListeOfEmailFromListeFavorisDao(favorisEntityList), bodyEmail, sujetEmail);

            objetRepo.delete(objetEntity);
        }

    }

    @Transactional
    @Override
    public void modifierObjet(Long idObjet, String titreUpdate, String descriptionUpdate, String urlUpdate, int prioriteUpdate) {

        ObjetEntity objetEntity = objetRepo.findByIdObjet(idObjet);

        if (objetEntity != null) {

            ListeEntity listeEntity = listeRepo.findByIdListe(objetEntity.getIdListe());

            String bodyEmail = "L'objet " + objetEntity.getTitre() + " : " + objetEntity.getDescription() + " - " + objetEntity.getUrl()
                    + " a été modifié dans la liste " + listeEntity.getNomListe()
                    + " qui fait partie de vos favoris.\n\n Voici les nouvelles informations :\n\n " + titreUpdate + " : " + descriptionUpdate + " - " + urlUpdate + " " + ObjetMapper.transcoPriorite(prioriteUpdate) + " \n\n consulter la liste : "
                    + ListeMapper.buildUrlPartage(baseUrl, listeEntity.getIdListe());
            String sujetEmail = "Objet modifié dans la liste : " + listeEntity.getNomListe();

            List<FavorisEntity> favorisEntityList = favorisRepo.findByIdListe(listeEntity.getIdListe());

            envoyerEmailToListe(getListeOfEmailFromListeFavorisDao(favorisEntityList), bodyEmail, sujetEmail);

            objetEntity.setTitre(titreUpdate);
            objetEntity.setDescription(descriptionUpdate);
            objetEntity.setUrl(urlUpdate);
            objetEntity.setPrioriteValue(prioriteUpdate);

            objetRepo.save(objetEntity);
        }
    }

    @Transactional
    @Override
    public String supprimerListe(String nomListe, String emailListe) {
        ListeEntity listeEntity = listeRepo.findByProprietaireAndNomListe(emailListe, nomListe);
        if (listeEntity != null) {
            List<FavorisEntity> favorisEntityList = favorisRepo.findByIdListe(listeEntity.getIdListe());
            for (FavorisEntity favorisEntity : favorisEntityList) {
                favorisRepo.delete(favorisEntity);
            }
            listeRepo.delete(listeEntity);
            return "La liste " + emailListe + " à bien été supprimé";
        } else {
            return "La liste " + emailListe + " est introuvable, elle ne peux pas être supprimée";
        }
    }

    private List<String> getListeOfEmailFromListeFavorisDao(List<FavorisEntity> favorisEntityList) {
        return Optional.ofNullable(favorisEntityList)
                .orElse(new ArrayList<>())
                .stream()
                .map(FavorisEntity::getEmail)
                .toList();
    }

    private void envoyerEmailToListe(List<String> listOfEmail,String body, String subject) {
        for(String email : listOfEmail) {
            mailService.sendEmail(email, subject, body);
        }
    }

}
