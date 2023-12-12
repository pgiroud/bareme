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

import static java.math.BigDecimal.ZERO;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.impotch.util.TypeArrondi;

public class BaremeTauxMarginalConstantParTranche extends BaremeParTranche implements BaremeTauxMarginal {

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

    @Override
    public BigDecimal calculSansSeuil(BigDecimal assiette) {
        BigDecimal resultat = getTranches().stream()
                .filter(t -> t.getIntervalle().encadre(assiette))
                .findFirst().orElseThrow().integre(assiette);

        resultat = getTypeArrondiSurChaqueTranche().arrondirMontant(resultat);
        return getTypeArrondiGlobal().arrondirMontant(resultat);
    }

    private void resetValeurs() {
        List<TrancheBareme> tranches = getTranches();
        for (int i = 1; i < tranches.size(); i++) {
            TrancheBareme tranchePrecedente = tranches.get(i-1);
            TrancheBareme tranche = tranches.get(i);
            tranches.set(i,tranche.setValeurOrdre0(calcul(tranchePrecedente.getIntervalle().getFin())));
        }
    }

    @Override
    public BaremeTauxMarginalConstantParTranche homothetie(BigDecimal taux, TypeArrondi typeArrondi) {
        BaremeTauxMarginalConstantParTranche bareme = (BaremeTauxMarginalConstantParTranche)super.homothetie(taux, typeArrondi);
        bareme.resetValeurs();
        return bareme;
    }

    @Override
    protected BaremeParTranche newBaremeParTranche() {
        return new BaremeTauxMarginalConstantParTranche();
    }

    @Override
    public String toString() {
        return "BaremeTauxMarginalConstantParTranche spécialisation de " + super.toString();
    }

    @Override
    public BigDecimal getTauxMaximum() {
        List<TrancheBareme> tranches = getTranches();
        if (tranches.size() > 0) {
            TrancheBareme derniereTranche = tranches.get(tranches.size()-1);
            if (!derniereTranche.getIntervalle().estBorneADroite()) {
                return derniereTranche.getValeurs().getIncrement();
            } else {
                // Si la dernière tranche est bornée à droite
                BigDecimal borne = derniereTranche.getIntervalle().getFin();
                BigDecimal valeur = calcul(borne);
                return valeur.divide(borne,10, RoundingMode.HALF_UP);
            }
        }
        if (tranches.size() == 0) {
            throw new IllegalStateException("Impossible de définir le taux maximal, il n’y a pas de tranches !!  ");
        }
        return null;
    }
}
