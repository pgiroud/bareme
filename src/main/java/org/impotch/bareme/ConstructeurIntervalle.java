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

import java.math.BigDecimal;

class ConstructeurIntervalle {

    private boolean fermeAGauche = false;
    private boolean fermeADroite = true;

    private boolean constructible = false;

    private BigDecimal borneInferieureCourante;
    private BigDecimal borneSuperieureCourante;
    private BigDecimal borneSuperieureIntervallePrecedent;


    public ConstructeurIntervalle fermeAGaucheEtOuvreADroite() {
        this.fermeAGauche = true;
        this.fermeADroite = false;
        return this;
    }

    public ConstructeurIntervalle ouvreAGaucheEtFermeADroite() {
        this.fermeAGauche = false;
        this.fermeADroite = true;
        return this;
    }


    public ConstructeurIntervalle ferme() {
        this.fermeAGauche = true;
        this.fermeADroite = true;
        return this;
    }

    public ConstructeurIntervalle tout() {
        constructible = true;
        return this;
    }

    public ConstructeurIntervalle jusqua(BigDecimal borne) {
        constructible = true;
        return a(borne);
    }

    public ConstructeurIntervalle puisJusqua(BigDecimal borne) {
        if (null == borneSuperieureIntervallePrecedent) {
            throw new RuntimeException("Vous ne pouvez pas utiliser la méthode puisJusqua sur la première tranche !");
        }
        return de(borneSuperieureIntervallePrecedent).a(borne);
    }

    public ConstructeurIntervalle puis() {
        if (null == borneSuperieureIntervallePrecedent) {
            throw new RuntimeException("Vous ne pouvez pas utiliser la méthode puisJusqua sur la première tranche !");
        }
        return plusDe(borneSuperieureIntervallePrecedent);
    }



    public  ConstructeurIntervalle de(BigDecimal borneInferieure) {
        borneInferieureCourante =  borneInferieure;
        return this;
    }

    public  ConstructeurIntervalle a(BigDecimal borneSuperieure) {
        constructible = true;
        borneSuperieureCourante =  borneSuperieure;
        return this;
    }

    public ConstructeurIntervalle plusDe(BigDecimal borneSuperieure) {
        constructible = true;
        return de(borneSuperieure);
    }

    private Intervalle construireIntervalle() {
        if(null == borneInferieureCourante) {
            if (null == borneSuperieureCourante) return Intervalle.TOUT;
            return construirePremierIntervalle(borneSuperieureCourante);
        }
        if (null == borneSuperieureCourante) return construireDernierIntervalle(borneInferieureCourante);
        return construireIntervalle(borneInferieureCourante,borneSuperieureCourante);
    }

    private void reinitialiseIntervalleCourant() {
        borneSuperieureIntervallePrecedent = borneSuperieureCourante;
        constructible = false;
        this.borneInferieureCourante = null;
        this.borneSuperieureCourante = null;
    }

    public boolean isConstructible() {
        return constructible;
    }

    public Intervalle construire() {
        // TODO PGI Utiliser des Option
        if (!constructible) return null;
        Intervalle intervalle =  construireIntervalle();
        reinitialiseIntervalleCourant();
        return intervalle;
    }

    private Intervalle construirePremierIntervalle(BigDecimal jusqua) {
        Intervalle.Cons cons = new Intervalle.Cons().deMoinsInfini().a(jusqua);
        if (fermeADroite) {
            cons = cons.inclus();
        } else {
            cons = cons.exclus();
        }
        return cons.intervalle();
    }

    private Intervalle construireIntervalle(BigDecimal de, BigDecimal a) {
        Intervalle.Cons cons = new Intervalle.Cons();
        if (null == de) {
            cons = cons.deMoinsInfini();
            if (fermeAGauche) {
                cons = cons.a(a).exclus();
            } else {
                cons = cons.a(a).inclus();
            }
        } else {
            if (fermeAGauche) {
                cons = cons.de(de).inclus();
            } else {
                cons = cons.de(de).exclus();
            }
            if (fermeADroite) {
                cons = cons.a(a).inclus();
            } else {
                cons = cons.a(a).exclus();
            }
        }
        return cons.intervalle();
    }

    private Intervalle construireDernierIntervalle(BigDecimal depuis) {
        Intervalle.Cons cons = new Intervalle.Cons().de(depuis);
        if (fermeAGauche) {
            cons = cons.inclus();
        } else {
            cons = cons.exclus();
        }
        return cons.aPlusInfini().intervalle();
    }



}
