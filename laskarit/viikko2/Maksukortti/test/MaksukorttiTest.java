
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaksukorttiTest {

    private Maksukortti maksukortti;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        maksukortti = new Maksukortti(10);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void konstruktoriAsettaaSaldonOikein() {
        assertEquals("Kortilla on rahaa 10.0 euroa", maksukortti.toString());
    }

    @Test
    public void syoEdullisestiVahentaaSaldoaOikein() {
        maksukortti.syoEdullisesti();

        assertEquals("Kortilla on rahaa 7.5 euroa", maksukortti.toString());
    }

    @Test
    public void syoMaukkaastiVahentaaSaldoaOikein() {
        maksukortti.syoMaukkaasti();

        assertEquals("Kortilla on rahaa 6.0 euroa", maksukortti.toString());
    }

    @Test
    public void syoEdullisestiEiVieSaldoaNegatiiviseksi() {
        maksukortti.syoMaukkaasti();
        maksukortti.syoMaukkaasti();
        maksukortti.syoEdullisesti();

        assertEquals("Kortilla on rahaa 2.0 euroa", maksukortti.toString());
    }

    @Test
    public void kortilleVoiLadataRahaa() {
        maksukortti.lataaRahaa(25);
        assertEquals("Kortilla on rahaa 35.0 euroa", maksukortti.toString());
    }

    @Test
    public void kortinSaldoEiYlitaMaksimiarvoa() {
        maksukortti.lataaRahaa(200);
        assertEquals("Kortilla on rahaa 150.0 euroa", maksukortti.toString());
    }

    @Test
    public void syoMaukkaastiEiVieSaldoaNegatiiviseksi() {
        maksukortti.syoMaukkaasti();
        maksukortti.syoMaukkaasti();
        maksukortti.syoMaukkaasti();

        assertEquals("Kortilla on rahaa 2.0 euroa", maksukortti.toString());
    }

    @Test
    public void kortilleEiVoiLadataNegatiivistaArvoa() {
        maksukortti.lataaRahaa(-25);
        assertEquals("Kortilla on rahaa 10.0 euroa", maksukortti.toString());
    }

    @Test
    public void syoEdullisestiOnnistuuTasarahalla() {
        Maksukortti edullinenKortti = new Maksukortti(2.5);
        edullinenKortti.syoEdullisesti();

        assertEquals("Kortilla on rahaa 0.0 euroa", edullinenKortti.toString());
    }

    @Test
    public void syoMaukkaastiOnnistuuTasarahalla() {
        Maksukortti maukasKortti = new Maksukortti(4.0);
        maukasKortti.syoMaukkaasti();

        assertEquals("Kortilla on rahaa 0.0 euroa", maukasKortti.toString());
    }
}
