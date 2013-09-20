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

import org.junit.Test;

import org.impotch.util.TypeArrondi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class BaremeTauxMarginalConstantParTrancheTest {

    @Test
    public void uneSeuleTranche() {
        BaremeTauxMarginalConstantParTranche.Constructeur constructeur = new BaremeTauxMarginalConstantParTranche.Constructeur();
        constructeur.derniereTranche("10 %");
        Bareme bareme = constructeur.construire();
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bareme.calcul(BigDecimal.TEN)).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(bareme.calcul(BigDecimal.valueOf(100))).isEqualByComparingTo(BigDecimal.TEN);
        // valeur négative : par convention, résultat = 0.
        assertThat(bareme.calcul(BigDecimal.valueOf(-100))).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
