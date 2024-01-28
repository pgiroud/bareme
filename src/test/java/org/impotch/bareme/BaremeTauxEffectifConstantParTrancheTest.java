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

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.impotch.util.TypeArrondi.UNITE_LA_PLUS_PROCHE;
import static org.impotch.bareme.ConstructeurBareme.unBaremeATauxEffectif;


public class BaremeTauxEffectifConstantParTrancheTest {

    private final static BigDecimal MILLE = BigDecimal.valueOf(1000);
    private final static BigDecimal CENT = BigDecimal.valueOf(100);


    @Test
    public void uneSeuleTranche() {
        Bareme bareme = unBaremeATauxEffectif()
                .typeArrondiSurChaqueTranche(UNITE_LA_PLUS_PROCHE)
                .uniqueTranche("10 %").construire();

        assertThat(bareme.calcul(MILLE)).isEqualTo(CENT);
        assertThat(bareme.calcul(BigDecimal.valueOf(-1000))).isEqualTo("-100");
        assertThat(bareme.calcul(ZERO)).isEqualTo("0");
        // Avec un test sur l'arrondi
        assertThat(bareme.calcul(123_456_789)).isEqualTo("12345679");
    }

    @Test
    public void plusieursTranches() {
        Bareme bareme = unBaremeATauxEffectif()
                .typeArrondiSurChaqueTranche(UNITE_LA_PLUS_PROCHE)
                .jusqua(1000).taux("1 %")
                .de(1000).a(2000).taux("2 %")
                .plusDe(2000).taux("3 %").construire();

        // 1 %
        assertThat(bareme.calcul(BigDecimal.valueOf(-1000))).isEqualTo("-10");
        assertThat(bareme.calcul(ZERO)).isEqualTo("0");
        assertThat(bareme.calcul(MILLE)).isEqualTo("10");

        // 2 %
        assertThat(bareme.calcul(BigDecimal.valueOf(1001))).isEqualTo("20");
        assertThat(bareme.calcul(BigDecimal.valueOf(1050))).isEqualTo("21");
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualTo("40");

        // 3 %
        assertThat(bareme.calcul(BigDecimal.valueOf(2001))).isEqualTo("60");
        assertThat(bareme.calcul(new BigDecimal("1000000000000000"))).isEqualTo("30000000000000");

    }

    @Test
    public void optimisation() {
        Bareme bareme = unBaremeATauxEffectif()
                .typeArrondiSurChaqueTranche(UNITE_LA_PLUS_PROCHE)
                .de(0).a(1000).taux("1 %")
                .de(1000).a(2000).taux("1 %")
                .plusDe(2000).taux("2 %").construire();

        Bareme baremeOptimise = unBaremeATauxEffectif()
                .typeArrondiSurChaqueTranche(UNITE_LA_PLUS_PROCHE)
                .de(0).a(2000).taux("1 %")
                .plusDe(2000).taux("2 %").construire();

        assertThat(bareme).isEqualTo(baremeOptimise);
    }

    @Test
    public void assertion() {
        BaremeParTranche bareme1 = unBaremeATauxEffectif()
                .typeArrondiSurChaqueTranche(UNITE_LA_PLUS_PROCHE)
                .de(0).a(1000).taux("1 %")
                .de(1000).a(2000).taux("1 %")
                .plusDe(2000).taux("2 %").construire();

        BaremeParTranche bareme2 = unBaremeATauxEffectif()
                .typeArrondiSurChaqueTranche(UNITE_LA_PLUS_PROCHE)
                .de(0).a(1000).taux("1 %")
                .de(1000).a(2000).taux("1 %")
                .plusDe(2000).taux("2 %").construire();

        BaremeAssert.assertThat(bareme1).isEqualTo(bareme2);

    }

}
