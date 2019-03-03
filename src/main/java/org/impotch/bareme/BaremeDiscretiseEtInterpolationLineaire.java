/**
 * This file is part of impotch/bareme.
 *
 * impotch/bareme is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * impotch/bareme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with impotch/bareme.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is part of impotch/bareme.
 * <p>
 * impotch/bareme is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 * <p>
 * impotch/bareme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with impotch/bareme.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impotch.bareme;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import org.impotch.util.TypeArrondi;

/**
 * Cette classe représente un barème qui est constitué de points reliés entre eux par
 * des droites.
 * Pour avoir la valeur du barème en un point donné, on recherche tout d'abord l'unique intervalle contenant ce point
 * puis on fait une interpolation linéaire pour trouver la valeur.
 *
 * @author Patrick Giroud
 *
 */
public final class BaremeDiscretiseEtInterpolationLineaire implements Bareme {

    private LinkedList<Point> montants = new LinkedList<Point>();
    private boolean listeTriee = true;
    private TypeArrondi typeArrondi = TypeArrondi.CINQ_CTS;
    private boolean definiAvantBorneInf;
    private boolean definiApresBorneSup;

    /**************************************************/
    /******* Accesseurs / Mutateurs *******************/
    /**************************************************/

    public void ajouterPointDiscretisation(BigDecimal abscisse, BigDecimal ordonnee) {
        listeTriee = false;
        montants.add(new Point(abscisse, ordonnee));
    }

    public void ajouterPointDiscretisation(Point point) {
        listeTriee = false;
        montants.add(point);
    }

    /**
     * @param typeArrondi the typeArrondi to set
     */
    public void setTypeArrondi(TypeArrondi typeArrondi) {
        this.typeArrondi = typeArrondi;
    }

    /**
     * @return the typeArrondi
     */
    protected TypeArrondi getTypeArrondi() {
        return typeArrondi;
    }

    public void setDefiniAvantBorneInf(boolean defini) {
        definiAvantBorneInf = defini;
    }

    public void setDefiniApresBorneSup(boolean defini) {
        definiApresBorneSup = defini;
    }

    protected BigDecimal interpolationLineaire(BigDecimal assiette, Point borneInf, Point borneSup) {
        if (0 == assiette.compareTo(borneInf.getAbscisse())) return borneInf.getOrdonnee();
        if (0 == assiette.compareTo(borneSup.getAbscisse())) return borneSup.getOrdonnee();
        BigDecimal deltaImpotTranche = borneSup.getOrdonnee().subtract(borneInf.getOrdonnee());
        BigDecimal deltaAssietteTranche = borneSup.getAbscisse().subtract(borneInf.getAbscisse());
        BigDecimal deltaAssiette = assiette.subtract(borneInf.getAbscisse());
        BigDecimal deltaImpot = deltaImpotTranche.multiply(deltaAssiette).divide(deltaAssietteTranche, 3, BigDecimal.ROUND_HALF_UP);
        return getTypeArrondi().arrondirMontant(borneInf.getOrdonnee().add(deltaImpot));
    }


    /* (non-Javadoc)
     * @see org.impotch.afc.calcul.bareme.Bareme#calcul(java.math.BigDecimal)
     */
    @Override
    public BigDecimal calcul(BigDecimal assiette) {
        if (!listeTriee) {
            Collections.sort(montants);
            listeTriee = true;
        }
        Point dernierPoint = montants.getLast();
        BigDecimal assietteMaximumDansBareme = dernierPoint.getAbscisse();
        int comparaisonAvecMax = assiette.compareTo(assietteMaximumDansBareme);
        if (0 < comparaisonAvecMax && !definiApresBorneSup)
            throw new IllegalArgumentException("Il est impossible de calculer l'impôt pour une assiette de " + assiette + " car le barème s'arrête à " + assietteMaximumDansBareme);
        else if (0 <= comparaisonAvecMax) return dernierPoint.getOrdonnee();

        Point premierPoint = montants.getFirst();
        BigDecimal assietteMinimumDansBareme = premierPoint.getAbscisse();
        int comparaisonAvecMin = assietteMinimumDansBareme.compareTo(assiette);
        if (0 < comparaisonAvecMin && !definiAvantBorneInf)
            throw new IllegalArgumentException("Il est impossible de calculer l'impôt pour une assiette de " + assiette + " car le barème commence à " + assietteMinimumDansBareme);
        else if (0 <= comparaisonAvecMin) return premierPoint.getOrdonnee();

        Point montantSup = null;
        Point montantInf = null;
        for (ListIterator<Point> iter = montants.listIterator(); iter.hasNext(); ) {
            montantSup = iter.next();
            if (0 < montantSup.getAbscisse().compareTo(assiette)) {
                break;
            } else {
                montantInf = montantSup;
            }
        }
        return interpolationLineaire(assiette, montantInf, montantSup);
    }


}
