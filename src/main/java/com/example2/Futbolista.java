package com.example2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

public class Futbolista {

    private String nom;
    private String cognoms;
    private Integer edat;
    private ArrayList<String> demarcacio;
    private Boolean internacional;

    public Futbolista() {
    }

    public Futbolista(String nom, String cognoms, Integer edat, ArrayList<String> demarcacio,
            Boolean internacional) {
        this.nom = nom;
        this.cognoms = cognoms;
        this.edat = edat;
        this.demarcacio = demarcacio;
        this.internacional = internacional;
    }

    // Transformem un Document que ens dóna MongoDB a un Objecte Java
    public Futbolista(Document documentFutbolista) {
        this.nom = documentFutbolista.getString("nom");
        this.cognoms = documentFutbolista.getString("cognoms");
        this.edat = documentFutbolista.getInteger("edat");

        // Amb compte quan traballem amb Arrays o Llistes
        List<Object> listDemarcacions = Arrays.asList(documentFutbolista.get("demarcacio"));
        this.demarcacio = new ArrayList<String>();
        for (Object demarc : listDemarcacions) {
            this.demarcacio.add(demarc.toString());
        }

        this.internacional = documentFutbolista.getBoolean("internacional");
    }

    public Document toDocumentFutbolista() {
        // Creem una instancia Document
        Document documentFutbolista = new Document();
        documentFutbolista.append("nom", this.getNom());
        documentFutbolista.append("cognoms", this.getCognoms());
        documentFutbolista.append("edat", this.getEdat());
        documentFutbolista.append("demarcacio", this.getDemarcacio());
        documentFutbolista.append("internacional", this.getInternacional());
        return documentFutbolista;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognoms() {
        return cognoms;
    }

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public Integer getEdat() {
        return edat;
    }

    public void setEdat(Integer edat) {
        this.edat = edat;
    }

    public ArrayList<String> getDemarcacio() {
        return demarcacio;
    }

    public void setDemarcacio(ArrayList<String> demarcacio) {
        this.demarcacio = demarcacio;
    }

    public Boolean getInternacional() {
        return internacional;
    }

    public void setInternacional(Boolean internacional) {
        this.internacional = internacional;
    }

    @Override
    public String toString() {
        return "nom: " + this.getNom() + " " + this.getCognoms() + " / edat: " + this.edat + " / Demarcació: "
                + this.demarcacio.toString() + " / Internacional: " + this.internacional;
    }
}