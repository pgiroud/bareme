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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class BaremeDiscretiseEtInterpolationLineaireTest {

    private final static BigDecimal MILLE = BigDecimal.valueOf(1000);
    private final static BigDecimal CENT = BigDecimal.valueOf(100);
    private final static Point ORIGINE = new Point(BigDecimal.ZERO, BigDecimal.ZERO);


    @Test
    public void uneSeuleTranche() {
        BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
        bareme.setTypeArrondi(TypeArrondi.FRANC);
        bareme.ajouterPointDiscretisation(ORIGINE);
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

    @Test
    public void apresDernierTrancheNonAcceptable() {
        BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
        bareme.setTypeArrondi(TypeArrondi.FRANC);
        bareme.ajouterPointDiscretisation(BigDecimal.ZERO, BigDecimal.ZERO);
        bareme.ajouterPointDiscretisation(MILLE, MILLE);
        // Test sur borne supérieure
        assertThat(bareme.calcul(MILLE)).isEqualTo(MILLE);
        // Test sur montant > borne supérieure
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bareme.calcul(new BigDecimal(1001))
        );
    }

    @Test
    public void avantPremiereTrancheNonAcceptable() {
        BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
        bareme.setTypeArrondi(TypeArrondi.FRANC);
        bareme.ajouterPointDiscretisation(BigDecimal.ZERO, BigDecimal.ZERO);
        bareme.ajouterPointDiscretisation(MILLE, MILLE);
        // Test sur borne inférieure
        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualTo("0");
        // Test sur montant < borne inférieure
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bareme.calcul(new BigDecimal(-1))
        );
    }

    @Test
    public void pointDiscretiseNonOrdonne() {
        BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
        bareme.setTypeArrondi(TypeArrondi.FRANC);
        bareme.ajouterPointDiscretisation(MILLE, CENT);
        bareme.ajouterPointDiscretisation(ORIGINE);
        assertThat(bareme.calcul(CENT)).isEqualTo(BigDecimal.valueOf(10));
    }
}
