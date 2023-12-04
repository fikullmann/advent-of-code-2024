import java.io.File

fun main() {
    day7Part2(File("input/7"))
}

fun day7Part2(content: File) {
    val listOfHands = mutableListOf<Hand2>()
    content.forEachLine { str ->
        val splittedLine = str.split(' ')
        val hand = splittedLine[0].map { Rank2.fromChar(it) }
        listOfHands.add(Hand2(hand, splittedLine[1].toInt()))
    }

    val sortedHands = listOfHands.sortedWith() { hand: Hand2, other: Hand2 ->
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
class Hand2(val list: List<Rank2>, val bid: Int) {
    lateinit var type: Type2
    init {
        val typeNums = mutableListOf<Int>()
        var jokerNum: Int = list.count { it == Rank2.JACK }
        val jacklessList = list.filter { it != Rank2.JACK }
        if (jacklessList.isNotEmpty()) {
            typeNums.add(1)
            jacklessList.sorted().reduce { acc, rank ->
                if (rank != Rank2.JACK) {
                    if (acc == rank) {
                        typeNums.add(typeNums.last() + 1)
                    } else {
                        typeNums.add(1)
                    }
                }
                rank
            }
        } else typeNums.add(0)
        val max = typeNums.max() + jokerNum
        if (max == 2) {
            if (typeNums.count { it == 2 } == 2) {
                type = Type2.TP
            } else { type = Type2.OP }
        } else if (max == 3) {
            if (typeNums.count { it == 2 } > 1) {
                type = Type2.FH
            } else { type = Type2.TOK }
        } else {
            type = Type2.fromInt(max)
        }
    }

    fun compareHands(other: Hand2) {
        this.type > other.type
    }

}
enum class Rank2(val value: Char) {
    JACK('J'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    companion object {
        fun fromChar(value: Char) = entries.first { it.value == value }
    }
}

enum class Type2(val value: Int) {
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