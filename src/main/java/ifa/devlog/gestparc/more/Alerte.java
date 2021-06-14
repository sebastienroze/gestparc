package ifa.devlog.gestparc.more;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Reparation;
import ifa.devlog.gestparc.view.CustomJsonView;

public class Alerte {
    @JsonView(CustomJsonView.VueAlerte.class)
    private String texte;
    @JsonView(CustomJsonView.VueAlerte.class)
    private Location location;
    @JsonView(CustomJsonView.VueAlerte.class)
    private Reparation reparation;

    public Alerte(String texte, Location location, Reparation reparation) {
        this.texte = texte;
        this.location = location;
        this.reparation = reparation;
    }

}
