package com.example2;

import java.io.FileNotFoundException;
import java.util.Scanner;
import org.bson.Document;

public class GestorFutbolistes {
    AdminFutbolistes adminFutbolistes = new AdminFutbolistes();
    Scanner lector = new Scanner(System.in);
    Boolean programRunning = false;

    public static void main(String args[]) throws FileNotFoundException {
        GestorFutbolistes gestorFutbolistes = new GestorFutbolistes();
        gestorFutbolistes.start();
    }

    void start() {
        adminFutbolistes.Connecta();
        programRunning = true;
        menu();
    }

    String preguntarString(String pregunta) {
        System.out.print(pregunta);
        String resposta = lector.nextLine();
        return resposta;
    }

    int preguntarInt(String pregunta) {
        System.out.print(pregunta);
        int resposta = 0;
        try {
            resposta = Integer.parseInt(lector.nextLine());
        } catch (Exception e) {
            System.out.println("no t'he entès. Prova-ho de nou, has d'introduir un número enter.");
            preguntarInt(pregunta);
        }
        return resposta;
    }

    Boolean preguntarBoolean(String pregunta) {
        System.out.print(pregunta);
        return Boolean.valueOf(lector.nextLine());
    }

    void menu() {
        while (programRunning) {
            String ordre = preguntarString("[futbolistes]");
            switch (ordre) {
                case "demarcacio":
                    demarcacio();
                    break;
                case "help":
                    help();
                    break;
                case "llista":
                    llista();
                    break;
                case "llistafiltres":
                    llistafiltres();
                    break;
                case "afegir":
                    afegir();
                    break;
                case "borrar":
                    borrar();
                    break;
                case "edat":
                    edat();
                    break;
                case "editaredat":
                    editaredat();
                    break;
                case "quit":
                    programRunning = false;
                    break;
                default:
                    System.out.println("no s'ha trobat l'ordre");
            }
            menu();
        }
    }

    void afegir() {

        String nom = preguntarString("nom");
        String cognoms = preguntarString("cognom");
        int edat = preguntarInt("edat");
        String demarcacio = preguntarString("demarcacio");
        Boolean internacional = preguntarBoolean("internacional?<true>/<false>");
        Document docFutbol = new Document("nom", nom).append("cognoms", cognoms).append("edat", edat)
                .append("demarcacio", demarcacio)
                .append("internacional", internacional);
        Futbolista fut = new Futbolista(docFutbol);
        adminFutbolistes.inserirFutbolista(fut);
    }

    void llista() {
        adminFutbolistes.mostrarTotsElsFutbolistes();
    }

    void edat() {
        String nom = preguntarString("Nom del futbolista de qui vols saber l'edat: ");
        String cognom = preguntarString("Cognom del futbolista de qui vols saber l'edat: ");
        if (adminFutbolistes.aquestFutbolistaExisteix(nom, cognom)) {
            int edat = adminFutbolistes.saberEdatFutbolista(nom, cognom);
            System.out.println(nom + " " + cognom + " té " + edat + " anys.");
        } else {
            System.out.println("No existeix el futbolista " + nom + " " + cognom);
        }
    }

    void editaredat() {
        String nom = preguntarString("Nom del futbolista de qui vols actualitzar l'edat: ");
        String cognom = preguntarString("Cognom del futbolista de qui vols actualitzar l'edat: ");

        if (adminFutbolistes.aquestFutbolistaExisteix(nom, cognom)) {
            adminFutbolistes.saberEdatFutbolista(nom, cognom);
            int novaEdat = preguntarInt("Nova edat: ");
            adminFutbolistes.EditarEdat(nom, cognom, novaEdat);
        }
        System.out.println("Aquest futbolista no existeix");
    }

    void llistafiltres() {
        int edatMinima = preguntarInt("edat minima: ");
        int edatMaxima = preguntarInt("edat maxima: ");
        boolean internacional = preguntarBoolean("internacional? [true/false]: ");
        Document filtre = new Document("edat", new Document("$gt", edatMinima).append("$lt", edatMaxima))
                .append("internacional", internacional);
        adminFutbolistes.mostrarFutbolistesFiltre(filtre);
    }

    void help() {
        System.out.println("> help:         ajuda");
        System.out.println("> llista:       llista");
        System.out.println("> llistafiltres:llista amb filtres");
        System.out.println("> afegir:       afegir");
        System.out.println("> borrar:       borrar futbolista");
        System.out.println("> edat:         mostrar l'edat");
        System.out.println("> demarcacio:   mostrar demarcacio");
        System.out.println("> editaredat:   editar edat");
        System.out.println("> quit:         sortir");
    }

    void borrar() {
        String nom = preguntarString("nom del futbolista");
        String cognom = preguntarString("nom del futbolista");
        adminFutbolistes.borrarFutbolista(nom, cognom);
    }

    void demarcacio() {
        String nom = preguntarString("nom del futbolista");
        String cognom = preguntarString("nom del futbolista");
        adminFutbolistes.mostrarDemarcacionsFutbolista(nom, cognom);
    }
}
