package rationals

import java.math.BigInteger

class RationalNotNormalized(numerator: BigInteger, denominator: BigInteger) : Rational {
    override val numerator: BigInteger
    override val denominator: BigInteger

    init {
        require(denominator != BigInteger.ZERO) { "Denominator must not be 0" }
        this.numerator = numerator * denominator.signum().toBigInteger()
        this.denominator = denominator.abs()
    }

    override operator fun plus(other: Rational): Rational {
        val newNumerator = numerator * other.denominator + other.numerator * denominator
        val newDenominator = denominator * other.denominator
        return RationalNotNormalized(newNumerator, newDenominator)
    }

    override operator fun minus(other: Rational): Rational {
        return plus(-other)
    }

    override operator fun times(other: Rational): Rational {
        val newNumerator = numerator * other.numerator
        val newDenominator = denominator * other.denominator
        return RationalNotNormalized(newNumerator, newDenominator)
    }

    override operator fun div(other: Rational): Rational {
        return times(RationalNotNormalized(other.denominator, other.numerator))
    }

    override operator fun unaryMinus(): Rational {
        return RationalNotNormalized(-numerator, denominator)
    }

    override operator fun compareTo(other: Rational): Int {
        return (this - other).signum()
    }

    override operator fun rangeTo(other: Rational): ClosedRange<Rational> {
        return object : ClosedRange<Rational> {
            override val start: Rational
                get() = this@RationalNotNormalized
            override val endInclusive: Rational
                get() = other
        }
    }

    override fun signum(): Int {
        return numerator.signum()
    }

    override fun normalize(): Rational {
        val gcd = numerator.gcd(denominator)
        return RationalNotNormalized(numerator / gcd, denominator / gcd)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        val thisNormalized = normalize()
        val otherNormalized = other.normalize()

        if (thisNormalized.numerator != otherNormalized.numerator) return false
        return thisNormalized.denominator == otherNormalized.denominator
    }

    override fun hashCode(): Int {
        val normalized = normalize()
        var result = normalized.numerator.hashCode()
        result = 31 * result + normalized.denominator.hashCode()
        return result
    }

    override fun toString(): String {
        val normalized = normalize()
        val normalizedDenominator = normalized.denominator
        return if (normalizedDenominator == BigInteger.ONE)
            "${normalized.numerator}"
        else
            "${normalized.numerator}/$normalizedDenominator"
    }
}