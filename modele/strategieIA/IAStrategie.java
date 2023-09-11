package modele.strategieIA;

import modele.CoupInvalideException;
import modele.IA;
import modele.Plateau;
import modele.PlateauOthello;

public interface IAStrategie {

    void jouerCoup(PlateauOthello plateau, IA joueur) throws CoupInvalideException;

}
