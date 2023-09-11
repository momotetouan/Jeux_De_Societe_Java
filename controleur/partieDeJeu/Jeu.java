package controleur.partieDeJeu;

import modele.Joueur;
import modele.Plateau;

public interface Jeu {

    /**
     * Permet d'initialiser le jeu. Crée les objets du modèle spécifique au jeu.
     */
     Joueur[] initJeu(boolean solo);

    /**
     * Permet au joueur de jouer son coup.
     */
     void jouerPartie();

    /**
     * Permet d'afficher le vainqueur d'une partie jouée
     */
    void afficherVainqueurPartie();

    /**
     * Permet d'afficher le gagnant du jeu
     */
    void afficherGagnant(int scoreA, int scoreB, String couleurC, Joueur joueurA, Joueur joueurB);

    void initJoueurs();

    Plateau getPlateau();
}
