package domain.purchase

class Price(val price: Int) {
    fun subtractPrice(subtrahend: Int): Price {
        val subtracted = price - subtrahend
        return Price(subtracted)
    }
}
