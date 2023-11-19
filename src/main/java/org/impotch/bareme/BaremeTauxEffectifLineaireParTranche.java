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
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class BaremeTauxEffectifLineaireParTranche extends BaremeTauxEffectifParTranche {

    public BaremeTauxEffectifLineaireParTranche() {
        super();
    }

    public BaremeTauxEffectifLineaireParTranche(List<TrancheBareme> tranches) {
        super();
        setTranches(tranches);
    }


    /* (non-Javadoc)
     * @see ch.ge.afc.calcul.impot.bareme.BaremeTauxEffectif#getTaux(java.math.BigDecimal)
     */
    @Override
    public BigDecimal getTaux(BigDecimal assiette) {
        return getTranches().stream().map(t -> t.calcul(assiette)).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    protected BaremeParTranche newBaremeParTranche() {
        return new BaremeTauxEffectifLineaireParTranche();
    }


}
