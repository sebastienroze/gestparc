package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Retour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueRetour.class,CustomJsonView.VueHistorique.class})
    private int id;

    @JsonView({CustomJsonView.VueRetour.class})
    private String audit;
    @JsonView({CustomJsonView.VueRetour.class})
    private String etatEntrant;
    @JsonView({CustomJsonView.VueRetour.class})
    private String etatSortant;

    @ManyToOne
    @JsonView({CustomJsonView.VueRetour.class,CustomJsonView.VueHistorique.class})
    private Location location;

    public int getId() {
        return id;
    }
    public Retour() {
    }

    public Retour(int id) {
        this.id = id;
    }

    public Integer getLocationId() {
        if (location == null) return null;
        return location.getId();
    }

    public void setEtatEntrant(String etatEntrant) {
        this.etatEntrant = etatEntrant;
    }

    public String getEtatSortant() {
        return etatSortant;
    }

    public Location getLocation() {
        return location;
    }

}
