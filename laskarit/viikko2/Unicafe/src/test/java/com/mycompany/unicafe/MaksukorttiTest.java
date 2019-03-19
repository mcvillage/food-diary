package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void latausKasvattaaArvoaOikein() {
        kortti.lataaRahaa(10);
        assertEquals(20, kortti.saldo());
    }
    
    @Test
    public void rahanottoToimiiKunSaldoRiittaa() {
        kortti.otaRahaa(10);
        assertEquals(0, kortti.saldo());
    }
    
    @Test
    public void rahanottoEiToimiKunSaldoEiRiita() {
        kortti.otaRahaa(11);
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void rahanottoPalauttaaTrueKunOnnistuu() {
        assertTrue(kortti.otaRahaa(5));
    }
    
    @Test
    public void rahanottoPalauttaaFalseKunEpaonnistuu() {
        assertFalse(kortti.otaRahaa(20));
    }
    
    @Test
    public void tulostusToimii() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
}
