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

class ConstructeurTranche {

    private final ConstructeurIntervalle consInter = new ConstructeurIntervalle();
    private final ConstructeurValeursAuPremierOrdre consValeurs = new ConstructeurValeursAuPremierOrdre();

    private Intervalle intervalleCourant;

    public static ConstructeurTranche uneTranche() { return new ConstructeurTranche(); }

    // ******************* Construction des intervalles *************************

    public ConstructeurTranche fermeAGaucheEtOuvreADroite() {
        consInter.fermeAGaucheEtOuvreADroite();
        return this;
    }

    public ConstructeurTranche ferme() {
        consInter.ferme();
        return this;
    }

    public ConstructeurTranche ouvreAGaucheEtFermeADroite() {
        consInter.ouvreAGaucheEtFermeADroite();
        return this;
    }


    public ConstructeurTranche jusqua(BigDecimal borne) {
        consInter.jusqua(borne);
        return this;
    }


    public ConstructeurTranche puisJusqua(BigDecimal borne) {
        consInter.puisJusqua(borne);
        return this;
    }

    public ConstructeurTranche puis() {
        consInter.puis();
        return this;
    }

    public ConstructeurTranche de(BigDecimal borne) {
        consInter.de(borne);
        return this;
    }

    public ConstructeurTranche de(int borne) {
        return de(BigDecimal.valueOf(borne));
    }

    public ConstructeurTranche a(BigDecimal borne) {
        consInter.a(borne);
        return this;
    }

    public ConstructeurTranche a(int borne) {
        return a(BigDecimal.valueOf(borne));
    }

    public ConstructeurTranche plusDe(BigDecimal borne) {
        consInter.plusDe(borne);
        return this;
    }

    public ConstructeurTranche tout() {
        consInter.tout();
        return this;
    }

    public ConstructeurTranche intervalle(Intervalle inter) {
        intervalleCourant = inter;
        return this;
    }

    public ConstructeurTranche valeur(BigDecimal montantOuTaux) {
        consValeurs.valeur(montantOuTaux);
        return this;
    }


    public ConstructeurTranche increment(BigDecimal increment) {
        consValeurs.increment(increment);
        return this;
    }

    public boolean isConstructible() {
        return consInter.isConstructible();
    }

    public TrancheBareme construire() {
        // TODO PGI retourner Option
        Intervalle inter = (null != intervalleCourant) ?  intervalleCourant : consInter.construire();
        if (null == inter) return null;
        TrancheBareme tranche = new TrancheBareme(inter, consValeurs.construire());
        intervalleCourant = null;
        return tranche;
    }

}
