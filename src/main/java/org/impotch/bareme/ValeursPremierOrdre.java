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

import org.impotch.util.TypeArrondi;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

class ValeursPremierOrdre {

    private final BigDecimal valeur;
    private final BigDecimal increment;

    ValeursPremierOrdre(BigDecimal valeur, BigDecimal increment) {
        this.valeur = valeur;
        this.increment = increment;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    ValeursPremierOrdre setValeurOrdre0(BigDecimal valeur) {
        return new ValeursPremierOrdre(valeur,this.increment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValeursPremierOrdre that = (ValeursPremierOrdre) o;
        Comparator<BigDecimal> comparaisonBD = new Comparator<BigDecimal>() {
            @Override
            public int compare(BigDecimal o1, BigDecimal o2) {
                return o1.compareTo(o2);
            }
        };
        return 0 == Objects.compare(valeur, that.valeur,comparaisonBD)
                && 0 == Objects.compare(increment, that.increment, comparaisonBD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur, increment);
    }

    @Override
    public String toString() {
        return "ValeursPremierOrdre{" +
                "valeur=" + valeur +
                ", increment=" + increment +
                '}';
    }

    public ValeursPremierOrdre multiplie(BigDecimal rapport, TypeArrondi arrondi) {
        BigDecimal valeurOrdre0 = arrondi.arrondirMontant(valeur.multiply(rapport));
        return new ValeursPremierOrdre(valeurOrdre0,this.increment);
    }

    public BigDecimal calcul(BigDecimal variable) {
        return valeur.add(increment.multiply(variable));
    }
}
