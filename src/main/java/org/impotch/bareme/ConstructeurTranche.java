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
        Intervalle inter = consInter.construire();
        if (null == inter) return null;
        return new TrancheBareme(inter, consValeurs.construire());
    }

//    protected TrancheBareme construireTranche(BigDecimal de, BigDecimal a, BigDecimal montantOuTaux) {
//        return de(de).a(a).valeur(montantOuTaux).construire();
//    }

//    /**
//     * Cette méthode sera surchargée pour spécialiser les tranches de barèmes.
//     * Par défaut, on utilise la tranche avec montant ou taux constant.
//     * @param inter L'intervalle délimitant la tranche
//     * @param montantOuTaux La valeur du barème pour tous les éléments appartenant à l'intervalle.
//     * @return Une tranche de barème qui pourra être ajoutée à la liste des tranches du barèmes.
//     */
//    protected TrancheBareme construireTranche(Intervalle inter, BigDecimal montantOuTaux) {
//        return new TrancheBareme(inter, montantOuTaux);
//    }

//    TrancheBareme construireUniqueTranche(BigDecimal valeur) {
//        return construireTranche(Intervalle.TOUT, valeur);
//    }

//    TrancheBareme construireTranche(BigDecimal de, BigDecimal a, BigDecimal montantOuTaux, BigDecimal incrementTaux) {
//        return de(de).a(a).valeur(montantOuTaux).construire();
//    }
//
//    TrancheBareme construireTranche(Intervalle inter, BigDecimal montantOuTaux, BigDecimal incrementTaux) {
//        return new TrancheBareme(inter, montantOuTaux, incrementTaux);
//    }
//
//
//    TrancheBareme construirePremiereTranche(BigDecimal montantOuTaux) {
//        Intervalle inter = consInter.construire();
//        return construireTranche(inter, montantOuTaux);
//    }
//
//    TrancheBareme construireTranche(BigDecimal montantOuTaux) {
//        return valeur(montantOuTaux).construire();
//    }
//
//
//    TrancheBareme construireDerniereTranche(BigDecimal depuis, BigDecimal montantOuTaux) {
//        return plusDe(depuis).valeur(montantOuTaux).construire();
//    }



}
