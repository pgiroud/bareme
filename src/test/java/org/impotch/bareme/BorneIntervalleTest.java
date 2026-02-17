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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class BorneIntervalleTest {

    @Test
    public void ouvertAGaucheBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).exclus().a(2).inclus()
                .intervalle();
        assertThat(inter.ouvertAGauche()).isTrue();
    }

    @Test
    public void nonOuvertAGaucheBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).inclus().a(2).inclus()
                .intervalle();
        assertThat(inter.ouvertAGauche()).isFalse();
    }

    @Test
    public void ouvertAGaucheInfini() {
        Intervalle inter = new Intervalle.Cons()
                .deMoinsInfini().a(1).inclus()
                .intervalle();
        assertThat(inter.ouvertAGauche()).isTrue();
    }

    @Test
    public void fermeAGaucheBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).inclus().a(2).inclus()
                .intervalle();
        assertThat(inter.fermeAGauche()).isTrue();
    }

    @Test
    public void nonFermeAGaucheBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).exclus().a(2).inclus()
                .intervalle();
        assertThat(inter.fermeAGauche()).isFalse();
    }

    @Test
    public void fermeAGaucheInfini() {
        Intervalle inter = new Intervalle.Cons()
                .deMoinsInfini().a(1).inclus()
                .intervalle();
        assertThat(inter.fermeAGauche()).isFalse();
    }

    @Test
    public void ouvertADroiteBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).exclus().a(2).exclus()
                .intervalle();
        assertThat(inter.ouvertADroite()).isTrue();
    }

    @Test
    public void nonOuvertADroiteBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).inclus().a(2).inclus()
                .intervalle();
        assertThat(inter.ouvertADroite()).isFalse();
    }

    @Test
    public void ouvertADroiteInfini() {
        Intervalle inter = new Intervalle.Cons()
                .de(0).inclus().aPlusInfini()
                .intervalle();
        assertThat(inter.ouvertADroite()).isTrue();
    }

    @Test
    public void fermeADroiteBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).inclus().a(2).inclus()
                .intervalle();
        assertThat(inter.fermeADroite()).isTrue();
    }

    @Test
    public void nonFermeADroiteBorne() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).inclus().a(2).exclus()
                .intervalle();
        assertThat(inter.fermeADroite()).isFalse();
    }

    @Test
    public void fermeADroiteInfini() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).inclus().aPlusInfini()
                .intervalle();
        assertThat(inter.fermeADroite()).isFalse();
    }

    @Test
    public void milieuNonBorneAGauche() {
        Intervalle inter = new Intervalle.Cons()
                .deMoinsInfini().a(0)
                .intervalle();
        assertThat(inter.getMilieu()).isEmpty();
    }

    @Test
    public void milieuNonBorneADroite() {
        Intervalle inter = new Intervalle.Cons()
                .de(0).aPlusInfini()
                .intervalle();
        assertThat(inter.getMilieu()).isEmpty();
    }

    @Test
    public void milieuBornesPositives() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).a(3)
                .intervalle();
        assertThat(inter.getMilieu())
                .isPresent()
                .hasValue(BigDecimal.TWO);
    }

    @Test
    public void milieuBornesNegatives() {
        Intervalle inter = new Intervalle.Cons()
                .de(-9).a(-1)
                .intervalle();
        assertThat(inter.getMilieu())
                .isPresent()
                .hasValue(BigDecimal.valueOf(-5));
    }

    @Test
    public void milieuBornesDePartEtDautreDeZero() {
        Intervalle inter = new Intervalle.Cons()
                .de(-20).a(10)
                .intervalle();
        assertThat(inter.getMilieu())
                .isPresent()
                .hasValue(BigDecimal.valueOf(-5));
    }

}
