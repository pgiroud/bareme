/**
 * This file is part of impotch/bareme.
 * <p>
 * impotch/bareme is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 * <p>
 * impotch/bareme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with impotch/bareme.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impotch.bareme;


import java.math.BigDecimal;

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.HashCodeBuilder;
import org.impotch.util.TypeArrondi;

/**
 * @author Patrick Giroud
 *
 */
public class TrancheBareme {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

    private final BigDecimal montantImposableMax;
    private final BigDecimal tauxOuMontant;

    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/

    /**
     * Construction d'une tranche de barème.
     * Les seules informations utiles sont le montant maximum d'une tranche
     * et le taux de la tranche.
     * @param montantImposableMax Le montant imposable maximum de la tranche (peut être l'infini)
     * @param tauxOuMontant Le taux ou le montant de la tranche
     */
    protected TrancheBareme(BigDecimal montantImposableMax, BigDecimal tauxOuMontant) {
        super();
        this.montantImposableMax = montantImposableMax;
        this.tauxOuMontant = tauxOuMontant;
    }

    /**************************************************/
    /************* Accesseurs / Mutateurs *************/
    /**************************************************/

    public BigDecimal getTauxOuMontant() {
        return tauxOuMontant;
    }

    /**
     * Retourne le montant imposable maximum de la tranche.
     * @return Le montant imposable maximum de la tranche.
     */
    public BigDecimal getMontantMaxTranche() {
        return montantImposableMax;
    }

    /**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

    /**
     * Une tranche peut être translatée. Translater une tranche consiste à
     * multiplier le montant imposable maximum par le rapport de translation
     * et à l'arrondir.
     * @param pRapport Le rapport de translation (ne peut pas être null et doit être strictement positif).
     * @param typeArrondi L'arrondi à effectuer sur la valeur obtenue. Ne doit pas être null.
     * @return Une nouvelle tranche translatée.
     */
    public TrancheBareme homothetie(BigDecimal pRapport, TypeArrondi typeArrondi) {
        if (!BigDecimalUtil.isStrictementPositif(pRapport))
            throw new IllegalArgumentException("Le rapport d'homothétie '" + pRapport + "' ne peut pas être négatif ou null !!");
        BigDecimal inter = this.getMontantMaxTranche().multiply(pRapport);
        BigDecimal montantImposableMax = typeArrondi.arrondirMontant(inter);
        return new TrancheBareme(montantImposableMax, this.getTauxOuMontant());
    }

    public TrancheBareme homothetieValeur(BigDecimal pRapport, TypeArrondi typeArrondi) {
        if (!BigDecimalUtil.isStrictementPositif(pRapport))
            throw new IllegalArgumentException("Le rapport d'homothétie '" + pRapport + "' ne peut pas être négatif ou null !!");
        BigDecimal inter = this.getTauxOuMontant().multiply(pRapport);
        BigDecimal tauxOuMontant = typeArrondi.arrondirMontant(inter);
        return new TrancheBareme(this.getMontantMaxTranche(), tauxOuMontant);
    }

    public BigDecimal calcul(BigDecimal pMontantImposableMaxTranchePrecedente, BigDecimal pMontantImposable) {
        return this.tauxOuMontant;
    }

    @Override
    public String toString() {
        return montantImposableMax + " " + tauxOuMontant;
    }

    private static boolean safeEquals(BigDecimal premier, BigDecimal second) {
        if (null == premier) return null == second;
        if (null == second) return false;
        return 0 == premier.compareTo(second);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TrancheBareme)) return false;
        TrancheBareme tranche = (TrancheBareme) obj;
        return safeEquals(montantImposableMax, tranche.montantImposableMax)
                && 0 == tauxOuMontant.compareTo(tranche.tauxOuMontant);
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().add(montantImposableMax).add(tauxOuMontant).hash();
    }


    /**************************************************/
    /************** Classes internes ******************/
    /**************************************************/

    public static class DerniereTrancheBareme extends TrancheBareme {

        public DerniereTrancheBareme(BigDecimal tauxOuMontant) {
            super(null, tauxOuMontant);
        }

        public TrancheBareme homothetie(BigDecimal pRapport, TypeArrondi typeArrondi) {
            return this;
        }

        @Override
        public TrancheBareme homothetieValeur(BigDecimal pRapport, TypeArrondi typeArrondi) {
            if (!BigDecimalUtil.isStrictementPositif(pRapport))
                throw new IllegalArgumentException("Le rapport d'homothétie '" + pRapport + "' ne peut pas être négatif ou null !!");
            BigDecimal inter = this.getTauxOuMontant().multiply(pRapport);
            BigDecimal tauxOuMontant = typeArrondi.arrondirMontant(inter);
            return new DerniereTrancheBareme(tauxOuMontant);
        }

        @Override
        public String toString() {
            return "+\u221E " + getTauxOuMontant();
        }


    }


}
