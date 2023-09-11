package modele;

public class Joueur {
    private final String nom;
    private String couleur;
    private int nbPartiesGagnees;

    private String side;

    private int score;

    public Joueur(String nom,String side,int k){
        this.nom=nom;
        this.score=0;
        this.side=side;
    }

    public Joueur(String nom,String couleur){
        this.nom=nom;
        this.couleur=couleur;
    }

    public String getNom(){
    return nom;
    }

    public String getCouleur(){
        return couleur;
    }

    public int getNbPartiesGagnees() {
        return nbPartiesGagnees;
    }

    public String getSide() {
        return side;
    }

    public void gagnePartie(){
        nbPartiesGagnees++;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Ajoute une valeur au score actuel du joueur
     *
     * @param score
     *            valeur à ajouter au joueur
     */
    public void addScore(int score)
    {
        setScore(this.score + score);
    }

    /**
     * Affichage du joueur (nom, score et son side)
     */
    @Override
    public String toString()
    {
        return "Joueur [nom : " + nom + " , score = " + score + " , side : "
                + side + " ]";
    }
}
