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

import java.math.BigDecimal;

class ConstructeurValeursAuPremierOrdre {

    private BigDecimal valeurOrdre_0_Courante = BigDecimal.ZERO;
    private BigDecimal valeurOrdre_1_Courante = BigDecimal.ZERO;

    public ConstructeurValeursAuPremierOrdre valeur(BigDecimal valeur) {
        valeurOrdre_0_Courante = valeur;
        return this;
    }

    public ConstructeurValeursAuPremierOrdre increment(BigDecimal increment) {
        valeurOrdre_1_Courante = increment;
        return this;
    }

    public ValeursPremierOrdre construire() {
        ValeursPremierOrdre valeurs = new ValeursPremierOrdre(valeurOrdre_0_Courante,valeurOrdre_1_Courante);
        valeurOrdre_0_Courante = BigDecimal.ZERO;
        valeurOrdre_1_Courante = BigDecimal.ZERO;
        return valeurs;
    }
}
