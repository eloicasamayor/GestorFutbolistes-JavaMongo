package com.example2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;

public class AdminFutbolistes {

    MongoClient mongoClient = null;
    MongoDatabase db = null;
    MongoCollection<Document> collection = null;

    public void Connecta() {
        // PAS 1: Conexió al Server de MongoDB Pasant-li el host i el port
        mongoClient = new MongoClient("localhost", 27017);
        // PAS 2: Conexió a la base de dades
        db = mongoClient.getDatabase("Futbol");
        // PAS 3: Obtenim una colecció per a treballar amb ella
        collection = db.getCollection("Futbolistes");
    }

    public void inserirFutbolista(Futbolista fut) {
        try {
            collection.insertOne(fut.toDocumentFutbolista());
            System.out.println("document afegit: " + fut);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void inserirFutbolistesDefault() {
        ArrayList<Futbolista> futbolistes = new ArrayList<Futbolista>();

        futbolistes.add(new Futbolista("Iker", "Casillas", 33, new ArrayList<String>(Arrays.asList("Porter")), true));
        futbolistes.add(new Futbolista("Carles", "Puyol", 36,
                new ArrayList<String>(Arrays.asList("Central", "Lateral")), true));
        futbolistes.add(new Futbolista("Sergio", "Ramos", 28,
                new ArrayList<String>(Arrays.asList("Lateral", "Central")), true));
        futbolistes.add(new Futbolista("Andrés", "Iniesta", 30,
                new ArrayList<String>(Arrays.asList("Centrocampista", "Davanter")), true));
        futbolistes
                .add(new Futbolista("Fernando", "Torres", 30, new ArrayList<String>(Arrays.asList("Davanter")), true));
        futbolistes
                .add(new Futbolista("Leo", " Baptistao", 22, new ArrayList<String>(Arrays.asList("Davanter")), false));

        for (Futbolista fut : futbolistes) {
            collection.insertOne(fut.toDocumentFutbolista());
        }
    }

    public void ContarTotsElsFutbolistes() {
        // Conta tots els documents de la col·lecció
        int numDocuments = (int) collection.countDocuments();
        System.out.println("Número de documents en la colecció Futbolistes: " + numDocuments + "\n");
    }

    public void mostrarTotsElsFutbolistes() {
        // Busca tots els documents de la colecció i els mostra per consola
        FindIterable<Document> docs = collection.find();
        try {
            for (Document doc : docs) {
                System.out.println(doc.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void mostrarFutbolistesDavanters() {
        // Busca els futbolistes davanters i els mostra per consola
        System.out.println("\nFutbolistes que juguen en la posició de Davanter");

        Document query = new Document("demarcacio", new Document("$regex", "Davanter"));
        FindIterable<Document> docs = collection.find(query);
        try {
            for (Document doc : docs) {
                Futbolista futbolista = new Futbolista(doc);
                System.out.println(futbolista.toString());
            }
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    public void mostrarFutbolistesFiltre(Document filtre) {
        FindIterable<Document> docs = collection.find(filtre);
        System.out.println(collection.countDocuments(filtre) + " futbolistes cumpleixen els filtres de búsqueda");
        try {
            for (Document doc : docs) {
                Futbolista futbolista = new Futbolista(doc);
                System.out.println(futbolista.toString());
            }
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    public Boolean aquestFutbolistaExisteix(String nom, String cognom) {
        Document query = new Document("nom", nom).append("cognoms", cognom);
        return collection.countDocuments(query) > 0;
    }

    public int saberEdatFutbolista(String nom, String cognom) {
        Document query = new Document("nom", nom).append("cognoms", cognom);
        FindIterable<Document> docs = collection.find(query);
        int edat = 0;
        try {
            Futbolista futbolista = new Futbolista(docs.first());
            edat = futbolista.getEdat();
            return edat;
        } catch (Exception e) {
            System.out.println("Error:" + e);
            System.out.println("No s'ha trobat el futbolista");
        }
        return edat;
    }

    public void mostrarDemarcacionsFutbolista(String nom, String cognom) {
        // Busca els futbolistes davanters i els mostra per consola
        System.out.println("\ndemarcacions de " + nom + " " + cognom);
        Document sql = new Document("nom", nom).append("cognoms", cognom);
        FindIterable<Document> docs = collection.find(sql);
        try {
            for (Document doc : docs) {
                Futbolista futbolista = new Futbolista(doc);
                List<String> demarcacions = futbolista.getDemarcacio();
                for (int i = 0; i < demarcacions.size(); i++) {
                    System.out.println("demarcacio = " + demarcacions.toArray()[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    public void EditarEdat(String nom, String cognom, int novaEdat) {
        Document find = new Document("nom", nom).append("cognoms", cognom);
        Document updated = new Document("$set", new Document("edat", novaEdat));
        UpdateResult updateresult = collection.updateMany(find, updated);
        long numDocs = updateresult.getModifiedCount();
        System.out.println("S'han actualitzat " + numDocs + " documents.");
    }

    public void borrarFutbolista(String nom, String cognom) {
        // Borra tots els futbolistes que siguin internacionals (internacional = true)
        Document findDoc = new Document("nom", nom).append("cognoms", cognom);
        DeleteResult deletedDocs = collection.deleteMany(findDoc);
        if (deletedDocs.getDeletedCount() == 0) {
            System.out.println("no s'han trobat futbolistes amb aquest nom i cognoms");
        } else {
            System.out.println(deletedDocs.getDeletedCount() + " deleted documents");
        }

    }

    public void BorrarElsJugadorsInternacionals() {
        // Borra tots els futbolistes que siguin internacionals (internacional = true)
        Document findDoc = new Document("internacional", true);
        collection.deleteMany(findDoc);
    }

    public void BorrarTotsElsJugadors() {
        // Borra tots els futbolistes
        Document findDoc = new Document();
        collection.deleteMany(findDoc);
    }

    public void TancarConnexioMongo() {// Tancar la conexio
        mongoClient.close();
    }
}