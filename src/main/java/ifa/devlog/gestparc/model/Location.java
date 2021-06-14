package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Location {
    public static final int Demande=0;
    public static final int Valide=1;
    public static final int EnPret=2;
    public static final int Finalisée=3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueHistorique.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueRetour.class})
    private int id;

    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueRetour.class})
    @ManyToOne
    private Cadre cadre;

    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueRetour.class})
    private int etat;
    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueRetour.class})
    private LocalDate date_debut;
    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueRetour.class})
    private LocalDate date_retour;

    @ManyToOne
    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueRetour.class})
    private TypeMateriel typeMateriel;
    @ManyToOne
    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueRetour.class})
    private Materiel materiel;
    @ManyToOne
    @JsonView({CustomJsonView.VueLocation.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueHistorique.class,
            CustomJsonView.VueRetour.class})
    private Utilisateur utilisateur;

    public int getId() {
        return id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Boolean getValide() {
        return this.etat>0;
    }

    public Integer getEtat() {
        return this.etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public LocalDate getDate_retour() {
        return date_retour;
    }

    public void setDate_retour(LocalDate date_retour) {
        this.date_retour = date_retour;
    }

    public TypeMateriel getTypeMateriel() {
        return typeMateriel;
    }

    public void setTypeMateriel(TypeMateriel typeMateriel) {
        this.typeMateriel = typeMateriel;
    }

    public void setCadre(Cadre cadre) {
        this.cadre = cadre;
    }

    public Cadre getCadre() {
        return cadre;
    }
}


