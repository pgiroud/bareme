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
import java.util.ArrayList;
import java.util.List;

import ch.ge.afc.util.TypeArrondi;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class BaremeConstantParTranche extends BaremeParTranche {

	public void ajouterTranche(int montantImposable, int montant) {
		this.ajouterTranche(new BigDecimal(montantImposable), new BigDecimal(montant));
	}
	
	public void ajouterDerniereTranche(int montant) {
		this.ajouterDerniereTranche(new BigDecimal(montant));
	}
	
	/* (non-Javadoc)
	 * @see ch.ge.afc.calcul.impot.bareme.BaremeParTranche#calculSansSeuil(java.math.BigDecimal)
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

	public BaremeConstantParTranche homothetieTranche(BigDecimal taux, TypeArrondi typeArrondi) {
		List<TrancheBareme> tranchesHomothetiques = new ArrayList<TrancheBareme>();
		for (TrancheBareme tranche : getTranches()) {
			tranchesHomothetiques.add(tranche.homothetie(taux,typeArrondi));
		}
		BaremeConstantParTranche bareme = new BaremeConstantParTranche();
		bareme.setTypeArrondi(this.getTypeArrondi());
		bareme.setTranches(tranchesHomothetiques);
		return bareme;
	}

	public BaremeConstantParTranche homothetieValeur(BigDecimal taux, TypeArrondi typeArrondi) {
		List<TrancheBareme> tranchesHomothetiques = new ArrayList<TrancheBareme>();
		for (TrancheBareme tranche : getTranches()) {
			tranchesHomothetiques.add(tranche.homothetieValeur(taux,typeArrondi));
		}
		BaremeConstantParTranche bareme = new BaremeConstantParTranche();
		bareme.setTypeArrondi(this.getTypeArrondi());
		bareme.setTranches(tranchesHomothetiques);
		return bareme;
	}
	
}
