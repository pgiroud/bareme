package ch.ge.afc.bareme;


import java.math.BigDecimal;

import org.junit.Test;

import ch.ge.afc.util.TypeArrondi;

import static org.junit.Assert.*;

public class BaremeDiscretiseEtInterpolationLineaireTest {

	private final static BigDecimal MILLE = new BigDecimal(1000);
	private final static BigDecimal CENT = new BigDecimal(100);
	
	
	@Test
	public void uneSeuleTranche() {
		BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
		bareme.setTypeArrondi(TypeArrondi.FRANC);
		bareme.ajouterPointDiscretisation(BigDecimal.ZERO,BigDecimal.ZERO);
		bareme.ajouterPointDiscretisation(MILLE, MILLE);
		bareme.setDefiniAvantBorneInf(true);
		bareme.setDefiniApresBorneSup(true);
		
		// Test avec montant inclus strictement sur la tranche
		BigDecimal resultat = bareme.calcul(CENT);
		assertEquals("Une seule tranche avec pente 1 et montant dans tranche",CENT,resultat);
		// Test sur borne inférieure
		resultat = bareme.calcul(BigDecimal.ZERO);
		assertEquals("Une seule tranche borne inférieure",BigDecimal.ZERO,resultat);
		// Test sur montant < borne inférieure
		resultat = bareme.calcul(new BigDecimal(-1000));
		assertEquals("Une seule tranche avec montant < borne inférieure",BigDecimal.ZERO,resultat);
		// Test sur borne supérieure
		resultat = bareme.calcul(MILLE);
		assertEquals("Une seule tranche borne supérieure",MILLE,resultat);
		// Test sur montant > borne supérieure
		resultat = bareme.calcul(new BigDecimal(1000000));
		assertEquals("Une seule tranche avec montant > borne supérieure",MILLE,resultat);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void apresDernierTrancheNonAcceptable() {
		BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
		bareme.setTypeArrondi(TypeArrondi.FRANC);
		bareme.ajouterPointDiscretisation(BigDecimal.ZERO,BigDecimal.ZERO);
		bareme.ajouterPointDiscretisation(MILLE, MILLE);
		// Test sur borne supérieure
		BigDecimal resultat = bareme.calcul(MILLE);
		assertEquals("Une seule tranche borne supérieure",MILLE,resultat);
		// Test sur montant > borne supérieure
		resultat = bareme.calcul(new BigDecimal(1001));
		fail("Le barème n'étant pas permissif, une exception aurait dû être levée !!");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void avantPremiereTrancheNonAcceptable() {
		BaremeDiscretiseEtInterpolationLineaire bareme = new BaremeDiscretiseEtInterpolationLineaire();
		bareme.setTypeArrondi(TypeArrondi.FRANC);
		bareme.ajouterPointDiscretisation(BigDecimal.ZERO,BigDecimal.ZERO);
		bareme.ajouterPointDiscretisation(MILLE, MILLE);
		// Test sur borne inférieure
		BigDecimal resultat = bareme.calcul(BigDecimal.ZERO);
		assertEquals("Une seule tranche borne inférieure",BigDecimal.ZERO,resultat);
		// Test sur montant < borne inférieure
		resultat = bareme.calcul(new BigDecimal(-1));
		fail("Le barème n'étant pas permissif, une exception aurait dû être levée !!");
	}
	
}
