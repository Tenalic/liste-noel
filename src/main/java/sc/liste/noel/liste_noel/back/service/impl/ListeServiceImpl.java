package sc.liste.noel.liste_noel.back.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.back.mapper.ListeMapper;
import sc.liste.noel.liste_noel.back.mapper.ObjetMapper;
import sc.liste.noel.liste_noel.back.db.entity.FavorisEntity;
import sc.liste.noel.liste_noel.back.db.entity.ListeEntity;
import sc.liste.noel.liste_noel.back.db.entity.ObjetEntity;
import sc.liste.noel.liste_noel.back.db.repo.CompteRepo;
import sc.liste.noel.liste_noel.back.db.repo.FavorisRepo;
import sc.liste.noel.liste_noel.back.db.repo.ListeRepo;
import sc.liste.noel.liste_noel.back.db.repo.ObjetRepo;
import sc.liste.noel.liste_noel.back.exception.ListeNotFoundException;
import sc.liste.noel.liste_noel.back.exception.ModificationInterditeException;
import sc.liste.noel.liste_noel.back.service.EmailTemplateService;
import sc.liste.noel.liste_noel.back.service.ListeServiceInterface;
import sc.liste.noel.liste_noel.back.dto.ListeContexteDto;
import sc.liste.noel.liste_noel.back.dto.ListeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ListeServiceImpl implements ListeServiceInterface {

    private final ListeRepo listeRepo;

    private final ObjetRepo objetRepo;

    private final FavorisRepo favorisRepo;

    private final CompteRepo compteRepo;

    @Value("${base_url}")
    private String baseUrl;

    private final MailService mailService;

    @Value("${send_email_active}")
    private Boolean mailServiceActived;

    private final EmailTemplateService emailTemplateService;

    public ListeServiceImpl(ListeRepo listeRepo, ObjetRepo objetRepo, FavorisRepo favorisRepo, CompteRepo compteRepo, MailService mailService, EmailTemplateService emailTemplateService) {
        this.listeRepo = listeRepo;
        this.objetRepo = objetRepo;
        this.favorisRepo = favorisRepo;
        this.compteRepo = compteRepo;
        this.mailService = mailService;
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    public void creerListe(String proprietaire, String nomListe, boolean publique) {

        ListeEntity listeEntity = new ListeEntity();
        listeEntity.setNomListe(nomListe);
        listeEntity.setProprietaire(proprietaire);
        listeEntity.setPublique(publique);

        listeRepo.save(listeEntity);
    }

    @Override
    public List<ListeDto> getListesOfEmail(String email) {
        List<ListeEntity> listeEntityList = listeRepo.findByProprietaire(email);
        return ListeMapper.entitiesToDtosSansListeObjet(listeEntityList);
    }

    @Override
    public ListeDto getListeById(Long id) throws ListeNotFoundException {
        ListeEntity listeEntity = listeRepo.findByIdListe(id);
        ListeDto listeDto = ListeMapper.entityToDto(listeEntity);
        if (listeDto != null) {
            listeDto.setUrlPartage(ListeMapper.buildUrlPartage(baseUrl, id));
            return listeDto;
        } else {
            throw new ListeNotFoundException("Liste introuvable : " + id);
        }
    }

    // FIXME creer un repo ListeSimpleEntity sans les objets (perf)
    @Override
    public List<ListeDto> getListes(boolean publique, String nomListe) throws ListeNotFoundException {
        List<ListeEntity> listeEntities;
        boolean isRechercheParNom = nomListe != null && !nomListe.isBlank();
        if (isRechercheParNom) {
            listeEntities = listeRepo.findByPubliqueAndNomListeContainingIgnoreCase(publique, nomListe);
        } else {
            listeEntities = listeRepo.findByPublique(publique);
        }
        List<ListeDto> listeDtos = ListeMapper.entitiesToDtosSansListeObjet(listeEntities);

        if (listeDtos != null) {
            listeDtos.forEach(listeDto -> {
                        listeDto.setUrlPartage(ListeMapper.buildUrlPartage(baseUrl, listeDto.getIdListe()));
                        this.remplacerEmailsParPseudo(listeDto);
                    }
            );
        } else {
            throw new ListeNotFoundException("Aucune liste trouvé" + (isRechercheParNom ? " " + nomListe : ""));
        }
        return listeDtos;
    }

    @Transactional
    @Override
    public void updatePublique(Long idListe, boolean publique, String email) throws ModificationInterditeException, ListeNotFoundException {
        ListeEntity listeEntity = listeRepo.findByIdListe(idListe);
        if (listeEntity == null) {
            throw new ListeNotFoundException("Liste introuvable");
        }
        if (listeEntity.getProprietaire().equals(email)) {
            listeEntity.setPublique(publique);
            listeRepo.save(listeEntity);
        } else {
            throw new ModificationInterditeException("La liste n'appartient pas à l'utilisateur");
        }

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

        return remplacerEmailsParPseudo(ListeMapper.entitiesToDtosSansListeObjet(list));
    }

    private List<ListeDto> remplacerEmailsParPseudo(List<ListeDto> list) {
        for (ListeDto listeDto : list) {
            listeDto.setProprietaire(compteRepo.findByEmail(listeDto.getProprietaire()).getPseudo());
        }
        return list;
    }

    private void remplacerEmailsParPseudo(ListeDto list) {
        list.setProprietaire(compteRepo.findByEmail(list.getProprietaire()).getPseudo());
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
    public void modifierFavori(Long idListe, String email) {
        FavorisEntity favorisEntityList = favorisRepo.findByEmailAndIdListe(email, idListe);
        if (favorisEntityList != null) {
            favorisRepo.delete(favorisEntityList);
        } else {
            this.ajouterFavori(idListe, email);
        }
    }

    @Transactional
    @Override
    public void supprimerObjet(Long idObjet, String email) throws ModificationInterditeException {

        ObjetEntity objetEntity = objetRepo.findByIdObjet(idObjet);

        if (objetEntity != null) {

            ListeEntity listeEntity = listeRepo.findByIdListe(objetEntity.getIdListe());

            if (!listeEntity.getProprietaire().equals(email)) {
                throw new ModificationInterditeException("Vous ne pouvez pas supprimer un objet qui n'appartient pas à l'une de vos liste");
            }


            if (mailServiceActived) {
                String bodyEmail = emailTemplateService.generateBodySuppressionObjet(objetEntity.getTitre(),
                        objetEntity.getDescription(),
                        objetEntity.getUrl(),
                        listeEntity.getNomListe(),
                        ListeMapper.buildUrlPartage(baseUrl, listeEntity.getIdListe()));
                String sujetEmail = "Objet supprimé de la liste : " + listeEntity.getNomListe();

                List<FavorisEntity> favorisEntityList = favorisRepo.findByIdListe(listeEntity.getIdListe());

                envoyerEmailToListe(getListeOfEmailFromListeFavorisDao(favorisEntityList), bodyEmail, sujetEmail);
            }

            objetRepo.delete(objetEntity);
        }

    }

    @Transactional
    @Override
    public void modifierObjet(Long idObjet, String titreUpdate, String descriptionUpdate, String urlUpdate, int prioriteUpdate, String email) throws ModificationInterditeException {

        ObjetEntity objetEntity = objetRepo.findByIdObjet(idObjet);

        if (objetEntity != null) {

            ListeEntity listeEntity = listeRepo.findByIdListe(objetEntity.getIdListe());

            if (!listeEntity.getProprietaire().equals(email)) {
                throw new ModificationInterditeException("Vous ne pouvez pas modifier un objet qui n'appartient pas à l'une de vos liste");
            }

            String bodyEmail = emailTemplateService.generateBodyModificationObjet(objetEntity.getTitre(),
                    objetEntity.getDescription(),
                    objetEntity.getUrl(),
                    titreUpdate,
                    descriptionUpdate,
                    urlUpdate,
                    ObjetMapper.transcoPriorite(prioriteUpdate),
                    listeEntity.getNomListe(),
                    ListeMapper.buildUrlPartage(baseUrl, listeEntity.getIdListe())
            );
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
    public String supprimerListe(Long idListe, String email) throws ModificationInterditeException, ListeNotFoundException {
        ListeEntity listeEntity = listeRepo.findByIdListe(idListe);
        if (listeEntity != null) {
            if (listeEntity.getProprietaire().equals(email)) {
                List<FavorisEntity> favorisEntityList = favorisRepo.findByIdListe(listeEntity.getIdListe());
                favorisRepo.deleteAll(favorisEntityList);
                listeRepo.delete(listeEntity);
                return "La liste " + listeEntity.getNomListe() + " à bien été supprimé";
            } else {
                throw new ModificationInterditeException("Vous ne pouvez pas supprimer une liste qui ne vous appartient pas");
            }
        } else {
            throw new ListeNotFoundException("Liste introuvable");
        }
    }

    @Override
    public ListeContexteDto getListeAvecContexte(Long id, String email) throws ListeNotFoundException {
        ListeDto liste = this.getListeById(id);

        ListeContexteDto listeContexte = new ListeContexteDto(liste);

        listeContexte.setEstProprietaire(liste.getProprietaire().equals(email));

        this.remplacerEmailsParPseudo(listeContexte);

        if (email != null) {
            if (!listeContexte.isEstProprietaire()) {
                listeContexte.setEstFavoris(
                        this.getListeFavorisOfEmail(email)
                                .stream()
                                .anyMatch(f -> Objects.equals(f.getIdListe(), id))
                );
            }
        } else {
            // Si non connecté, on anonymise les infos
            listeContexte.setEstFavoris(false);
            listeContexte.getListeObjet()
                    .forEach(objetDto -> {
                        objetDto.setDetenteur(null);
                        objetDto.setEstPrit(null);
                        objetDto.setPseudoDetenteur(null);
                    });
        }

        return listeContexte;
    }

    private List<String> getListeOfEmailFromListeFavorisDao(List<FavorisEntity> favorisEntityList) {
        return Optional.ofNullable(favorisEntityList)
                .orElse(new ArrayList<>())
                .stream()
                .map(FavorisEntity::getEmail)
                .toList();
    }

    private void envoyerEmailToListe(List<String> listOfEmail, String body, String subject) {
        for (String email : listOfEmail) {
            mailService.sendEmail(email, subject, body);
        }
    }

}
