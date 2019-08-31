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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.AssertionsForClassTypes;
import org.impotch.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: patrick
 * Date: 29/09/13
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
public class BaremeTauxEffectifConstantParTrancheAssert extends AbstractAssert<BaremeTauxEffectifConstantParTrancheAssert, BaremeTauxEffectifConstantParTranche> {

    private BigDecimal offset;
    private Comparator<BigDecimal> bigDecimalComparator;


    public BaremeTauxEffectifConstantParTrancheAssert(BaremeTauxEffectifConstantParTranche actual) {
        super(actual, BaremeTauxEffectifConstantParTrancheAssert.class);
    }

    public BaremeTauxEffectifConstantParTrancheAssert tolerance(String offsetStr) {
        offset = BigDecimalUtil.parseTaux(offsetStr);
        bigDecimalComparator = new BigDecimalComparator(offset);
        return this;
    }

    public static BaremeTauxEffectifConstantParTrancheAssert assertThat(BaremeTauxEffectifConstantParTranche actual) {
        return new BaremeTauxEffectifConstantParTrancheAssert(actual);
    }

    private void compareNbreTranche(BaremeTauxEffectifConstantParTranche expected) {
        int size = actual.obtenirTranches().size();
        int sizeExpected = expected.obtenirTranches().size();
        Assertions.assertThat(actual.obtenirTranches().size())
                .overridingErrorMessage("Nbre tranches attendues %1$s mais est %2$s", sizeExpected, size)
                .isEqualTo(sizeExpected);
    }

    public BaremeTauxEffectifConstantParTrancheAssert isEqualTo(BaremeTauxEffectifConstantParTranche expected) {
        compareNbreTranche(expected);
        List<TrancheBareme> tranches = actual.obtenirTranches();
        int i = 0;
        for (TrancheBareme trancheAttendue : expected.obtenirTranches()) {
            TrancheBareme tranche = tranches.get(i);
            // Comparaison des intervalles
            Intervalle intervalle = tranche.getIntervalle();
            Intervalle intervalleAttendu = trancheAttendue.getIntervalle();
            AssertionsForClassTypes.assertThat(intervalle)
                    .overridingErrorMessage("Sur la tranche %1$d, abscisse attendue %2$s mais est %3$s", i, intervalleAttendu, intervalle)
                    .isEqualTo(intervalleAttendu);
            // Comparaison des ordonnées
            BigDecimal ordonnee = tranche.getTauxOuMontant();
            BigDecimal ordonneeAttendue = trancheAttendue.getTauxOuMontant();
            AbstractBigDecimalAssert assertion = Assertions.assertThat(ordonnee)
                    .overridingErrorMessage("Sur la tranche %1$d, dont l'intervalle est %2$s, l'ordonnée attendue %3$s mais est %4$s", i, intervalle, ordonneeAttendue, ordonnee);
            if (null != offset) {
                assertion = assertion.usingComparator(bigDecimalComparator);
            }
            assertion.isEqualTo(ordonneeAttendue);
            i++;
        }
        return this;
    }

    private static class BigDecimalComparator implements Comparator<BigDecimal> {

        private final BigDecimal tolerance;

        public BigDecimalComparator(BigDecimal offset) {
            tolerance = offset;
        }

        @Override
        public int compare(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
            if (tolerance.compareTo(bigDecimal2.subtract(bigDecimal1).abs()) >= 0) return 0;
            return bigDecimal2.compareTo(bigDecimal1);
        }
    }

}
