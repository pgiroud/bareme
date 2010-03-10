package ch.ge.afc.bareme;

import java.math.BigDecimal;

import org.junit.Test;

import ch.ge.afc.util.TypeArrondi;
import static org.junit.Assert.*;

public class TrancheBaremeTest {

	@Test
	public void homothetie() {
		TrancheBareme tranche = new TrancheBareme(new BigDecimal(100),BigDecimal.ONE);
		TrancheBareme homothetique = tranche.homothetie(new BigDecimal(2), TypeArrondi.FRANC);
		assertEquals("Montant imposable",new BigDecimal(200),homothetique.getMontantMaxTranche());
		assertEquals("Taux identique",BigDecimal.ONE,homothetique.getTauxOuMontant());
		
		homothetique = tranche.homothetie(new BigDecimal("1.23456"), TypeArrondi.CT);
		assertEquals("Montant imposable",new BigDecimal("123.46"),homothetique.getMontantMaxTranche());
		assertEquals("Taux identique",BigDecimal.ONE,homothetique.getTauxOuMontant());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void homothetieAvecRapportNull() {
		TrancheBareme tranche = new TrancheBareme(new BigDecimal(100),BigDecimal.ONE);
		tranche.homothetie(null, TypeArrondi.FRANC);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void homothetieAvecRapportZero() {
		TrancheBareme tranche = new TrancheBareme(new BigDecimal(100),BigDecimal.ONE);
		tranche.homothetie(BigDecimal.ZERO, TypeArrondi.FRANC);
	}

	@Test(expected=IllegalArgumentException.class)
	public void homothetieAvecRapportNegatif() {
		TrancheBareme tranche = new TrancheBareme(new BigDecimal(100),BigDecimal.ONE);
		tranche.homothetie(BigDecimal.ONE.negate(), TypeArrondi.FRANC);
	}
	
	
	
}
