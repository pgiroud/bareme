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

import org.impotch.util.BigDecimalUtil;

import java.math.BigDecimal;

public class ConstructeurBaremeTauxEffectifLineaireParTranche extends ConstructeurBareme {

    public ConstructeurBaremeTauxEffectifLineaireParTranche() {
        super();
        this.fermeAGauche();
    }


    protected TrancheBareme construireTranche(Intervalle inter, BigDecimal montantOuTaux) {
        // Utilisé pour construire la première et la dernière tranche
        return new TrancheBaremeLineaire(inter,montantOuTaux);
    }


    private TrancheBaremeLineaire construireTranche(Intervalle inter, BigDecimal montantOuTaux, BigDecimal incrementTaux) {
        return new TrancheBaremeLineaire(inter,montantOuTaux,incrementTaux);
    }

    public ConstructeurBaremeTauxEffectifLineaireParTranche premiereTranche(BigDecimal jusqua, BigDecimal taux) {
        super.premiereTranche(jusqua,taux);
        return this;
    }

    public ConstructeurBaremeTauxEffectifLineaireParTranche premiereTranche(int jusqua, String taux) {
        return premiereTranche(BigDecimal.valueOf(jusqua), BigDecimalUtil.parseTaux(taux));
    }


    private TrancheBaremeLineaire construireTranche(BigDecimal de, BigDecimal a, BigDecimal montantOuTaux, BigDecimal incrementTaux) {
        Intervalle inter = construireIntervalle(de,a);
        TrancheBaremeLineaire tranche = construireTranche(inter,montantOuTaux,incrementTaux);
        return tranche;
    }


    public final ConstructeurBaremeTauxEffectifLineaireParTranche tranche(BigDecimal de, BigDecimal a, BigDecimal taux, BigDecimal incrementTaux) {
        Intervalle intervalle = construireIntervalle(de,a);
        if (tranches.size() > 0) {
            TrancheBareme derniereTranche = tranches.get(tranches.size()-1);
            if (0 == derniereTranche.getTauxOuMontant().compareTo(taux)) {
                Intervalle inter = intervalle.union(derniereTranche.getIntervalle());
                tranches.set(tranches.size()-1,construireTranche(inter,taux,incrementTaux));
            } else {
                ajouterTranche(construireTranche(de,a,taux,incrementTaux));
            }
        } else {
            ajouterTranche(construireTranche(de,a,taux,incrementTaux));
        }
        return this;
    }


    public final ConstructeurBaremeTauxEffectifLineaireParTranche tranche(int de, int a, String taux, String tauxEnPlusPar100Francs) {
        return this.tranche(BigDecimal.valueOf(de),BigDecimal.valueOf(a), BigDecimalUtil.parseTaux(taux),BigDecimalUtil.parseTaux(tauxEnPlusPar100Francs).movePointLeft(2));
    }

    public Bareme construire() {
        BaremeTauxEffectifLineaireParTranche bareme = new BaremeTauxEffectifLineaireParTranche(tranches);
        bareme.setTypeArrondiGlobal(this.getTypeArrondiGlobal());
        bareme.setTypeArrondiSurChaqueTranche(this.getTypeArrondiSurChaqueTranche());
        return bareme;
    }

    protected static class TrancheBaremeLineaire extends TrancheBareme {

        private final BigDecimal pente;
        private final boolean isPremiereOuDerniere;

        public TrancheBaremeLineaire(Intervalle intervalle,
                                     BigDecimal taux, BigDecimal pente) {
            super(intervalle, taux);
            this.pente = pente;
            this.isPremiereOuDerniere = false;
        }

        public TrancheBaremeLineaire(Intervalle intervalle,
                                     BigDecimal taux) {
            super(intervalle, taux);
            this.pente = null;
            this.isPremiereOuDerniere = true;
        }


        /* (non-Javadoc)
         * @see ch.ge.afc.calcul.impot.bareme.TrancheBareme#calcul(java.math.BigDecimal, java.math.BigDecimal)
         */
        @Override
        public BigDecimal calcul(BigDecimal montant) {
            if (getIntervalle().encadre(montant) && BigDecimalUtil.isStrictementPositif(this.getTauxOuMontant())) {
                BigDecimal largeur = montant.subtract(getIntervalle().getDebut());
                if (isPremiereOuDerniere) {
                    return this.getTauxOuMontant();
                } else {
                    return this.getTauxOuMontant().add(pente.multiply(largeur));
                }
            }
            else return BigDecimal.ZERO;
        }

    }

}
