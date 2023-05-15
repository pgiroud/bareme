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

import org.impotch.util.TypeArrondi;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.impotch.bareme.ConstructeurBareme.unBaremeATauxMarginal;

public class BaremeTauxMarginalConstantParTrancheTest {

    @Test
    public void uneSeuleTranche() {
        Bareme bareme =  unBaremeATauxMarginal()
                .plusDe(0).taux("10 %").construire();

        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bareme.calcul(BigDecimal.TEN)).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(bareme.calcul(BigDecimal.valueOf(100))).isEqualByComparingTo(BigDecimal.TEN);
    }


    @Test
    public void deuxTranche() {
        Bareme bareme =  unBaremeATauxMarginal()
                .de(0).a(1000).taux("5 %")
                .plusDe(1000).taux("10 %").construire();

        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bareme.calcul(BigDecimal.valueOf(100))).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    public void troisTranche() {
        Bareme bareme =  unBaremeATauxMarginal()
                .de(0).a(1000).taux("5 %")
                .de(1000).a(2000).taux("10 %")
                .plusDe(2000).taux("15 %").construire();


        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bareme.calcul(BigDecimal.valueOf(100))).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(bareme.calcul(BigDecimal.valueOf(3000))).isEqualByComparingTo(BigDecimal.valueOf(300));
    }

    @Test
    public void baremeConstruitAvecDesPuis() {
        Bareme bareme =  unBaremeATauxMarginal()
                .jusqua(200).taux("0 %")
                .puisJusqua(1000).taux("5 %")
                .puisJusqua(2000).taux("10 %")
                .puis().taux("15 %").construire();


        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualByComparingTo(BigDecimal.valueOf(40));
        assertThat(bareme.calcul(BigDecimal.valueOf(3000))).isEqualByComparingTo(BigDecimal.valueOf(290));

    }

    @Test
    public void homothetieBaremeRevenuGE2023() {
        BigDecimal indiceReference = new BigDecimal("102.9");
        BigDecimal indiceRencherissement = new BigDecimal("106.2");
        BigDecimal rapportRencherissement = indiceRencherissement.divide(indiceReference, 15, BigDecimal.ROUND_HALF_UP);

        Bareme bareme =  unBaremeATauxMarginal()
                .typeArrondiSurChaqueTranche(TypeArrondi.CINQ_CENTIEMES_LES_PLUS_PROCHES)
                .typeArrondiGlobal(TypeArrondi.CINQ_CENTIEMES_LES_PLUS_PROCHES)
                .jusqua(17493).taux("0 %")
                .puisJusqua(21076).taux("8 %")
                .puisJusqua(23184).taux("9 %")
                .puis().taux("10 %")
                .construire()
                .homothetie(rapportRencherissement,TypeArrondi.UNITE_LA_PLUS_PROCHE);

        assertThat(bareme.calcul(BigDecimal.valueOf(23928))).isEqualTo(new BigDecimal("491.70"));

    }
}
