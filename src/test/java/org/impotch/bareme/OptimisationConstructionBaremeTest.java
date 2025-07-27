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

import static org.assertj.core.api.Assertions.assertThat;

import static org.impotch.bareme.ConstructeurBareme.unBaremeATauxEffectifSansOptimisation;
import static org.impotch.bareme.ConstructeurBareme.unBaremeATauxEffectifSansOptimisationDesQueNonNul;

public class OptimisationConstructionBaremeTest {

    @Test
    void sansOptimisationDesQueNonNul_optimisationSurLesNuls() {
         Bareme bar1 = unBaremeATauxEffectifSansOptimisationDesQueNonNul()
                 .de(0).a(100).taux("0 %")
                 .de(100).a(200).taux("0 %")
                 .de(200).a(499).taux("0 %")
                 .plusDe(499).taux("1 %").construire();

         Bareme bar2 = unBaremeATauxEffectifSansOptimisation()
                 .de(0).a(499).taux("0 %")
                 .plusDe(499).taux("1 %").construire();

         assertThat(bar1).isEqualTo(bar2);
    }


    @Test
    void sansOptimisationDesQueNonNul() {
        Bareme bar1 = unBaremeATauxEffectifSansOptimisationDesQueNonNul()
                .de(0).a(100).taux("0 %")
                .de(100).a(200).taux("1 %")
                .de(200).a(499).taux("1 %")
                .plusDe(499).taux("1 %").construire();

        Bareme bar2 = unBaremeATauxEffectifSansOptimisation()
                .de(0).a(100).taux("0 %")
                .de(100).a(200).taux("1 %")
                .de(200).a(499).taux("1 %")
                .plusDe(499).taux("1 %").construire();

        assertThat(bar1).isEqualTo(bar2);
    }
}
