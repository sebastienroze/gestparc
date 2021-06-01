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
    @JsonView({CustomJsonView.VueMateriel.class})
    private int id;
    @JsonView({CustomJsonView.VueMateriel.class})
    private String Nom;

    @JsonView({CustomJsonView.VueMateriel.class})
    private String Reference;

    @JsonView({CustomJsonView.VueMateriel.class})
    private Integer Etat;

    @ManyToOne
    @JsonView({CustomJsonView.VueMateriel.class})
    private TypeMateriel typeMateriel;

    public int getId() {
        return id;
    }

}
