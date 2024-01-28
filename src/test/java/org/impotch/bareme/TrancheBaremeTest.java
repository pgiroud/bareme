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

import java.math.BigDecimal;

import org.impotch.util.TypeArrondi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static org.impotch.bareme.ConstructeurTranche.uneTranche;

public class TrancheBaremeTest {

    @Test
    public void homothetie() {
        TrancheBareme tranche = uneTranche().de(0).a(100).valeur(BigDecimal.ONE).construire();
        TrancheBareme homothetique = tranche.homothetie(BigDecimal.valueOf(2), TypeArrondi.UNITE_LA_PLUS_PROCHE);
        assertThat(homothetique.getIntervalle().getFin().get()).isEqualTo("200");
        // TODO à reprendre
        assertThat(homothetique.getValeurs().getValeur()).isEqualTo("1");

        homothetique = tranche.homothetie(new BigDecimal("1.23456"), TypeArrondi.CENTIEME_LE_PLUS_PROCHE);
        assertThat(homothetique.getIntervalle().getFin().get()).isEqualTo("123.46");
        // TODO à reprendre
        assertThat(homothetique.getValeurs().getValeur()).isEqualTo("1");
    }

    @Test
    public void homothetieAvecRapportNull() {
        TrancheBareme tranche = uneTranche().de(0).a(100).valeur(BigDecimal.ONE).construire();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> tranche.homothetie(null, TypeArrondi.UNITE_LA_PLUS_PROCHE)
        );
    }

    @Test
    public void homothetieAvecRapportZero() {
        TrancheBareme tranche = uneTranche().de(0).a(100).valeur(BigDecimal.ONE).construire();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> tranche.homothetie(BigDecimal.ZERO, TypeArrondi.UNITE_LA_PLUS_PROCHE)
        );
    }

    @Test
    public void homothetieAvecRapportNegatif() {
        TrancheBareme tranche = uneTranche().de(0).a(100).valeur(BigDecimal.ONE).construire();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> tranche.homothetie(BigDecimal.ONE.negate(), TypeArrondi.UNITE_LA_PLUS_PROCHE)
        );
    }


}
