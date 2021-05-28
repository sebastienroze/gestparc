package ifa.devlog.gestparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)

public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private int id;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String login;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String mot_de_passe;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String nom;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String prenom;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String cp;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String ville;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String telephone;
    @JsonView({CustomJsonView.VueUtilisateur.class})
    private String email;
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return mot_de_passe;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonView({CustomJsonView.VueUtilisateur.class})
    @JoinTable(
            name="utilisateur_role",
            joinColumns = @JoinColumn(name= "utilisateur_id",referencedColumnName  ="id"),
            inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName  ="id")
    )
    private List<Role> listeRole = new ArrayList<>();

    public int getId() {
        return id;
    }
    public List<Role> getListeRole() {
        return listeRole;
    }
    public void setPassword(String password) {
        this.mot_de_passe = password;
    }

}
