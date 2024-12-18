package sc.liste.noel.liste_noel.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.Utile.mapper.ListeMapper;
import sc.liste.noel.liste_noel.dao.entity.FavorisDao;
import sc.liste.noel.liste_noel.dao.entity.ListeDao;
import sc.liste.noel.liste_noel.dao.entity.ObjetDao;
import sc.liste.noel.liste_noel.dao.repo.CompteRepo;
import sc.liste.noel.liste_noel.dao.repo.FavorisRepo;
import sc.liste.noel.liste_noel.dao.repo.ListeRepo;
import sc.liste.noel.liste_noel.dao.repo.ObjetRepo;
import sc.liste.noel.liste_noel.dto.ListeDto;
import sc.liste.noel.liste_noel.service.ListeServiceInterface;

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

        ListeDao listeDao = new ListeDao();
        listeDao.setNomListe(nomListe);
        listeDao.setProprietaire(proprietaire);

        try {
            listeRepo.save(listeDao);
            return new ListeDto();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ListeDto> getListesOfEmail(String email) {
        List<ListeDao> listeDaoList = listeRepo.findByProprietaire(email);
        return ListeMapper.daosToDtos(listeDaoList);
    }

    @Override
    public ListeDto getListeById(Long id) {
        ListeDao listeDao = listeRepo.findByIdListe(id);
        ListeDto listeDto = ListeMapper.daoToDto(listeDao);
        listeDto.setUrlPartage(ListeMapper.buildUrlPartage(baseUrl, id));
        return listeDto;
    }

    @Override
    public void ajouterObjetDansUneListe(String titre, String url, String description, String idListe, String proprietaire) {
        ObjetDao objetDao = new ObjetDao();
        objetDao.setDescription(description);
        objetDao.setIdListe(Long.valueOf(idListe));
        objetDao.setTitre(titre);
        objetDao.setEstPrit(false);
        objetDao.setUrl(url);
        objetRepo.save(objetDao);
    }

    @Override
    @Transactional
    public void prendreUnObjet(String idListe, String idObjet, String personne, String pseudo) {
        ObjetDao objetDao = objetRepo.findByIdObjet(Long.valueOf(idObjet));
        objetDao.setDetenteur(personne);
        objetDao.setPseudoDetenteur(pseudo);
        objetDao.setEstPrit(true);
        objetRepo.save(objetDao);
    }

    @Override
    @Transactional
    public void nePlusPrendreUnObjet(String idObjet) {
        ObjetDao objetDao = objetRepo.findByIdObjet(Long.valueOf(idObjet));
        objetDao.setDetenteur(null);
        objetDao.setPseudoDetenteur(null);
        objetDao.setEstPrit(false);
        objetRepo.save(objetDao);
    }


    public List<ListeDto> getListeFavorisOfEmail(String email) {
        List<FavorisDao> favorisDaoList = favorisRepo.findByEmail(email);

        if (favorisDaoList == null) {
            return null;
        }
        List<ListeDao> list = new ArrayList<>();

        for (FavorisDao favorisDao : favorisDaoList) {
            ListeDao listeDao = listeRepo.findByIdListe(favorisDao.getIdListe());
            if (listeDao != null) {
                list.add(listeDao);
            }
        }

        return transcoEmailToPPseudo(ListeMapper.daosToDtos(list));
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
        FavorisDao favorisDaoList = favorisRepo.findByEmailAndIdListe(email, idListe);
        if (favorisDaoList == null) {
            FavorisDao favorisDao = new FavorisDao();
            favorisDao.setEmail(email);
            favorisDao.setIdListe(idListe);
            favorisRepo.save(favorisDao);
        }
    }

    @Transactional
    @Override
    public void supprimerFavori(Long idListe, String email) {
        FavorisDao favorisDaoList = favorisRepo.findByEmailAndIdListe(email, idListe);
        if (favorisDaoList != null) {
            favorisRepo.delete(favorisDaoList);
        }
    }

    @Transactional
    @Override
    public void supprimerObjet(Long idObjet, String email) {

        ObjetDao objetDao = objetRepo.findByIdObjet(idObjet);

        if (objetDao != null) {

            ListeDao listeDao = listeRepo.findByIdListe(objetDao.getIdListe());

            String bodyEmail = "L'objet " + objetDao.getTitre() + " : " + objetDao.getDescription() + " " + objetDao.getUrl()
                    + " a été supprimé de la liste " + listeDao.getNomListe()
                    + " qui fait partie de vos favoris" + " consulter la liste : "
                    + ListeMapper.buildUrlPartage(baseUrl, listeDao.getIdListe());;
            String sujetEmail = "Objet supprimé de la liste : " + listeDao.getNomListe();

            List<FavorisDao> favorisDaoList = favorisRepo.findByIdListe(listeDao.getIdListe());

            envoyerEmailToListe(getListeOfEmailFromListeFavorisDao(favorisDaoList), bodyEmail, sujetEmail);

            objetRepo.delete(objetDao);
        }

    }

    @Transactional
    @Override
    public void modifierObjet(Long idObjet, String titreUpdate, String descriptionUpdate, String urlUpdate) {

        ObjetDao objetDao = objetRepo.findByIdObjet(idObjet);

        if (objetDao != null) {

            ListeDao listeDao = listeRepo.findByIdListe(objetDao.getIdListe());

            String bodyEmail = "L'objet " + objetDao.getTitre() + " : " + objetDao.getDescription() + " - " + objetDao.getUrl()
                    + " a été modifier dans la liste " + listeDao.getNomListe()
                    + " qui fait partie de vos favoris. Voici les nouvelles informations, " + titreUpdate + " : " + descriptionUpdate + " - " + urlUpdate + " consulter la liste : "
                    + ListeMapper.buildUrlPartage(baseUrl, listeDao.getIdListe());
            String sujetEmail = "Objet modifié dans la liste : " + listeDao.getNomListe();

            List<FavorisDao> favorisDaoList = favorisRepo.findByIdListe(listeDao.getIdListe());

            envoyerEmailToListe(getListeOfEmailFromListeFavorisDao(favorisDaoList), bodyEmail, sujetEmail);

            objetDao.setTitre(titreUpdate);
            objetDao.setDescription(descriptionUpdate);
            objetDao.setUrl(urlUpdate);

            objetRepo.save(objetDao);
        }
    }

    private List<String> getListeOfEmailFromListeFavorisDao(List<FavorisDao> favorisDaoList) {
        return Optional.ofNullable(favorisDaoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(FavorisDao::getEmail)
                .toList();
    }

    private void envoyerEmailToListe(List<String> listOfEmail,String body, String subject) {
        for(String email : listOfEmail) {
            mailService.sendEmail(email, subject, body);
        }
    }

}
