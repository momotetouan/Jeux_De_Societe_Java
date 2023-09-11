package modele;

public abstract class Plateau {
    protected int taille;
    protected Joueur vainqueur;
    protected static Joueur joueurAdversaire;
    protected static Joueur courant;
    protected String[][] plateau;

    public Plateau(int x,int y) {
        this.plateau = new String[x][y];
        this.taille = plateau.length;
    }

    public String getCase(int x, int y) {
        return plateau[x][y];
    }

    public void setCase(int x, int y, String valeur) {
        plateau[x][y] = valeur;
    }


    public void setCourant(Joueur courant) {
        Plateau.courant = courant;
    }

    public Joueur getJoueurAdversaire() {
        return joueurAdversaire;
    }

    public void setJoueurAdversaire(Joueur joueurAdversaire) {
        Plateau.joueurAdversaire = joueurAdversaire;
    }
    public String[][] getPlateau() {
        return plateau;
    }

    public Joueur getVainqueur(){
        return vainqueur;
    }

    public void setVainqueur(Joueur j){
        vainqueur=j;
    }

    public int getTaille(){
        return plateau.length;
    }

    public abstract void initialiserPions();

    public abstract boolean partieTerminee();



}


