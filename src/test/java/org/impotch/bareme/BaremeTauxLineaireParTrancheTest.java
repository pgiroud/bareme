/*
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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.impotch.bareme.ConstructeurBareme.unBaremeATauxEffectif;

public class BaremeTauxLineaireParTrancheTest {

    @Test
    public void troisTranches() {
        Bareme bareme = unBaremeATauxEffectif().fermeAGauche()
                .jusqua(1000).taux("0 %")
                .de(1000).a(2000).taux("1 %").increment("0.1 %")
                .plusDe(2000).taux("2 %").increment("0.0015 %")
                .construire();

        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualTo(new BigDecimal("10.00"));
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualTo(new BigDecimal("40.00"));
        assertThat(bareme.calcul(BigDecimal.valueOf(3000))).isEqualTo(new BigDecimal("105.00"));

    }

}
