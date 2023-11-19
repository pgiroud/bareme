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

/**
 * Un barème est une fonction mathématique qui en fonction d'un montant retourne un montant.
 *
 * @author Patrick Giroud
 *
 */
public interface Bareme {

    /**
     * Calcule le montant par application du barème.
     * @param pAssiette L'assiette fiscale
     * @return le montant calculé qui est en général arrondi.
     */
    BigDecimal calcul(BigDecimal pAssiette);

}
