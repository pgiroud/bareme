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

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.TypeArrondi;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaremeConstantParTrancheTest {

    private ConstructeurBareme constructeur;

    @Before
    public void setUp() throws Exception {
        constructeur = new ConstructeurBareme()
                .premiereTranche(1000,1)
                .tranche(1000,2000,2)
                .derniereTranche(2000,3);
    }

    @Test
    public void baremeSimple() {
        BaremeParTranche bareme = constructeur.construireBaremeParTranche();
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("1");
        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualTo("1");
        assertThat(bareme.calcul(BigDecimal.valueOf(1001))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(1500))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(2001))).isEqualTo("3");
        assertThat(bareme.calcul(BigDecimal.valueOf(10000))).isEqualTo("3");
    }

    @Test
    public void baremeFermeAGauche() {
        ConstructeurBareme cons = new ConstructeurBareme().fermeAGauche()
                .premiereTranche(1000,1)
                .tranche(1000,2000,2)
                .derniereTranche(2000,3);
        BaremeParTranche bareme = cons.construireBaremeParTranche();
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("1");
        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(1001))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(1500))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualTo("3");
        assertThat(bareme.calcul(BigDecimal.valueOf(2001))).isEqualTo("3");
        assertThat(bareme.calcul(BigDecimal.valueOf(10000))).isEqualTo("3");
    }

    @Test
    public void homothetieTranchesDe110PourCent() {
        BaremeParTranche homothetique = constructeur.construireBaremeParTranche()
                .homothetie(BigDecimalUtil.parseTaux("110 %"), TypeArrondi.FRANC);
        assertThat(homothetique.calcul(BigDecimal.valueOf(1000))).isEqualTo("1");
        assertThat(homothetique.calcul(BigDecimal.valueOf(1001))).isEqualTo("1");
        assertThat(homothetique.calcul(BigDecimal.valueOf(1100))).isEqualTo("1");
        assertThat(homothetique.calcul(BigDecimal.valueOf(1101))).isEqualTo("2");
    }

    @Test
    public void homothetieValeursDe120PourCent() {
        BaremeParTranche homothetique = constructeur.construireBaremeParTranche()
                .homothetieValeur(BigDecimalUtil.parseTaux("120 %"), TypeArrondi.CINQ_CTS);
        assertThat(homothetique.calcul(BigDecimal.valueOf(1000))).isEqualTo("1.20");
        assertThat(homothetique.calcul(BigDecimal.valueOf(1001))).isEqualTo("2.40");
        assertThat(homothetique.calcul(BigDecimal.valueOf(1500))).isEqualTo("2.40");
    }

    @Test
    public void seuil() {
        constructeur.seuil(new BigDecimal("1.5"));
        BaremeParTranche bareme = constructeur.construireBaremeParTranche();

        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualByComparingTo("0");
        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualByComparingTo("0");
        assertThat(bareme.calcul(BigDecimal.valueOf(1001))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(1500))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(2001))).isEqualTo("3");
        assertThat(bareme.calcul(BigDecimal.valueOf(10000))).isEqualTo("3");
    }
}
