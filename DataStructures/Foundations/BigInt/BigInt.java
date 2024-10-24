package DataStructures.Foundations.BigInt;

import java.util.Arrays;

import DataStructures.Foundations.BigInt.test.BigIntTest;
public class BigInt implements Comparable<BigInt>{
    // A single 32-bit int can be used to store up to 9 decimal digits
    // Every block of 10 bit represent 3 digits, with 2 digits wasted 
    private final DigitMap[] arr;

    public static final BigInt ZERO = new BigInt(0);
    public static final BigInt ONE = new BigInt(1);

    private enum Sign{
        NON_NEGATIVE,
        NEGATIVE
    }
    public static Sign changeSign(Sign oldSign){
        if (oldSign == Sign.NON_NEGATIVE){
            return Sign.NEGATIVE;
        } else {
            return Sign.NON_NEGATIVE;
        }
    }


    private final Sign sign;

    private static class DigitMap implements Comparable<DigitMap>{
        // A utility class for converting between bits and digits
        public static final int MAX_VALUE = 999_999_999;

        private final int map;
        
        public DigitMap(int digits){
            int tempMap = 0;
            //Map the lower 3 digits
            tempMap |= digits % 1000;
            digits /= 1000;
            //Map the middle 3 digits
            tempMap |= (digits % 1000) << 10;
            digits /= 1000;
            //Map the upper 3 digits
            tempMap |= (digits % 1000) << 20;
            this.map = tempMap;
        }
        public int[] split(){
            int[] result = new int[3];
            result[0] = this.map >> 20; // Extract the upper 10 bits
            result[1] = (this.map >> 10) & 0x3FF; // Extract the middle 10 bits
            result[2] = this.map & 0x3FF; // Extract the lower 10 bits
            
            return result;
        }

        public int toDigits(){
            int[] splitBits = this.split();
            return splitBits[0] * 1_000_000 + splitBits[1] * 1_000 + splitBits[2];
        }

        public boolean equals(Object o){
            if(o == this){
                return true;
            }
            if(!(o instanceof DigitMap)){
                return false;
            }
            DigitMap other = (DigitMap) o;
            return other.map == this.map;
        }

        public int compareTo(DigitMap o){
            if(this.map > o.map){
                return 1;
            } else if(this.map == o.map){
                return 0;
            } else {
                return -1;
            }
        }
        
        @Override
        public int hashCode(){
            return map;
        }
        
    }

    public BigInt(){
        this.arr = new DigitMap[1];
        this.arr[0] = new DigitMap(0);
        this.sign = Sign.NON_NEGATIVE;
    }
    
    public BigInt(BigInt other){
        this.arr = other.arr.clone();
        this.sign = other.sign;
    }

    private BigInt(BigInt other, Sign sign){
        this.arr = other.arr.clone();
        this.sign = sign;
    }

    private BigInt(DigitMap[] arr, Sign sign){
        this.arr = arr.clone();
        this.sign = sign;
    }

