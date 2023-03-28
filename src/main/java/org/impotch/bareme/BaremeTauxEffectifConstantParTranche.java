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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.HashCodeBuilder;
import org.impotch.util.TypeArrondi;

/**
 * Barème à taux effectif défini par tranche et dont le taux est constant sur chacune des tranches.
 * Ces barèmes sont typiquement utilisés pour l'impôt à la source.
 *
 * @author Patrick Giroud
 *
 */
public final class BaremeTauxEffectifConstantParTranche extends
        BaremeTauxEffectifParTranche {

    /**
     * On réduit la portée pour forcer l'utilisation du ConstructeurBareme
     */
    BaremeTauxEffectifConstantParTranche() {
        super();
    }

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/


    @Override
    protected BaremeParTranche newBaremeParTranche() {
        return new BaremeTauxEffectifConstantParTranche();
    }



    /* (non-Javadoc)
     * @see org.impotch.afc.calcul.impot.bareme.BaremeTauxEffectif#getTaux(java.math.BigDecimal)
     */
    @Override
    public BigDecimal getTaux(BigDecimal assiette) {
        return getTranches().stream().map(t -> t.calcul(assiette)).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaremeTauxEffectifConstantParTranche)) return false;
        BaremeTauxEffectifConstantParTranche bareme = (BaremeTauxEffectifConstantParTranche) obj;
        if (!this.getTypeArrondiSurChaqueTranche().equals(bareme.getTypeArrondiSurChaqueTranche())) return false;
        if (0 != BigDecimalUtil.nullSafeCompare(this.getSeuil(), bareme.getSeuil())) return false;
        if (!this.getTranches().equals(bareme.getTranches())) return false;
        return true;
    }

 }
