package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Cadre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueCadre.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueLocation.class})
    private int id;

    @JsonView({CustomJsonView.VueCadre.class,
            CustomJsonView.VueAlerte.class,
            CustomJsonView.VueMaterielDetail.class,
            CustomJsonView.VueLocation.class})
    private String description;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
