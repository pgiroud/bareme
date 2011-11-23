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


/**
 * Le barème IFD et le barème de Neuchâtel sont des barèmes à taux marginal constant par tranche mais dont
 * la dernière tranche est définie par un taux effectif. C'est la raison pour laquelle
 * le taux marginal n'est pas croissant mais décroissant sur la dernière tranche.
 * 
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class BaremeTxMarginalEtEffectifParTranche extends
		BaremeTauxMarginalConstantParTranche {

	@Override
	public BigDecimal calcul(BigDecimal pAssiette) {
		if (0 > getMontantImposableMaxDeAvantDerniereTranche().compareTo(pAssiette)) return getTypeArrondiSurChaqueTranche().arrondirMontant(pAssiette.multiply(this.getTauxDerniereTranche()));
		else return super.calcul(pAssiette);
	}

	public static class Constructeur extends BaremeTauxMarginalConstantParTranche.Constructeur {
		
		public BaremeTxMarginalEtEffectifParTranche construire() {
			BaremeTxMarginalEtEffectifParTranche bareme = new BaremeTxMarginalEtEffectifParTranche();
			bareme.setTranches(tranches);
			bareme.setTypeArrondiSurChaqueTranche(getTypeArrondiSurChaqueTranche());
            bareme.setTypeArrondiGlobal(getTypeArrondiGlobal());
			bareme.setSeuil(getSeuil());
			return bareme;
		}
		
	}	
	
}
