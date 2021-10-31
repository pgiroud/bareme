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

public class ConstructeurBareme {

    protected List<TrancheBareme> tranches = new ArrayList();

    private TypeArrondi typeArrondiSurChaqueTranche = TypeArrondi.CT;
    private TypeArrondi typeArrondiGlobal = TypeArrondi.CT;
    private BigDecimal seuil;
    private boolean fermeAGauche = false;

    private BigDecimal borneInferieure = null;
    private BigDecimal borneSuperieure = null;

    /**
     * Permet la construction des barèmes constants par tranche que
     * ce soit la valeur ou le taux qui est constant.
     * Un barème par tranche est une suite d'intervalle qui ne s'intersectent pas.
     * Par défaut, les intervalles sont ouverts à gauche et fermés à droite c.-à-d.
     * que l'intervalle dont le début est 3 et la fin est 4 ne comprend pas 3 mais comprend 4.
     */
    public ConstructeurBareme() {
        super();
    }

    /**
     * Avec une liste de tranche prédéfinie. Ne devrait pas être utilisé très souvent !
     * @param tranches
     */
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

    protected TypeArrondi getTypeArrondiSurChaqueTranche() {
        return typeArrondiSurChaqueTranche;
    }

    protected TypeArrondi getTypeArrondiGlobal() {
        return typeArrondiGlobal;
    }

    protected BigDecimal getSeuil() {
        return seuil;
    }

    public ConstructeurBareme fermeAGauche() {
        this.fermeAGauche = true;
        return this;
    }

    protected void ajouterTranche(TrancheBareme tranche) {
        // TODO vérifier le non recouvrement des tranches
        // Attention aux performances sur les barèmes avec beaucoup de tranches
        // par exemple les barèmes d'impôt à la source.
        // On pourra avoir une stratégie fonction du nombre de tranche.
        // Afin de permettre la construction parallèle des tranches, il ne faut surtout pas
        // faire l'hypothèse que les tranches sont ordonnées.
        tranches.add(tranche);
    }

    protected Intervalle construirePremierIntervalle(BigDecimal jusqua) {
        Intervalle.Cons cons = new Intervalle.Cons().deMoinsInfini().a(jusqua);
        if (fermeAGauche) {
            cons = cons.exclus();
        } else {
            cons = cons.inclus();
        }
        return cons.intervalle();
    }

    protected Intervalle construireIntervalle(BigDecimal de, BigDecimal a) {
        Intervalle.Cons cons = new Intervalle.Cons();
        if (null == de) {
            cons = cons.deMoinsInfini();
            if (fermeAGauche) {
                cons = cons.a(a).exclus();
            } else {
                cons = cons.a(a).inclus();
            }
        } else {
            if (fermeAGauche) {
                cons = cons.de(de).inclus()
                        .a(a).exclus();
            } else {
                cons = cons.de(de).exclus()
                        .a(a).inclus();
            }
        }
        return cons.intervalle();
    }

    protected Intervalle construireDernierIntervalle(BigDecimal depuis) {
        Intervalle.Cons cons = new Intervalle.Cons().de(depuis);
        if (fermeAGauche) {
            cons = cons.inclus();
        } else {
            cons = cons.exclus();
        }
        return cons.aPlusInfini().intervalle();
    }

    /**
     * Cette méthode sera surchargée pour spécialiser les tranches de barèmes.
     * Par défaut, on utilise la tranche avec montant ou taux constant.
     * @param inter L'intervalle délimitant la tranche
     * @param montantOuTaux La valeur du barème pour tous les éléments appartenant à l'intervalle.
     * @return Une tranche de barème qui pourra être ajoutée à la liste des tranches du barèmes.
     */
    protected TrancheBareme construireTranche(Intervalle inter, BigDecimal montantOuTaux) {
        return new TrancheBareme(inter,montantOuTaux);
    }

    private TrancheBareme construireUniqueTranche(BigDecimal valeur) {
        return construireTranche(Intervalle.TOUT,valeur);
    }

    private TrancheBareme construirePremiereTranche(BigDecimal jusqua, BigDecimal montantOuTaux) {
        Intervalle inter = construirePremierIntervalle(jusqua);
        return construireTranche(inter,montantOuTaux);
    }

    private TrancheBareme construireDerniereTranche(BigDecimal depuis, BigDecimal montantOuTaux) {
        Intervalle inter = construireDernierIntervalle(depuis);
        return construireTranche(inter,montantOuTaux);
    }

    private TrancheBareme construireTranche(BigDecimal de, BigDecimal a, BigDecimal montantOuTaux) {
        Intervalle inter = construireIntervalle(de,a);
        return construireTranche(inter,montantOuTaux);
    }

    public ConstructeurBareme de(BigDecimal borneInferieure) {
        // TODO jeter IllegalStateException
        this.borneInferieure = borneInferieure;
        return this;
    }

    public ConstructeurBareme de(int borneInferieure) {
        return de(BigDecimal.valueOf(borneInferieure));
    }

