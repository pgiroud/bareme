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

import org.impotch.util.TypeArrondi;
import org.impotch.util.math.integration.IntegrationExacte;
import org.impotch.util.math.integration.MethodeIntegration;
import org.impotch.util.math.integration.Primitivable;

/**
 * Les barèmes à taux marginal intégrable sont ceux pour lesquels on connaît la primitive du taux marginal.
 * C'est le cas par exemple du barème genevois pour personne seule entre 2001 et 2009.
 * Pour les barèmes dont les taux marginaux sont constants par tranche, on préférera utiliser la classe @link org.impotch.bareme.BaremeTauxMarginalConstantParTranche
 * @author Patrick Giroud
 *
 */
public class BaremeTauxMarginalIntegrable implements Bareme {

    private Primitivable tauxMarginal;
    private final MethodeIntegration methode = new IntegrationExacte();
    private TypeArrondi typeArrondi = TypeArrondi.CINQ_CTS;

    /**
     * On précise ici une fonction "taux marginal" qui est intégrable c.-à-d. dont on connaît une primitive.
     * @param tauxMarginal la fonction intégrable.
     */
    public void setTauxMarginal(Primitivable tauxMarginal) {
        this.tauxMarginal = tauxMarginal;
    }

    /**
     * Précise le type d'arrondi désiré en fin de calcul.
     * Par défaut, l'arrondi est fait aux 5 centimes les plus proches.
     * @param typeArrondi Le type d'arrondi désiré.
     */
    public void setTypeArrondi(TypeArrondi typeArrondi) {
        this.typeArrondi = typeArrondi;
    }

    /**
     * Le barème étant intégrable, le calcul consiste simplement à intégrer le taux entre 0 et l'assiette.
     * Le résultat est arrondi (voir #setTypeArrondi(org.impotch.util.TypeArrondi)).
     * @see org.impotch.bareme.Bareme#calcul(java.math.BigDecimal)
     */
    @Override
    public BigDecimal calcul(BigDecimal pAssiette) {
        double resultatFlottant = methode.integre(tauxMarginal, 0.0, pAssiette.doubleValue());
        BigDecimal resultat = BigDecimal.valueOf(resultatFlottant);
        return typeArrondi.arrondirMontant(resultat);
    }

}
