package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueHistorique.class})
    private int id;

    @JsonView({CustomJsonView.VueHistorique.class})
    private LocalDate date;

    @JsonView({CustomJsonView.VueHistorique.class})
    private String etat;

    @ManyToOne
    @JsonView({CustomJsonView.VueHistorique.class})
    private Materiel materiel;

    @ManyToOne
    @JsonView({CustomJsonView.VueHistorique.class})
    private Location location;

    @ManyToOne
    @JsonView({CustomJsonView.VueHistorique.class})
    private Retour retour;

    @ManyToOne
    @JsonView({CustomJsonView.VueHistorique.class})
    private Reparation reparation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Retour getRetour() {
        return retour;
    }

    public void setRetour(Retour retour) {
        this.retour = retour;
    }

    public Reparation getReparation() {
        return reparation;
    }

    public void setReparation(Reparation reparation) {
        this.reparation = reparation;
    }
}
