import Day16Contraption.Companion.fromChar
import java.io.File
import kotlin.math.max

fun main() {
    //Day16.part1().let(::println)
    //Day16.part2().let(::println)
    Day16.solve().let(::println)
}

object Day16: Day<Any?, Int>() {
    /**
     * parsed input as 2d array of contraption layout
     */
    private val contraption: List<List<Day16Contraption>> = File("input/16").readLines().map { str ->
        str.fold(mutableListOf()) { acc, ch ->
            acc.add(fromChar(ch)); acc
        }
    }

    /**
     * solves Advent of Code part 2 of Day 16
     * @see <a href="https://adventofcode.com/2023/day/16">Advent of Code Day 16</a>
     */
    override fun part1(): Int = testSetup(0, 0, Dir.EAST)

    /**
     * solves Advent of Code part 2 of Day 16
     * @see <a href="https://adventofcode.com/2023/day/16">Advent of Code Day 16</a>
     */
    override fun part2(): Int {
        val snResult = contraption.indices.fold(0) { acc, vStart ->
            max(acc,
                max(testSetup(0, vStart, Dir.SOUTH),
                    testSetup(contraption.size - 1, vStart, Dir.NORTH)
            ))
        }
        val weResult = (0..<contraption[0].size).fold(0) { acc, vStart ->
            max(acc,
                max(testSetup(vStart, 0, Dir.EAST),
                    testSetup(vStart, contraption[0].size - 1, Dir.WEST)
            ))
        }
        return max(snResult, weResult)
    }

    /**
     * runs through one setup, with the given starting indices and starting direction
     * @return sum of energized fields
     */
    private fun testSetup(lIdx: Int, cIdx: Int, dir: Dir): Int {
        val setup = Setup(contraption)
        setup.energized[lIdx][cIdx] = 1
        setup.beams.add(Beam(lIdx, cIdx, dir))
        while (setup.beams.isNotEmpty()) {
            val firstBeam = setup.beams.removeFirst()
            setup.archiveBeams.add(firstBeam)
            setup.shootBeam(firstBeam)
        }
        return setup.energized.sumOf { it.sum() }
    }

}

/**
 * One Setup with the given contraption, where no fields are initially energized,
 * no beams should be traversed and no beams have been traversed so far
 */
class Setup(val contraption: List<List<Day16Contraption>>) {

    // marks energized fields
    val energized: Array<Array<Int>> = Array(contraption.size) {
        Array(contraption[0].size) { 0 }
    }
    val beams: MutableList<Beam> = mutableListOf()
    // list of traversed beams, so we avoid checking double
    val archiveBeams: MutableList<Beam> = mutableListOf()

    // Dictionaries for direction to movement on grid and mirror directions
    companion object {
        private val mapDir: Map<Dir, Pair<Int, Int>> = mapOf(
                Dir.NORTH to (-1 to 0),
                Dir.WEST to (0 to -1),
                Dir.SOUTH to (1 to 0),
                Dir.EAST to (0 to 1),
        )
        private val leftUP: Map<Dir, Dir> = mapOf(
                Dir.NORTH to Dir.EAST,
                Dir.WEST to Dir.SOUTH,
                Dir.SOUTH to Dir.WEST,
                Dir.EAST to Dir.NORTH,
        )
        private val rightUP: Map<Dir, Dir> = mapOf(
                Dir.NORTH to Dir.WEST,
                Dir.WEST to Dir.NORTH,
                Dir.SOUTH to Dir.EAST,
                Dir.EAST to Dir.SOUTH,
        )
    }

    /**
     * Traverses one step of a beam
     *
     * Calculates new direction(s), and if next step is valid (and hasn't been traversed before)
     * energizes the next field and saves next step in worklist "beams".
     */
    fun shootBeam(beam: Beam) {
        val dir = mutableListOf<Dir>()

        when (contraption[beam.lIdx][beam.cIdx]) {
            Day16Contraption.EMPTY -> dir.add(beam.dir)
            Day16Contraption.LEFTUP_MIRROR -> dir.add(leftUP[beam.dir]!!)
            Day16Contraption.RIGHTUP_MIRROR -> dir.add(rightUP[beam.dir]!!)
            Day16Contraption.HORIZ_SPLINTER -> {
                if (beam.dir == Dir.NORTH || beam.dir == Dir.SOUTH) {
                    dir.add(leftUP[beam.dir]!!)
                    dir.add(rightUP[beam.dir]!!)
                } else dir.add(beam.dir)
            }
            Day16Contraption.VERTICAL_SPLINTER -> {
                if (beam.dir == Dir.WEST || beam.dir == Dir.EAST) {
                    dir.add(leftUP[beam.dir]!!)
                    dir.add(rightUP[beam.dir]!!)
                } else dir.add(beam.dir)
            }
        }

        dir.forEach { direction ->
            val ltravel = beam.lIdx + mapDir[direction]?.first!!
            val ctravel = beam.cIdx + mapDir[direction]?.second!!
            if (ltravel >= 0 && ctravel >= 0 && ltravel < contraption.size && ctravel < contraption[0].size) { // bound checking
                energized[ltravel][ctravel] = 1
                val newBeam = Beam(ltravel, ctravel, direction)
                if (!archiveBeams.any { b -> b.lIdx == newBeam.lIdx && b.cIdx == newBeam.cIdx && b.dir == newBeam.dir }) {
                    beams.add(Beam(ltravel, ctravel, direction))
                }
            }
        }
    }
}

class Beam(
    val lIdx: Int, val cIdx: Int, val dir: Dir
)

enum class Day16Contraption(val value: Char) {
    EMPTY('.'),
    LEFTUP_MIRROR('/'),
    RIGHTUP_MIRROR('\\'),
    HORIZ_SPLINTER('-'),
    VERTICAL_SPLINTER('|');

    companion object {
        fun fromChar(value: Char) = entries.first { it.value == value }
    }
}