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

import org.impotch.util.BigDecimalUtil;
import org.impotch.util.TypeArrondi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstructeurBareme {

    private final TypeBareme type;

    private ModeCalcul mode = ModeCalcul.CONSTANT;

    protected List<TrancheBareme> tranches = new ArrayList();

    private TypeArrondi typeArrondiSurChaqueTranche = TypeArrondi.CENTIEME_LE_PLUS_PROCHE;
    private TypeArrondi typeArrondiGlobal = TypeArrondi.CENTIEME_LE_PLUS_PROCHE;
    private BigDecimal seuil;

    private final ConstructeurTranche consTranche = new ConstructeurTranche();

    private BigDecimal valeur = BigDecimal.ZERO;

    /**
     * Permet la construction des barèmes constants par tranche que
     * ce soit la valeur ou le taux qui est constant.
     * Un barème par tranche est une suite d'intervalle qui ne s'intersectent pas.
     * Par défaut, les intervalles sont ouverts à gauche et fermés à droite c.-à-d.
     * que l'intervalle dont le début est 3 et la fin est 4 ne comprend pas 3 mais comprend 4.
     */
    public ConstructeurBareme(TypeBareme type) {
        super();
        this.type = type;
    }

    /**
     * Avec une liste de tranche prédéfinie. Ne devrait pas être utilisé très souvent !
     * @param tranches
     */
    public ConstructeurBareme(TypeBareme type, List<TrancheBareme> tranches) {
        this(type);
        this.tranches = tranches;
    }

    public static ConstructeurBareme unBareme() {
        return new ConstructeurBareme(TypeBareme.BAREME);
    }

    public static ConstructeurBareme unBaremeATauxEffectif() {
        return new ConstructeurBareme(TypeBareme.BAREME_A_TAUX_EFFECTIF);
    }

    public static ConstructeurBareme unBaremeATauxMarginal(BigDecimal demarrage) {
        ConstructeurBareme cons = new ConstructeurBareme(TypeBareme.BAREME_A_TAUX_MARGINAL);
        cons.jusqua(demarrage).valeur(0).taux("0");
        return cons;
    }

    public static ConstructeurBareme unBaremeATauxMarginal() {
        return unBaremeATauxMarginal(BigDecimal.ZERO);
    }

    public static ConstructeurBareme unBaremeATauxMarginal(BaremeParTranche bareme) {
        return new ConstructeurBareme(TypeBareme.BAREME_A_TAUX_MARGINAL)
                .setTranches(bareme.getTranches())
                .typeArrondiGlobal(bareme.getTypeArrondiGlobal())
                .typeArrondiSurChaqueTranche(bareme.getTypeArrondiSurChaqueTranche())
                .seuil(bareme.getSeuil());
    }

    public BaremeParTranche construire() {
        BaremeParTranche bareme = null;
        switch (type) {
            case BAREME -> bareme = new BaremeConstantParTranche();
            case BAREME_A_TAUX_EFFECTIF ->
                    bareme = ModeCalcul.CONSTANT == mode ? new BaremeTauxEffectifConstantParTranche() : new BaremeTauxEffectifLineaireParTranche();
            case BAREME_A_TAUX_MARGINAL -> bareme = new BaremeTauxMarginalConstantParTranche();
        }
        completerBareme(bareme);
        return bareme;
    }


    // ********************** Construction des intervalles *****************

    private void ajouterTranchePrecedente() {
        TrancheBareme tranche = consTranche.construire();
        if (null != tranche) ajouterTranche(tranche);
    }

    public ConstructeurBareme jusqua(BigDecimal borne) {
        consTranche.jusqua(borne);
        return this;
    }

    public ConstructeurBareme jusqua(int borne) {
        return jusqua(BigDecimal.valueOf(borne));
    }

    public ConstructeurBareme puisJusqua(int borne) { return puisJusqua(BigDecimal.valueOf(borne)); }


    public ConstructeurBareme puisJusqua(BigDecimal borne) {
        ajouterTranchePrecedente();
        consTranche.puisJusqua(borne);
        return this;
    }

    public ConstructeurBareme de(BigDecimal borne) {
        ajouterTranchePrecedente();
        consTranche.de(borne);
        return this;
    }

    public ConstructeurBareme de(int borne) {
        return de(BigDecimal.valueOf(borne));
    }

    public ConstructeurBareme a(BigDecimal borne) {
        consTranche.a(borne);
        return this;
    }

    public ConstructeurBareme a(int borne) {
        return a(BigDecimal.valueOf(borne));
    }


    public ConstructeurBareme plusDe(BigDecimal borne) {
        ajouterTranchePrecedente();
        consTranche.plusDe(borne);
        return this;
    }

    public ConstructeurBareme plusDe(int borne) {
        return plusDe(BigDecimal.valueOf(borne));
    }


    public ConstructeurBareme valeur(BigDecimal valeur) {
        consTranche.valeur(valeur);
        return this;
    }

    public ConstructeurBareme valeur(int valeur) {
        return valeur(BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme valeur(String valeur) {
        return valeur(BigDecimalUtil.parse(valeur));
    }

    public ConstructeurBareme increment(BigDecimal increment) {
        consTranche.increment(increment);
        return this;
    }


    public ConstructeurBareme increment(String taux) {
        return increment(BigDecimalUtil.parse(taux));
    }


    public ConstructeurBareme taux(String taux) {
        BigDecimal tauxBD = BigDecimalUtil.parseTaux(taux);
        if (TypeBareme.BAREME_A_TAUX_MARGINAL == type) {
            BigDecimal valeurEnDebutDeTranche = this.valeur;
            // TODO PGI recalculer la valeur en fin d'intervalle
            return valeur(valeurEnDebutDeTranche).increment(tauxBD);
        } else {
            return valeur(tauxBD);
        }

    }


    // ********************** Construction des tranches ********************


    public ConstructeurBareme uniqueTranche(BigDecimal valeur) {
        if (TypeBareme.BAREME_A_TAUX_MARGINAL == type) {
            // implicitement, on commence à 0 (évite l'exception)
            plusDe(0);
            increment(valeur);
        } else {
            consTranche.tout();
            valeur(valeur);
        }
        return this;
    }

    public ConstructeurBareme uniqueTranche(String taux) {
        return uniqueTranche(BigDecimalUtil.parseTaux(taux));
    }

    public ConstructeurBareme uniqueTranche(int valeur) {
        return uniqueTranche(BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme premiereTranche(BigDecimal jusqua, BigDecimal taux) {
        jusqua(jusqua);
        if (TypeBareme.BAREME_A_TAUX_MARGINAL == type) {
            increment(taux);
        } else {
            valeur(taux);
        }
        ajouterTranche(consTranche.construire());
        return this;
    }

    public ConstructeurBareme premiereTranche(int jusqua, String taux) {
        return premiereTranche(BigDecimal.valueOf(jusqua), BigDecimalUtil.parseTaux(taux));
    }

    public ConstructeurBareme premiereTranche(int jusqua, int valeur) {
        return premiereTranche(BigDecimal.valueOf(jusqua), BigDecimal.valueOf(valeur));
    }

    public final ConstructeurBareme tranche(BigDecimal de, BigDecimal a, BigDecimal valeur, BigDecimal pente) {
        ajouterTranche(de(de).a(a).valeur(valeur).increment(pente).consTranche.construire());
//        if (TypeBareme.BAREME_A_TAUX_MARGINAL == type) {
//
//        } else {
//            this.mode = ModeCalcul.LINEAIRE;
//            if (tranches.size() > 0) {
//                TrancheBareme derniereTranche = tranches.get(tranches.size() - 1);
//                if (0 == derniereTranche.getValeur().compareTo(valeur)) {
//                    Intervalle inter = intervalle.union(derniereTranche.getIntervalle());
//                    tranches.set(tranches.size() - 1, consTranche.construireTranche(inter, valeur, pente));
//                } else {
//                    ajouterTranche(consTranche.construireTranche(de, a, valeur, pente));
//                }
//            } else {
//                ajouterTranche(consTranche.construireTranche(de, a, valeur, pente));
//            }
//        }
        return this;
    }


    public final ConstructeurBareme tranche(int de, int a, String taux, String tauxEnPlusPar100Francs) {
        return this.tranche(BigDecimal.valueOf(de), BigDecimal.valueOf(a), BigDecimalUtil.parseTaux(taux), BigDecimalUtil.parseTaux(tauxEnPlusPar100Francs).movePointLeft(2));
    }


    public ConstructeurBareme tranche(BigDecimal de, BigDecimal a, BigDecimal taux) {
//        // TODO Reprendre cette partie qui suppose une cosntruction ordonnée des tranches.
        de(de).a(a);
        if (TypeBareme.BAREME_A_TAUX_MARGINAL == type) {
            increment(taux);
        } else {
            valeur(taux);
        }
        ajouterTranche(consTranche.construire());
//        Intervalle intervalle = consInter.construireIntervalle(de, a);
//        if (TypeBareme.BAREME_A_TAUX_EFFECTIF == type) {
//            if (tranches.size() > 0) {
//                TrancheBareme derniereTranche = tranches.get(tranches.size() - 1);
//                if (0 == derniereTranche.getValeur().compareTo(taux)) {
//                    Intervalle inter = intervalle.union(derniereTranche.getIntervalle());
//                    tranches.set(tranches.size() - 1, consTranche.construireTranche(inter, taux));
//                    return this;
//                }
//            }
//            ajouterTranche(consTranche.construireTranche(de, a, taux));
//            return this;
//        }
//        if (TypeBareme.BAREME_A_TAUX_MARGINAL == type) {
//            // TODO
//            return this;
//        }
        return this;
    }

    public ConstructeurBareme tranche(int de, int a, BigDecimal taux) {
        return tranche(BigDecimal.valueOf(de), BigDecimal.valueOf(a), taux);
    }

    public ConstructeurBareme tranche(int de, int a, String taux) {
        return this.tranche(de, a, BigDecimalUtil.parseTaux(taux));
    }

    public ConstructeurBareme tranche(int de, int a, int valeur) {
        return this.tranche(BigDecimal.valueOf(de), BigDecimal.valueOf(a), BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme derniereTranche(BigDecimal depuis, BigDecimal taux) {

        plusDe(depuis);
        if (TypeBareme.BAREME_A_TAUX_MARGINAL == type) {
            increment(taux);
        } else {
            valeur(taux);
        }
//        if (tranches.size() > 0) {
//            TrancheBareme derniereTranche = tranches.get(tranches.size() - 1);
//            if (0 == derniereTranche.getValeur().compareTo(taux)) {
//                Intervalle inter = intervalle.union(derniereTranche.getIntervalle());
//                tranches.set(tranches.size() - 1, consTranche.construireTranche(inter, taux));
//                return this;
//            }
//        }
        ajouterTranche(consTranche.construire());
        return this;
    }

    public ConstructeurBareme derniereTranche(int depuis, int valeur) {
        return derniereTranche(BigDecimal.valueOf(depuis), BigDecimal.valueOf(valeur));
    }

    public ConstructeurBareme derniereTranche(int depuis, String taux) {
        return derniereTranche(BigDecimal.valueOf(depuis), BigDecimalUtil.parseTaux(taux));
    }

    protected void completerBareme(BaremeParTranche bareme) {
        ajouterTranche(consTranche.construire());
        bareme.setTranches(tranches);
        bareme.setTypeArrondiSurChaqueTranche(typeArrondiSurChaqueTranche);
        bareme.setTypeArrondiGlobal(typeArrondiGlobal);
        bareme.setSeuil(seuil);
    }



    private ConstructeurBareme setTranches(List<TrancheBareme> tranches) {
        this.tranches = Collections.unmodifiableList(tranches);
        return this;
    }

    private boolean trancheCompactable(TrancheBareme tranche1, TrancheBareme tranche2) {
        if (null == tranche1) return false;
        return tranche1.compactable(tranche2);
    }

    private TrancheBareme derniereTranche() {
        if (tranches.isEmpty()) return null;
        return tranches.get(tranches.size()-1);
    }

    private BigDecimal obtenirValeurFinTranche(TrancheBareme tranche) {
        Intervalle inter = tranche.getIntervalle();
        if (!inter.estBorneADroite()) {
            return BigDecimal.ZERO;
        }
        return tranche.integre(inter.getFin());
    }

    protected void ajouterTranche(TrancheBareme tranche) {
        // TODO vérifier le non recouvrement des tranches
        // Attention aux performances sur les barèmes avec beaucoup de tranches
        // par exemple les barèmes d'impôt à la source.
        // On pourra avoir une stratégie fonction du nombre de tranche.
        // Afin de permettre la construction parallèle des tranches, il ne faut surtout pas
        // faire l'hypothèse que les tranches sont ordonnées.

        this.valeur = obtenirValeurFinTranche(tranche);
        if(trancheCompactable(derniereTranche(),tranche)) {
            TrancheBareme trancheCompacte = derniereTranche().compacte(tranche);
            tranches.set(tranches.size()-1,trancheCompacte);
        } else {
            tranches.add(tranche);
        }
    }

    // ********************** Construction globales ************************

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
        consTranche.fermeAGauche();
        return this;
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

}
