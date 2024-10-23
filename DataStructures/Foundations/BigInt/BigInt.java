package DataStructures.Foundations.BigInt;

import java.util.Arrays;
public class BigInt implements Comparable<BigInt>{
    // A single 32-bit int can be used to store up to 9 decimal digits
    // Every block of 10 bit represent 3 digits, with 2 digits wasted 
    private final DigitMap[] arr;

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

    private class DigitMap implements Comparable<DigitMap>{
        // A utility class for converting between bits and digits
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

        public int toDigits(){
            int upperBits = this.map >> 20; // Extract the upper 10 bits
            int middleBits = (this.map >> 10) & 0x3FF; // Extract the middle 10 bits
            int lowerBits = this.map & 0x3FF; // Extract the lower 10 bits
            return upperBits * 1_000_000 + middleBits * 1_000 + lowerBits;
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
        for(DigitMap m : this.arr){
            sb.append(m.toDigits());
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

    // Implementing basic operations as static methods
    // Since BigInts are supposed to be immutable
    public static BigInt add(BigInt a, BigInt b){
        // Handle 0 correctly
        if(a.equals(0)){
            return b;
        } else if (b.equals(0)){
            return a;
        }
        // Check if different signs
        if(a.sign!=b.sign){
            if(a.sign == Sign.NON_NEGATIVE){
                return BigInt.minus(a, b.inverse());
            } else {
                return BigInt.minus(b, a.inverse());
            }
        }
        // Now we know both numbers have the same sign


    }

    public static BigInt minus(BigInt a, BigInt b){
        //
    }

}