    public ConstructeurBareme a(BigDecimal borneSuperieure) {
        if (null == borneInferieure) throw new IllegalStateException("La méthode 'a' doit être précédée de la méthode 'de'.");
        this.borneSuperieure = borneSuperieure;
        return this;
    }

    public ConstructeurBareme a(int borneSuperieure) {
        return a(BigDecimal.valueOf(borneSuperieure));
    }

    public ConstructeurBareme jusqua(BigDecimal borneSuperieure) {
        if (null != borneInferieure) throw new IllegalStateException("La méthode 'jusqua' ne peut être " +
                "utilisée qu'avec le premier intervalle sans borne inférieure !");
        this.borneSuperieure = borneSuperieure;
        return this;
    }

    public ConstructeurBareme jusqua(int borneSuperieure) {
        return jusqua(BigDecimal.valueOf(borneSuperieure));
    }

    public ConstructeurBareme aPartirDe(BigDecimal borneInferieure) {
        if (null != borneSuperieure) throw new IllegalStateException("La méthode 'aPartirDe' ne peut être " +
                "utilisée qu'avec le dernier intervalle sans borne supérieure !");
        this.borneInferieure = borneInferieure;
        return this;
    }

    public ConstructeurBareme aPartirDe(int borneInferieure) {
        return aPartirDe(BigDecimal.valueOf(borneInferieure));
    }

    public ConstructeurBareme tauxOuValeur(BigDecimal tauxOuValeur) {
        ConstructeurBareme cons = null;
        if (null == borneInferieure) {
            if (null == borneSuperieure) {
                cons = uniqueTranche(tauxOuValeur);
            } else {
                cons = premiereTranche(borneSuperieure,tauxOuValeur);
            }
        } else {
            if (null == borneSuperieure) {
                cons = derniereTranche(borneInferieure,tauxOuValeur);
            } else {
                cons = tranche(borneInferieure,borneSuperieure,tauxOuValeur);
            }
        }
        borneInferieure = null;
        borneSuperieure = null;
        return cons;
    }

    public ConstructeurBareme taux(String taux) {
        return tauxOuValeur(BigDecimalUtil.parseTaux(taux));
    }

    public ConstructeurBareme uniqueTranche(BigDecimal valeur) {
        ajouterTranche(construireUniqueTranche(valeur));
        return this;
    }

    public ConstructeurBareme uniqueTranche(String taux) {
        return uniqueTranche(BigDecimalUtil.parseTaux(taux));
    }

    public ConstructeurBareme uniqueTranche(int valeur) {
        return uniqueTranche(BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme premiereTranche(BigDecimal jusqua, BigDecimal taux) {
        ajouterTranche(construirePremiereTranche(jusqua,taux));
        return this;
    }

    public ConstructeurBareme premiereTranche(int jusqua, String taux) {
        return premiereTranche(BigDecimal.valueOf(jusqua),BigDecimalUtil.parseTaux(taux));
    }

    public ConstructeurBareme premiereTranche(int jusqua, int valeur) {
        return premiereTranche(BigDecimal.valueOf(jusqua),BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme tranche(BigDecimal de, BigDecimal a, BigDecimal taux) {
        // TODO Reprendre cette partie qui suppose une cosntruction ordonnée des tranches.
        Intervalle intervalle = construireIntervalle(de,a);
        if (tranches.size() > 0) {
            TrancheBareme derniereTranche = tranches.get(tranches.size()-1);
            if (0 == derniereTranche.getTauxOuMontant().compareTo(taux)) {
                Intervalle inter = intervalle.union(derniereTranche.getIntervalle());
                tranches.set(tranches.size()-1,construireTranche(inter,taux));
            } else {
                ajouterTranche(construireTranche(de,a,taux));
            }
        } else {
            ajouterTranche(construireTranche(de,a,taux));
        }
        return this;
    }

    public ConstructeurBareme tranche(int de, int a, BigDecimal taux) {
        return tranche(BigDecimal.valueOf(de), BigDecimal.valueOf(a),taux);
    }

    public ConstructeurBareme tranche(int de, int a, String taux) {
        return this.tranche(de,a, BigDecimalUtil.parseTaux(taux));
    }

    public ConstructeurBareme tranche(int de, int a, int valeur) {
        return this.tranche(BigDecimal.valueOf(de), BigDecimal.valueOf(a), BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme derniereTranche(BigDecimal depuis, BigDecimal taux) {
        ajouterTranche(construireDerniereTranche(depuis,taux));
        return this;
    }

    public ConstructeurBareme derniereTranche(int depuis,int valeur) {
        return derniereTranche(BigDecimal.valueOf(depuis),BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme derniereTranche(int depuis, String taux) {
        return derniereTranche(BigDecimal.valueOf(depuis),BigDecimalUtil.parseTaux(taux));
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

    public BaremeTauxEffectifConstantParTranche construireBaremeTauxEffectifConstantParTranche() {
        BaremeTauxEffectifConstantParTranche bareme = new BaremeTauxEffectifConstantParTranche();
        completerBareme(bareme);
        return bareme;

    }

}
