package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({
            CustomJsonView.VueMateriel.class,
            CustomJsonView.VueLocation.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueReparation.class,
            CustomJsonView.VueRetour.class})
    private int id;
    @JsonView({CustomJsonView.VueMateriel.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueLocation.class,
            CustomJsonView.VueReparation.class,
            CustomJsonView.VueRetour.class})
    private String nom;

    @JsonView({CustomJsonView.VueMateriel.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueLocation.class,
            CustomJsonView.VueReparation.class,
            CustomJsonView.VueRetour.class})
    private String reference;

    @JsonView({
            CustomJsonView.VueMateriel.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueLocation.class,
            CustomJsonView.VueRetour.class
    })
    private String etat;

    @ManyToOne
    @JsonView({CustomJsonView.VueMateriel.class,
            CustomJsonView.VueMaterielDetail.class
    })
    private TypeMateriel typeMateriel;

    /*
    @OneToMany(mappedBy = "materiel")
    @JsonView({CustomJsonView.VueMateriel.class})
    private List<Location> locations ;
*/
    /*
    @JsonView({CustomJsonView.VueMateriel.class})
    @Transient
    private Location location=null;
*/
    public Materiel() {
    }

    public Materiel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
/*
    public void setLocation(Location location) {
        this.location = location;
    }
    */

    public String getNom() {
        return nom;
    }

    public String getReference() {
        return reference;
    }
}
