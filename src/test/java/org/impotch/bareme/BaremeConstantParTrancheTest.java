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

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class BaremeConstantParTrancheTest {

	private BaremeConstantParTranche bareme;
	
	@Before
	public void setUp() throws Exception {
		bareme = new BaremeConstantParTranche();
		bareme.ajouterTranche(1000, 1);
		bareme.ajouterTranche(2000, 2);
		bareme.ajouterDerniereTranche(3);
	}

	@Test
	public void baremeSimple() {
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
        BaremeConstantParTranche bareme = new BaremeConstantParTranche();
        bareme.setMontantMaxNonInclus();
        bareme.ajouterTranche(1000, 1);
        bareme.ajouterTranche(2000, 2);
        bareme.ajouterDerniereTranche(3);
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("1");
        assertThat(bareme.calcul(BigDecimal.valueOf(1000))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(1001))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(1500))).isEqualTo("2");
        assertThat(bareme.calcul(BigDecimal.valueOf(2000))).isEqualTo("3");
        assertThat(bareme.calcul(BigDecimal.valueOf(2001))).isEqualTo("3");
        assertThat(bareme.calcul(BigDecimal.valueOf(10000))).isEqualTo("3");
    }
}
