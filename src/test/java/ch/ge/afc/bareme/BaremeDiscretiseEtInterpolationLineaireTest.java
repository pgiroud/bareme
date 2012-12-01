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
package ch.ge.afc.bareme;


import java.math.BigDecimal;

import org.junit.Test;

import org.impotch.util.TypeArrondi;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class BaremeDiscretiseEtInterpolationLineaireTest {

	private final static BigDecimal MILLE = BigDecimal.valueOf(1000);
	private final static BigDecimal CENT = BigDecimal.valueOf(100);
	
	
	@Test
	public void uneSeuleTranche() {
		BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
		bareme.setTypeArrondi(TypeArrondi.FRANC);
		bareme.ajouterPointDiscretisation(BigDecimal.ZERO,BigDecimal.ZERO);
		bareme.ajouterPointDiscretisation(MILLE, MILLE);
		bareme.setDefiniAvantBorneInf(true);
		bareme.setDefiniApresBorneSup(true);
		
		// Test avec montant inclus strictement sur la tranche
        //  Une seule tranche avec pente 1 et montant dans tranche
        assertThat(bareme.calcul(CENT)).isEqualTo(CENT);
		// Test sur borne inférieure
        //  Une seule tranche borne inférieure
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("0");
		// Test sur montant < borne inférieure
        assertThat(bareme.calcul(BigDecimal.valueOf(-1000))).isEqualTo("0");
		// Test sur borne supérieure
        assertThat(bareme.calcul(MILLE)).isEqualTo(MILLE);
		// Test sur montant > borne supérieure
		assertThat(bareme.calcul(BigDecimal.valueOf(1000000))).isEqualTo(MILLE);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void apresDernierTrancheNonAcceptable() {
		BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
		bareme.setTypeArrondi(TypeArrondi.FRANC);
		bareme.ajouterPointDiscretisation(BigDecimal.ZERO,BigDecimal.ZERO);
		bareme.ajouterPointDiscretisation(MILLE, MILLE);
		// Test sur borne supérieure
        assertThat(bareme.calcul(MILLE)).isEqualTo(MILLE);
        // Test sur montant > borne supérieure
		bareme.calcul(new BigDecimal(1001));
        failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void avantPremiereTrancheNonAcceptable() {
		BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
		bareme.setTypeArrondi(TypeArrondi.FRANC);
		bareme.ajouterPointDiscretisation(BigDecimal.ZERO,BigDecimal.ZERO);
		bareme.ajouterPointDiscretisation(MILLE, MILLE);
		// Test sur borne inférieure
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("0");
        // Test sur montant < borne inférieure
		bareme.calcul(new BigDecimal(-1));
        failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
    }
	
}
