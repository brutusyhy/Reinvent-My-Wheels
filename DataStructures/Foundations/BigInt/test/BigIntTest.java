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
    public void testConstructorZero(){
        BigInt bi = new BigInt(0);
        assertEquals("0", bi.toString());
        assertTrue(bi.equals(new BigInt()));
    }

    @Test
    public void testCopyConstructor(){
        BigInt a = new BigInt(12345);
        BigInt b = new BigInt(a);
        assertTrue(a.equals(b));
        assertFalse(a == b);
    }

    @Test
    public void testToStringLong(){
        BigInt a = new BigInt(10_000_000_000L);
        assertEquals("10000000000", a.toString());
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

    @Test
    public void testAddZero(){
        BigInt bi = new BigInt(12345);
        BigInt zero = new BigInt(0);
        assertTrue(bi.equals(BigInt.add(bi, zero)));
        assertTrue(bi.equals(bi.add(zero)));
        int zeroInt = 0;
        assertTrue(bi.equals(BigInt.add(bi, zeroInt)));
        assertTrue(bi.equals(bi.add(zeroInt)));
    }

    @Test
    public void testAddSameLengthWithoutCarrying(){
        BigInt a = new BigInt(12345);
        BigInt b = new BigInt(54321);
        BigInt c = new BigInt(66666);
        assertTrue(a.add(b).equals(c));
    }

    @Test
    public void testAddSameLengthWithCarrying(){
        BigInt a = new BigInt(999_999_999);
        BigInt b = new BigInt(1);
        BigInt c = new BigInt(1_000_000_000);
        assertTrue(a.add(b).equals(c));
    }

    @Test
    public void testAddDifferentLength(){
        BigInt a = new BigInt(1_000_000_001);
        BigInt b = new BigInt(999_999_999);
        BigInt c = new BigInt(2_000_000_000);
        assertTrue(a.add(b).equals(c));
    }

    @Test
    public void testAddNegativeNumbers(){
        BigInt a = new BigInt(-1);
        BigInt b = new BigInt(-2);
        BigInt c = new BigInt(-3);
        assertTrue(a.add(b).equals(c));
    }

    @Test
    public void testAddDifferentSign(){
        BigInt a = new BigInt(1);
        BigInt b = new BigInt(-1);
        assertTrue(a.add(b).equals(BigInt.ZERO));
        assertTrue(b.add(a).equals(BigInt.ZERO));
    }

    @Test
    public void testMinusDifferentSigns(){
        BigInt a = new BigInt(1);
        BigInt b = new BigInt(-1);
        BigInt c = new BigInt(2);
        assertTrue(a.minus(b).equals(c));
    }

    @Test
    public void testMinusNormal(){
        BigInt a = new BigInt(3);
        BigInt b = new BigInt(2);
        assertTrue(a.minus(b).equals(BigInt.ONE));
    }

    @Test
    public void testMinusReversed(){
        BigInt a = new BigInt(2);
        BigInt b = new BigInt(3);
        BigInt c = new BigInt(-1);
        assertTrue(a.minus(b).equals(c));
    }
    
    @Test
    public void testMinusDifferentLength(){
        BigInt a = new BigInt(1_000_000_001);
        BigInt b = new BigInt(1);
        BigInt c = new BigInt(1_000_000_000);
        assertTrue(a.minus(b).equals(c));
    }

    @Test
    public void testMinusWithCarrying(){
        BigInt a = new BigInt(1_000_000_000);
        BigInt b = new BigInt(999_999_999);
        assertTrue(a.minus(b).equals(BigInt.ONE));
    }

    @Test
    public void testMultiplyZero(){
        BigInt a = new BigInt(12345);
        assertTrue(a.multiply(0).equals(BigInt.ZERO));
    }

    @Test
    public void testMultiplyOne(){
        BigInt a = new BigInt(12345);
        assertTrue(a.multiply(BigInt.ONE).equals(a));
    }

    @Test
    public void testMultiplyShort(){
        BigInt a = new BigInt(11111);
        BigInt b = new BigInt(123454321);
        assertTrue(a.multiply(a).equals(b));
    }

    @Test
    public void testMultiplyLong(){
        BigInt a = new BigInt(10);
        BigInt b = new BigInt(1_000_000_000);
        BigInt c = new BigInt(10_000_000_000L);
        assertTrue(a.multiply(b).equals(c));

    }
    @Test
    public void testMultiplyVeryLong(){
        BigInt a = new BigInt(10_000_000_000L); // 10^10
        BigInt b = new BigInt(a);
        b = b.multiply(a);
        b = b.multiply(a);
        b = b.multiply(a);
        b = b.multiply(a); // 10^50
        assertEquals("1"+"0".repeat(50), b.toString());
        BigInt c = new BigInt(111_111_111);
        assertEquals("12345678987654321", c.multiply(c).toString());


    }
    
}
