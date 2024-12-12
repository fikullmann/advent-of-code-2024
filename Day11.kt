import java.io.File
import kotlin.math.pow

fun main() {
    //Day11.part1().let (::println)
    Day11.part2().let (::println)
    //Day11.part2().let (::println)
}

object Day11: Day<Int, Long>() {
    private val input = File("input/11").readLines().first().split(" ").map { it.toLong() }
    val countMap = mutableMapOf<Long, MutableList<Long>>()

    override fun part1(): Int {
        var resAfterBlink = input
        for (i in 1..25) {
            resAfterBlink = applyList(resAfterBlink)
        }
        return resAfterBlink.size

    }
    fun applyList(list: List<Long>): List<Long> {
        var result = mutableListOf<Long>()
        list.forEach { result.addAll(applyRule(it)) }
        return result
    }

    fun applyRule(stone: Long): List<Long> {
        if (stone == 0L) return listOf(1)
        val len = stone.lengthInt()
        if (len % 2 == 0) {
            val fst = stone / (10.0).pow(len/2).toLong()
            val snd = (stone % (10.0).pow(len/2)).toLong()
            return listOf(fst, snd)
        }
        else return listOf(stone * 2024)
    }

    override fun part2(): Long {
        var resAfterBlink = input.associateWith { 1L }
        for (i in 1..75) {
            resAfterBlink = applyListMap(resAfterBlink)
        }
        return resAfterBlink.values.sum()
    }


    fun applyListMap(inputList: Map<Long, Long>): Map<Long, Long> {
        val result = mutableMapOf<Long, Long>()
        inputList.forEach {
            val resList = applyRule(it.key)
            resList.forEach { oneStone ->
                result[oneStone] = result.getOrDefault(oneStone, 0) + it.value
            }
        }
        return result
    }
}