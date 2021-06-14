package ifa.devlog.gestparc.more;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Reparation;
import ifa.devlog.gestparc.view.CustomJsonView;

public class MaterielDetail {
    @JsonView(CustomJsonView.VueMaterielDetail.class)
    private Materiel materiel;
    @JsonView(CustomJsonView.VueMaterielDetail.class)
    private Location location;
    @JsonView(CustomJsonView.VueMaterielDetail.class)
    private Reparation reparation;

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

    public Reparation getReparation() {
        return reparation;
    }

    public void setReparation(Reparation reparation) {
        this.reparation = reparation;
    }
}
