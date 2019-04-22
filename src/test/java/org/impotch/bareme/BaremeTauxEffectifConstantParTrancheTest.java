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

import java.math.BigDecimal;


import org.impotch.util.TypeArrondi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaremeTauxEffectifConstantParTrancheTest {

    private final static BigDecimal MILLE = BigDecimal.valueOf(1000);
    private final static BigDecimal CENT = BigDecimal.valueOf(100);


    @Test
    public void uneSeuleTranche() {
        ConstructeurBareme constructeur = new ConstructeurBareme()
            .typeArrondiSurChaqueTranche(TypeArrondi.FRANC)
            .uniqueTranche("10 %");


        Bareme bareme = constructeur.construireBaremeTauxEffectifConstantParTranche();

        assertThat(bareme.calcul(MILLE)).isEqualTo(CENT);
        assertThat(bareme.calcul(BigDecimal.valueOf(-1000))).isEqualTo("-100");
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("0");
        // Avec un test sur l'arrondi
        assertThat(bareme.calcul(new BigDecimal("123456789"))).isEqualTo("12345679");
    }

    @Test
    public void plusieursTranches() {
        ConstructeurBareme constructeur = new ConstructeurBareme()
                .typeArrondiSurChaqueTranche(TypeArrondi.FRANC)
                .premiereTranche(1000, "1 %")
                .tranche(1000,2000, "2 %")
                .derniereTranche(2000,"3 %");

        Bareme bareme = constructeur.construireBaremeTauxEffectifConstantParTranche();

        // 1 %
        assertThat(bareme.calcul(BigDecimal.valueOf(-1000))).isEqualTo("-10");
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("0");
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
        ConstructeurBareme constructeur = new ConstructeurBareme()
                .typeArrondiSurChaqueTranche(TypeArrondi.FRANC)
                .tranche(0,1000, "1 %")
                .tranche(1000,2000, "1 %")
                .derniereTranche(2000,"2 %");


        Bareme bareme = constructeur.construireBaremeTauxEffectifConstantParTranche();

        constructeur = new ConstructeurBareme()
                .typeArrondiSurChaqueTranche(TypeArrondi.FRANC)
                .tranche(0,2000, "1 %")
                .derniereTranche(2000,"2 %");
        Bareme baremeOptimise = constructeur.construireBaremeTauxEffectifConstantParTranche();

        assertThat(bareme).isEqualTo(baremeOptimise);
    }

}
