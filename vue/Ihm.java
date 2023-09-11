package vue;


import modele.Joueur;

import java.util.Scanner;

/**
 * Ihm
 */

public class Ihm {

    public static void afficher(String s){
         System.out.println(s);
    }

    public static String saisie(){
        Scanner sc=new Scanner(System.in);
        return sc.nextLine();
    }

    public static String saisieCoup() throws IllegalArgumentException{
        Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
        if(s.length()!=3)
            throw new IllegalArgumentException("erreur de saisie");
        return s;
    }

    public static int saisieTaille() {
        Scanner sc = new Scanner(System.in);
        int taille;
        while (!sc.hasNextInt()) {
            afficher("La valeur saisie n'est pas un nombre !");
            afficher("Veuillez saisir un nombre : ");
            sc.next();
        }
        taille = sc.nextInt();
        while(taille>8 || taille<4){
            afficher("la taille doit etre entre 4 et 8 ");
            taille=saisieTaille();
        }
        return taille;
    }

    public static void saisieP(){
        boolean test = true;
        // TANT QUE la réponse donnée n'est pas p ou P
        while(test) {
            // String reponse est la réponse saisie par le joueur
            String reponse = saisie();
            // Si la réponse est p ou P
            if (reponse.equals("p") || reponse.equals("P")) {
                test=false;
            }
            // SI la saisie n'est pas correcte
            else {
                afficher("Veuillez saisir 'p' ou 'P' !");
            }
        }
    }

    public void afficherRejouer(){
        afficher("Souhaitez vous refaire une partie ? \n" +
                "Saisissez 'y' pour oui ou 'n' pour non.");
    }

    public void afficherSaisieIncorrecte(){
        afficher("Veuillez saisir 'y' pour oui ou 'n' pour non.");
    }

    public void afficherScoreFinal(Joueur j1,Joueur j2){
        afficher(j1.getNom() + " " + j1.getNbPartiesGagnees() + "-" + j2.getNbPartiesGagnees() + " " + j2.getNom());
    }

    public void afficherNomVainqueur(Joueur j){
        afficher("Vainqueur : " + j.getNom());
    }

    public void afficherEgalite(){
        afficher("ex aequo");
    }

}