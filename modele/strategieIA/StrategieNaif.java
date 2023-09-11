package modele.strategieIA;

import modele.CoupInvalideException;
import modele.IA;
import modele.Plateau;
import modele.PlateauOthello;

import java.util.*;

public class StrategieNaif implements IAStrategie{

    public void jouerCoup(PlateauOthello plateau, IA joueur) throws CoupInvalideException {
        // Trouver tous les coups valides
        List<String> coupPossibles = plateau.getCoupPossibles(joueur);
        // Vérifier si la liste n'est pas vide sinon ca retourne null
        if (!coupPossibles.isEmpty()) {
            // Choisir un coup aléatoire parmi les coups valides
            Random rand = new Random();
            String coup = coupPossibles.get(rand.nextInt(coupPossibles.size()));
            plateau.gererCoup(joueur, Integer.parseInt(String.valueOf(coup.charAt(0))), plateau.lettreEnNombre(coup.charAt(2)));
        }
    }

}
