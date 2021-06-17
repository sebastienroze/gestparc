package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Reparation {
    public static final int Demande=0;
    public static final int Finalisée=1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueReparation.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueHistorique.class
    })
    private int id;
    @JsonView({CustomJsonView.VueReparation.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueHistorique.class
    })
    private int etat;
    @ManyToOne
    @JsonView({CustomJsonView.VueReparation.class,
            CustomJsonView.VueAlerte.class,
    })
    private Materiel materiel;
    @JsonView({CustomJsonView.VueReparation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueMaterielDetail.class})
    private LocalDate date_envoi;
    @JsonView({CustomJsonView.VueReparation.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueHistorique.class})
    private LocalDate date_retour;
    @JsonView({CustomJsonView.VueReparation.class})
    private String etatEntrant;
    @JsonView({CustomJsonView.VueReparation.class,
            CustomJsonView.VueAlerte.class
    })
    private String etatCasse;
    @JsonView({CustomJsonView.VueReparation.class})
    private String etatRepare;

    public Reparation() {
    }

    public Reparation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public LocalDate getDate_envoi() {
        return date_envoi;
    }

    public void setDate_envoi(LocalDate date_envoi) {
        this.date_envoi = date_envoi;
    }

    public LocalDate getDate_retour() {
        return date_retour;
    }

    public void setDate_retour(LocalDate date_retour) {
        this.date_retour = date_retour;
    }

    public String getEtatEntrant() {
        return etatEntrant;
    }

    public void setEtatEntrant(String etatEntrant) {
        this.etatEntrant = etatEntrant;
    }

    public String getEtatCasse() {
        return etatCasse;
    }

    public void setEtatCasse(String etatCasse) {
        this.etatCasse = etatCasse;
    }

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }

    public String getEtatRepare() {
        return etatRepare;
    }

    public void setEtatRepare(String etatRepare) {
        this.etatRepare = etatRepare;
    }
}
