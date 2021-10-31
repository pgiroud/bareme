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
import java.util.List;

public class ConstructeurBaremeTauxMarginal extends ConstructeurBareme {


    public ConstructeurBaremeTauxMarginal() {
        super();
    }

    public ConstructeurBaremeTauxMarginal(List<TrancheBareme> tranches) {
        super(tranches);
    }

    public ConstructeurBaremeTauxMarginal(BaremeTauxMarginalConstantParTranche bareme) {
        this(bareme.getTranches());
        typeArrondiSurChaqueTranche(bareme.getTypeArrondiSurChaqueTranche());
    }


    @Override
    protected TrancheBareme construireTranche(Intervalle inter, BigDecimal montantOuTaux) {
        return new TrancheBaremeTxMarginal(inter, montantOuTaux);
    }

    @Override
    public ConstructeurBaremeTauxMarginal uniqueTranche(String taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.uniqueTranche(taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal premiereTranche(BigDecimal jusqua, BigDecimal taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.premiereTranche(jusqua, taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal premiereTranche(int jusqua, String taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.premiereTranche(jusqua, taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal premiereTranche(int jusqua, int valeur) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.premiereTranche(jusqua, valeur);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal tranche(BigDecimal de, BigDecimal a, BigDecimal taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.tranche(de, a, taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal tranche(int de, int a, BigDecimal taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.tranche(de, a, taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal tranche(int de, int a, String taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.tranche(de, a, taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal tranche(int de, int a, int valeur) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.tranche(de, a, valeur);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal derniereTranche(BigDecimal depuis, BigDecimal taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.derniereTranche(depuis, taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal derniereTranche(int depuis, int valeur) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.derniereTranche(depuis, valeur);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal derniereTranche(int depuis, String taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.derniereTranche(depuis, taux);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal de(BigDecimal borneInferieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.de(borneInferieure);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal de(int borneInferieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.de(borneInferieure);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal a(BigDecimal borneSuperieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.a(borneSuperieure);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal a(int borneSuperieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.a(borneSuperieure);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal jusqua(BigDecimal borneSuperieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.jusqua(borneSuperieure);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal jusqua(int borneSuperieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.jusqua(borneSuperieure);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal aPartirDe(BigDecimal borneInferieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.aPartirDe(borneInferieure);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal aPartirDe(int borneInferieure) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.aPartirDe(borneInferieure);
        return this;
    }

    @Override
    public ConstructeurBareme tauxOuValeur(BigDecimal tauxOuValeur) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.tauxOuValeur(tauxOuValeur);
        return this;
    }

    @Override
    public ConstructeurBaremeTauxMarginal taux(String taux) {
        // On surcharge uniquement pour permettre le chaînage sur les
        // constructeurs (pattern builder)
        super.taux(taux);
        return this;
    }

    public BaremeTauxMarginalConstantParTranche construire() {
        BaremeTauxMarginalConstantParTranche bareme = new BaremeTauxMarginalConstantParTranche();
        completerBareme(bareme);
        return bareme;
    }


    private class TrancheBaremeTxMarginal extends TrancheBareme {

        private BigDecimal montantTranche;

        public TrancheBaremeTxMarginal(Intervalle intervalle, BigDecimal tauxOuMontant) {
            super(intervalle, tauxOuMontant);
        }

        private void calculMontantTranche() {
            if (getIntervalle().isBorne()) {
                BigDecimal largeur = getIntervalle().getFin().subtract(getIntervalle().getDebut());
                montantTranche = getTauxOuMontant().multiply(largeur);
            } else if (getIntervalle().isFinPlusInfini()) {
                throw new UnsupportedOperationException("Impossible de calculer le montant d'impôt d'une tranche " + getIntervalle() +"infinie !!");
            } else {
                montantTranche = getTauxOuMontant().multiply(getIntervalle().getFin());
            }
        }

        private BigDecimal getMontantTranche() {
            if (null == montantTranche) {
                calculMontantTranche();
            }
            return montantTranche;
        }

        @Override
        protected TrancheBareme newTranche(Intervalle intervalle, BigDecimal tauxOuMontant) {
            return new TrancheBaremeTxMarginal(intervalle, tauxOuMontant);
        }

        @Override
        public BigDecimal calcul(BigDecimal montant) {
            if (getIntervalle().encadre(montant)) {
                BigDecimal debut = getIntervalle().getDebut();
                if (null == debut) {
                    debut = BigDecimal.ZERO;
                }
                BigDecimal largeur = montant.subtract(debut);
                return largeur.multiply(getTauxOuMontant());
            }
            else if (getIntervalle().valeursInferieuresA(montant)) {
                return getMontantTranche();
            }
            else return BigDecimal.ZERO;
        }
    }

}
