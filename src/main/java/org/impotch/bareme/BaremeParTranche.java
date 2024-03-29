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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.TypeArrondi;

/**
 * Un barème par tranche est une fonction définie sur des tranches c.-à-d. sur des intervalles
 * formant une partition de l'ensemble des nombres réels positifs.
 * En général, la fonction est soit constante soit linéaire sur les tranches.
 *
 * @author Patrick Giroud
 *
 */
public abstract class BaremeParTranche implements Bareme {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

    private List<TrancheBareme> tranches = new ArrayList<>();
    private TypeArrondi typeArrondiSurEntrant = TypeArrondi.UNITE_INF;

    private TypeArrondi typeArrondiSurChaqueTranche;
    private TypeArrondi typeArrondiGlobal;
    private BigDecimal seuil;



    /**************************************************/
    /************* Accesseurs / Mutateurs *************/
    /**************************************************/

    /**
     * @param tranches the tranches to set
     */
    protected void setTranches(List<TrancheBareme> tranches) {
        this.tranches = tranches;
    }

    public List<TrancheBareme> obtenirTranches() {
        // Pas de risque de modifications : les tranches sont immutables
        return new ArrayList<>(getTranches());
    }

    /**
     * @param typeArrondi the typeArrondi to set
     */
    public void setTypeArrondiSurChaqueTranche(TypeArrondi typeArrondi) {
        this.typeArrondiSurChaqueTranche = typeArrondi;
    }

    /**
     * Indique un seuil en dessous duquel l'impôt sera égal à 0.
     * Par exemple, à l'IFD, il existe un seuil à 25 CHF c.-à-d. qu'on ne
     * peut pas avoir à payer 10 CHF d'impôt.
     * @param seuil Le seuil (25 CHF pour l'IFD par exemple)
     */
    protected void setSeuil(BigDecimal seuil) {
        this.seuil = seuil;
    }


    protected void setTypeArrondiSurEntrant(TypeArrondi typeArrondiSurEntrant) {
        this.typeArrondiSurEntrant = typeArrondiSurEntrant;
    }

    /**
     * Retournes les tranches du barème.
     * @return the tranches
     */
    protected List<TrancheBareme> getTranches() {
        return tranches;
    }

    /**
     * @return the typeArrondi
     */
    protected TypeArrondi getTypeArrondiSurChaqueTranche() {
        return typeArrondiSurChaqueTranche;
    }

    protected BigDecimal getSeuil() {
        return seuil;
    }

    protected TypeArrondi getTypeArrondiGlobal() {
        return typeArrondiGlobal;
    }

    public void setTypeArrondiGlobal(TypeArrondi typeArrondiGlobal) {
        this.typeArrondiGlobal = typeArrondiGlobal;
    }

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/


    /**
     * Le calcul est d'abord fait en invoquant #calculSansSeuil(java.math.BigDecimal)
     * puis, si le seuil est défini (voir #setSeuil(java.math.BigDecimal)), on applique le
     * seuillage.
     * @see org.impotch.bareme.Bareme#calcul(java.math.BigDecimal)
     */
    @Override
    public BigDecimal calcul(BigDecimal assiette) {
        BigDecimal entrantArrondi = typeArrondiSurEntrant.arrondirMontant(assiette);
        BigDecimal resultat = calculSansSeuil(entrantArrondi);
        if (null != seuil && 0 < seuil.compareTo(resultat)) return BigDecimalUtil.ZERO_2_DECIMALES;
        return resultat;
    }

    /**
     * Retourne la valeur du barème sans tenir compte d'un éventuel montant minimum.
     * @param assiette l'assiette fiscal.
     * @return le montant fournis par le barème.
     */
    protected abstract BigDecimal calculSansSeuil(BigDecimal assiette);

    protected abstract BaremeParTranche newBaremeParTranche();

    public BaremeParTranche homothetie(BigDecimal taux, TypeArrondi typeArrondi) {
        BaremeParTranche bareme = newBaremeParTranche();
        bareme.setTypeArrondiSurChaqueTranche(this.getTypeArrondiSurChaqueTranche());
        bareme.setTypeArrondiGlobal(this.typeArrondiGlobal);
        bareme.setTranches(getTranches().stream().map(tranche -> tranche.homothetie(taux, typeArrondi)).collect(Collectors.toList()));
        return bareme;
    }

    public BaremeParTranche homothetieValeur(BigDecimal taux, TypeArrondi typeArrondi) {
        throw new RuntimeException();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("BaremeParTranche");
        for (TrancheBareme tranche : tranches) {
            builder.append("\n\t\t").append(tranche);
        }
        builder.append("\n\ttypeArrondiSurChaqueTranche : ").append(typeArrondiSurChaqueTranche);
        builder.append("\n\ttypeArrondiGlobal : ").append(typeArrondiGlobal);
        builder.append("\n\tseuil : ").append(seuil);
        return builder.toString();
    }
}
