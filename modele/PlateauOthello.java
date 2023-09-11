package modele;

import vue.Ihm;


import java.util.*;

public class PlateauOthello extends Plateau {


    private final String[] lettres={"  A "," B "," C "," D "," E "," F "," G "," H "};

    public PlateauOthello(int taille){
        super(taille,taille);
    }
    public String[] getLettres() {
        return lettres;
    }


    public void initialiserPions(){
        int taille=plateau.length;
        plateau[0][0]=" ";
        plateau[0][taille-1]=" ";
        plateau[taille-1][0]=" ";
        plateau[taille-1][taille-1]=" ";

        for (int i = 1; i < taille-1; i++) {
            plateau[i][0]= i +" ";
            plateau[i][taille-1]=" "+ i;
            plateau[0][i] = lettres[i - 1];
            plateau[taille-1][i] = lettres[i - 1];
        }

        for (int i = 1; i < taille-1; i++) {
            for (int j = 1; j < taille-1; j++) {
                if ((i == (taille/2)-1 && j == (taille/2)-1) || (i == taille/2 && j == taille/2)) {
                    plateau[i][j] = "\u26AA ";
                } else if ((i == taille/2 && j == (taille/2)-1) || (i == (taille/2)-1 && j == taille/2)) {
                    plateau[i][j] = "\u26AB ";
                } else {
                    plateau[i][j] = "\uD83D\uDFE9 ";
                }

            }
        }
    }

    public boolean plateauPlein(){
        for (String[] strings : plateau) {
            for (String string : strings) {
                if (string.equals("\uD83D\uDFE9 ")) { // si une case vide est trouvée
                    return false; // le plateau n'est pas plein
                }
            }
        }
        return true; // toutes les cases sont remplies
    }

