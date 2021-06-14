package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class TypeMateriel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({
            CustomJsonView.VueTypeMateriel.class,
            CustomJsonView.VueMateriel.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueLocation.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueRetour.class
    })
    private int id;

    @JsonView({
            CustomJsonView.VueTypeMateriel.class,
            CustomJsonView.VueMateriel.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueLocation.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueRetour.class})
    private String description;

/*
mappedBy reference an unknown target entity property: ifa.devlog.gestparc.model.Materiel.materiel in ifa.devlog.gestparc.model.TypeMateriel.listeMateriel

    @OneToMany(mappedBy = "materiel")
    @JsonView({CustomJsonView.VueTypeMateriel.class})
    private List<Materiel> listeMateriel = new ArrayList<>();
*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
}
