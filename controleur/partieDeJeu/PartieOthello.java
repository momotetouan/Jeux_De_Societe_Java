package controleur.partieDeJeu;

import modele.*;
import modele.strategieIA.IAStrategie;
import modele.strategieIA.StrategieMiniMax;
import modele.strategieIA.StrategieNaif;
import vue.Ihm;

public class PartieOthello implements Jeu{
    private IAStrategie IAstrategie;
    private Joueur j1;
    private Joueur j2;
    private static Joueur courant;
    private static Joueur ancienCourant;

    private int taillePlateau ;

    public int getTaille(){
        return taillePlateau;
    }

    public PlateauOthello getPlateau() {
        return plateauOthello;
    }

    private PlateauOthello plateauOthello;

    public void initJoueurs() {
        courant = j1;
        ancienCourant = j2;
        plateauOthello.setJoueurAdversaire(ancienCourant);
        plateauOthello.setCourant(courant);
    }

    public Joueur[] initJeu(boolean solo) {
        Joueur[] joueurs=new Joueur[2];
        Ihm.afficher("\nBienvenue au jeu Othello !\n");
        Ihm.afficher("veuillez choisir la taille du plateau");
        taillePlateau = Ihm.saisieTaille() + 2;
        plateauOthello = new PlateauOthello(taillePlateau);

        Ihm.afficher("Veuillez saisir le nom du joueur 1 :");
        j1 = new Joueur(Ihm.saisie(), "⚫ ");
        while (j1.getNom().equals("")) {
            Ihm.afficher("Saisissez un nom valide");
            j1 = new Joueur(Ihm.saisie(), "⚫ ");
        }
        boolean test = false;
        while (!test) {
            if (solo) {
                j2 = new IA("IA", "⚪ ");
                IAstrategie = choisirStrategie();
                test = true;
            } else {
                Ihm.afficher("Veuillez saisir le nom du joueur 2 :");
                j2 = new Joueur(Ihm.saisie(), "⚪ ");
                while (j2.getNom().equals("") || j2.getNom().equals(j1.getNom())) {
                    Ihm.afficher("Saisissez un nom valide et différent de " + j1.getNom());
                    j2 = new Joueur(Ihm.saisie(), "⚪ ");
                }
                test = true;
            }
        }
        joueurs[0]=j1;
        joueurs[1]=j2;
        return joueurs;

    }


    public void jouerPartie() {
        while (plateauOthello.partieTerminee()) {
            Ihm.afficher(plateauOthello.toString());
            Ihm.afficher("\n");
            if (!(courant instanceof IA)) {
                Ihm.afficher(courant.getNom() + " à vous de jouer. Saisir une ligne entre 1 et " + (taillePlateau - 2) + " suivie d’une lettre entre A et " + plateauOthello.getLettres()[taillePlateau - 3]);
            }
            String coup = "";
            try {
                if (plateauOthello.impossibleDeJouer(courant)) {
                    plateauOthello.passerTour(courant, ancienCourant);
                    Joueur tmp = courant;
                    courant = ancienCourant;
                    ancienCourant = tmp;
                    plateauOthello.setJoueurAdversaire(courant);
                    plateauOthello.setCourant(ancienCourant);
                    Ihm.afficher("C'est maintenant le tour de " + courant.getNom());
                }
                if ((courant instanceof IA)) {
                    IAstrategie.jouerCoup(plateauOthello, (IA) courant);
                } else {
                    coup += Ihm.saisieCoup();
                    plateauOthello.gererCoup(courant, Integer.parseInt(String.valueOf(coup.charAt(0))), plateauOthello.lettreEnNombre(coup.charAt(2)));
                }
                Joueur tmp = courant;
                courant = ancienCourant;
                ancienCourant = tmp;
                plateauOthello.setJoueurAdversaire(courant);
                plateauOthello.setCourant(ancienCourant);
            } catch (CoupInvalideException | IllegalArgumentException | StringIndexOutOfBoundsException e) {
                Ihm.afficher(e.getMessage());
            }


        }
    }

    public void afficherVainqueurPartie() {
        Ihm.afficher(plateauOthello.toString());
        int[] pions = plateauOthello.compterPions();
        int scoreN = pions[0];
        int scoreB = pions[1];
        String couleurC = courant.getCouleur();

        if (scoreN > scoreB) {
            afficherGagnant(scoreN, scoreB, couleurC, courant, ancienCourant);
        } else if (scoreB > scoreN) {
            afficherGagnant(scoreB, scoreN, couleurC, ancienCourant, courant);
        } else {
            afficherExAequo();
        }
    }

    public void afficherGagnant(int scoreA, int scoreB, String couleurC, Joueur joueurA, Joueur joueurB) {
        if (couleurC.equals("⚫ ")) {
            Ihm.afficher("Le joueur " + joueurA.getNom() + " a gagné !\n\n");
            Ihm.afficher("Le joueur " + joueurA.getNom() + " : " + scoreA + " pions");
            Ihm.afficher("Le joueur " + joueurB.getNom() + " : " + scoreB + " pions");
            joueurA.gagnePartie();
            plateauOthello.setVainqueur(joueurA);
        } else {
            Ihm.afficher("Le joueur " + joueurB.getNom() + " a gagné !\n\n");
            Ihm.afficher("Le joueur " + joueurB.getNom() + " : " + scoreA + " pions");
            Ihm.afficher("Le joueur " + joueurA.getNom() + " : " + scoreB + " pions");
            joueurB.gagnePartie();
            plateauOthello.setVainqueur(joueurB);
        }
    }
    private void afficherExAequo() {
        Ihm.afficher("ex aequo");
    }
    private IAStrategie choisirStrategie() {
        Ihm.afficher("Veuillez choisir la stratégie pour l'IA : naif ou minimax");
        boolean choixValide = false;
        IAStrategie strategie = null;
        while (!choixValide) {
            String choix = Ihm.saisie();
            if (choix.equals("naif")) {
                strategie = new StrategieNaif();
                choixValide = true;
            } else if (choix.equals("minimax")) {
                StrategieMiniMax strategieIA = new StrategieMiniMax();
                strategieIA.setProfondeur(choisirDiff());
                strategie=strategieIA;
                choixValide = true;
            } else {
                Ihm.afficher("Veuillez choisir naif ou minimax");
            }
        }
        return strategie;
    }

    private int choisirDiff(){
        Ihm.afficher("Veuillez choisir la difficulté pour le mode de jeu minimax\n facile, normal ou difficile");
        boolean choixValide = false;
        int profondeur =0;
        while (!choixValide) {
            String choix = Ihm.saisie();
            switch (choix) {
                case "facile" -> {
                    profondeur = 2;
                    choixValide = true;
                }
                case "normal" -> {
                    profondeur = 4;
                    choixValide = true;
                }
                case "difficile" -> {
                    profondeur = 6;
                    choixValide = true;
                }
                default -> Ihm.afficher("Veuillez choisir facile, normal ou difficile");
            }
        }
        return profondeur;
    }
}
