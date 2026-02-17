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
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

import static org.impotch.util.BigDecimalUtil.trim;
import static org.impotch.bareme.DecimalEtendu.INFINI_POSITIF;
import static org.impotch.bareme.DecimalEtendu.INFINI_NEGATIF;

public class Intervalle {

    public static final Intervalle TOUT = new Intervalle(INFINI_NEGATIF,true, INFINI_POSITIF,true);

    private final DecimalEtendu debut;
    private final boolean debutInclus;
    private final DecimalEtendu fin;
    private final boolean finInclus;

    private Intervalle(DecimalEtendu debut, boolean debutInclus, DecimalEtendu fin , boolean finInclus) {
        this.debut = debut;
        this.debutInclus = debutInclus;
        this.fin = fin;
        this.finInclus = finInclus;
    }


    private Intervalle(BigDecimal debut, boolean debutInclus, BigDecimal fin , boolean finInclus) {
        this(DecimalEtendu.de(debut),debutInclus,DecimalEtendu.de(fin),finInclus);
    }

    public Optional<BigDecimal> getMilieu() {
        return fin.valeur().flatMap(
                f -> debut.valeur().map(d -> f.add(d).divide(BigDecimal.valueOf(2),2, RoundingMode.HALF_UP).stripTrailingZeros()));
    }

    public boolean ouvertAGauche() {
        return INFINI_NEGATIF.equals(debut) || !debutInclus;
    }

    public boolean fermeAGauche() {
        return debutInclus;
    }

    public boolean ouvertADroite() {
        return INFINI_POSITIF.equals(fin) || !finInclus;
    }

    public boolean fermeADroite() {
        return finInclus;
    }


    public boolean estOuvertOuvert() {
        return ouvertAGauche() && ouvertADroite();
    }

    public boolean estFermeFerme() {
        return fermeAGauche() && fermeADroite();
    }

    public boolean estOuvertFerme() {
        return ouvertAGauche() && fermeADroite();
    }

    public boolean estFermeOuvert() {
        return fermeAGauche() && ouvertADroite();
    }

    public boolean estBorneAGauche() {
        return debut.borne();
    }

    public Optional<BigDecimal> getDebut() {
        return debut.valeur();
    }

    public boolean estBorneADroite() {
        return fin.borne();
    }

    public boolean isBorne() {
        return estBorneAGauche() && estBorneADroite();
    }

    public Optional<BigDecimal> getFin() {
        return fin.valeur();
    }


    public Optional<BigDecimal> longueur() {
        return longueurAvant(fin);
    }

    public Optional<BigDecimal> longueurAvant(DecimalEtendu dec) {
        if (dec.compareTo(debut) <= 0) return Optional.of(BigDecimal.ZERO);
        return fin.min(dec).valeur().flatMap(f -> debut.valeur().map(f::subtract));
    }

    private DecimalEtendu translate(DecimalEtendu coordonnee, BigDecimal rapport, TypeArrondi typeArrondi) {
        assert null != coordonnee;
        assert 0 != BigDecimal.ZERO.compareTo(rapport);
        return coordonnee.mutiply(DecimalEtendu.de(rapport)).map(de -> de.arrondir(typeArrondi)).orElseThrow();
    }

    public Intervalle homothetie(BigDecimal rapport, TypeArrondi typeArrondi) {
        assert 0 != BigDecimal.ZERO.compareTo(rapport);
        Cons cons = new Cons()
                .de(translate(debut,rapport,typeArrondi))
                .a(translate(fin,rapport,typeArrondi))
                ;
        cons.debutInclus = this.debutInclus;
        cons.finInclus = this.finInclus;
        return cons.intervalle();
    }

    private boolean plusGrandQueBorneInferieure(DecimalEtendu val) {
        int compareBorneInferieure = val.compareTo(this.debut);
        return debutInclus ? compareBorneInferieure >= 0 : compareBorneInferieure > 0;
    }

    private boolean plusPetitQueBorneSuperieure(DecimalEtendu val) {
        int compareBorneSuperieure = val.compareTo(this.fin);
        return finInclus ? compareBorneSuperieure <= 0 : compareBorneSuperieure < 0;
    }

