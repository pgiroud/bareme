/**
 * This file is part of CalculImpotCH.
 *
 * CalculImpotCH is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * CalculImpotCH is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CalculImpotCH.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ge.afc.bareme;


import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
		assertEquals("Abscisse 0",new BigDecimal("1"),bareme.calcul(BigDecimal.ZERO));
		assertEquals("Abscisse 1000",new BigDecimal("1"),bareme.calcul(new BigDecimal("1000")));
		assertEquals("Abscisse 1001",new BigDecimal("2"),bareme.calcul(new BigDecimal("1001")));
		assertEquals("Abscisse 1500",new BigDecimal("2"),bareme.calcul(new BigDecimal("1500")));
		assertEquals("Abscisse 2000",new BigDecimal("2"),bareme.calcul(new BigDecimal("2000")));
		assertEquals("Abscisse 2001",new BigDecimal("3"),bareme.calcul(new BigDecimal("2001")));
		assertEquals("Abscisse 10000",new BigDecimal("3"),bareme.calcul(new BigDecimal("10000")));
	}

    @Test
    public void baremeFermeAGauche() {
        BaremeConstantParTranche bareme = new BaremeConstantParTranche();
        bareme.setMontantMaxNonInclus();
        bareme.ajouterTranche(1000, 1);
        bareme.ajouterTranche(2000, 2);
        bareme.ajouterDerniereTranche(3);
        assertEquals("Abscisse 0",new BigDecimal("1"),bareme.calcul(BigDecimal.ZERO));
        assertEquals("Abscisse 1000",new BigDecimal("2"),bareme.calcul(new BigDecimal("1000")));
        assertEquals("Abscisse 1001",new BigDecimal("2"),bareme.calcul(new BigDecimal("1001")));
        assertEquals("Abscisse 1500",new BigDecimal("2"),bareme.calcul(new BigDecimal("1500")));
        assertEquals("Abscisse 2000",new BigDecimal("3"),bareme.calcul(new BigDecimal("2000")));
        assertEquals("Abscisse 2001",new BigDecimal("3"),bareme.calcul(new BigDecimal("2001")));
        assertEquals("Abscisse 10000",new BigDecimal("3"),bareme.calcul(new BigDecimal("10000")));
    }
}
