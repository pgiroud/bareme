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

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.HashCodeBuilder;
import org.impotch.util.TypeArrondi;
import static java.util.stream.Collectors.toList;

/**
 * Barème à taux effectif défini par tranche et dont le taux est constant sur chacune des tranches.
 * Ces barèmes sont typiquement utilisés pour l'impôt à la source.
 *
 * @author Patrick Giroud
 *
 */
public final class BaremeTauxEffectifConstantParTranche extends
		BaremeTauxEffectifParTranche {

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
		BaremeTauxEffectifConstantParTranche bareme = new BaremeTauxEffectifConstantParTranche();
		bareme.setTypeArrondiSurChaqueTranche(this.getTypeArrondiSurChaqueTranche());
        bareme.setTranches(getTranches().stream().map(tranche -> tranche.homothetie(taux,typeArrondi)).collect(toList()));
		return bareme;
	}
	
	/* (non-Javadoc)
	 * @see org.impotch.afc.calcul.impot.bareme.BaremeTauxEffectif#getTaux(java.math.BigDecimal)
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
        return this.montantMaxNonInclus==bareme.montantMaxNonInclus
                && this.getTypeArrondiSurChaqueTranche().equals(bareme.getTypeArrondiSurChaqueTranche())
                && 0==BigDecimalUtil.nullSafeCompare(this.getSeuil(), bareme.getSeuil())
                && this.getTranches().equals(bareme.getTranches());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().add(montantMaxNonInclus).add(getTypeArrondiSurChaqueTranche())
				.add(getSeuil()).add(getTranches()).hash();
	}

	public List<TrancheBareme> obtenirTranches() {
		return new ArrayList<>(getTranches());
	}
}
