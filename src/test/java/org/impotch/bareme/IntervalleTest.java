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
import java.util.Optional;

import org.impotch.util.TypeArrondi;
import org.junit.jupiter.api.Test;

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


    @Test
    public void longueurAvantAvecValeurAvantLIntervalle() {
        Intervalle inter = new Intervalle.Cons()
                .de(20).a(40)
                .intervalle();
        Optional<BigDecimal> longueur = inter.longueurAvant(DecimalEtendu.de(BigDecimal.valueOf(10)));
        assertThat(longueur).isEqualTo(Optional.of(BigDecimal.ZERO));
    }

    @Test
    public void longueurAvantAvecValeurDansLIntervalle() {
        Intervalle inter = new Intervalle.Cons()
                .de(20).a(40)
                .intervalle();
        Optional<BigDecimal> longueur = inter.longueurAvant(DecimalEtendu.de(BigDecimal.valueOf(35)));
        assertThat(longueur).isEqualTo(Optional.of(BigDecimal.valueOf(15)));
    }

    @Test
    public void longueurAvantAvecValeurApresLIntervalle() {
        Intervalle inter = new Intervalle.Cons()
                .de(20).a(40)
                .intervalle();
        Optional<BigDecimal> longueur = inter.longueurAvant(DecimalEtendu.de(BigDecimal.valueOf(120)));
        assertThat(longueur).isEqualTo(Optional.of(BigDecimal.valueOf(20)));
    }

    @Test
    public void homothetie_centree_origine_ouvert_ouvert() {
        Intervalle inter = new Intervalle.Cons()
                .de(-1).exclus().a(1).exclus()
                .intervalle();
        Intervalle resultatAttendu = new Intervalle.Cons()
                .de(-2).exclus().a(2).exclus()
                .intervalle();
        assertThat(inter.homothetie(BigDecimal.TWO, TypeArrondi.UNITE_LA_PLUS_PROCHE)).isEqualTo(resultatAttendu);
    }

    @Test
    public void homothetie_a_droite_ouvert_ouvert() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).exclus().a(2).exclus()
                .intervalle();
        Intervalle resultatAttendu = new Intervalle.Cons()
                .de(2).exclus().a(4).exclus()
                .intervalle();
        assertThat(inter.homothetie(BigDecimal.TWO, TypeArrondi.UNITE_LA_PLUS_PROCHE)).isEqualTo(resultatAttendu);
    }

    @Test
    public void homothetie_a_droite_ouvert_ferme() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).exclus().a(2)
                .intervalle();
        Intervalle resultatAttendu = new Intervalle.Cons()
                .de(2).exclus().a(4)
                .intervalle();
        assertThat(inter.homothetie(BigDecimal.TWO, TypeArrondi.UNITE_LA_PLUS_PROCHE)).isEqualTo(resultatAttendu);
    }

    @Test
    public void homothetie_a_droite_ferme_ouvert() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).a(2).exclus()
                .intervalle();
        Intervalle resultatAttendu = new Intervalle.Cons()
                .de(2).a(4).exclus()
                .intervalle();
        assertThat(inter.homothetie(BigDecimal.TWO, TypeArrondi.UNITE_LA_PLUS_PROCHE)).isEqualTo(resultatAttendu);
    }

    @Test
    public void homothetie_a_droite_ferme_ferme() {
        Intervalle inter = new Intervalle.Cons()
                .de(1).a(2)
                .intervalle();
        Intervalle resultatAttendu = new Intervalle.Cons()
                .de(2).a(4)
                .intervalle();
        assertThat(inter.homothetie(BigDecimal.TWO, TypeArrondi.UNITE_LA_PLUS_PROCHE)).isEqualTo(resultatAttendu);
    }


    @Test
    public void inclusDansAvecBornesInferieuresEgalesEtComprises() {
        Intervalle inclus = new Intervalle.Cons()
                .de(1).inclus().a(2).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(1).inclus().a(3).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }

    @Test
    public void nonInclusDansAvecBornesInferieuresEgalesEtUneCompriseMaisPasLAutre() {
        Intervalle inclus = new Intervalle.Cons()
                .de(1).inclus().a(2).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(1).exclus().a(3).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isFalse();
    }

    @Test
    public void moinsInfiniDeuxBorneExclueNonInclusDansMoinsInfiniUnBorneExclue() {
        Intervalle inclus = new Intervalle.Cons()
                .deMoinsInfini().a(2).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .deMoinsInfini().a(1).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isFalse();
    }

    @Test
    public void moinsInfiniDeuxBorneIncluseNonInclusDansMoinsInfiniUnBorneExclue() {
        Intervalle inclus = new Intervalle.Cons()
                .deMoinsInfini().a(2).inclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .deMoinsInfini().a(1).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isFalse();
    }


    @Test
    public void moinsInfiniUnBorneExclueInclusDansMoinsInfiniDeuxBorneExclue() {
        Intervalle inclus = new Intervalle.Cons()
                .deMoinsInfini().a(1).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .deMoinsInfini().a(2).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }

    @Test
    public void moinsInfiniUnBorneExclueInclusDansMoinsInfiniDeuxBorneIncluse() {
        Intervalle inclus = new Intervalle.Cons()
                .deMoinsInfini().a(1).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .deMoinsInfini().a(2).inclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }

    @Test
    public void memeIntervalleMoinsInfiniDeuxBorneExclue() {
        Intervalle inclus = new Intervalle.Cons()
                .deMoinsInfini().a(2).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .deMoinsInfini().a(2).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }

    @Test
    public void inclusDansAvecBornesSuperieuresEgalesEtNonComprises() {
        Intervalle inclus = new Intervalle.Cons()
                .de(2).inclus().a(3).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(1).inclus().a(3).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }

    @Test
    public void unInclusDeuxExclusDansZeroInclusDeuxExclus() {
        Intervalle inclus = new Intervalle.Cons()
                .de(1).inclus().a(2).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(0).inclus().a(2).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }

    @Test
    public void unInclusDeuxExclusPasInclusDansUnExclusDeuxExclus() {
        Intervalle inclus = new Intervalle.Cons()
                .de(1).inclus().a(2).exclus()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(1).exclus().a(2).exclus()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isFalse();
    }

    @Test
    public void zeroInclusPlusInfiniPasInclusDansZeroExclusPlusInfini() {
        Intervalle inclus = new Intervalle.Cons()
                .de(0).inclus().aPlusInfini()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(0).exclus().aPlusInfini()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isFalse();
    }

    @Test
    public void zeroInclusPlusInfiniInclusDansZeroInclusPlusInfini() {
        Intervalle inclus = new Intervalle.Cons()
                .de(0).inclus().aPlusInfini()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(0).inclus().aPlusInfini()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }


    @Test
    public void unInclusPlusInfiniInclusDansZeroInclusPlusInfini() {
        Intervalle inclus = new Intervalle.Cons()
                .de(1).inclus().aPlusInfini()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(0).inclus().aPlusInfini()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }

    @Test
    public void unInclusPlusInfiniInclusDansZeroExclusPlusInfini() {
        Intervalle inclus = new Intervalle.Cons()
                .de(1).inclus().aPlusInfini()
                .intervalle();
        Intervalle englobant = new Intervalle.Cons()
                .de(0).exclus().aPlusInfini()
                .intervalle();
        assertThat(inclus.estInclusDans(englobant)).isTrue();
    }
}
