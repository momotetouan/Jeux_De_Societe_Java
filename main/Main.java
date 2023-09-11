package main;


import controleur.partieDeJeu.Jeu;
import controleur.partieDeJeu.PartieAwale;
import controleur.partieDeJeu.PartieOthello;
import modele.PlateauAwale;
import vue.Ihm;
import controleur.Controleur;
public class Main {
    public static void main(String[] args) {
        Ihm ihm = new Ihm();
        Controleur controleur=new Controleur(ihm);
        Jeu jeu = null;
        while (jeu == null) {
            Ihm.afficher("Sélectionnez le jeu auquel vous souhaitez jouer :" +
                    "\n(1) Jeu d'Othello" +
                    "\n(2) Jeu d'Awalé");
            String choix = Ihm.saisie();

            switch (choix) {
                case "1":
                    jeu = new PartieOthello();
                    break;
                case "2":
                    jeu = new PartieAwale();
                    break;
                default:
                    Ihm.afficher("Veuillez saisir un nombre indiqué.");
            }
        }
        boolean solo=false;
        String mode="0";
        while (!mode.equals("1") && !mode.equals("2")) {
                Ihm.afficher("Souhaitez vous jouer à un joueur ( joueur vs IA ) ou à deux joueurs ( joueur vs joueur ) ?" +
                        "\n(1) joueur vs IA " +
                        "\n(2) joueur vs joueur ");
                mode = Ihm.saisie();

                switch (mode) {
                    case "1":
                        solo = true;
                        break;
                    case "2":
                        solo = false;
                        break;
                    default:
                        Ihm.afficher("Veuillez saisir un nombre indiqué.");
                }

        }
        controleur.jouer(jeu,solo);




    }
}