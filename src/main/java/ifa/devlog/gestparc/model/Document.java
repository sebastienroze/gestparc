package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueDocument.class})
    private int id;
    @JsonView({CustomJsonView.VueDocument.class})
    private String nom;
    @JsonView({CustomJsonView.VueDocument.class})
    private String extension;
    private String fileName;
    @JsonView({CustomJsonView.VueDocument.class})
    private String originalFilename;
    @ManyToOne
    @JsonView({CustomJsonView.VueDocument.class})
    private Materiel materiel;
    @ManyToOne
    @JsonView({CustomJsonView.VueDocument.class})
    private Reparation reparation;
    @ManyToOne
    @JsonView({CustomJsonView.VueDocument.class})
    private Retour retour;

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }

    public Reparation getReparation() {
        return reparation;
    }

    public void setReparation(Reparation reparation) {
        this.reparation = reparation;
    }

    public Retour getRetour() {
        return retour;
    }

    public void setRetour(Retour retour) {
        this.retour = retour;
    }
}
