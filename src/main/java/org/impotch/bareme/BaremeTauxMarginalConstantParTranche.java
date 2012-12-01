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
import static java.math.BigDecimal.ZERO;
import java.util.ArrayList;
import java.util.List;

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.TypeArrondi;

public class BaremeTauxMarginalConstantParTranche extends BaremeParTranche implements Bareme {

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

	@Override
	public BigDecimal calculSansSeuil(BigDecimal pAssiette) {
		BigDecimal resultat = ZERO;
		BigDecimal montantMaxTranchePrecedente = ZERO;
		for (TrancheBareme tranche : getTranches()) {
			resultat = resultat.add(getTypeArrondiSurChaqueTranche().arrondirMontant(tranche.calcul(montantMaxTranchePrecedente,pAssiette)));
			montantMaxTranchePrecedente = tranche.getMontantMaxTranche();
		}
		return getTypeArrondiGlobal().arrondirMontant(resultat);
	}

	
	protected BigDecimal getMontantImposableMaxDeAvantDerniereTranche() {
		TrancheBareme avantDerniereTranche = getTranches().get(getTranches().size()-2);
		return avantDerniereTranche.getMontantMaxTranche();
	}
	
	protected BigDecimal getTauxDerniereTranche() {
		TrancheBareme derniereTranche = getTranches().get(getTranches().size()-1);
		return derniereTranche.getTauxOuMontant();
	}
	
	public BaremeTauxMarginalConstantParTranche homothetie(BigDecimal taux, TypeArrondi typeArrondi) {
		List<TrancheBareme> tranchesHomothetiques = new ArrayList<TrancheBareme>();
		for (TrancheBareme tranche : getTranches()) {
			tranchesHomothetiques.add(tranche.homothetie(taux,typeArrondi));
		}
		BaremeTauxMarginalConstantParTranche bareme = new BaremeTauxMarginalConstantParTranche();
		bareme.setTypeArrondiSurChaqueTranche(this.getTypeArrondiSurChaqueTranche());
        bareme.setTypeArrondiGlobal(this.getTypeArrondiGlobal());
		bareme.setTranches(tranchesHomothetiques);
		return bareme;
	}
	
    /**************************************************/
    /************** Classes internes ******************/
    /**************************************************/
	
	public static class Constructeur {
		protected List<TrancheBareme> tranches = new ArrayList<TrancheBareme>();
		private TypeArrondi typeArrondiSurChaqueTranche = TypeArrondi.CT;
        private TypeArrondi typeArrondiGlobal = TypeArrondi.CT;
		private BigDecimal seuil;
		
		public Constructeur() {
			super();
		}
		
		public Constructeur(BaremeTauxMarginalConstantParTranche bareme) {
			super();
			tranches = bareme.getTranches();
			typeArrondiSurChaqueTranche = bareme.getTypeArrondiSurChaqueTranche();
		}

		public Constructeur typeArrondiSurChaqueTranche(TypeArrondi typeArrondi) {
			this.typeArrondiSurChaqueTranche = typeArrondi;
			return this;
		}

        public Constructeur typeArrondiGlobal(TypeArrondi typeArrondi) {
            this.typeArrondiGlobal = typeArrondi;
            return this;
        }

		public Constructeur seuil(int montant) {
			this.seuil = new BigDecimal(montant);
			return this;
		}
		
		public Constructeur tranche(int montantImposable, String taux) {
			tranches.add(TrancheBaremeTxMarginal.construireTranche(new BigDecimal(montantImposable),BigDecimalUtil.parseTaux(taux)));
			return this;
		}
		
		public Constructeur derniereTranche(String taux) {
			tranches.add(TrancheBaremeTxMarginal.construireDerniereTranche(BigDecimalUtil.parseTaux(taux)));
			return this;
		}
		
		protected TypeArrondi getTypeArrondiSurChaqueTranche() {
			return typeArrondiSurChaqueTranche;
		}

        protected TypeArrondi getTypeArrondiGlobal() {
			return typeArrondiGlobal;
		}


		protected BigDecimal getSeuil() {
			return seuil;
		}
		
