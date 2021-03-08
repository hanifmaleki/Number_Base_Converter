package converter.service

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
const val PRECISION_COUNT = 5
class NumeralSystem(source: String, fromBase: Int) {

    private val integralPart: BigDecimal
    private val fractionalPart: BigDecimal
    private val isFractional: Boolean


    init {
        val possibleSignChar = source[0]
        val signIndex = if (possibleSignChar == '+' || possibleSignChar == '-') 0 else -1
        val pointIndex = source.indexOf('.')
        isFractional = pointIndex >= 0

        val intPartLength = if (pointIndex >= 0) pointIndex else source.length
        var i = intPartLength - (signIndex + 1) - 1
        var sum = BigDecimal.ZERO
        for (ch: Char in source) {
            if (ch in listOf('-', '+', '.')) {
                continue
            }
            val multiply = fromBase.toDouble().pow(i) * getValueOf(ch)
            sum = sum.add(BigDecimal.valueOf(multiply))
            i--
        }
        //TODO apply the sign here


        val dAndR = sum.divideAndRemainder(BigDecimal.ONE)
        integralPart = dAndR[0].setScale(PRECISION_COUNT, RoundingMode.HALF_UP)
        fractionalPart = dAndR[1].setScale(PRECISION_COUNT, RoundingMode.HALF_UP)
    }


    private fun getValueOf(ch: Char): Long {
        if (ch <= '9')
            return (ch - '0').toLong()
        return (ch.toUpperCase() - 'A' + 10).toLong()
    }

    private fun BigDecimal.divideAndRemainder(base: Int): Pair<BigDecimal, Char> {
        val divideAndRemainder = this.divideAndRemainder(BigDecimal.valueOf(base.toLong()))
        val remainder = divideAndRemainder[1].toInt()
        val charRemainder = getRemainderInBase(remainder)
        return Pair<BigDecimal, Char>(divideAndRemainder[0], charRemainder)
    }

    private fun getRemainderInBase(remainder: Int): Char {
        if (remainder < 10)
            return '0'.plus(remainder)
        return 'A'.plus(remainder - 10)
    }


    fun calculateRepresentationInBase(toBase: Int): String {
        var converted = calculateIntegerPartInBase(toBase)
        if (isFractional) {
            converted = "$converted.${calculateFractionalPartInBase(toBase)}"
        }
        return converted

    }

    private fun calculateIntegerPartInBase(toBase: Int): String {
        var dividend = integralPart
        var converted = ""
        while (dividend > BigDecimal.ZERO) {
            val (bigInteger, char) = dividend.divideAndRemainder(toBase)
            converted = char + converted
            dividend = bigInteger
        }
        return if (converted != "") converted else "0"
    }

    private fun calculateFractionalPartInBase(toBase: Int): String {
        var multiply = fractionalPart
        var converted = ""
        var counter = PRECISION_COUNT
        while (counter-- > 0) {
            multiply = multiply.multiply(BigDecimal.valueOf(toBase.toLong()))
            val dAndR = multiply.divideAndRemainder(BigDecimal.ONE)
            multiply = dAndR[1]
            val char = getRemainderInBase(dAndR[0].toInt())
            converted += char
        }
        return converted
    }


}