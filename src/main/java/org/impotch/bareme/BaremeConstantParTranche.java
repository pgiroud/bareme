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
package org.impotch.bareme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.impotch.util.TypeArrondi;

/**
 * Un barème par tranche est une fonction en escalier c.-à-d. qu'elle est constante
 * sur des intervalles et les intervalles forment une partition de l'ensemble des nombres
 * réels positifs.
 * @author Patrick Giroud
 *
 */
public class BaremeConstantParTranche extends BaremeParTranche {

    public BaremeConstantParTranche() {
        super();
    }

    public BaremeConstantParTranche(BigDecimal seuil) {
        super();
        setSeuil(seuil);
    }

	public void ajouterTranche(int montantImposable, int montant) {
		this.ajouterTranche(new BigDecimal(montantImposable), new BigDecimal(montant));
	}
	
	public void ajouterDerniereTranche(int montant) {
		this.ajouterDerniereTranche(new BigDecimal(montant));
	}
	
	/* (non-Javadoc)
	 * @see org.impotch.afc.calcul.impot.bareme.BaremeParTranche#calculSansSeuil(java.math.BigDecimal)
	 */
	@Override
	protected BigDecimal calculSansSeuil(BigDecimal assiette) {
		for (TrancheBareme tranche : getTranches()) {
			if (null != tranche.getMontantMaxTranche() &&
                    ((!montantMaxNonInclus && 0 >= assiette.compareTo(tranche.getMontantMaxTranche()))
                            || (montantMaxNonInclus && 0 > assiette.compareTo(tranche.getMontantMaxTranche()))
                    )) return tranche.getTauxOuMontant();
		}
		return getTranches().get(getTranches().size()-1).getTauxOuMontant();
	}

    /**
     * Retourne un barème constant par tranche dont les tranches sont translatés du facteur taux.
     * Ainsi, pour un taux de 100 %, le barème obtenu sera identique au barème fourni.
     * @param taux  le taux de dilatation des tranches (en général, il est plus grand que 100 % en cas d'inflation)
     * @param typeArrondi L'arrondi appliqué sur chacune des tranches après application du taux.
     * @return le barème dont les tranches sont translatées (les valeurs sont quant à elles inchangées).
     */
	public BaremeConstantParTranche homothetieTranche(BigDecimal taux, TypeArrondi typeArrondi) {
		List<TrancheBareme> tranchesHomothetiques = new ArrayList<TrancheBareme>();
		for (TrancheBareme tranche : getTranches()) {
			tranchesHomothetiques.add(tranche.homothetie(taux,typeArrondi));
		}
		BaremeConstantParTranche bareme = new BaremeConstantParTranche();
		bareme.setTypeArrondiSurChaqueTranche(this.getTypeArrondiSurChaqueTranche());
		bareme.setTranches(tranchesHomothetiques);
		return bareme;
	}

    /**
     * Retourne un barème constant par tranche dont les valeurs sont translatés du facteur taux.
     * @param taux  le taux de dilatation des valeurs
     * @param typeArrondi  L'arrondi appliqué sur chacune des valeurs après application du taux
     * @return le barème dont les valeurs sont translatées (les tranches sont quant à elles inchangées).
     */
	public BaremeConstantParTranche homothetieValeur(BigDecimal taux, TypeArrondi typeArrondi) {
		List<TrancheBareme> tranchesHomothetiques = new ArrayList<TrancheBareme>();
		for (TrancheBareme tranche : getTranches()) {
			tranchesHomothetiques.add(tranche.homothetieValeur(taux,typeArrondi));
		}
		BaremeConstantParTranche bareme = new BaremeConstantParTranche();
		bareme.setTypeArrondiSurChaqueTranche(this.getTypeArrondiSurChaqueTranche());
		bareme.setTranches(tranchesHomothetiques);
		return bareme;
	}
	
}
