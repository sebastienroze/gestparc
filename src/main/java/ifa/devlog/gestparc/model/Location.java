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

}


