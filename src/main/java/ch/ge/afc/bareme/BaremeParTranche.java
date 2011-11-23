/**
 * This file is part of CalculImpotCH.
 *
 * CalculImpotCH is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * CalculImpotCH is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CalculImpotCH.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ge.afc.bareme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.ge.afc.util.BigDecimalUtil;
import ch.ge.afc.util.TypeArrondi;

/**
 * Un barème par tranche est une fonction en escalier c.-à-d. qu'elle est constante
 * sur des intervalles et les intervalles forment une partition de l'ensemble des nombres
 * réels positifs.
 *  
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
abstract class BaremeParTranche implements Bareme {
	
	/**************************************************/
	/****************** Attributs *********************/
	/**************************************************/

	private List<TrancheBareme> tranches = new ArrayList<TrancheBareme>();
	private TypeArrondi typeArrondiSurChaqueTranche;
    private TypeArrondi typeArrondiGlobal;
	private BigDecimal seuil;
    protected boolean montantMaxNonInclus;


    /**************************************************/
    /************* Accesseurs / Mutateurs *************/
    /**************************************************/

	/**
	 * @param tranches the tranches to set
	 */
	public void setTranches(List<TrancheBareme> tranches) {
		this.tranches = tranches;
	}

	/**
	 * @param typeArrondi the typeArrondi to set
	 */
	public void setTypeArrondiSurChaqueTranche(TypeArrondi typeArrondi) {
		this.typeArrondiSurChaqueTranche = typeArrondi;
	}

	/**
	 * @param seuil the seuil to set
	 */
	protected void setSeuil(BigDecimal seuil) {
		this.seuil = seuil;
	}

	/**
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
	
    public void setMontantMaxNonInclus() {
        montantMaxNonInclus = true;
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

	public void ajouterTranche(BigDecimal montantImposable, BigDecimal montant) {
		getTranches().add(new TrancheBareme(montantImposable,montant));
	}
	
	public void ajouterDerniereTranche(BigDecimal montant) {
		getTranches().add(new TrancheBareme.DerniereTrancheBareme(montant));
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.calcul.impot.bareme.Bareme#calcul(java.math.BigDecimal)
	 */
	@Override
	public BigDecimal calcul(BigDecimal assiette) {
		BigDecimal resultat = calculSansSeuil(assiette);
		if (null != seuil && 0 < seuil.compareTo(resultat)) return BigDecimalUtil.ZERO_2_DECIMALES;
		return resultat;
	}

	/**
	 * Retourne la valeur du barème sans tenir compte d'un éventuel montant minimum.
	 * @param assiette l'assiette fiscal.
	 * @return le montant fournis par le barème.
	 */
	protected abstract BigDecimal calculSansSeuil(BigDecimal assiette);

	
	
}
