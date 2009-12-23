/**
 * This file is part of CalculImpotCH.
 *
 * CalculImpotCH is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * CalculImpotCH is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CalculImpotCH.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ge.afc.bareme;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import ch.ge.afc.util.TypeArrondi;

/**
 * Cette classe représente un barème qui est constitué de points reliés entre eux par
 * des droites.
 * Pour avoir la valeur du barème en un point donné, on recherche tout d'abord l'unique intervalle contenant ce point
 * puis on fait une interpolation linéaire pour trouver la valeur.
 *
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class BaremeDiscretiseEtInterpolationLineaire implements Bareme {

	private LinkedList<Point> montants = new LinkedList<Point>();
	private boolean listeTriee = true;
	
    /**************************************************/
    /******* Accesseurs / Mutateurs *******************/
    /**************************************************/
	
	public void ajouterPointDiscretisation(BigDecimal abscisse, BigDecimal ordonnee) {
		listeTriee = false;
		montants.add(new Point(abscisse,ordonnee));
	}
	
	public void ajouterPointDiscretisation(Point point) {
		listeTriee = false;
		montants.add(point);
	}
	
	protected BigDecimal interpolationLineaire(BigDecimal assiette, Point borneInf, Point borneSup) {
		if (0 == assiette.compareTo(borneInf.getAbscisse())) return borneInf.getOrdonnee();
		if (0 == assiette.compareTo(borneSup.getAbscisse())) return borneSup.getOrdonnee();
		BigDecimal deltaImpotTranche = borneSup.getOrdonnee().subtract(borneInf.getOrdonnee());
		BigDecimal deltaAssietteTranche = borneSup.getAbscisse().subtract(borneInf.getAbscisse());
		BigDecimal deltaAssiette = assiette.subtract(borneInf.getAbscisse());
		BigDecimal deltaImpot = deltaImpotTranche.multiply(deltaAssiette).divide(deltaAssietteTranche,3,BigDecimal.ROUND_HALF_UP);
		deltaImpot = TypeArrondi.CINQ_CTS.arrondirMontant(deltaImpot);
		return borneInf.getOrdonnee().add(deltaImpot); 
	}
	
	
	/* (non-Javadoc)
	 * @see ch.ge.afc.calcul.bareme.Bareme#calcul(java.math.BigDecimal)
	 */
	@Override
	public BigDecimal calcul(BigDecimal assiette) {
		if (!listeTriee) {
			Collections.sort(montants);
			listeTriee = true;
		}
		BigDecimal assietteMaximumDansBareme = montants.getLast().getAbscisse();
		int comparaisonAvecMax = assiette.compareTo(assietteMaximumDansBareme);
		if (0 < comparaisonAvecMax) throw new IllegalArgumentException("Il est impossible de calculer l'impôt pour une assiette de " + assiette + " car le barème s'arrête à " + assietteMaximumDansBareme);
		if (0 == comparaisonAvecMax) return montants.get(montants.size() -1).getOrdonnee();
		Point montantSup = null;
		Point montantInf = null;
		for (ListIterator<Point> iter = montants.listIterator(); iter.hasNext();) {
			montantSup = iter.next();
			if (0 < montantSup.getAbscisse().compareTo(assiette)) {
				break;
			} else {
				montantInf = montantSup;
			}
		}
		return interpolationLineaire(assiette,montantInf,montantSup);
	}

	
}
