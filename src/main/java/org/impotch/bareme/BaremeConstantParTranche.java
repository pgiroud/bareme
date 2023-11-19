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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.impotch.util.TypeArrondi;

/**
 * Un barème par tranche est une fonction en escalier c.-à-d. qu'elle est constante
 * sur des intervalles et les intervalles forment une partition de l'ensemble des nombres
 * réels positifs.
 * @author Patrick Giroud
 *
 */
public class BaremeConstantParTranche extends BaremeParTranche {

    public BaremeConstantParTranche() {
        super();
    }

    public BaremeConstantParTranche(BigDecimal seuil) {
        super();
        setSeuil(seuil);
    }


    /* (non-Javadoc)
     * @see org.impotch.afc.calcul.impot.bareme.BaremeParTranche#calculSansSeuil(java.math.BigDecimal)
     */
    @Override
    protected BigDecimal calculSansSeuil(BigDecimal assiette) {
        return getTranches().stream().map(t -> t.calcul(assiette)).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    protected BaremeParTranche newBaremeParTranche() {
        return new BaremeConstantParTranche();
    }

    /**
     * Retourne un barème constant par tranche dont les valeurs sont translatés du facteur taux.
     * @param taux  le taux de dilatation des valeurs
     * @param typeArrondi  L'arrondi appliqué sur chacune des valeurs après application du taux
     * @return le barème dont les valeurs sont translatées (les tranches sont quant à elles inchangées).
     */
    @Override
    public BaremeConstantParTranche homothetieValeur(BigDecimal taux, TypeArrondi typeArrondi) {
        BaremeConstantParTranche bareme = new BaremeConstantParTranche();
        bareme.setTypeArrondiSurChaqueTranche(this.getTypeArrondiSurChaqueTranche());
        bareme.setTranches(getTranches().stream().map(tranche -> tranche.homothetieValeur(taux, typeArrondi)).collect(Collectors.toList()));
        return bareme;
    }

}
