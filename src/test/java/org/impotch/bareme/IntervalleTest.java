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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntervalleTest {

    @Test
    public void appartient() {
        Intervalle inter = new Intervalle.Cons()
                .de(1000).inclus().a(2000).inclus()
                .intervalle();
        assertThat(inter.encadre(1001)).isTrue();
    }

    @Test
    public void appartientBorneSuperieureIncluse() {
        Intervalle inter = new Intervalle.Cons()
                .de(1000).inclus().a(2000).inclus()
                .intervalle();
        assertThat(inter.encadre(2000)).isTrue();
    }

    @Test
    public void appartientBorneSuperieureExcluse() {
        Intervalle inter = new Intervalle.Cons()
                .de(1000).inclus().a(2000).exclus()
                .intervalle();
        assertThat(inter.encadre(2000)).isFalse();
    }

    @Test
    public void appartientBorneInferieureIncluse() {
        Intervalle inter = new Intervalle.Cons()
                .de(1000).inclus().a(2000).inclus()
                .intervalle();
        assertThat(inter.encadre(1000)).isTrue();
    }

    @Test
    public void appartientBorneInferieureExcluse() {
        Intervalle inter = new Intervalle.Cons()
                .de(1000).exclus().a(2000).inclus()
                .intervalle();
        assertThat(inter.encadre(1000)).isFalse();
    }

    @Test
    public void appartientTouteLaDroiteReelle() {
        Intervalle inter = new Intervalle.Cons()
                .deMoinsInfini().aPlusInfini()
                .intervalle();
        assertThat(inter.encadre(1000)).isTrue();
    }



}
