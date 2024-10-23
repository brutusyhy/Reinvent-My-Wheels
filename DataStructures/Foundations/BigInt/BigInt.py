"""
Writing data structures in Python turns out to be trickier, precisely because of its dynamic typing.
You cannot leverage multiple dispatching and must do run-time type checks.
Furthermore, a more efficient implementation of digits array would require a smaller element type.
Or fixed-length integers where a chunk of bits map to a chunk of digits.
For 32-bit integers, the optimal chunking seems to be 10 bits (0-1023) for every three digits.
Maybe I should revisit implementing BitInt in python after sorting out what it depends on.
"""

from enum import Enum

class Sign(Enum):
    NON_NEGATIVE = 0
    NEGATIVE = 1

class BigInt:
    digits = []
    sign = Sign.NON_NEGATIVE

    def __init__(self):
        self.digits = []
        self.sign = Sign.NON_NEGATIVE
        
    def _assign_from_int(self, integer: Int):
        if integer < 0:
            integer = -integer
            self.sign = Sign.NEGATIVE
        else:
            self.sign = Sign.NON_NEGATIVE
        self.digits = []
        while integer != 0:
            new_digit = integer % 10
            integer //= 10
            self.digits.insert(0, new_digit)

    def __init__(self, integer: Int | BigInt):
        if isinstance(integer, int): 
            self._assign_from_int(integer)
        elif isinstance(integer, BigInt):
            self.digits = integer.digits.copy()
            self.sign = integer.sign
    
    def __str__(self):
        return "".join([str(digit) for digit in self.digits])

    def _add_same_sign():
        pass

    def _add_bigint(self, bi: BigInt):
        pass

    def _add_int(self, integer: Int):
        return self.__add_bigint(BigInt(integer))

    def __add__(self, other: BigInt | Int):
        if isinstance(other, int):
            return self._add_int(other)
        elif isinstance(other, BigInt):
            return self._add_bigint(other)
        else:
            raise "Only support adding BigInts with int/BigInts"
    



# 1111: 4 bits (15) 1 digits                32/4 = 8        => 16           64/4 = 16
# 1111111 7 bits (127) 2 digits             32/7 * 2 = 8    => 16           64/7 * 2 = 18
# 1111111111 10 bits (1023) 3 digits        32/10 * 3 = 9   => 18           64/10 * 3 = 18   
# 11111111111111 14 bits(16383) 4 digits    32/14 * 4 = 8   => 16           64/14 * 4 = 16
# 11111111111111111 17 bits(131071) 5 digits                                64/17 * 5 = 15
# 20 bits(1048575) 6 digits                                                 64/20 * 6 = 18
# 24 bits (16777215) 7 digits                                               64/24 * 7 = 14
        