    // Vérifie si la partie est terminé
    public boolean partieTerminee() {
        // Vérifier si l'un des joueurs n'a plus de pions
        boolean noirPresent = false;
        boolean blancPresent = false;
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau.length; j++) {
                if (plateau[i][j].equals("\u26AB ")) {
                    noirPresent = true;
                } else if (plateau[i][j].equals("\u26AA ")) {
                    blancPresent = true;
                }
            }
        }
        if (!noirPresent || !blancPresent || plateauPlein()) {
            return false;
        }
        // Vérifier si les deux joueurs ne peuvent pas jouer
        return !impossibleDeJouer(courant) || !impossibleDeJouer(joueurAdversaire);

        // Si aucun des cas précédents n'est vrai, la partie n'est pas terminée
    }

    public boolean impossibleDeJouer(Joueur jc) {
        int taille = plateau.length;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (plateau[i][j].equals("\uD83D\uDFE9 ")) { // Case vide
                    boolean valide = false;
                    // Vérifie les 8 directions pour voir si la case est valide
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            if (di == 0 && dj == 0) {
                                continue;
                            }
                            int ii = i + di;
                            int jj = j + dj;
                            boolean trouveAdversaire = false;
                            while (ii >= 0 && ii < taille && jj >= 0 && jj < taille) {
                                if (plateau[ii][jj].equals("\uD83D\uDFE9 ")) {
                                    break;
                                } else if (plateau[ii][jj].equals(jc.getCouleur())) {
                                    if (trouveAdversaire) {
                                        valide = true;
                                    }
                                    break;
                                } else {
                                    trouveAdversaire = true;
                                }
                                ii += di;
                                jj += dj;
                            }
                        }
                    }
                    if (valide) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String toString(){
        String s="";
        for(int i=0;i<plateau.length;i++){

            for(int j=0;j<plateau.length;j++){

                s+=(plateau[i][j]);
            }
            if(i==(plateau.length/2)-1){
                s+=("                                                              \u26AB"+" : "+compterPions()[0]);
            }
            if(i==plateau.length/2) {
                s+=("                                                              \u26AA"+" : "+compterPions()[1]);
            }
            s+="\n";
        }
        return s;
    }

    public int[] compterPions() {
        int[] nbPions = new int[2]; // nbPions[0] pour les noirs, nbPions[1] pour les blancs

        for (String[] strings : plateau) {
            for (String string : strings) {
                if (string.equals("\u26AB ")) {
                    nbPions[0]++;
                } else if (string.equals("\u26AA ")) {
                    nbPions[1]++;
                }
            }
        }

        return nbPions;
    }



    // Effectue le coup
    public void gererCoup(Joueur joueur, int ligne, int colonne) throws CoupInvalideException {

        // Vérifier si les coordonnées sont valides
        if (ligne < 0 || ligne >= plateau.length || colonne < 0 || colonne >= plateau.length) {
            throw new CoupInvalideException("Coordonnées invalides : (" + ligne + ", " + colonne + ")");
        }

        // Vérifier si le coup est valide
        if (!coupValide(joueur,ligne, colonne)) {
            throw new CoupInvalideException("Coup invalide : (" + ligne + ", " + colonne + ")");
        }

        // Vérifier si la case est vide
        if (!plateau[ligne][colonne].equals("\uD83D\uDFE9 ")) {
            throw new CoupInvalideException("Coup invalide : (" + ligne + ", " + colonne + ")");
        }
        // Mettre le pion du joueur sur la case jouée
        plateau[ligne][colonne] = joueur.getCouleur();

        // Capturer les pions ennemis dans toutes les directions
        String adversaire = (joueur.getCouleur().equals("\u26AB ")) ? "\u26AA " : "\u26AB ";

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                // Ignorer la case jouée elle-même
                if (x == 0 && y == 0) {
                    continue;
                }

                // Capturer des pions ennemis dans cette direction
                int i = ligne + x;
                int j = colonne + y;
                boolean captureDirection = false;

                while (i >= 0 && i < plateau.length && j >= 0 && j < plateau.length && plateau[i][j].equals(adversaire)) {
                    i += x;
                    j += y;
                    captureDirection = true;
                }

                if (captureDirection && i >= 0 && i < plateau.length && j >= 0 && j < plateau.length && plateau[i][j].equals(joueur.getCouleur())) {
                    // Capturer les pions ennemis dans cette direction
                    i -= x;
                    j -= y;

                    while (plateau[i][j].equals(adversaire)) {
                        plateau[i][j] = joueur.getCouleur();
                        i -= x;
                        j -= y;
                    }
                }
            }
        }
    }






    public String transformeLettre(int nombre) {
        // On utilise l'ASCII code pour convertir le nombre en lettre
        char lettre = (char) (nombre + 'A' - 1);

        // On vérifie si la lettre est en majuscule ou en minuscule
        if (Character.isUpperCase(lettre)) {
            return String.valueOf(lettre);
        } else {
            return String.valueOf(Character.toLowerCase(lettre));
        }
    }

    public int lettreEnNombre(char lettre) {
        return switch (lettre) {
            case 'a', 'A' -> 1;
            case 'b', 'B' -> 2;
            case 'c', 'C' -> 3;
            case 'd', 'D' -> 4;
            case 'e', 'E' -> 5;
            case 'f', 'F' -> 6;
            case 'g', 'G' -> 7;
            case 'h', 'H' -> 8;
            default -> throw new IllegalArgumentException("Lettre invalide : " + lettre);
        };
    }

    public ArrayList<String> getCoupPossibles(Joueur joueur) {
        ArrayList<String> coupsPossibles = new ArrayList<>();
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau.length; j++) {
                if (coupValide(joueur,i,j)) {
                    coupsPossibles.add(i+" "+transformeLettre(j));
                }
            }
        }
        return coupsPossibles;
    }


    public boolean coupValide(Joueur courant,int x, int y) {
        // Vérifier si la case est libre
        if (!plateau[x][y].equals("\uD83D\uDFE9 ")) {
            return false;
        }

        // Vérifier si le coup capture des pions adverses
        boolean capture = false;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nx = x + dx;
                int ny = y + dy;
                if (nx < 0 || nx >= plateau.length || ny < 0 || ny >= plateau.length) {
                    continue;
                }
                if (plateau[nx][ny].equals("\uD83D\uDFE9 ")) {
                    continue;
                }
                if (plateau[nx][ny].equals(courant.getCouleur())) {
                    continue;
                }
                while (nx >= 0 && nx < plateau.length && ny >= 0 && ny < plateau.length && !plateau[nx][ny].equals("\uD83D\uDFE9 ")) {
                    if (plateau[nx][ny].equals(courant.getCouleur())) {
                        capture = true;
                        break;
                    }
                    nx += dx;
                    ny += dy;
                }
                if (capture) {
                    break;
                }
            }
            if (capture) {
                break;
            }
        }
        return capture;
    }

    public void passerTour(Joueur courant,Joueur ancienCourant){
        if((courant instanceof IA)) {
            Ihm.afficher("L'IA ne peut pas jouer un coup");
        }
        else {
            Ihm.afficher("Le joueur "+courant.getNom()+" ne peut pas poser un pion, il faut taper p ou P pour passer votre tour au joueur "+ancienCourant.getNom());
            Ihm.saisieP();
        }

    }

    public PlateauOthello gererCoupPlateau(Joueur joueur, int ligne, int colonne) throws CoupInvalideException {
        PlateauOthello plateau1=copierPlateau(this);
        // Vérifier si les coordonnées sont valides
        if (ligne < 0 || ligne >= plateau1.getTaille() || colonne < 0 || colonne >= plateau1.getTaille()) {
            throw new CoupInvalideException("Coordonnées invalides : (" + ligne + ", " + colonne + ")");
        }

        // Vérifier si le coup est valide
        if (!coupValide(joueur,ligne, colonne)) {
            throw new CoupInvalideException("Coup invalide : (" + ligne + ", " + colonne + ")");
        }

        // Vérifier si la case est vide
        if (!plateau1.getCase(ligne,colonne).equals("\uD83D\uDFE9 ")) {
            throw new CoupInvalideException("Coup invalide : (" + ligne + ", " + colonne + ")");
        }
        // Mettre le pion du joueur sur la case jouée
        plateau1.setCase(ligne,colonne, joueur.getCouleur());

        // Capturer les pions ennemis dans toutes les directions
        String adversaire = (joueur.getCouleur().equals("\u26AB ")) ? "\u26AA " : "\u26AB ";

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                // Ignorer la case jouée elle-même
                if (x == 0 && y == 0) {
                    continue;
                }

                // Capturer des pions ennemis dans cette direction
                int i = ligne + x;
                int j = colonne + y;
                boolean captureDirection = false;

                while (i >= 0 && i < plateau1.getTaille() && j >= 0 && j < plateau1.getTaille() && plateau1.getCase(i,j).equals(adversaire)) {
                    i += x;
                    j += y;
                    captureDirection = true;
                }

                if (captureDirection && i >= 0 && i < plateau1.getTaille() && j >= 0 && j < plateau1.getTaille() && plateau1.getCase(i,j).equals(joueur.getCouleur())) {
                    // Capturer les pions ennemis dans cette direction
                    i -= x;
                    j -= y;

                    while (plateau1.getCase(i,j).equals(adversaire)) {
                        plateau1.setCase(i,j, joueur.getCouleur());
                        i -= x;
                        j -= y;
                    }
                }
            }
        }

        return plateau1;
    }

    // Copier un plateau de jeu
    public PlateauOthello copierPlateau(PlateauOthello pl) {
        PlateauOthello copie = new PlateauOthello(pl.getTaille());
        for (int i = 0; i < pl.getTaille(); i++) {
            for (int j = 0; j < pl.getTaille(); j++) {
                copie.setCase(i, j, getCase(i, j));
            }
        }
        copie.setCourant(courant);
        copie.setJoueurAdversaire(joueurAdversaire);
        copie.setVainqueur(vainqueur);
        return copie;
    }




}


