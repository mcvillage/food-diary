package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    private Kassapaate kassapaate;
    private Maksukortti maksukortti;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
        maksukortti = new Maksukortti(500);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void rahamaaraAlussaOikein() {
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void lounaidenMaaraAlussaOikein() {
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty() + kassapaate.maukkaitaLounaitaMyyty());
    }
    
    
    /* Edullisesti käteisellä */
    
    
    @Test
    public void kassanRahamaaraKasvaaKunEdullinenOstetaan() {
        kassapaate.syoEdullisesti(500);
        assertEquals(100250, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahanOikeinKunEdullinenOstetaan() {
        assertEquals(250, kassapaate.syoEdullisesti(500));
    }
    
    @Test
    public void maksujenMaaraKasvaaKunEdullisenOstoOnnistuu() {
        kassapaate.syoEdullisesti(500);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1 && kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void maksuPalautetaanJosEdullisenOstoEiOnnistu() {
        assertEquals(249, kassapaate.syoEdullisesti(249));
    }
    
    @Test
    public void myytyjenLounaidenMaaraPysyyJosEdullisenOstoEiOnnistu() {
        kassapaate.syoEdullisesti(249);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0 && kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
    
    
    /* Maukkaasti käteisellä */
    
    
    @Test
    public void kassanRahamaaraKasvaaKunMaukasOstetaan() {
        kassapaate.syoMaukkaasti(500);
        assertEquals(100400, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahanOikeinKunMaukasOstetaan() {
        assertEquals(100, kassapaate.syoMaukkaasti(500));
    }
    
    @Test
    public void maksujenMaaraKasvaaKunMaukkaanOstoOnnistuu() {
        kassapaate.syoMaukkaasti(500);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0 && kassapaate.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void maksuPalautetaanJosMaukkaanOstoEiOnnistu() {
        assertEquals(399, kassapaate.syoMaukkaasti(399));
    }
    
    @Test
    public void myytyjenLounaidenMaaraPysyyJosMaukkaanOstoEiOnnistu() {
        kassapaate.syoMaukkaasti(249);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0 && kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
    
    
    /* Edullisesti kortilla */
    
    
    @Test
    public void kortiltaVeloitetaanKunEdullinenOstetaanKortilla() {
        boolean palautettava = kassapaate.syoEdullisesti(maksukortti);
        assertTrue(maksukortti.saldo() == 250 && palautettava);
    }
    
    @Test
    public void myytyjenLounaidenMaaraKasvaaKunEdullinenOstetaanKortilla() {
        kassapaate.syoEdullisesti(maksukortti);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1 && kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void saldoEiMuutuJosEdullistaOstettaessaSaldoEiRiita() {
        Maksukortti koyhaKortti = new Maksukortti(50);
        boolean palautettava = kassapaate.syoEdullisesti(koyhaKortti);
        assertTrue(koyhaKortti.saldo() == 50 && !palautettava);
    }
    
    @Test
    public void myytyjenLounaidenMaaraEiMuutuKunEdulliseenEiRiitaSaldo() {
        Maksukortti koyhakortti = new Maksukortti(50);
        kassapaate.syoEdullisesti(koyhakortti);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0 && kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void kassassaRahamaaraEiMuutuEdullistaOstettaessa() {
        kassapaate.syoEdullisesti(maksukortti);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    
    /* Maukkaasti kortilla */
    
    
    @Test
    public void kortiltaVeloitetaanKunMaukkaastiOstetaanKortilla() {
        boolean palautettava = kassapaate.syoMaukkaasti(maksukortti);
        assertTrue(maksukortti.saldo() == 100 && palautettava);
    }
    
    @Test
    public void myytyjenLounaidenMaaraKasvaaKunMaukasOstetaanKortilla() {
        kassapaate.syoMaukkaasti(maksukortti);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0 && kassapaate.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void saldoEiMuutuJosMaukastaOstettaessaSaldoEiRiita() {
        Maksukortti koyhaKortti = new Maksukortti(50);
        boolean palautettava = kassapaate.syoMaukkaasti(koyhaKortti);
        assertTrue(koyhaKortti.saldo() == 50 && !palautettava);
    }
    
    @Test
    public void myytyjenLounaidenMaaraEiMuutuKunMaukkaaseenEiRiitaSaldo() {
        Maksukortti koyhakortti = new Maksukortti(50);
        kassapaate.syoMaukkaasti(koyhakortti);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0 && kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void kassassaRahamaaraEiMuutuMaukastaOstettaessa() {
        kassapaate.syoMaukkaasti(maksukortti);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    
    /* Kortin lataus */

    
    @Test
    public void kortilleLadatessaSaldoMuuttuu() {
        kassapaate.lataaRahaaKortille(maksukortti, 250);
        assertEquals(750, maksukortti.saldo());
    }
    
    @Test
    public void kortilleEiVoiLadataNegatiivistaArvoa() {
        kassapaate.lataaRahaaKortille(maksukortti, -500);
        assertEquals(500, maksukortti.saldo());
    }
    
    @Test
    public void kortilleLadattessaKassanRahamaaraKasvaa() {
        kassapaate.lataaRahaaKortille(maksukortti, 250);
        assertEquals(100250, kassapaate.kassassaRahaa());
    }
    
}
