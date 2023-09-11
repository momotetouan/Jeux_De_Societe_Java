package controleur.partieDeJeu;

import modele.*;
import vue.Ihm;

public class PartieAwale implements Jeu {

    private Joueur[] joueurs;
    private PlateauAwale plateauAwale;
    private Joueur j1;
    private Joueur j2;
    private static Joueur courant;
    private static Joueur ancienCourant;

    private int taillePlateau;

    public Joueur[] initJeu(boolean solo){
        joueurs=new Joueur[2];
        Ihm.afficher("\nBienvenue au jeu Awalé !\n");
        plateauAwale = new PlateauAwale();
        taillePlateau=plateauAwale.getTaille();

        Ihm.afficher("Veuillez saisir le nom du joueur 1 :");
        j1 = new Joueur(Ihm.saisie(), "J1",1);

        while (j1.getNom().equals("")) {
            Ihm.afficher("Saisissez un nom valide");
            j1 = new Joueur(Ihm.saisie(), "J1",1);

        }
        boolean test = false;
        while (!test) {
                Ihm.afficher("Veuillez saisir le nom du joueur 2 :");
                j2 = new Joueur(Ihm.saisie(), "J2",2);
                while (j2.getNom().equals("") || j2.getNom().equals(j1.getNom())) {
                    Ihm.afficher("Saisissez un nom valide et différent de " + j1.getNom());
                    j2 = new Joueur(Ihm.saisie(), "J2",2);
                }
                test = true;
        }
        joueurs[0]=j1;
        joueurs[1]=j2;
        return joueurs;
    }
    @Override
    public void initJoueurs() {
        courant = j1;
        ancienCourant = j2;
        plateauAwale.setJoueurAdversaire(ancienCourant);
        plateauAwale.setCourant(courant);
    }

    @Override
    public Plateau getPlateau() {
        return plateauAwale;
    }

    public void jouerPartie(){
        while (!(plateauAwale.partieTerminee())) {
            Ihm.afficher(plateauAwale.toString());
            Ihm.afficher("\n");
            Ihm.afficher("\n"+courant.getNom() + " à vous de jouer. Choisissez la cellule à répartir\n");
            try {
                int valeur=jouerCoup();
                courant.addScore(valeur);
                afficherInfos();
                // ------------ Vérifie nombre de graines sur le plateau
                // --------------- //
                int NbTotalGraines = plateauAwale.NbGraines();

                // Si il reste plus assez de graines ou que le j1 vient de faire un
                // coup gagnant on sort de la boucle
                if (NbTotalGraines <= 6 || courant.getScore() >= 25)
                    break;
                mettreAjourJoueurs();
            } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
                Ihm.afficher(e.getMessage());
            }
        }
    }

    public void afficherVainqueurPartie(){
        int scoreA = joueurs[0].getScore();
        int scoreB = joueurs[1].getScore();
        afficherGagnant(scoreA,scoreB,"",joueurs[0],joueurs[1]);
    }

    public void afficherGagnant(int scoreA, int scoreB, String couleurC, Joueur joueurA, Joueur joueurB){
        if(scoreA == scoreB) {
            Ihm.afficher("Egalité !");
        } else {
            Joueur gagnant = scoreA > scoreB ? joueurA : joueurB;
            Ihm.afficher("Le joueur " + gagnant.getNom() + " gagne la manche avec " + couleurC + " !");
            gagnant.gagnePartie();
            plateauAwale.setVainqueur(gagnant);
        }
    }

    public void mettreAjourJoueurs(){
        Joueur tmp = courant;
        courant = ancienCourant;
        ancienCourant = tmp;
        plateauAwale.setJoueurAdversaire(courant);
        plateauAwale.setCourant(ancienCourant);
    }

    public void afficherInfos(){

        // ------------ Vérifie nombre de graines sur le plateau
        // --------------- //
        int NbTotalGraines = plateauAwale.NbGraines();
        Ihm.afficher("\nValeur du plateau : " + NbTotalGraines + "\n");

        // ------------ Affiche joueurs --------------- //
        Ihm.afficher(j1.toString());
        Ihm.afficher(j2.toString());

        // ------------ Affiche coups possibles ------- //
        Ihm.afficher(plateauAwale.coupPossiblesToString(plateauAwale.coupPossibles()));
    }

    public int jouerCoup(){
        int cellule = Integer.parseInt(Ihm.saisie());
        while (cellule > 5 || cellule < 0)
        {
            Ihm.afficher("Choisissez un nombre entre 0 et 5\n");
            cellule = Integer.parseInt(Ihm.saisie());
        }
        int valeur=plateauAwale.jouerCoup(cellule,courant.getSide());
        while (valeur < 0)
        {
            if (valeur == -1)
                Ihm.afficher("J1 : choisissez une case non vide\n");
            if (valeur == -2)
                Ihm.afficher("J1 : vous devez nourrir le plateau adverse");
            if(valeur == -3){
                j1.addScore(plateauAwale.NbGraines(0));
                j2.addScore(plateauAwale.NbGraines(1));
                break;
            }
            cellule = Integer.parseInt(Ihm.saisie());
            while (cellule > 5 || cellule < 0)
            {
                Ihm.afficher("Choisissez un nombre entre 0 et 5\n");
                cellule = Integer.parseInt(Ihm.saisie());
            }
            valeur = plateauAwale.jouerCoup(cellule, courant.getSide());
        }
        return valeur;
    }



}
