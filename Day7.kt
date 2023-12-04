import java.io.File

fun main() {
    day7Part1(File("input/7"))
}

fun day7Part1(content: File) {
    val listOfHands = mutableListOf<Hand>()
    content.forEachLine { str ->
        val splittedLine = str.split(' ')
        val hand = splittedLine[0].map { Rank.fromChar(it) }
        listOfHands.add(Hand(hand, splittedLine[1].toInt()))
    }

    val sortedHands = listOfHands.sortedWith() { hand: Hand, other: Hand ->
        val typeOrder = hand.type.compareTo(other.type)
        val rankOrder = mutableListOf<Int>()
        rankOrder.add(hand.list[0].compareTo(other.list[0]))
        rankOrder.add(hand.list[1].compareTo(other.list[1]))
        rankOrder.add(hand.list[2].compareTo(other.list[2]))
        rankOrder.add(hand.list[3].compareTo(other.list[3]))
        rankOrder.add(hand.list[4].compareTo(other.list[4]))

        if (typeOrder == 0) {
            rankOrder.find { it != 0 }!!
        } else {
            typeOrder
        }
    }
    var sum = 0
    for (i in sortedHands.indices) {
        sum += sortedHands[i].bid * (i+1)
    }
    println(sum)

}


class Hand(val list: List<Rank>, val bid: Int) {
    lateinit var type: Type
    init {
        val typeNums = mutableListOf<Int>(1)
        list.sorted().reduce { acc, rank ->
            if (acc == rank) {
                typeNums.add(typeNums.last() + 1)
            } else {
                typeNums.add(1)
            }
            rank
        }
        val max = typeNums.max()
        if (max == 2) {
            if (typeNums.count { it == 2 } == 2) {
                type = Type.TP
            } else { type = Type.OP }
        } else if (max == 3) {
            if (typeNums.count { it == 2 } > 1) {
                type = Type.FH
            } else { type = Type.TOK }
        } else {
            type = Type.fromInt(typeNums.max())
        }
    }
}
enum class Rank(val value: Char) {
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    companion object {
        fun fromChar(value: Char) = entries.first { it.value == value }
    }
}

enum class Type(val value: Int) {
    HC(1),
    OP(2),
    TP(8),
    TOK(3),
    FH(9),
    FourOK(4),
    FiveOK(5);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}