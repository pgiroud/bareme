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
import java.util.Objects;

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.HashCodeBuilder;
import org.impotch.util.TypeArrondi;

/**
 * @author Patrick Giroud
 *
 */
public class TrancheBareme {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

    private final Intervalle intervalle;
    private final BigDecimal tauxOuMontant;

    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/


    protected TrancheBareme(Intervalle intervalle, BigDecimal tauxOuMontant) {
        super();
        this.intervalle = intervalle;
        this.tauxOuMontant = tauxOuMontant;
    }

    /**************************************************/
    /************* Accesseurs / Mutateurs *************/
    /**************************************************/

    public BigDecimal getTauxOuMontant() {
        return tauxOuMontant;
    }

    public Intervalle getIntervalle() {
        return intervalle;
    }

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

    protected TrancheBareme newTranche(Intervalle intervalle, BigDecimal tauxOuMontant) {
        return new TrancheBareme(intervalle,tauxOuMontant);
    }

    /**
     * Une tranche peut être translatée. Translater une tranche consiste à
     * multiplier le montant imposable maximum par le rapport de translation
     * et à l'arrondir.
     * @param rapport Le rapport de translation (ne peut pas être null et doit être strictement positif).
     * @param typeArrondi L'arrondi à effectuer sur la valeur obtenue. Ne doit pas être null.
     * @return Une nouvelle tranche translatée.
     */
    public TrancheBareme homothetie(BigDecimal rapport, TypeArrondi typeArrondi) {
        if (!BigDecimalUtil.isStrictementPositif(rapport))
            throw new IllegalArgumentException("Le rapport d'homothétie '" + rapport + "' ne peut pas être négatif ou null !!");
        return newTranche(intervalle.homothetie(rapport,typeArrondi), this.getTauxOuMontant());
    }

    public TrancheBareme homothetieValeur(BigDecimal rapport, TypeArrondi typeArrondi) {
        if (!BigDecimalUtil.isStrictementPositif(rapport))
            throw new IllegalArgumentException("Le rapport d'homothétie '" + rapport + "' ne peut pas être négatif ou null !!");
        BigDecimal inter = this.getTauxOuMontant().multiply(rapport);
        BigDecimal tauxOuMontant = typeArrondi.arrondirMontant(inter);
        return newTranche(this.intervalle, tauxOuMontant);
    }

    public BigDecimal calcul(BigDecimal montant) {
        if (intervalle.encadre(montant)) return this.tauxOuMontant;
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return intervalle + " " + tauxOuMontant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrancheBareme that = (TrancheBareme) o;
        return Objects.equals(intervalle, that.intervalle) &&
                Objects.equals(tauxOuMontant, that.tauxOuMontant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intervalle, tauxOuMontant);
    }

}
