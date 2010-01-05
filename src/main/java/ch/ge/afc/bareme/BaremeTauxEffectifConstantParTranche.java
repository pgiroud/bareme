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

import ch.ge.afc.util.BigDecimalUtil;
import ch.ge.afc.util.HashCodeBuilder;
import ch.ge.afc.util.TypeArrondi;

/**
 * Barème à taux effectif défini par tranche et dont le taux est constant sur chacune des tranches.
 * Ces barèmes sont typiquement utilisés pour l'impôt à la source.
 *
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public final class BaremeTauxEffectifConstantParTranche extends
		BaremeTauxEffectifParTranche {

	/**************************************************/
	/****************** Attributs *********************/
	/**************************************************/

	private boolean montantMaxNonInclus;
	
    /**************************************************/
    /************* Accesseurs / Mutateurs *************/
    /**************************************************/

	public void setMontantMaxNonInclus() {
		montantMaxNonInclus = true;
	}
	
    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

	public void ajouterTranche(int montant, BigDecimal taux) {
		getTranches().add(new TrancheBareme(new BigDecimal(montant),taux));
	}
	
	public void ajouterTranche(int montant, String taux) {
		getTranches().add(new TrancheBareme(new BigDecimal(montant),BigDecimalUtil.parseTaux(taux)));
	}
	
	public void ajouterDerniereTranche(String taux) {
		getTranches().add(new TrancheBareme.DerniereTrancheBareme(BigDecimalUtil.parseTaux(taux)));
	}
	
	public void ajouterDerniereTranche(BigDecimal taux) {
		getTranches().add(new TrancheBareme.DerniereTrancheBareme(taux));
	}
	
	public BaremeTauxEffectifConstantParTranche homothetie(BigDecimal taux, TypeArrondi typeArrondi) {
		List<TrancheBareme> tranchesHomothetiques = new ArrayList<TrancheBareme>();
		for (TrancheBareme tranche : getTranches()) {
			tranchesHomothetiques.add(tranche.homothetie(taux,typeArrondi));
		}
		BaremeTauxEffectifConstantParTranche bareme = new BaremeTauxEffectifConstantParTranche();
		bareme.setTypeArrondi(this.getTypeArrondi());
		bareme.setTranches(tranchesHomothetiques);
		return bareme;
	}
	
	/* (non-Javadoc)
	 * @see ch.ge.afc.calcul.impot.bareme.BaremeTauxEffectif#getTaux(java.math.BigDecimal)
	 */
	@Override
	public BigDecimal getTaux(BigDecimal assiette) {
		for (TrancheBareme tranche : getTranches()) {
			int comparaison;
			if (null == tranche.getMontantMaxTranche()) {
				comparaison = -1;
			} else {
				comparaison = assiette.compareTo(tranche.getMontantMaxTranche());
			}
			if ((montantMaxNonInclus && 0 > comparaison) 
					|| 
				(!montantMaxNonInclus && 0 >= comparaison)) {
				return tranche.getTauxOuMontant();
			}
		}
		return getTranches().get(getTranches().size()-1).getTauxOuMontant();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BaremeTauxEffectifConstantParTranche)) return false;
		BaremeTauxEffectifConstantParTranche bareme = (BaremeTauxEffectifConstantParTranche)obj;
		if (this.montantMaxNonInclus != bareme.montantMaxNonInclus) return false;
		if (!this.getTypeArrondi().equals(bareme.getTypeArrondi())) return false;
		if (0 != BigDecimalUtil.nullSafeCompare(this.getSeuil(), bareme.getSeuil())) return false;
		if (!this.getTranches().equals(bareme.getTranches())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().add(montantMaxNonInclus).add(getTypeArrondi())
				.add(getSeuil()).add(getTranches()).hash();
	}

	
}
