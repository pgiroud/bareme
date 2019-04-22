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
 * <p>
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

public class TrancheBaremeTest {

    @Test
    public void homothetie() {
        Intervalle inter = new Intervalle.Cons().de(0).a(100).intervalle();
        TrancheBareme tranche = new TrancheBareme(inter, BigDecimal.ONE);
        TrancheBareme homothetique = tranche.homothetie(BigDecimal.valueOf(2), TypeArrondi.FRANC);
        assertThat(homothetique.getIntervalle().getFin()).isEqualTo("200");
        assertThat(homothetique.getTauxOuMontant()).isEqualTo("1");

        homothetique = tranche.homothetie(new BigDecimal("1.23456"), TypeArrondi.CT);
        assertThat(homothetique.getIntervalle().getFin()).isEqualTo("123.46");
        assertThat(homothetique.getTauxOuMontant()).isEqualTo("1");
    }

    @Test
    public void homothetieAvecRapportNull() {
        Intervalle inter = new Intervalle.Cons().de(0).a(100).intervalle();
        TrancheBareme tranche = new TrancheBareme(inter, BigDecimal.ONE);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> tranche.homothetie(null, TypeArrondi.FRANC)
        );
    }

    @Test
    public void homothetieAvecRapportZero() {
        Intervalle inter = new Intervalle.Cons().de(0).a(100).intervalle();
        TrancheBareme tranche = new TrancheBareme(inter, BigDecimal.ONE);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> tranche.homothetie(BigDecimal.ZERO, TypeArrondi.FRANC)
        );
    }

    @Test
    public void homothetieAvecRapportNegatif() {
        Intervalle inter = new Intervalle.Cons().de(0).a(100).intervalle();
        TrancheBareme tranche = new TrancheBareme(inter, BigDecimal.ONE);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> tranche.homothetie(BigDecimal.ONE.negate(), TypeArrondi.FRANC)
        );
    }


}
