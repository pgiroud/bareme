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


/**
 * Created with IntelliJ IDEA.
 * User: patrick
 * Date: 29/09/13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public class BaremeAssert {

    public static BaremeTauxEffectifConstantParTrancheAssert assertThat(BaremeTauxEffectifConstantParTranche actual) {
        return new BaremeTauxEffectifConstantParTrancheAssert(actual);
    }

}
