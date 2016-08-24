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

/**
 * Un barème à taux effectif est une fonction mathématique simple : on multiplie
 * le montant fournit en paramètre par le taux effectif (qui peut dépendre du montant).
 *
 * @author Patrick Giroud
 */
public interface BaremeTauxEffectif extends Bareme {

    /**
     * Retourne le taux effectif en fournissant l'assiette
     * @param assiette le revenu déterminant ou autre assiette
     * @return le taux effectif qui sera appliqué à l'assiette imposable
     */
    BigDecimal getTaux(BigDecimal assiette);
}
