package rationals

import java.math.BigInteger

class RationalNormalized(numerator: BigInteger, denominator: BigInteger) : Rational {
    override val numerator: BigInteger
    override val denominator: BigInteger
    private val rationalNotNormalized: Rational

    private fun fromRational(rational: Rational): Rational {
        return RationalNormalized(rational.numerator, rational.denominator)
    }

    init {
        require(denominator != BigInteger.ZERO) { "Denominator must not be 0" }
        rationalNotNormalized = RationalNotNormalized(numerator, denominator).normalize()
        this.numerator = rationalNotNormalized.numerator
        this.denominator = rationalNotNormalized.denominator
    }

    override operator fun plus(other: Rational): Rational {
        return fromRational(rationalNotNormalized.plus(other))
    }

    override operator fun minus(other: Rational): Rational {
        return fromRational(rationalNotNormalized.minus(other))
    }

    override operator fun times(other: Rational): Rational {
        return fromRational(rationalNotNormalized.times(other))
    }

    override operator fun div(other: Rational): Rational {
        return fromRational(rationalNotNormalized.div(other))
    }

    override operator fun unaryMinus(): Rational {
        return fromRational(rationalNotNormalized.unaryMinus())
    }

    override operator fun compareTo(other: Rational): Int {
        return (this - other).signum()
    }

    override operator fun rangeTo(other: Rational): ClosedRange<Rational> {
        return object : ClosedRange<Rational> {
            override val start: Rational
                get() = this@RationalNormalized
            override val endInclusive: Rational
                get() = fromRational(other)
        }
    }

    override fun signum(): Int {
        return numerator.signum()
    }

    override fun normalize(): Rational {
        val gcd = numerator.gcd(denominator)
        return RationalNormalized(numerator / gcd, denominator / gcd)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RationalNormalized

        if (numerator != other.numerator) return false
        return denominator == other.denominator
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    override fun toString(): String {
        return if (denominator == BigInteger.ONE)
            "$numerator"
        else
            "$numerator/$denominator"
    }
}