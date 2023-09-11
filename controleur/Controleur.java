package controleur;
import controleur.partieDeJeu.Jeu;
import modele.*;
import vue.Ihm;


public class Controleur {

    private final Ihm ihm;


    public Controleur(Ihm ihm) {
        this.ihm = ihm;
    }
    private Joueur j1,j2;

    public void jouer(Jeu jeu,boolean solo) {
        boolean jeuOk = true;
        Joueur[] joueurs=jeu.initJeu(solo);
        j1=joueurs[0];
        j2=joueurs[1];
        while (jeuOk) {
            initialiser(jeu);
            jeu.jouerPartie();
            jeu.afficherVainqueurPartie();
            jeuOk = rejouerPartie(jeu,jeuOk);
        }
        afficherScoreFinal();
    }

    public void initialiser(Jeu jeu) {
        jeu.initJoueurs();
        jeu.getPlateau().initialiserPions();
    }



    public boolean rejouerPartie(Jeu j,boolean jeu) {
        // Le systeme demande au joueurs si ils souhaitent rejouer
        ihm.afficherRejouer();
        boolean test = true;
        // TANT QUE la réponse donnée n'est pas oui('y') ou non('n')
        while (test) {
            // String reponse est la réponse saisie par le joueur
            String reponse = Ihm.saisie();

            // SI les joueurs ne souhaitent pas rejouer
            if (reponse.equals("n")) {
                // Arrete le jeu
                jeu = false;
                test = false;
            }
            // SI les joueurs souhaitent rejouer
            else if (reponse.equals("y")) {
                // Le joueur commence la prochaine partie
                j.initJoueurs();
                test = false;
            }
            // SI la saisie n'est pas correcte
            else {
                ihm.afficherSaisieIncorrecte();
            }
        }
        return jeu;
    }

    public void afficherScoreFinal() {
        // Le système affiche les scores et le gagnant du jeu ou ex aqueo si il y a égalité
        ihm.afficherScoreFinal(j1,j2);
        if (j1.getNbPartiesGagnees() > j2.getNbPartiesGagnees()) {
            ihm.afficherNomVainqueur(j1);
        } else if (j1.getNbPartiesGagnees() < j2.getNbPartiesGagnees()) {
            ihm.afficherNomVainqueur(j2);
        } else {
            ihm.afficherEgalite();
        }
    }


}




