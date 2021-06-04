package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueCadre.class})
    private int id;

    @JsonView({CustomJsonView.VueLocation.class})
    private Boolean valide;
    @JsonView({CustomJsonView.VueLocation.class})
    private LocalDate date_debut;
    @JsonView({CustomJsonView.VueLocation.class})
    private LocalDate date_retour;

    @ManyToOne
    @JsonView({CustomJsonView.VueLocation.class})
    private TypeMateriel typeMateriel;
    @ManyToOne
    @JsonView({CustomJsonView.VueLocation.class})
    private Materiel materiel;
    @ManyToOne
    @JsonView({CustomJsonView.VueLocation.class})
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
        return valide;
    }
    public void setValide(boolean valide) {
        this.valide = valide;
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
}


