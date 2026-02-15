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
import java.util.Optional;

public sealed interface DecimalEtendu extends Comparable<DecimalEtendu> permits Borne, Infini {
    static DecimalEtendu de(BigDecimal valeur) {
        if (null == valeur) throw new IllegalArgumentException("On ne peut pas créer un décimal étendu avec un null !");
        return new Borne(valeur);
    }

    DecimalEtendu INFINI_POSITIF = new Infini(true);

    DecimalEtendu INFINI_NEGATIF = new Infini(false);

    boolean borne();

    Optional<BigDecimal> valeur();

    Optional<DecimalEtendu> mutiply(DecimalEtendu facteur);

    DecimalEtendu arrondir(TypeArrondi arrondi);

    DecimalEtendu min(DecimalEtendu de);
}

record Borne(BigDecimal val) implements DecimalEtendu {

    @Override
    public boolean borne() {
        return true;
    }

    @Override
    public int compareTo(DecimalEtendu o) {
        return switch(o) {
            case Borne u -> this.val.compareTo(u.val);
            case Infini i  -> i.positif() ? -1 : 1;
        };
    }

    @Override
    public String toString() {
        return val.toString();
    }

    private boolean strictementPositif() {
        return BigDecimal.ZERO.compareTo(this.val) < 0;
    }

    private boolean strictementNegatif() {
        return BigDecimal.ZERO.compareTo(this.val) > 0;
    }

    @Override
    public Optional<BigDecimal> valeur() {
        return Optional.of(val);
    }

    @Override
    public Optional<DecimalEtendu> mutiply(DecimalEtendu facteur) {
        return switch(facteur) {
            case Borne u -> Optional.of(new Borne(this.val.multiply(u.val)));
            case Infini i -> strictementPositif() ? Optional.of(i) :
                strictementNegatif() ? Optional.of(i.oppose()) : Optional.empty();
        };
    }

    @Override
    public DecimalEtendu arrondir(TypeArrondi arrondi) {
        return DecimalEtendu.de(arrondi.arrondirMontant(this.val));
    }

    @Override
    public DecimalEtendu min(DecimalEtendu de) {
        return switch(de) {
            case Borne u -> new Borne(this.val.min(u.val));
            case Infini i -> i.positif() ? this : i ;
        };
    }
}

record Infini(boolean positif) implements DecimalEtendu {

    @Override
    public boolean borne() {
        return false;
    }

    public boolean positif() {
        return positif;
    }

    @Override
    public int compareTo(DecimalEtendu o) {
        return switch (o) {
            case Borne u -> positif ? 1 : -1;
            case Infini i -> this.compareTo(i);
        };
    }

    @Override
    public String toString() {
        return positif ? "+\u221e" : "-\u221e";
    }

    @Override
    public Optional<BigDecimal> valeur() {
        return Optional.empty();
    }

    public DecimalEtendu oppose() {
        return new Infini(!this.positif);
    }

    @Override
    public Optional<DecimalEtendu> mutiply(DecimalEtendu facteur) {
        return switch(facteur) {
            case Borne u -> u.mutiply(this);
            case Infini i -> Optional.of(new Infini(!(this.positif ^ i.positif)));
        };
    }

    @Override
    public DecimalEtendu arrondir(TypeArrondi arrondi) {
        return this;
    }

    @Override
    public DecimalEtendu min(DecimalEtendu de) {
        return this.positif ? de : this ;
    }
}