		public BaremeTauxMarginalConstantParTranche construire() {
			BaremeTauxMarginalConstantParTranche bareme = new BaremeTauxMarginalConstantParTranche();
			bareme.setTranches(tranches);
			bareme.setTypeArrondiSurChaqueTranche(getTypeArrondiSurChaqueTranche());
            bareme.setTypeArrondiGlobal(getTypeArrondiGlobal());
			bareme.setSeuil(getSeuil());
			return bareme;
		}
	
	}
	
	
    /**************************************************/
    /************** Classes internes ******************/
    /**************************************************/
	
	public static class TrancheBaremeTxMarginal extends TrancheBareme {
		/**
		 * Construction d'une tranche de barème.
		 * Les seules informations utiles sont le montant maximum d'une tranche 
		 * et le taux de la tranche.
		 * @param pMontantImposableMax Le montant imposable maximum de la tranche (ne doit pas être null) 
		 * @param pTaux Le taux de la tranche (ne doit pas être null)
		 */
		public static TrancheBareme construireTranche(BigDecimal pMontantImposableMax, BigDecimal pTaux) {
			return new TrancheBaremeTxMarginal(pMontantImposableMax, pTaux);
		}
		
		public static TrancheBareme construireDerniereTranche(BigDecimal pTaux) {
			return new DerniereTrancheBaremeTxMarginal(pTaux);
		}
		
		/**
		 * Construction d'une tranche de barème.
		 * Les seules informations utiles sont le montant maximum d'une tranche 
		 * et le taux de la tranche.
		 * @param pMontantImposableMax Le montant imposable maximum de la tranche (peut être l'infini) 
		 * @param pTaux Le taux de la tranche
		 */
		protected TrancheBaremeTxMarginal(BigDecimal pMontantImposableMax, BigDecimal pTaux) {
			super(pMontantImposableMax,pTaux);
		}

		protected BigDecimal getTaux() {
			return super.getTauxOuMontant();
		}
		
		/**
		 * Calcul le montant d'impôts dans la tranche. Le résultat est arrondi au 5 centimes
		 * les plus proche.
		 * 
		 * @param pMontantImposableMaxTranchePrecedente Le montant imposable maximum de la tranche précédente.
		 * @param pMontantImposable Le montant imposable.
		 * @return Le montant d'impôts dans la tranche.
		 */
		public BigDecimal calcul(BigDecimal pMontantImposableMaxTranchePrecedente, BigDecimal pMontantImposable) {
			BigDecimal montantDansTranche = ZERO;
			if (0 < pMontantImposable.compareTo(this.getMontantMaxTranche())) {
				montantDansTranche = this.getMontantMaxTranche().subtract(pMontantImposableMaxTranchePrecedente);
			} else {
				if (0 < pMontantImposable.compareTo(pMontantImposableMaxTranchePrecedente)) {
					montantDansTranche = pMontantImposable.subtract(pMontantImposableMaxTranchePrecedente);
				}
			}
			return montantDansTranche.multiply(this.getTauxOuMontant());
		}

		/**
		 * Une tranche peut être translatée. Translater une tranche consiste à
		 * multiplier le montant imposable maximum par le rapport de translation
		 * et à l'arrondir au francs le plus proche.
		 * @param pRapport Le rapport de translation.
		 * @return Une nouvelle tranche translatée.
		 */
		public TrancheBareme homothetie(BigDecimal pRapport, TypeArrondi typeArrondi) {
			BigDecimal inter = this.getMontantMaxTranche().multiply(pRapport);
			BigDecimal montantImposableMax = typeArrondi.arrondirMontant(inter);
			return new TrancheBaremeTxMarginal(montantImposableMax,this.getTauxOuMontant());
		}

		private static class DerniereTrancheBaremeTxMarginal extends TrancheBareme.DerniereTrancheBareme
		{
			protected DerniereTrancheBaremeTxMarginal(BigDecimal pTaux)
			{
				super(pTaux);
			}
			
			public BigDecimal calcul(BigDecimal pMontantImposableMaxTranchePrecedente, BigDecimal pMontantImposable) {
				BigDecimal montantDansTranche = ZERO; 
				if (0 < pMontantImposable.compareTo(pMontantImposableMaxTranchePrecedente)) {
					montantDansTranche = pMontantImposable.subtract(pMontantImposableMaxTranchePrecedente);
				}
				return montantDansTranche.multiply(getTauxOuMontant());
			}

		}
		
	}
}
