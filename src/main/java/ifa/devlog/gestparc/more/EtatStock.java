package ifa.devlog.gestparc.more;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import ifa.devlog.gestparc.dao.LocationDao;
import ifa.devlog.gestparc.dao.MaterielDao;
import ifa.devlog.gestparc.dao.ReparationDao;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Reparation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class EtatStock {
    public void generate(String documentName,MaterielDao materielDao, LocationDao locationDao, ReparationDao reparationDao) {
        System.out.println("generation "+documentName);
        List<Materiel> materiels = materielDao.findAll();
        try {
            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            PdfWriter.getInstance(document, new FileOutputStream(documentName));
            document.open();
            Font fontTitre = FontFactory.getFont(FontFactory.TIMES_BOLD, 16, BaseColor.BLACK);
            Font font = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Paragraph titre = new Paragraph();
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setFont(fontTitre);
            titre.add("Etat des stocks du " + LocalDate.now());
            document.add(titre);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            Paragraph paragraph = new Paragraph();
            paragraph.setFont(font);
            paragraph.setTabSettings(new TabSettings(100));
            paragraph.add("Reférence");
            paragraph.add(Chunk.TABBING);
            paragraph.add("Nom");
            paragraph.add(Chunk.TABBING);
            paragraph.add("Etat");
            paragraph.add(Chunk.TABBING);
            paragraph.add("Situation");
            paragraph.add(Chunk.NEWLINE);
            for (Materiel materiel:materiels) {
                paragraph.add(materiel.getReference());
                paragraph.add(Chunk.TABBING);
                paragraph.add(materiel.getNom());
                paragraph.add(Chunk.TABBING);
                paragraph.add(materiel.getEtat());
                paragraph.add(Chunk.TABBING);

                List<Reparation> reparations = reparationDao.findByMaterielAndEtat(materiel,0);
                if (reparations.size()>0) {
                    paragraph.add("En réparation " + reparations.get(0).getDate_retour());
                } else {
                    List<Location> locations = locationDao.findByMaterielAndEtat(materiel, Location.EnPret);
                    if (locations.size() > 0) {
                        Location location = locations.get(0);
                        paragraph.add("Sortit " + location.getUtilisateur().getLogin());
                    } else {
                        paragraph.add("En stock");
                    }
                }
                paragraph.add(Chunk.NEWLINE);
            }
            document.add(paragraph);
            document.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}
