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
package ch.ge.afc.bareme;

import java.math.BigDecimal;

import org.impotch.util.TypeArrondi;
import org.impotch.util.math.integration.IntegrationExacte;
import org.impotch.util.math.integration.MethodeIntegration;
import org.impotch.util.math.integration.Primitivable;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class BaremeTauxMarginalIntegrable implements Bareme {

	private Primitivable tauxMarginal;
	private final MethodeIntegration methode = new IntegrationExacte();
	private TypeArrondi typeArrondi = TypeArrondi.CINQ_CTS;
	
	public void setTauxMarginal(Primitivable tauxMarginal) {
		this.tauxMarginal = tauxMarginal;
	}

	public void setTypeArrondi(TypeArrondi typeArrondi) {
		this.typeArrondi = typeArrondi;
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.calcul.bareme.Bareme#calcul(java.math.BigDecimal)
	 */
	@Override
	public BigDecimal calcul(BigDecimal pAssiette) {
		double resultatFlottant = methode.integre(tauxMarginal, 0.0, pAssiette.doubleValue());
		BigDecimal resultat = BigDecimal.valueOf(resultatFlottant);
		return typeArrondi.arrondirMontant(resultat);
	}

}