    public BigInt(long other){

        boolean isMin = other == Long.MIN_VALUE;
        boolean isNegative = other < 0;
        // TODO: add overflow check for flipping sign
        if(isMin){
            
        }
        if (isNegative){
            other*=-1;
            this.sign = Sign.NEGATIVE;
        } else {
            this.sign = Sign.NON_NEGATIVE;
        }



        //Initialize arr length
        int len = 0;
        long temp = other;
        while (temp != 0){
            temp /= 1_000_000_000;
            len+=1;
        }
        // Ensure that BigInt(0) is equal to BigInt()
        if(len == 0){
            len=1;
        }
        this.arr = new DigitMap[len];

        //Map long to DigitMap[]
        for(int i = this.arr.length-1; i>=0; i--){
            //Extract the lowest 9 digits
            int block = (int)(other % 1_000_000_000);
            this.arr[i] = new DigitMap(block);
            other /= 1_000_000_000;
        }
    }
    
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        if(this.sign == Sign.NEGATIVE){
            sb.append("-");
        }
        sb.append(this.arr[0].toDigits());
        if(this.arr.length == 1){
            return sb.toString();
        }
        for (int i = 1; i < this.arr.length; i++){
            String value = String.valueOf(this.arr[i].toDigits());
            int paddingZeros = 9-value.length();
            if(paddingZeros>0){
                sb.append("0".repeat(paddingZeros));
            }
            sb.append(value);
        }
        return sb.toString();
        
    }

    @Override
    public boolean equals(Object o){
        /**
         * The equals method only supports comparison with another BigInt
         * For comparison between a primitive type and BigInt
         * Use the static method BigInt.isEqual()
         */
        if (o == this){
            return true;
        }
        if (!(o instanceof BigInt)){
            return false;
        }

        BigInt other = (BigInt) o;

        return this.sign == other.sign && Arrays.equals(this.arr, other.arr);
    }

    public static boolean isEqual(long i, BigInt bi){
        return bi.equals(new BigInt(i));
    }

    public static boolean isEqual(BigInt bi, long i){
        return bi.equals(new BigInt(i));
    }

    @Override
    public int compareTo(BigInt o) throws NullPointerException{
        /**
         * compareTo only supports comparison with other BigInt
         * To compare BigInt with primitives, use the static method
         * BigInt.compare(a, b)
         */
        // e.compareTo(null) should throw a NullPointerException
        if(o == null){
            throw new NullPointerException();
        }
        // Non-negative always larger than negative
        if (this.sign!=o.sign){
            return this.sign == Sign.NON_NEGATIVE? 1: -1;
        }
        int direction = this.sign == Sign.NON_NEGATIVE? 1: -1;

        // More digits mean larger absolute value
        // If both are negative, one with lower absolute value is larger
        
        if(this.arr.length> o.arr.length){
            return direction;
        } else if (this.arr.length < o.arr.length){
            return -direction;
        }

        for(int i = 0; i < this.arr.length; i++){
            int comparison = this.arr[i].compareTo(o.arr[i]);
            //Checking digits by descending significance
            if(comparison == 1){
                return direction;
            } else if (comparison == -1){
                return -direction;
            }
        }
        return 0;
    }

    public static int compare(long i, BigInt bi){
        return new BigInt(i).compareTo(bi);
    }

    public static int compare(BigInt bi, long i){
        return bi.compareTo(new BigInt(i));
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + (sign == Sign.NON_NEGATIVE ? 1 : -1);
        for(DigitMap m: this.arr){
            result = 31*result + m.hashCode();
        }
        return result;
    }

    public BigInt inverse(){
        // 0's inverse is itself, no negative 0
        if (BigInt.isEqual(this, 0)){
            return this;
        }
        Sign newSign = BigInt.changeSign(this.sign);
        return new BigInt(this, newSign);
    }

    public BigInt abs(){
        return new BigInt(this, Sign.NON_NEGATIVE);
    }
    // Implementing basic operations as static methods
    // Since BigInts are supposed to be immutable
    public static BigInt add(BigInt a, BigInt b){
        // Handle 0 correctly
        if(BigInt.isEqual(a, 0)){
            return b;
        } else if (BigInt.isEqual(b, 0)){
            return a;
        }
        //Check if different signs
        if(a.sign!=b.sign){
            if(a.sign == Sign.NON_NEGATIVE){
                return BigInt.minus(a, b.inverse());
            } else {
                return BigInt.minus(b, a.inverse());
            }
        }
        // Now we know both numbers have the same sign
        // Initialize an accumulator whose size is the same as the larger one
        Sign resultSign = a.sign;
        int length = 0;
        if(a.arr.length>=b.arr.length){
            length = a.arr.length;
        } else {
            length = b.arr.length;
        }
        DigitMap[] temp = new DigitMap[length];
        int carry = 0;
        for(int i = 0; i < length; i++){
            // Matching block index
            int ai = a.arr.length-i-1;
            int bi = b.arr.length-i-1;
            int aVal, bVal;

            // If one BigInt is shorter, ignore missing blocks
            if(ai < 0){
                aVal = 0;
            } else {
                aVal = a.arr[ai].toDigits();
            }

            if(bi < 0){
                bVal = 0;
            } else {
                bVal = b.arr[bi].toDigits();
            }
            int newVal = aVal+bVal+carry;
            carry = 0;
            // Check if there's carrying to a higher block
            if (newVal < DigitMap.MAX_VALUE){
                temp[length-i-1] = new DigitMap(newVal);
            } else {
                newVal-= (DigitMap.MAX_VALUE+1);
                carry = 1;
                temp[length-i-1] = new DigitMap(newVal);
            }
        }
        if(carry == 0){
            return new BigInt(temp, resultSign);
        } else {
            DigitMap[] result = new DigitMap[length+1];
            System.arraycopy(temp, 0, result, 1, length);
            result[0] = new DigitMap(1);
            return new BigInt(result, resultSign);
        }

    }

    public static BigInt add(BigInt bi, long i){
        return BigInt.add(bi, new BigInt(i));
    }

    public static BigInt add(long i, BigInt bi){
        return BigInt.add(bi, new BigInt(i));
    }

    public BigInt add(BigInt bi){
        return BigInt.add(this, bi);
    }

    public BigInt add(long i){
        return BigInt.add(this, new BigInt(i));
    }

    public static BigInt minus(BigInt a, BigInt b){
        // If b < 0, then a-b == a + (-b)
        if (b.compareTo(BigInt.ZERO) == -1){
            return BigInt.add(a, b.inverse());
        }
        // If a == b, then a-b == 0
        if (a.equals(b)){
            return BigInt.ZERO;
        }
        // If abs(a) < abs(b), then a-b = -(b-a)
        // It's easier for a large number to subtract a smaller one
        if (a.abs().compareTo(b.abs()) == -1){
            return BigInt.minus(b, a).inverse();
        }
        
        // At this step, we know that abs(a)>abs(b)
        // Initialize temporary result with length of a.arr
        Sign resultSign = a.sign;
        int length = a.arr.length;
        DigitMap[] temp = new DigitMap[length];
        int carry = 0;
        for(int i = 0; i < length; i++){
            // Matching block index
            int ai = a.arr.length-i-1;
            int bi = b.arr.length-i-1;
            int aVal, bVal;
            
            // Because a must be at least the same length as b
            // We only have to check b
            aVal = a.arr[ai].toDigits();
            if(bi < 0){
                bVal = 0;
            } else {
                bVal = b.arr[bi].toDigits();
            }
            int newVal = aVal-bVal-carry;
            carry = 0;
            // Check if there's carrying from a higher block
            if (newVal >= 0){
                temp[length-i-1] = new DigitMap(newVal);
            } else {
                newVal+= (DigitMap.MAX_VALUE+1);
                carry = 1;
                temp[length-i-1] = new DigitMap(newVal);
            }
        }

        // There couldn't be a carry after the calculation
        // But we need to remove leading maps that have a value of zero
        int newLength = length;
        for(int i = 0; i < length && temp[i].toDigits() == 0; i++){
            newLength -= 1;
        }
        // No leading empty maps
        if (newLength == length){
            return new BigInt(temp, resultSign);
        } else{
            DigitMap[] result = new DigitMap[newLength];
            System.arraycopy(temp, length-newLength, result, 0, newLength);
            return new BigInt(result, resultSign);
        }
    }

    public static BigInt minus(BigInt bi, long i){
        return BigInt.minus(bi, new BigInt(i));
    }

    public static BigInt minus(long i, BigInt bi){
        return BigInt.minus(new BigInt(i), bi);
    }

    public BigInt minus(BigInt other){
        return BigInt.minus(this, other);
    }

    public BigInt minus(long other){
        return BigInt.minus(this, new BigInt(other));
    }

    private BigInt leftShift(int n){
        // Left shift the DigitMap array by n
        // For example, BigInt(1).leftShift == BigInt(1_000_000_000)
        // This method is crucial for multiplication.
        if(n == 0){
            // No need to shift
            return this;
        }
        if(this.equals(BigInt.ZERO)){
            // Zero is always zero
            return BigInt.ZERO;
        }
        int newLength = this.arr.length + n;
        DigitMap[] newArr = new DigitMap[newLength];
        System.arraycopy(this.arr, 0, newArr, 0, this.arr.length);
        for(int i = this.arr.length; i < newLength; i++){
            newArr[i] = new DigitMap(0);
        }
        return new BigInt(newArr, this.sign);
    }

    public static BigInt multiply(BigInt a, BigInt b){
        /**
         * Given two BigInts with their DigitMap arrays:
         * M1M2M3...Mx * N1N2N3...Ny
         * We can consider Mx * Ny first, since other terms only involve left shifting
         * Mx and Ny are both made up of three sections, each representing 3 digits:
         * m1m2m3 = 10^6m1 + 10^3m2 + m3
         * n1n2n3 = 10^6n1 + 10^3n2 + n3
         * m1m2m3 * n1n2n3 is thus
         * 
         * (1) 10^12(m1n1) +
         * (2) 10^9(m1n2 +m2n1) +
         * (3) 10^6(m1n3 + m2n2 + m3n1) +
         * (4) 10^3(m2n3 + m3n2) +
         * (5) (m3n3)
         * 
         * We use (n) to refer to the term within the parenthesis
         * 
         * 
         * We can split it into two BigInt
         * res1 = BigInt(10^3(1) + (2)).leftShift(1)
         * res2 = BigInt(10^6(3) + 10^3(4) + (5))
         * 
         * Because mx and nx <= 999, mx*nx < 10^6
         * Thus, all the multiplications can be safely done using primitive long. 
         * 
         * 
         */
        // 0 * anything is 0
        if(a.equals(BigInt.ZERO) || b.equals(BigInt.ZERO)){
            return BigInt.ZERO;
        }
        // 1 * x = x
        if(a.equals(BigInt.ONE)){
            return b;
        } else if(b.equals(BigInt.ONE)){
            return a;
        }
        // If same sign, the result is positive, negative otherwise
        Sign resultSign = a.sign == b.sign? Sign.NON_NEGATIVE : Sign.NEGATIVE;
        BigInt accumulator = new BigInt(0);
        for(int i = 0; i < a.arr.length; i++){
            for(int j = 0; j < b.arr.length; j++){
                int[] m = a.arr[a.arr.length-1-i].split();
                int[] n = b.arr[b.arr.length-1-j].split();
                long base1 = 1_000 * (m[0] * n[0]) + (m[0]*n[1] + m[1]*n[0]);
                accumulator = accumulator.add(new BigInt(base1).leftShift(1+i+j));
                long base2 = (long)1_000_000 * (m[0]*n[2] + m[1]*n[1] + m[2]*n[0])
                            +1_000 * (m[1]*n[2] + m[2]*n[1])
                            +m[2]*n[2];
                accumulator = accumulator.add(new BigInt(base2).leftShift(i+j));
            }
        }
        if (resultSign == Sign.NEGATIVE){
            return accumulator.inverse();
        } else {
            return accumulator;
        }
    }
    public static BigInt multiply(BigInt bi, long i){
        return BigInt.multiply(bi, new BigInt(i));
    }

    public static BigInt multiply(long i, BigInt bi){
        return BigInt.multiply(bi, new BigInt(i));
    }

    public BigInt multiply(BigInt other){
        return BigInt.multiply(this, other);
    }
    
    public BigInt multiply(long other){
        return BigInt.multiply(this, other);
    }
}