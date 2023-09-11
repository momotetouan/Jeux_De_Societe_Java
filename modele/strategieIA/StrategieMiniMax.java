package modele.strategieIA;

import modele.*;

import java.util.ArrayList;


public class StrategieMiniMax implements IAStrategie {
    private int profondeur;
    public void setProfondeur(int profondeur) {
        this.profondeur = profondeur;
    }

    // Cette méthode prend en paramètre un plateau et un joueur,
    // et joue le coup recommandé par l'algorithme minimax.
    // La méthode appelle la méthode minimax pour obtenir le meilleur coup,
    // puis elle utilise la méthode gererCoup du plateau pour jouer ce coup
    @Override
    public void jouerCoup(PlateauOthello plateau, IA joueur) throws CoupInvalideException {
        String meilleurCoup = minimax(plateau, joueur,profondeur);
        plateau.gererCoup(joueur, Integer.parseInt(String.valueOf(meilleurCoup.charAt(0))), plateau.lettreEnNombre(meilleurCoup.charAt(2)));
    }

    public String minimax(PlateauOthello plateau, IA j, int profondeurJeu) throws CoupInvalideException {
        ArrayList<String> coupsPossibles = plateau.getCoupPossibles(j);
        String meilleurCoup = null;
        int meilleurScore = Integer.MIN_VALUE;

        for (String coup : coupsPossibles) {
            PlateauOthello nvPlateau = plateau.gererCoupPlateau(j, Integer.parseInt(String.valueOf(coup.charAt(0))), plateau.lettreEnNombre(coup.charAt(2)));

            int score = minimaxRecursive(nvPlateau, plateau.getJoueurAdversaire(), profondeurJeu - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = coup;
            }
        }

        return meilleurCoup;
    }

    private int minimaxRecursive(PlateauOthello plateau, Joueur j, int profondeurJeu, int min, int max, boolean maximisant) throws CoupInvalideException {
        if (profondeurJeu == 0 || plateau.partieTerminee()) {
            return evaluer(plateau, j);
        }

        ArrayList<String> coupsPossibles = plateau.getCoupPossibles(j);
        int meilleurScore = maximisant ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (String coup : coupsPossibles) {
            PlateauOthello nvPlateau = plateau.gererCoupPlateau(j, Integer.parseInt(String.valueOf(coup.charAt(0))), plateau.lettreEnNombre(coup.charAt(2)));
            int score = minimaxRecursive(nvPlateau, plateau.getJoueurAdversaire(), profondeurJeu - 1, min, max, !maximisant);

            if (maximisant) {
                meilleurScore = Math.max(meilleurScore, score);
                min = Math.max(min, score);
            } else {
                meilleurScore = Math.min(meilleurScore, score);
                max = Math.min(max, score);
            }

            if (max <= min) {
                break;
            }
        }

        return meilleurScore;
    }


    /**
         * Fonction d'évaluation pour une couleur donnée.
         */
    public int evaluer(PlateauOthello plateau,Joueur jc) {
        int score = 0;
        String[][] p = plateau.getPlateau();

        // Vérifier si la partie est terminée.
        if (plateau.partieTerminee()) {
            // Ajouter les points pour chaque jeton.
            for (int i = 0; i < plateau.getTaille(); i++) {
                for (int j = 0; j < plateau.getTaille(); j++) {
                    if (p[i][j].equals(jc.getCouleur())) {
                        if ((i == 1 || i == plateau.getTaille() - 2) && (j == 1 || j == plateau.getTaille() - 2)) {
                            // Jeton dans un coin.
                            score += 11;
                        } else if (i == 1 || i == plateau.getTaille() - 2 || j == 1 || j == plateau.getTaille() - 2) {
                            // Jeton sur un bord.
                            score += 6;
                        } else {
                            // Jeton ailleurs.
                            score += 1;
                        }
                    }
                }
            }
        } else {
            // La partie est terminée.
            Joueur vainqueur = plateau.getVainqueur();
            if (vainqueur == null) {
                // La partie est nulle.
                return 0;
            } else if (jc.getCouleur().equals(vainqueur.getCouleur())) {
                // Le joueur a gagné.
                return 1000;
            } else {
                // Le joueur a perdu.
                return -1000;
            }
        }

        return score;
    }


}