    public boolean encadre(BigDecimal x) {
        if (null == x) return false;
        DecimalEtendu val = DecimalEtendu.de(x);
        return plusGrandQueBorneInferieure(val) && plusPetitQueBorneSuperieure(val);
    }

    public boolean adjacent(Intervalle inter) {
        if (this.equals(TOUT)) return false;
        return 0 == fin.compareTo(inter.debut)
                ||  0 == debut.compareTo(inter.fin);
    }

    public boolean estInclusDans(Intervalle englobant) {
        if (this.equals(englobant)) return true;

        boolean debutMoinsInfiniDansEnglobant = !debut.borne() && !englobant.debut.borne();
        boolean debutBorneIncluseDansEnglobant = debut.valeur().isPresent() && debutInclus && englobant.encadre(debut.valeur().get());
        boolean debutBorneExcluseDansEnglobant = debut.valeur().isPresent() && !debutInclus && (englobant.encadre(debut.valeur().get()) || (!englobant.debutInclus && englobant.debut.equals(debut)));
        boolean debutBorneDansEnglobant = debutBorneIncluseDansEnglobant || debutBorneExcluseDansEnglobant;
        boolean debutDansEnglobant = debutMoinsInfiniDansEnglobant || debutBorneDansEnglobant;

        boolean finPlusInifiniDansEnglobant = !fin.borne() && !englobant.fin.borne();
        boolean finBorneIncluseDansEnglobant = fin.valeur().isPresent() && finInclus && englobant.encadre(fin.valeur().get());
        boolean finBorneExcluseDansEnglobant = fin.valeur().isPresent() && !finInclus && (englobant.encadre(fin.valeur().get()) || (!englobant.finInclus && englobant.fin.equals(fin)));
        boolean finBorneDansEnglobant = finBorneIncluseDansEnglobant || finBorneExcluseDansEnglobant;
        boolean finDansEnglobant = finPlusInifiniDansEnglobant || finBorneDansEnglobant;

        return debutDansEnglobant && finDansEnglobant;

    }

    public boolean valeursInferieuresA(BigDecimal x) {
        assert null != x;
        return fin.compareTo(DecimalEtendu.de(x)) < 0;
    }

    public boolean valeursSuperieuresA(BigDecimal x) {
        assert null != x;
        return debut.compareTo(DecimalEtendu.de(x)) > 0;
    }

    /**
     * On ne considère que l'union de 2 intervalles adjacents
     * @param inter
     * @return
     */
    public Intervalle union(Intervalle inter) {
        Cons cons = new Cons();
        if (fin.borne() && inter.debut.borne() && 0 == fin.compareTo(inter.debut)) {
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
        } else if (debut.borne() && inter.fin.borne() && 0 == debut.compareTo(inter.fin)) {
            cons = cons.de(inter.debut);
            if (inter.debutInclus) {
                cons = cons.inclus();
            } else {
                cons = cons.exclus();
            }
            cons = cons.a(fin);
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
        if (!estBorneAGauche()) {
            builder.append("]-\u221e");
        } else {
            builder.append(debutInclus ? "[" : "]");
            builder.append(debut);
        }
        builder.append("; ");
        if (!estBorneADroite()) {
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
        private DecimalEtendu debut;
        private DecimalEtendu fin;
        private boolean finRenseigne = false;
        private boolean debutInclus = false;
        private boolean finInclus = true;

        Cons de(DecimalEtendu debut) {
            this.debut = debut;
            return this;
        }

        public Cons de(BigDecimal debut) {
            return de(DecimalEtendu.de(debut));
        }

        public Cons de(String debut) {
            return de(new BigDecimal(debut));
        }

        public Cons de(long debut) {
            return de(BigDecimal.valueOf(debut));
        }

        public Cons deMoinsInfini() {
            return de(INFINI_NEGATIF);
        }

        public Cons a(DecimalEtendu fin) {
            this.fin = fin;
            this.finRenseigne = true;
            return this;
        }

        public Cons a(BigDecimal fin) {
            return a(DecimalEtendu.de(fin));
        }

        public Cons a(String fin) {
            return a(new BigDecimal(fin));
        }

        public Cons a(long fin) {
            return a(BigDecimal.valueOf(fin));
        }

        public Cons aPlusInfini() {
            return a(INFINI_POSITIF).exclus();
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
