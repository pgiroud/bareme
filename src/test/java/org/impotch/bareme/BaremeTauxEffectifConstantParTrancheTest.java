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
import static org.impotch.bareme.BaremeAssert.*;

public class BaremeTauxEffectifConstantParTrancheTest {

    private final static BigDecimal MILLE = BigDecimal.valueOf(1000);
    private final static BigDecimal CENT = BigDecimal.valueOf(100);


    @Test
    public void uneSeuleTranche() {
        BaremeTauxEffectifConstantParTranche bareme = new BaremeTauxEffectifConstantParTranche();
        bareme.setTypeArrondiSurChaqueTranche(TypeArrondi.FRANC);
        bareme.ajouterDerniereTranche("10 %");

        assertThat(bareme.calcul(MILLE)).isEqualTo(CENT);
        assertThat(bareme.calcul(BigDecimal.valueOf(-1000))).isEqualTo("-100");
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("0");
        // Avec un test sur l'arrondi
        assertThat(bareme.calcul(new BigDecimal("123456789"))).isEqualTo("12345679");
    }

    @Test
    public void plusieursTranches() {
        BaremeTauxEffectifConstantParTranche bareme = new BaremeTauxEffectifConstantParTranche();
        bareme.setTypeArrondiSurChaqueTranche(TypeArrondi.FRANC);
        bareme.ajouterTranche(1000,"1 %");
        bareme.ajouterTranche(2000,"2 %");
        bareme.ajouterDerniereTranche("3 %");

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
    public void comparaisonBareme() {
        BaremeTauxEffectifConstantParTranche bareme1 = new BaremeTauxEffectifConstantParTranche();
        bareme1.setTypeArrondiSurChaqueTranche(TypeArrondi.FRANC);
        bareme1.ajouterDerniereTranche("10 %");

        BaremeTauxEffectifConstantParTranche bareme2 = new BaremeTauxEffectifConstantParTranche();
        bareme2.setTypeArrondiSurChaqueTranche(TypeArrondi.FRANC);
        bareme2.ajouterDerniereTranche("10 %");

        BaremeAssert.assertThat(bareme1).isEqualTo(bareme2);

        bareme1 = new BaremeTauxEffectifConstantParTranche();
        bareme1.setTypeArrondiSurChaqueTranche(TypeArrondi.FRANC);
        bareme1.ajouterTranche(1000,"5 %");
        bareme1.ajouterDerniereTranche("10 %");

        bareme2 = new BaremeTauxEffectifConstantParTranche();
        bareme2.setTypeArrondiSurChaqueTranche(TypeArrondi.FRANC);
        bareme2.ajouterTranche(1000,"5 %");
        bareme2.ajouterDerniereTranche("10 %");

        BaremeAssert.assertThat(bareme1).isEqualTo(bareme2);

        bareme2 = new BaremeTauxEffectifConstantParTranche();
        bareme2.setTypeArrondiSurChaqueTranche(TypeArrondi.FRANC);
        bareme2.ajouterTranche(1000,"6 %");
        bareme2.ajouterDerniereTranche("10 %");

        BaremeAssert.assertThat(bareme1).tolerance("1 %").isEqualTo(bareme2);
    }
}
