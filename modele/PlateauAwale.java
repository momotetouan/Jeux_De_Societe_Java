package modele;


public class PlateauAwale extends Plateau {

    public PlateauAwale() {
        super(2,6);
    }

    @Override
    public void initialiserPions() {
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 6; j++)
                plateau[i][j] = String.valueOf(4);
        }
    }

    public String toString() {
        String s="";
        s+="Plateau actuel : \n";

        s+="  5 4 3 2 1 0\n"; // inverser l'ordre des numéros de colonnes
        for (int i = 1; i >= 0; i--) // inverser l'ordre des joueurs
        {
            s+="[";
            for (int j = 5; j >= 0; j--) // utiliser une boucle descendante pour parcourir les colonnes
                s+=" " + plateau[i][j];
            s+=" ] J" + (i + 1)+"\n";
        }
        s+="  5 4 3 2 1 0\n"; // inverser l'ordre des numéros de colonnes

        s+="\n";
        return s;
    }

    /**
     * Retourne le nombre de graines sur une case selectionnée
     *
     * @param i
     *            numéro de la ligne
     * @param j
     *            numéro de la colonne
     * @return le nombre de graines sur la case sélectionnée
     */
    public int getCellule(int i, int j)
    {
        return plateau[i][j].length();
    }

    /**
     * Attribue une valeur à une case du plateau
     *
     * @param i
     *            numéro de la ligne
     * @param j
     *            numéro de la colonne
     * @param value
     *            valeur à attribuer
     */
    public void setCellule(int i, int j, int value)
    {
        plateau[i][j] = String.valueOf(value);
    }

    /**
     * Ajoute une graine dans la case demandée
     *
     * @param i
     *            numéro de la ligne
     * @param j
     *            numéro de la colonne
     */
    public void ajoutGraine(int i, int j)
    {
        plateau[i][j]= String.valueOf(Integer.parseInt(plateau[i][j])+1);
    }

    /**
     * Retourne le nombre de graines sur le plateau
     *
     * @return le nombre de graines sur le plateau
     */
    public int NbGraines()
    {
        int resultat = 0;
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 6; j++)
                resultat += plateau[i][j].length();
        }
        return resultat;
    }

    /**
     * Retourne le nombre de graines sur le plateau du joueur
     *
     * @param i
     *            ligne représentant le joueur (0 ou 1)
     * @return le nombre de graines sur le plateau du joueur
     */
    public int NbGraines(int i)
    {
        int resultat = 0;
        for (int j = 0; j < 6; j++)
            resultat += plateau[i][j].length();
        return resultat;
    }


    /**
     * Déplace les graines d'une cellules dans les cellules suivantes et tente
     * de manger des graines si on termine sur le camp adverse
     *
     * @param j
     *            cellule du joueur à répartir
     * @param side
     *            ligne sur laquelle choisir la cellule
     */
    public int jouerCoup(int j, String side)
    {

        if (side.equals("J1"))
        {

            int NbGraine = Integer.parseInt(plateau[0][j]);

            // Si la cellule sélectionnée est vide on renvoit -1 et redemande
            // une autre cellule
            if (NbGraine == 0)
                return -1;

            // On vérifie que le coup est possible
            int verif = verifPlateau(1, j);
            if (verif != 0)
                return verif;

            setCellule(0, j, 0);

            // On répartie à partir de la cellule actuelle jusqu'à ne plus avoir
            // de graines à répartir ou jusqu'à la cellule 0 du J1
            for (int i = j - 1; i >= 0; i--)
            {
                if (NbGraine != 0)
                {
                    ajoutGraine(0, i);
                    NbGraine--;
                }

            }

            // Si il reste des graines à répartir, on boucle tant qu'il en reste
            while (NbGraine != 0)
            {
                // On répartie sur le camp du joueur opposé
                // Si on termine de répartir sur le camp adverse on essaye alors
                // de manger les graines
                for (int k = 0; k < 6; k++)
                {
                    if (NbGraine != 0)
                    {
                        ajoutGraine(1, k);
                        NbGraine--;
                        if (NbGraine == 0)
                        {
                            return manger(1, k);
                        }
                    }
                }

                // On répartie sur son camp si il reste des graines
                if (NbGraine != 0)
                {
                    for (int i = 5; i > 0; i--)
                    {
                        if (NbGraine != 0 && i != j)
                        {
                            ajoutGraine(0, i--);
                            NbGraine--;
                        }
                    }
                }

            }

        }
        // identique au J1 excepté que l'on commence à répartir du côté du J2
        else
        {

            int NbGraine = Integer.parseInt(plateau[1][j]);

            if (NbGraine == 0)
                return -1;

            // On vérifie que le coup est possible
            int verif = verifPlateau(0, j);
            if (verif != 0)
                return verif;

            setCellule(1, j, 0);

            for (int i = j + 1; i < 6; i++)
            {
                if (NbGraine != 0)
                {
                    ajoutGraine(1, i);
                    NbGraine--;
                }
            }

            while (NbGraine != 0)
            {
                for (int k = 5; k >= 0; k--)
                {
                    if (NbGraine != 0)
                    {
                        ajoutGraine(0, k);
                        NbGraine--;
                        if (NbGraine == 0)
                        {
                            return manger(0, k);
                        }
                    }
                }

                for (int i = 0; i < 6; i++)
                {
                    if (NbGraine != 0 && i != j)
                    {
                        ajoutGraine(1, i);
                        NbGraine--;
                    }
                }

            }
        }
        return 0;
    }

    /**
     * Mange les graines du camp adverse tant que c'est possible
     *
     * @param i
     *            camp sur lequel on mange les graines
     * @param cellulefinale
     *            cellule sur laquelle s'est arreté le joueur
     */
    public int manger(int i, int cellulefinale)
    {
        int score = 0;

        // Joueur 2 a terminé sur Joueur 1
        if (i == 0)
        {
            for (int j = cellulefinale; j < 6; j++)
            {
                if (Integer.parseInt(plateau[0][j]) == 3 || Integer.parseInt(plateau[0][j]) == 2)
                {
                    score += Integer.parseInt(plateau[0][j]);
                    plateau[0][j] = String.valueOf(0);
                }
                else
                    break;
            }
        }
        // Joueur 1 a terminé sur Joueur 2
        else
        {
            for (int j = cellulefinale; j >= 0; j--)
            {
                if (Integer.parseInt(plateau[1][j]) == 3 || Integer.parseInt(plateau[1][j]) == 2)
                {
                    score += Integer.parseInt(plateau[1][j]);
                    plateau[1][j] = String.valueOf(0);
                }
                else
                    break;
            }
        }

        return score;

    }

    /**
     * Vérifie si le plateau adverse est vide et si un coup est possible
     *
     * @param i
     *            camp à vérifier
     *
     * @param j
     *            cellule que l'on souhaite jouer
     *
     * @return 0 si coup possible, -2 si plateau adverse vide et il existe au
     *         moins un coup possible pour le nourrir mais pas celui demandé, -3
     *         si le plateau adverse est vide et qu'on ne peut pas le nourrir
     */
    public int verifPlateau(int i, int j)
    {
        // On vérifie si le plateau du J1 est vide, signifique que c'est le tour
        // de J2
        if (i == 0)
        {
            int grainesJoueur = 0;
            for (int k = 0; k < 6; k++)
                grainesJoueur += Integer.parseInt(plateau[0][k]);
            // Le plateau adverse n'est pas vide, on joue ce que l'on veut
            if (grainesJoueur != 0)
                return 0;

            // Plateau adverse vide, on vérifie que sur la case demandée on a
            // assez de graines pour le nourrir
            if (Integer.parseInt(plateau[1][j]) + j > 6)
                return 0;

            // Sinon le plateau adverse est vide et case impossible à jouer. On
            // vérifie donc si une case est possible
            for (int k = 0; k < 6; k++)
            {
                if (Integer.parseInt(plateau[1][k]) + k > 6)
                    return -2;
            }

            // Si rien de tout cela ne marche, cela signifie que le plateau
            // adverse est affamé et qu'on ne peut pas le nourrir,fin de la
            // partie
            return -3;

        }

        // On vérifie si le plateau du J2 est vide, signifique que c'est le tour
        // de J1
        // Même procédé que pour le tour de J2
        else
        {
            int grainesJoueur = 0;
            for (int k = 0; k < 6; k++)
                grainesJoueur += Integer.parseInt(plateau[1][k]);
            // Le plateau adverse n'est pas vide, on joue ce que l'on veut
            if (grainesJoueur != 0)
                return 0;

            // Plateau adverse vide, on vérifie que sur la case demandée on a
            // assez de graines pour le nourrir
            if (Integer.parseInt(plateau[0][j]) - j < 0)
                return 0;

            // Sinon le plateau adverse est vide et case impossible à jouer. On
            // vérifie donc si une case est possible
            for (int k = 0; k < 6; k++)
            {
                if (Integer.parseInt(plateau[0][k]) - k < 0)
                    return -2;
            }

            // Si rien de tout cela ne marche, cela signifie que le plateau
            // adverse est affamé et qu'on ne peut pas le nourrir,fin de la
            // partie
            return -3;
        }
    }

    /**
     * Indique les coups possibles pour chaque joueur : 0 : coup impossible pour
     * ce joueur et cette cellule, 1 : coup possible pour ce joueur et cette
     * cellule
     *
     * @return les coups possibles pour chaque joueur sous forme d'un tableau à
     *         deux dimensions avec valeurs 0 et 1
     */
    public int[][] coupPossibles()
    {
        int[][] coup;

        coup = new int[2][6];

        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 6; j++)
                coup[i][j] = 0;

        int NbGrainesJ1 = 0;

        for (int j = 0; j < 6; j++)
            NbGrainesJ1 += Integer.parseInt(plateau[0][j]);

        int NbGrainesJ2 = 0;

        for (int j = 0; j < 6; j++)
            NbGrainesJ2 += Integer.parseInt(plateau[1][j]);

        // Si le joueur 2 possède des graines, le joueur 1 peut jouer n'importe
        // quelle cellule avec des graines
        if (NbGrainesJ2 != 0)
        {
            for (int j = 0; j < 6; j++)
                if (Integer.parseInt(plateau[0][j]) != 0)
                    coup[0][j] = 1;
        }
        // Sinon le joueur 1 devra nourrir l'adversaire obligatoirement
        else
        {
            for (int j = 0; j < 6; j++)
                if (Integer.parseInt(plateau[0][j]) - j < 0)
                    coup[0][j] = 1;
        }

        // Si le joueur 1 possède des graines, le joueur 2 peut jouer n'importe
        // quelle cellule avec des graines
        if (NbGrainesJ1 != 0)
        {
            for (int j = 0; j < 6; j++)
                if (Integer.parseInt(plateau[1][j]) != 0)
                    coup[1][j] = 1;
        }
        // Sinon le joueur 2 devra nourrir l'dversaire obligatoirement
        else
        {
            for (int j = 0; j < 6; j++)
                if (Integer.parseInt(plateau[1][j]) + j > 6)
                    coup[1][j] = 1;
        }

        return coup;

    }

    /**
     * Renvoie en string le tableau des coups possibles
     * @param coup tableau des coups possibles
     * @return string du tableau des coups possibles
     */
    public String coupPossiblesToString(int[][] coup)
    {
        String coups = "";

        for (int i = 0; i < 2; i++)
        {
            coups += "J" + (i + 1) + " peut jouer les cases :";
            for (int j = 0; j < 6; j++)
                if (coup[i][j] == 1)
                    coups += " " + j;
            coups += "\n";
        }

        return coups;
    }

    public boolean partieTerminee() {
        int etatJoueur1 = NbGraines(0);
        int etatJoueur2 = NbGraines(1);

        boolean joueur1Termine = true;
        boolean joueur2Termine = true;
        boolean termine=true;
        // Vérifie si l'un des deux joueurs a encore des graines dans ses maisons
        if (etatJoueur1 > 0 || etatJoueur2 > 0) {
                joueur1Termine = false;
                joueur2Termine = false;
                termine=false;
            }

        // Si l'un des deux joueurs n'a plus de graines dans ses maisons, la partie est terminée
        if (joueur1Termine || joueur2Termine) {
            termine = true;
        }

        return termine;
    }






}



