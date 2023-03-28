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

import static java.math.BigDecimal.ZERO;

import java.util.ArrayList;
import java.util.List;

import org.impotch.util.TypeArrondi;

public class BaremeTauxMarginalConstantParTranche extends BaremeParTranche implements Bareme {

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

    @Override
    public BigDecimal calculSansSeuil(BigDecimal assiette) {
        BigDecimal resultat = getTranches().stream()
                .filter(t -> t.getIntervalle().encadre(assiette))
                .findFirst().orElseThrow().integre(assiette);

        return getTypeArrondiGlobal().arrondirMontant(resultat);
    }



    @Override
    protected BaremeParTranche newBaremeParTranche() {
        return new BaremeTauxMarginalConstantParTranche();
    }

    @Override
    public String toString() {
        return "BaremeTauxMarginalConstantParTranche spécialisation de " + super.toString();
    }
}
