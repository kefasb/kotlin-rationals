package rationals

import java.math.BigInteger

interface Rational : Comparable<Rational> {
    val numerator: BigInteger
    val denominator: BigInteger

    operator fun plus(other: Rational): Rational

    operator fun minus(other: Rational): Rational

    operator fun times(other: Rational): Rational

    operator fun div(other: Rational): Rational

    operator fun unaryMinus(): Rational

    override operator fun compareTo(other: Rational): Int

    /**
     * This method is not necessary because {@link Comparable} interface
     * allows creating ranges automatically.
     * However, for training purposes the method has been implemented :)
     */
    operator fun rangeTo(other: Rational): ClosedRange<Rational>

    fun normalize(): Rational

    fun signum(): Int

    companion object From {
        private var fromCreator: FromCreator = FromCreator.fromNormalizedCreator()

        fun fromString(str: String) = fromCreator.fromString(str)
        fun fromIntDivBy(a: Int, b: Int) = fromBigIntegerDivBy(a.toBigInteger(), b.toBigInteger())
        fun fromLongDivBy(a: Long, b: Long) = fromBigIntegerDivBy(a.toBigInteger(), b.toBigInteger())
        fun fromBigIntegerDivBy(a: BigInteger, b: BigInteger) = fromCreator.fromBigIntegerDivBy(a, b)

        fun initNormalized() {
            fromCreator = FromCreator.fromNormalizedCreator()
        }

        fun initNotNormalized() {
            fromCreator = FromCreator.fromNotNormalizedCreator()
        }
    }
}

private class FromCreator(val creator: Creator) {
    fun fromString(str: String): Rational {
        val numeratorStr = str.substringBefore('/')
        val denominatorStr = str.substringAfter('/', "1")
        return creator.create(numeratorStr.toBigInteger(), denominatorStr.toBigInteger())
    }

    fun fromBigIntegerDivBy(a: BigInteger, b: BigInteger) =
        creator.create(a, b)

    companion object FromCreator {
        fun fromNormalizedCreator() = FromCreator(Creator.normalizedCreator())
        fun fromNotNormalizedCreator() = FromCreator(Creator.notNormalizedCreator())
    }
}

private interface Creator {
    fun create(numerator: BigInteger, denominator: BigInteger): Rational

    private class NormalizedCreator : rationals.Creator {
        override fun create(numerator: BigInteger, denominator: BigInteger): Rational {
            return RationalNormalized(numerator, denominator)
        }
    }

    private class NotNormalizedCreator : rationals.Creator {
        override fun create(numerator: BigInteger, denominator: BigInteger): Rational {
            return RationalNotNormalized(numerator, denominator)
        }
    }

    companion object Creator {
        fun normalizedCreator(): rationals.Creator = NormalizedCreator()
        fun notNormalizedCreator(): rationals.Creator = NotNormalizedCreator()
    }
}

infix fun Int.divBy(denominator: Int): Rational {
    return Rational.fromIntDivBy(this, denominator)
}

infix fun Long.divBy(denominator: Long): Rational {
    return Rational.fromLongDivBy(this, denominator)
}

infix fun BigInteger.divBy(denominator: BigInteger): Rational {
    return Rational.fromBigIntegerDivBy(this, denominator)
}

fun String.toRational(): Rational {
    return Rational.fromString(this)
}
