package ifa.devlog.gestparc.more;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import ifa.devlog.gestparc.model.Location;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class Bordereau {
    Location location;

    public Bordereau(Location location) {
        this.location = location;
    }

    public void generate() {
        String documentName = "Bordereau " + location.getId() + ".pdf";
        System.out.println("generation "+documentName);

        if (location.getEtat() != Location.Demande) try {
            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            PdfWriter.getInstance(document, new FileOutputStream(documentName));
            document.open();
            Font fontTitre = FontFactory.getFont(FontFactory.TIMES_BOLD, 16, BaseColor.BLACK);
            Font font = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Paragraph titre = new Paragraph();
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setFont(fontTitre);
            titre.add("Bordereau de pret " + location.getId() + " du " + LocalDate.now());
            document.add(titre);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            Paragraph paragraph = new Paragraph();
            paragraph.setFont(font);
            paragraph.setTabSettings(new TabSettings(200));
            paragraph.add("Emprunt du " + location.getDate_debut() + " au " + location.getDate_retour());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Cadre :");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getCadre().getDescription());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Matériel :");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getTypeMateriel().getDescription());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Nom du matériel :");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getMateriel().getNom());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Référence du matériel :");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getMateriel().getReference());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Etat du matériel :");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getMateriel().getEtat());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Locataire : ");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getUtilisateur().getNom() + " " + location.getUtilisateur().getPrenom());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Localisation : ");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getUtilisateur().getCp() + " " + location.getUtilisateur().getVille());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Email :");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getUtilisateur().getEmail());
            paragraph.add(Chunk.NEWLINE);
            paragraph.add("Téléphone :");
            paragraph.add(Chunk.TABBING);
            paragraph.add(location.getUtilisateur().getTelephone());
            document.add(paragraph);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
