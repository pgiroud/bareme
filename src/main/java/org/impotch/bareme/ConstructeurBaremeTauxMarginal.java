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
import java.util.List;

public class ConstructeurBaremeTauxMarginal extends ConstructeurBareme {


    public ConstructeurBaremeTauxMarginal() {
        super();
        this.montantMaxPrecedent = BigDecimal.ZERO;
    }

    public ConstructeurBaremeTauxMarginal(List<TrancheBareme> tranches) {
        super(tranches);
    }

    public ConstructeurBaremeTauxMarginal(BaremeTauxMarginalConstantParTranche bareme) {
        this(bareme.getTranches());
        typeArrondiSurChaqueTranche(bareme.getTypeArrondiSurChaqueTranche());
    }


    @Override
    protected TrancheBareme construireTranche(Intervalle inter, BigDecimal montantOuTaux) {
        TrancheBaremeTxMarginal tranche = new TrancheBaremeTxMarginal(inter, montantOuTaux);
        montantMaxPrecedent = inter.getFin();
        return tranche;
    }

    @Override
    protected TrancheBareme construireTranche(BigDecimal taux) {
        Intervalle intervalle = new Intervalle.Cons().de(montantMaxPrecedent).aPlusInfini().intervalle();
        return construireTranche(intervalle,taux);
    }


    public Bareme construire() {
        BaremeTauxMarginalConstantParTranche bareme = new BaremeTauxMarginalConstantParTranche();
        completerBareme(bareme);
        return bareme;
    }


    private class TrancheBaremeTxMarginal extends TrancheBareme {

        private BigDecimal montantTranche;

        public TrancheBaremeTxMarginal(Intervalle intervalle, BigDecimal tauxOuMontant) {
            super(intervalle, tauxOuMontant);
            calculMontantTranche();
        }

        private void calculMontantTranche() {
            if (getIntervalle().isBorne()) {
                BigDecimal largeur = getIntervalle().getFin().subtract(getIntervalle().getDebut());
                montantTranche = getTauxOuMontant().multiply(largeur);
            } else {
                montantTranche = null;
            }
        }

        @Override
        protected TrancheBareme newTranche(Intervalle intervalle, BigDecimal tauxOuMontant) {
            return new TrancheBaremeTxMarginal(intervalle, tauxOuMontant);
        }

        @Override
        public BigDecimal calcul(BigDecimal montant) {
            if (getIntervalle().encadre(montant)) {
                BigDecimal largeur = montant.subtract(getIntervalle().getDebut());
                return largeur.multiply(getTauxOuMontant());
            }
            else if (getIntervalle().valeursInferieuresA(montant)) return montantTranche;
            else return BigDecimal.ZERO;
        }
    }

}
