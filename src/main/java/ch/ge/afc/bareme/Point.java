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


/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class Point implements Comparable<Point> {
	
	public static final Point ORIGINE = new Point(BigDecimal.ZERO,BigDecimal.ZERO);
	
	private final BigDecimal abscisse;
	private final BigDecimal ordonnee;
	
	public Point(BigDecimal assiette, BigDecimal impot) {
		this.abscisse = assiette;
		this.ordonnee = impot;
	}

	@Override
	public int compareTo(Point o) {
		return abscisse.compareTo(o.abscisse);
	}

	public BigDecimal getAbscisse() {
		return abscisse;
	}

	public BigDecimal getOrdonnee() {
		return ordonnee;
	}
	
	
}
