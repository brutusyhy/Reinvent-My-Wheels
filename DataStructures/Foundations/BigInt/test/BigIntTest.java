package DataStructures.Foundations.BigInt.test;



import DataStructures.Foundations.BigInt.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.Test;

public class BigIntTest {

    @Test
    public void testDefaultConstructor(){
        BigInt bi = new BigInt();
        assertEquals("0", bi.toString());
    }

    @Test
    public void testConstructorSmallNumber(){
        BigInt bi = new BigInt(999_999_999);
        assertEquals("999999999", bi.toString());
    }

    @Test
    public void testConstructorLargeNumber(){
        BigInt bi = new BigInt(999_999_999_999L);
        assertEquals("999999999999", bi.toString());
    }

    @Test
    public void testConstructorNegativeNumber(){
        BigInt bi = new BigInt(-1);
        assertEquals("-1", bi.toString());
    }

    @Test
    public void testCopyConstructor(){
        BigInt a = new BigInt(12345);
        BigInt b = new BigInt(a);
        assertTrue(a.equals(b));
        assertFalse(a == b);
    }

    @Test
    public void testEqualityNull(){
        BigInt a = new BigInt();
        assertFalse(a.equals(null));
    }

    @Test
    public void testEqualityBetweenIdenticalBigInt(){
        BigInt a = new BigInt(12345);
        BigInt b = a;
        assertTrue(a.equals(b));
    }

    @Test
    public void testEqualityBetweenSameValuedBigInt(){
        BigInt a = new BigInt(12345);
        BigInt b = new BigInt(12345);
        assertTrue(a.equals(b));
        BigInt c = new BigInt(b);
        assertTrue(b.equals(c));
    }

    @Test
    public void testStaticEqualityWithPrimitives(){
        BigInt bi = new BigInt(12345);
        int i = 12345;
        assertTrue(BigInt.isEqual(bi, i));
        assertTrue(BigInt.isEqual(i, bi));
    }

    @Test
    public void testComparisonEqualValues(){
        BigInt a = new BigInt(12345);
        BigInt b = new BigInt(12345);
        assertEquals(0, a.compareTo(b));
        assertEquals(0, b.compareTo(a));
    }

    @Test
    public void testComparisonDifferentSigns(){
        BigInt pos = new BigInt(12345);
        BigInt neg = new BigInt(-12345);
        assertEquals(1, pos.compareTo(neg));
        assertEquals(-1, neg.compareTo(pos));
    }

    @Test
    public void testComparisonPositives(){
        BigInt a = new BigInt(12345);
        BigInt b = new BigInt(12346);
        assertEquals(-1, a.compareTo(b));
        assertEquals(1, b.compareTo(a));
    }

    @Test
    public void testComparisonNegatives(){
        BigInt a = new BigInt(-12345);
        BigInt b = new BigInt(-12346);
        assertEquals(1, a.compareTo(b));
        assertEquals(-1, b.compareTo(a));
    }

    @Test
    public void testComparisonNull(){
        BigInt bi = new BigInt();
        assertThrows(NullPointerException.class, () -> bi.compareTo(null));
    }

    @Test
    public void testStaticComparisonWithPrimitives(){
        BigInt bi = new BigInt(12345);
        int i = -12345;
        assertEquals(-1, BigInt.compare(i, bi));
        assertEquals(1, BigInt.compare(bi, i));
    }

    @Test
    public void testHashConsistency(){
        BigInt bi = new BigInt(12345);
        int hash1 = bi.hashCode();
        int hash2 = bi.hashCode();
        assertTrue(hash1 == hash2);
    }

    @Test
    public void testHashEquality(){
        BigInt a = new BigInt(12345);
        BigInt b = new BigInt(a);
        assertTrue(a.hashCode() == b.hashCode());
    }
}
