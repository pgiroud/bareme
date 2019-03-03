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
import org.impotch.util.TypeArrondi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class ConstructeurBareme {

    protected List<TrancheBareme> tranches = new ArrayList();
    private TypeArrondi typeArrondiSurChaqueTranche = TypeArrondi.CT;
    private TypeArrondi typeArrondiGlobal = TypeArrondi.CT;
    private BigDecimal seuil;
    private boolean fermeAGauche = false;
    protected BigDecimal montantMaxPrecedent = null;

    public ConstructeurBareme() {
        super();
    }

    public ConstructeurBareme(List<TrancheBareme> tranches) {
        this();
        this.tranches = tranches;
    }

    public ConstructeurBareme typeArrondiSurChaqueTranche(TypeArrondi typeArrondi) {
        this.typeArrondiSurChaqueTranche = typeArrondi;
        return this;
    }

    public ConstructeurBareme typeArrondiGlobal(TypeArrondi typeArrondi) {
        this.typeArrondiGlobal = typeArrondi;
        return this;
    }

    public ConstructeurBareme seuil(BigDecimal seuil) {
        this.seuil = seuil;
        return this;
    }


    public ConstructeurBareme seuil(int montant) {
        return seuil(BigDecimal.valueOf(montant));
    }

    public ConstructeurBareme fermeAGauche() {
        this.fermeAGauche = true;
        return this;
    }

    protected TrancheBareme construireTranche(Intervalle inter, BigDecimal montantOuTaux) {
        return new TrancheBareme(inter,montantOuTaux);
    }

    protected Intervalle construireIntervalle(BigDecimal montant) {
        Intervalle.Cons cons = new Intervalle.Cons();
        if (null == montantMaxPrecedent) {
            cons = cons.deMoinsInfini();
            if (fermeAGauche) {
                cons = cons.a(montant).exclus();
            } else {
                cons = cons.a(montant).inclus();
            }
        } else {
            if (fermeAGauche) {
                cons = cons.de(montantMaxPrecedent).inclus()
                        .a(montant).exclus();
            } else {
                cons = cons.de(montantMaxPrecedent).exclus()
                        .a(montant).inclus();
            }
        }
        return cons.intervalle();
    }

    protected TrancheBareme construireTranche(BigDecimal montant, BigDecimal montantOuTaux) {
        Intervalle inter = construireIntervalle(montant);
        TrancheBareme tranche = construireTranche(inter,montantOuTaux);
        montantMaxPrecedent = montant;
        return tranche;
    }

    private TrancheBareme construireTranche(int montant, BigDecimal taux) {
        return construireTranche(BigDecimal.valueOf(montant),taux);
    }

    protected TrancheBareme construireTranche(BigDecimal montantOuTaux) {
        Intervalle.Cons cons = new Intervalle.Cons().de(montantMaxPrecedent);
        if (fermeAGauche) {
            cons = cons.inclus();
        } else {
            cons = cons.exclus();
        }
        Intervalle intervalle = cons.aPlusInfini().intervalle();
        return new TrancheBareme(intervalle,montantOuTaux);
    }

    public final ConstructeurBareme tranche(int montant, BigDecimal taux) {
        Intervalle intervalle = construireIntervalle(BigDecimal.valueOf(montant));
        if (tranches.size() > 0) {
            TrancheBareme derniereTranche = tranches.get(tranches.size()-1);
            if (0 == derniereTranche.getTauxOuMontant().compareTo(taux)) {
                Intervalle inter = intervalle.union(derniereTranche.getIntervalle());
                tranches.set(tranches.size()-1,construireTranche(inter,taux));
                montantMaxPrecedent = BigDecimal.valueOf(montant);
            } else {
                tranches.add(construireTranche(montant,taux));
            }
        } else {
            tranches.add(construireTranche(montant,taux));
        }
        return this;
    }


    public final ConstructeurBareme tranche(int montant, String taux) {
        return this.tranche(montant, BigDecimalUtil.parseTaux(taux));
    }

    public final ConstructeurBareme tranche(int montantImposable, int montant) {
        return this.tranche(montantImposable, new BigDecimal(montant));
    }

    public final ConstructeurBareme derniereTranche(BigDecimal taux) {
        tranches.add(construireTranche(taux));
        return this;
    }

    public final ConstructeurBareme derniereTranche(int montant) {
        return derniereTranche(new BigDecimal(montant));
    }

    public final ConstructeurBareme derniereTranche(String taux) {
        return derniereTranche(BigDecimalUtil.parseTaux(taux));
    }

    protected void completerBareme(BaremeParTranche bareme) {
        bareme.setTranches(tranches);
        bareme.setTypeArrondiSurChaqueTranche(typeArrondiSurChaqueTranche);
        bareme.setTypeArrondiGlobal(typeArrondiGlobal);
        bareme.setSeuil(seuil);
    }

    public BaremeParTranche construireBaremeParTranche() {
        BaremeConstantParTranche bareme = new BaremeConstantParTranche();
        completerBareme(bareme);
        return bareme;
    }

    public BaremeTauxEffectif construireBaremeTauxEffectif() {
        BaremeTauxEffectifConstantParTranche bareme = new BaremeTauxEffectifConstantParTranche();
        completerBareme(bareme);
        return bareme;

    }

}
