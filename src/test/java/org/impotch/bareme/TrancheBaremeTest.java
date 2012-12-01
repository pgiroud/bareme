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
import static org.fest.assertions.api.Assertions.assertThat;
public class TrancheBaremeTest {

	@Test
	public void homothetie() {
		TrancheBareme tranche = new TrancheBareme(BigDecimal.valueOf(100),BigDecimal.ONE);
		TrancheBareme homothetique = tranche.homothetie(BigDecimal.valueOf(2), TypeArrondi.FRANC);
        assertThat(homothetique.getMontantMaxTranche()).isEqualTo("200");
        assertThat(homothetique.getTauxOuMontant()).isEqualTo("1");

		homothetique = tranche.homothetie(new BigDecimal("1.23456"), TypeArrondi.CT);
        assertThat(homothetique.getMontantMaxTranche()).isEqualTo("123.46");
        assertThat(homothetique.getTauxOuMontant()).isEqualTo("1");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void homothetieAvecRapportNull() {
		TrancheBareme tranche = new TrancheBareme(BigDecimal.valueOf(100),BigDecimal.ONE);
		tranche.homothetie(null, TypeArrondi.FRANC);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void homothetieAvecRapportZero() {
		TrancheBareme tranche = new TrancheBareme(BigDecimal.valueOf(100),BigDecimal.ONE);
		tranche.homothetie(BigDecimal.ZERO, TypeArrondi.FRANC);
	}

	@Test(expected=IllegalArgumentException.class)
	public void homothetieAvecRapportNegatif() {
		TrancheBareme tranche = new TrancheBareme(BigDecimal.valueOf(100),BigDecimal.ONE);
		tranche.homothetie(BigDecimal.ONE.negate(), TypeArrondi.FRANC);
	}
	
	
	
}
