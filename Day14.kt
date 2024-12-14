import java.io.File

fun main() {
    Day14.part2().let (::println)
    //Day10.part1("10_test2").let (::println)
    //Day10.part1("10_test3").let (::println)
    //Day10.part2().let (::println)
}

object Day14 : Day<Int, Int>() {
    var guards = mutableListOf<Pair<Location, Distance>>()
    val maxLine =  103
    val maxCol = 101
    init {
        File("input/14").readLines().forEach {
            val (fst, snd) = it.split(" ")
            val (pX, pY) = fst.drop(2).split(",")
            val (vX, vY) = snd.drop(2).split(",")
            val distance =
            guards.add(Location(pY.toInt(), pX.toInt()) to Distance(
                Math.floorMod(vY.toInt(), maxLine),
                Math.floorMod(vX.toInt(), maxCol))
            )
        }
    }

    override fun part1(): Int {
        var newLocations = mutableListOf<Location>()
        guards.forEach { (loc, dis) ->
            val unmodded = loc.applyDistance(dis, 100)
            newLocations.add(Location(unmodded.row % maxLine, unmodded.col % maxCol))
        }

        val topLeft = newLocations.count { it.row < maxLine / 2 && it.col < maxCol / 2 }
        val topRight = newLocations.count { it.row < maxLine / 2 && it.col > maxCol / 2 }
        val botLeft = newLocations.count { it.row > maxLine / 2 && it.col < maxCol / 2 }
        val botRight = newLocations.count { it.row > maxLine / 2 && it.col > maxCol / 2 }
        return topLeft * topRight * botLeft * botRight
    }
    override fun part2(): Int {
        val newLocations = mutableListOf<Location>()
        var smallest = 0 to 229980828L
        for (i in 1..10000) {
            guards.forEach { (loc, dis) ->
                val unmodded = loc.applyDistance(dis, i)
                newLocations.add(Location(unmodded.row % maxLine, unmodded.col % maxCol))
            }

            val topLeft = newLocations.count { it.row < maxLine / 2 && it.col < maxCol / 2 }
            val topRight = newLocations.count { it.row < maxLine / 2 && it.col > maxCol / 2 }
            val botLeft = newLocations.count { it.row > maxLine / 2 && it.col < maxCol / 2 }
            val botRight = newLocations.count { it.row > maxLine / 2 && it.col > maxCol / 2 }
            val danger = topLeft.toLong() * topRight.toLong() * botLeft.toLong() * botRight.toLong()
            if (danger > 0 && smallest.second > danger) {
                smallest = (i to danger)
            }
            newLocations.clear()
        }

        return smallest.first
    }
}
