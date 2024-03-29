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

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.impotch.util.BigDecimalUtil.parse;
import static org.impotch.util.TypeArrondi.CINQ_CENTIEMES_LES_PLUS_PROCHES;
import static org.impotch.util.TypeArrondi.UNITE_LA_PLUS_PROCHE;
import static org.impotch.bareme.ConstructeurBareme.unBareme;

public class BaremeConstantParTrancheTest {


    @Test
    public void baremeSimple() {
        BaremeParTranche bareme = unBareme()
                .jusqua(1000).valeur(1)
                .de(1000).a(2000).valeur(2)
                .plusDe(2000).valeur(3).construire();

        assertThat(bareme.calcul(ZERO))         .isEqualTo("1");
        assertThat(bareme.calcul(  1000)).isEqualTo("1");
        assertThat(bareme.calcul(  1001)).isEqualTo("2");
        assertThat(bareme.calcul(  1500)).isEqualTo("2");
        assertThat(bareme.calcul(  2000)).isEqualTo("2");
        assertThat(bareme.calcul(  2001)).isEqualTo("3");
        assertThat(bareme.calcul(10_000)).isEqualTo("3");
    }

    @Test
    public void baremeFermeAGauche() {
        BaremeParTranche bareme = unBareme().fermeAGauche()
                .jusqua(1000).valeur(1)
                .de(1000).a(2000).valeur(2)
                .plusDe(2000).valeur(3).construire();

        assertThat(bareme.calcul(ZERO)).isEqualTo("1");
        assertThat(bareme.calcul(1000)).isEqualTo("2");
        assertThat(bareme.calcul(1001)).isEqualTo("2");
        assertThat(bareme.calcul(1500)).isEqualTo("2");
        assertThat(bareme.calcul(2000)).isEqualTo("3");
        assertThat(bareme.calcul(2001)).isEqualTo("3");
        assertThat(bareme.calcul(10000)).isEqualTo("3");
    }
    @Test
    public void baremeConstantAvecPlusieursTranches() {
        int valeur = 2; // On ne tient pas compte de cette valeur
        BaremeParTranche bareme = unBareme()
                .jusqua(1000).valeur(valeur)
                .de(1000).a(2000).valeur(valeur)
                .plusDe(2000).valeur(valeur).construire();

        assertThat(bareme.getTranches()).hasSize(1);
    }

    @Test
    public void homothetieTranchesDe110PourCent() {
        BaremeParTranche homothetique = unBareme()
                .jusqua(1000).valeur(1)
                .de(1000).a(2000).valeur(2)
                .plusDe(2000).valeur(3).construire()
                .homothetie(parse("110 %"), UNITE_LA_PLUS_PROCHE);

        assertThat(homothetique.calcul(1000)).isEqualTo("1");
        assertThat(homothetique.calcul(1001)).isEqualTo("1");
        assertThat(homothetique.calcul(1100)).isEqualTo("1");
        assertThat(homothetique.calcul(1101)).isEqualTo("2");
    }

    @Test
    public void homothetieValeursDe120PourCent() {
        BaremeParTranche homothetique = unBareme()
                .jusqua(1000).valeur(1)
                .de(1000).a(2000).valeur(2)
                .plusDe(2000).valeur(3).construire()
                .homothetieValeur(parse("120 %"), CINQ_CENTIEMES_LES_PLUS_PROCHES);

        assertThat(homothetique.calcul(1000)).isEqualTo("1.20");
        assertThat(homothetique.calcul(1001)).isEqualTo("2.40");
        assertThat(homothetique.calcul(1500)).isEqualTo("2.40");
    }

    @Test
    public void seuil() {
        BaremeParTranche bareme = unBareme()
                .jusqua(1000).valeur(1)
                .de(1000).a(2000).valeur(2)
                .plusDe(2000).valeur(3)
                .seuil(parse("1.5")).construire();

        assertThat(bareme.calcul(ZERO)).isEqualByComparingTo("0");
        assertThat(bareme.calcul(1000)).isEqualByComparingTo("0");
        assertThat(bareme.calcul(1001)).isEqualTo("2");
        assertThat(bareme.calcul(1500)).isEqualTo("2");
        assertThat(bareme.calcul(2000)).isEqualTo("2");
        assertThat(bareme.calcul(2001)).isEqualTo("3");
        assertThat(bareme.calcul(10_000)).isEqualTo("3");
    }
}
