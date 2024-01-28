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

import static java.math.RoundingMode.HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.*;
import static org.impotch.util.TypeArrondi.UNITE_LA_PLUS_PROCHE;
import static org.impotch.util.TypeArrondi.CINQ_CENTIEMES_LES_PLUS_PROCHES;
import static org.impotch.bareme.ConstructeurBareme.unBaremeATauxMarginal;

public class BaremeTauxMarginalConstantParTrancheTest {

    private final static BigDecimal UN = BigDecimal.ONE;
    private final static BigDecimal CINQ = BigDecimal.valueOf(5);
    private final static BigDecimal DIX = BigDecimal.TEN;
    private final static BigDecimal CINQUANTE = BigDecimal.valueOf(50);
    private final static BigDecimal CENT_CINQUANTE = BigDecimal.valueOf(150);
    private final static BigDecimal TROIS_CENTS = BigDecimal.valueOf(300);

    @Test
    public void uneSeuleTranche() {
        Bareme bareme =  unBaremeATauxMarginal()
                .plusDe(0).taux("10 %").construire();

        assertThat(bareme.calcul(ZERO)).isEqualByComparingTo(ZERO);
        assertThat(bareme.calcul(DIX)).isEqualByComparingTo(UN);
        assertThat(bareme.calcul(BigDecimal.valueOf(100))).isEqualByComparingTo(DIX);
    }


    @Test
    public void deuxTranche() {
        Bareme bareme =  unBaremeATauxMarginal()
                .de(0).a(1000).taux("5 %")
                .plusDe(1000).taux("10 %").construire();

        assertThat(bareme.calcul(ZERO)).isEqualByComparingTo(ZERO);
        assertThat(bareme.calcul(100)).isEqualByComparingTo(CINQ);
        assertThat(bareme.calcul(1000)).isEqualByComparingTo(CINQUANTE);
        assertThat(bareme.calcul(2000)).isEqualByComparingTo(CENT_CINQUANTE);
    }

    @Test
    public void troisTranche() {
        Bareme bareme =  unBaremeATauxMarginal()
                .de(0).a(1000).taux("5 %")
                .de(1000).a(2000).taux("10 %")
                .plusDe(2000).taux("15 %").construire();


        assertThat(bareme.calcul(ZERO)).isEqualByComparingTo(ZERO);
        assertThat(bareme.calcul(100)).isEqualByComparingTo(CINQ);
        assertThat(bareme.calcul(1000)).isEqualByComparingTo(CINQUANTE);
        assertThat(bareme.calcul(2000)).isEqualByComparingTo(CENT_CINQUANTE);
        assertThat(bareme.calcul(3000)).isEqualByComparingTo(TROIS_CENTS);
    }

    @Test
    public void baremeConstruitAvecDesPuis() {
        Bareme bareme =  unBaremeATauxMarginal()
                .jusqua(200).taux("0 %")
                .puisJusqua(1000).taux("5 %")
                .puisJusqua(2000).taux("10 %")
                .puis().taux("15 %").construire();


        assertThat(bareme.calcul(BigDecimal.ZERO)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bareme.calcul(1000)).isEqualByComparingTo(BigDecimal.valueOf(40));
        assertThat(bareme.calcul(3000)).isEqualByComparingTo(BigDecimal.valueOf(290));

    }

    @Test
    public void homothetieBaremeRevenuGE2023() {
        BigDecimal indiceReference = new BigDecimal("102.9");
        BigDecimal indiceRencherissement = new BigDecimal("106.2");
        BigDecimal rapportRencherissement = indiceRencherissement.divide(indiceReference, 15, HALF_UP);

        Bareme bareme =  unBaremeATauxMarginal()
                .typeArrondiSurChaqueTranche(CINQ_CENTIEMES_LES_PLUS_PROCHES)
                .typeArrondiGlobal(CINQ_CENTIEMES_LES_PLUS_PROCHES)
                .jusqua(17493).taux("0 %")
                .puisJusqua(21076).taux("8 %")
                .puisJusqua(23184).taux("9 %")
                .puis().taux("10 %")
                .construire()
                .homothetie(rapportRencherissement,UNITE_LA_PLUS_PROCHE);

        assertThat(bareme.calcul(23928)).isEqualTo("491.70");
    }


    @Test
    public void tauxMaximalAvecUneTranche() {
        BaremeTauxMarginal bareme =  (BaremeTauxMarginal)unBaremeATauxMarginal()
                .plusDe(0).taux("10 %").construire();
        assertThat(bareme.getTauxMaximum()).isEqualByComparingTo("0.10");
    }

    @Test
    public void tauxMaximalAvecQuatreTranches() {
        BaremeTauxMarginal bareme =  (BaremeTauxMarginal)unBaremeATauxMarginal()
                .jusqua(200).taux("0 %")
                .puisJusqua(1000).taux("5 %")
                .puisJusqua(2000).taux("10 %")
                .puis().taux("15 %").construire();
        assertThat(bareme.getTauxMaximum()).isEqualByComparingTo("0.15");
    }

    @Test
    public void tauxMaximalAvecDerniereTrancheBorneeADroite() {
        BaremeTauxMarginal bareme =  (BaremeTauxMarginal)unBaremeATauxMarginal()
                .de(0).a(1000).taux("5 %")
                .de(1000).a(2000).taux("10 %")
                .construire();

        assertThat(bareme.getTauxMaximum()).isEqualByComparingTo("0.075");

    }
}
