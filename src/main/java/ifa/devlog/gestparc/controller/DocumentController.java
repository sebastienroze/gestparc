package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.DocumentDao;
import ifa.devlog.gestparc.model.Document;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Reparation;
import ifa.devlog.gestparc.model.Retour;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin

public class DocumentController {
    final String dossierFichier = "/home/doc/doc/";

    private DocumentDao documentDao;

    @Autowired
    DocumentController(
            DocumentDao documentDao
    ) {
        this.documentDao = documentDao;
    }

    @GetMapping("/admin/documents")
    public ResponseEntity<List<Document>> getDocuments() {
        return ResponseEntity.ok(documentDao.findAll());
    }
    @GetMapping("/admin/documents/materiel/{id}")
    public ResponseEntity<List<Document>> getDocumentsMateriel(@PathVariable int id) {
        return ResponseEntity.ok(documentDao.findByMateriel(new Materiel(id)));
    }
    @GetMapping("/admin/documents/reparation/{id}")
    public ResponseEntity<List<Document>> getDocumentsReparation(@PathVariable int id) {
        return ResponseEntity.ok(documentDao.findByReparation(new Reparation(id)));
    }

    @GetMapping("/admin/documents/retour/{id}")
    public ResponseEntity<List<Document>> getDocumentsRetour(@PathVariable int id) {
        return ResponseEntity.ok(documentDao.findByRetour(new Retour(id)));
    }

    @GetMapping("/docs/document/fichier/{id}/*")
    public ResponseEntity<byte[]> getImageAsResource(@PathVariable int id) {
        Optional<Document> optionalDocument = documentDao.findById(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            try {
                String nomDeFichier = document.getFileName()+"."+document.getExtension();
                String mimeType = Files.probeContentType(new File(nomDeFichier).toPath());
                HttpHeaders headers = new HttpHeaders();
                //note si mimeType est null c'est qe le fichier n'a pas d'extension ou n'existe pas
                if(mimeType != null) {
                    headers.setContentType(MediaType.valueOf(mimeType));
                }
                headers.setCacheControl(CacheControl.noCache().getHeaderValue());
                byte[] media = this.getFileFromUploadFolder(nomDeFichier);

                return new ResponseEntity<>(media, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur : le fichier a une extension inconnue ou n'existe pas");
            }
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/admin/document/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable int id) {
        Optional<Document> document = documentDao.findById(id);
        if (document.isPresent()) {
            return ResponseEntity.ok(document.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @PostMapping("/admin/document/new")
    public ResponseEntity<Document> create(
            @RequestParam("nom") String nom,
            @RequestParam("materiel") String idMateriel,
            @RequestParam("reparation") String idReparation,
            @RequestParam("retour") String idRetour,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Document document = new Document();
        document = updateDocument(document, nom, idMateriel, idReparation,idRetour, file);
        if (document == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
        document = documentDao.saveAndFlush(document);
        return ResponseEntity.status(HttpStatus.OK).body(document);
    }

    @DeleteMapping("/admin/document/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id) {
        Optional<Document> optionalDocument = this.documentDao.findById(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            if (document.getFileName()!=null && document.getExtension()!=null) {
                StringBuffer fileName = new StringBuffer();
                fileName.append(document.getFileName());
                fileName.append("." + document.getExtension());
                Path pathFile = Paths.get(dossierFichier + fileName.toString());
                try {
                    Files.delete(pathFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                documentDao.deleteById(id);
                return ResponseEntity.ok(id);
            }
        }
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/admin/document/update")
    public ResponseEntity<Document> uploadMultipartFile(
            @RequestParam("id") int id,
            @RequestParam("nom") String nom,
            @RequestParam("materiel") String idMateriel,
            @RequestParam("reparation") String idReparation,
            @RequestParam("retour") String idRetour,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Optional<Document> optionalDocument = this.documentDao.findById(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            document = updateDocument(document, nom, idMateriel, idReparation,idRetour, file);
            if (document == null) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
            }
            documentDao.saveAndFlush(document);
            return ResponseEntity.status(HttpStatus.OK).body(document);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
    }

    Document updateDocument(Document document, String nom,
                            String idMateriel, String idReparation, String idRetour,
                            MultipartFile file) {
        if (file != null) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            List<String> extensionAccepte = Arrays.asList(
                    "bmp", "gif", "png", "jpg", "jpeg", "html", "pdf");
            if (!extensionAccepte.contains(extension)) {
                return null;
            }
            document.setOriginalFilename(file.getOriginalFilename());
            if (document.getFileName() == null) {
                document.setFileName(UUID.randomUUID().toString().replaceAll("-", ""));
            } else {
                StringBuffer fileName = new StringBuffer();
                fileName.append(document.getFileName());
                fileName.append("." + document.getExtension());
                Path pathFile  = Paths.get(dossierFichier+fileName.toString());
                try {
                    Files.delete(pathFile );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            document.setExtension(extension);
            StringBuffer fileName = new StringBuffer();
            fileName.append(document.getFileName());
            fileName.append("." + document.getExtension());
            try {
                InputStream inputStream = file.getInputStream();
                uploadToLocalFileSystem(file, fileName.toString());
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        }
        document.setNom(nom);
        if (!"".equals(idMateriel)) {
            document.setMateriel(new Materiel(Integer.parseInt(idMateriel)));
        }
        if (!"".equals(idReparation)) {
            document.setReparation(new Reparation(Integer.parseInt(idReparation)));
        }
        if (!"".equals(idRetour)) {
            document.setRetour(new Retour(Integer.parseInt(idRetour)));
        }
        return document;
    }

    public void uploadToLocalFileSystem(MultipartFile multipartFile, String fileName) throws IOException {
        uploadToLocalFileSystem(multipartFile.getInputStream(), fileName);
    }

    public void uploadToLocalFileSystem(InputStream inputStream, String fileName) throws IOException {
        Path storageDirectory = Paths.get(dossierFichier);
        if(!Files.exists(storageDirectory)){ // if the folder does not exist
            try {
                Files.createDirectories(storageDirectory); // we create the directory in the given storage directory path
            }catch (Exception e){
                e.printStackTrace();// print the exception
            }
        }
        Path destination = Paths.get(dossierFichier + fileName);
        Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);// we are Copying all bytes from an input stream to a file
    }

    public byte[] getFileFromUploadFolder(String fileName) throws IOException, FileNotFoundException {
        if(!nomFichierValide(fileName)) {
            throw new IOException("nom de fichier incorrect, uniquement : - _ . lettres et chiffres (pas de slash)");
        }

        Path destination = Paths.get(dossierFichier+fileName);// retrieve the image by its name

        try {
            return IOUtils.toByteArray(destination.toUri());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    public boolean nomFichierValide(String fileName) {
        return fileName.matches("[-_A-Za-z0-9.]*");
    }

}