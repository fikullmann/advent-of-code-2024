import Boxes.Companion.hash
import java.io.File
import java.util.Dictionary
import java.util.TreeMap

fun main() {
    val splitInput = File("input/15").readLines()[0].split(',')
    day15Part2(splitInput)
}

fun day15Part1(input: List<String>) {
    val boxes = Boxes()
    val results = mutableListOf<Int>()
    input.forEach {str ->
        results.add(hash(str))
    }
    println(results.sum())
}

class Boxes() {
    var boxes: MutableMap<Int, MutableList<Lens>> = TreeMap()
    companion object {
        fun hash(str: String): Int {
            var value = 0
            str.forEach {ch ->
                value += ch.code
                value *= 17
                value %= 256
            }
            return value
        }

    }
    fun inputSequence(str: String) {

        if (str.contains('-')) {
            val splitString = str.split('-')
            val boxInput = Lens(splitString[0], -1)
            boxes[boxInput.hashCode()]?.removeIf {
                str.dropLast(1) == it.id
            }
        } else {
            val splitString = str.split('=')
            val boxInput = Lens(splitString[0], splitString[1].toInt())
            if (boxes.containsKey(boxInput.hashCode())) {
                if (boxes[boxInput.hashCode()]?.map { it.id }?.contains(boxInput.id) == true) {
                    boxes[boxInput.hashCode()]?.replaceAll {
                        if (it.id == boxInput.id) boxInput else it
                    }
                } else {
                    boxes[boxInput.hashCode()]?.add(boxInput)!!
                }
            } else {
                boxes[boxInput.hashCode()] = mutableListOf(boxInput)
            }
        }
    }
}
class Lens(val id: String, val focalLength: Int) {
    override fun hashCode(): Int {
        var value = 0
        id.forEach {ch ->
            value += ch.code
            value *= 17
            value %= 256
        }
        return value
    }

}

fun day15Part2(input: List<String>) {
    val boxes = Boxes()
    input.forEach { str ->
        boxes.inputSequence(str)
    }
    println(boxes.boxes.map {entry: Map.Entry<Int, MutableList<Lens>> ->
        val boxNr = entry.key + 1
        entry.value.mapIndexed { idx, lens ->
            var slot = idx + 1
            //println(boxNr * slot * lens.focalLength)
            boxNr * slot * lens.focalLength
        }.sum()
    }.sum())

}