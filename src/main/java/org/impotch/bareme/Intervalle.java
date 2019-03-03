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

import org.impotch.util.TypeArrondi;

import java.math.BigDecimal;
import java.util.Objects;

public class Intervalle {

    private final BigDecimal debut;
    private final boolean debutInclus;
    private final BigDecimal fin;
    private final boolean finInclus;

    private Intervalle(BigDecimal debut, boolean debutInclus, BigDecimal fin , boolean finInclus) {
        this.debut = debut;
        this.debutInclus = debutInclus;
        this.fin = fin;
        this.finInclus = finInclus;
    }

    public boolean isDebutMoinsInfini() {
        return null == debut;
    }

    public BigDecimal getDebut() {
        return debut;
    }

    public boolean isFinPlusInfini() {
        return null == fin;
    }

    public boolean isBorne() {
        return !isDebutMoinsInfini() && !isFinPlusInfini();
    }

    public BigDecimal getFin() {
        return fin;
    }


    private BigDecimal translate(BigDecimal coordonnee, BigDecimal rapport, TypeArrondi typeArrondi) {
        assert null != coordonnee;
        return typeArrondi.arrondirMontant(coordonnee.multiply(rapport));
    }

    public Intervalle homothetie(BigDecimal rapport, TypeArrondi typeArrondi) {
        Cons cons = new Cons();
        cons = (isDebutMoinsInfini()) ?  cons.deMoinsInfini() : cons.de(translate(debut,rapport,typeArrondi));
        cons = (isFinPlusInfini()) ? cons.aPlusInfini() : cons.a(translate(fin,rapport,typeArrondi));
        return cons.intervalle();
    }

    public boolean encadre(BigDecimal x) {
        if (null == x) return false;
        boolean plusGrandQueBorneInferieure = true;
        if (null != this.debut) {
            int compareBorneInferieure = x.compareTo(this.debut);
            plusGrandQueBorneInferieure = debutInclus ? compareBorneInferieure >= 0 : compareBorneInferieure > 0;
        }
        if (plusGrandQueBorneInferieure) {
            if (null == this.fin) return true;
            int compareBorneSuperieure = x.compareTo(this.fin);
            return finInclus ? compareBorneSuperieure <= 0 : compareBorneSuperieure < 0;
        } else {
            return false;
        }
    }

    public boolean valeursInferieuresA(BigDecimal x) {
        assert null != x;
        return !isFinPlusInfini() && getFin().compareTo(x) < 0;
    }

    public boolean valeursSuperieuresA(BigDecimal x) {
        assert null != x;
        return !isDebutMoinsInfini() && getDebut().compareTo(x) > 0;
    }

    /**
     * On ne considère que l'union de 2 intervalles adjacents
     * @param inter
     * @return
     */
    public Intervalle union(Intervalle inter) {
        Cons cons = new Cons();
        if (null != getFin() && null != inter.getDebut() && 0 == this.getFin().compareTo(inter.getDebut())) {
            cons = cons.de(this.debut);
            if (this.debutInclus) {
                cons =cons.inclus();
            } else {
                cons = cons.exclus();
            }
            cons = cons.a(inter.fin);
            if (inter.finInclus) {
                cons =cons.inclus();
            } else {
                cons = cons.exclus();
            }
            return cons.intervalle();
        } else if (null != getDebut() && null != inter.getFin() && 0 == this.getDebut().compareTo(inter.getFin())) {
            cons = cons.de(inter.getDebut());
            if (inter.debutInclus) {
                cons = cons.inclus();
            } else {
                cons = cons.exclus();
            }
            cons = cons.a(this.getFin());
            if (this.finInclus) {
                cons = cons.inclus();
            } else {
                cons = cons.exclus();
            }
            return cons.intervalle();
        } else {
            throw new IllegalArgumentException("Les 2 intervalles " + this + " et " + inter + " ne sont pas adjacents !!");
        }
    }

    public boolean encadre(long x) {
        return encadre(BigDecimal.valueOf(x));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (isDebutMoinsInfini()) {
            builder.append("]-\u221e");
        } else {
            builder.append(debutInclus ? "[" : "]");
            builder.append(debut);
        }
        builder.append("; ");
        if (isFinPlusInfini()) {
            builder.append("+\u221e[");
        } else {
            builder.append(fin);
            builder.append(finInclus ? "]" : "[");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intervalle that = (Intervalle) o;
        return debutInclus == that.debutInclus &&
                finInclus == that.finInclus &&
                Objects.equals(debut, that.debut) &&
                Objects.equals(fin, that.fin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(debut, debutInclus, fin, finInclus);
    }

    public static class Cons {
        private BigDecimal debut;
        private BigDecimal fin;
        private boolean finRenseigne = false;
        private boolean debutInclus = false;
        private boolean finInclus = true;

        public Cons de(BigDecimal debut) {
            this.debut = debut;
            return this;
        }

        public Cons de(String debut) {
            return de(new BigDecimal(debut));
        }

        public Cons de(long debut) {
            return de(BigDecimal.valueOf(debut));
        }

        public Cons deMoinsInfini() {
            return de((BigDecimal)null);
        }

        public Cons a(BigDecimal fin) {
            this.fin = fin;
            this.finRenseigne = true;
            return this;
        }

        public Cons a(String fin) {
            return a(new BigDecimal(fin));
        }

        public Cons a(long fin) {
            return a(BigDecimal.valueOf(fin));
        }

        public Cons aPlusInfini() {
            return a((BigDecimal)null);
        }

        public Cons inclus() {
            if (finRenseigne) {
                finInclus = true;
            } else {
                debutInclus = true;
            }
            return this;
        }

        public Cons exclus() {
            if (finRenseigne) {
                finInclus = false;
            } else {
                debutInclus = false;
            }
            return this;
        }

        public Intervalle intervalle() {
            Intervalle inter = new Intervalle(debut, debutInclus,fin,finInclus);
            debut = null;
            fin = null;
            finRenseigne = false;
            debutInclus = false;
            finInclus = true;
            return inter;
        }
    }

}
