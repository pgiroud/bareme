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

/**
 * Barème défini par tranche et dont le taux est fourni dans une tranche.
 *
 * @author Patrick Giroud
 *
 */
public abstract class BaremeTauxEffectifParTranche extends BaremeParTranche implements
        BaremeTauxEffectif {

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

    /**
     * Le calcul consiste simplement à multiplier le taux effectif par l'assiette
     * et à effectuer l'arrondi.
     * @see org.impotch.bareme.BaremeParTranche#calculSansSeuil(java.math.BigDecimal)
     */
    @Override
    public BigDecimal calculSansSeuil(BigDecimal assiette) {
        BigDecimal taux = getTaux(assiette);
        BigDecimal montantAvantArrondi = assiette.multiply(taux);
        return this.getTypeArrondiSurChaqueTranche().arrondir(montantAvantArrondi);
    }


    private TrancheBareme derniereTranche() {
        List<TrancheBareme> tranches = this.getTranches();
        return tranches.getLast();
    }

    @Override
    public BigDecimal getTauxMaximum() {
        TrancheBareme der = derniereTranche();
        return der.getValeurs().getValeur();
    }
}
