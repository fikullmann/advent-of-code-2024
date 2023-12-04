import java.io.File

fun main() {
    day12Part2(File("input/12").readLines())
}

fun day12Part1(lines: List<String>) {
    val list = mutableListOf<Long>()
    lines.forEach { str ->
        val splitLine = str.split(" ")
        val arr = Arrangement()
        list.add(arr.calcArrangement(splitLine[0], splitLine[1].split(",").mapNotNull { it.toIntOrNull() }))
    }
    println(list)
    println(list.sum())
}


class Arrangement() {
    val map: HashMap<String, Long> = HashMap()
    fun calcArrangement(condition: String, damagedInfo: List<Int>): Long {
        val trimmedCond = condition.trim('.')
        //print("$trimmedCond ")
        //println(damagedInfo)
        val cacheKey = "$trimmedCond $damagedInfo"
        if (damagedInfo.isEmpty() && !condition.contains('#')) {
            return 1
        }
        if (damagedInfo.isEmpty() && condition.contains('#')) {
            return 0
        }
        if (trimmedCond.length < damagedInfo.sum() + damagedInfo.size - 1) {
            return 0
        }
        var options: Long = 0
        if (map.containsKey(cacheKey)) {
            return map[cacheKey]!!
        }
        for (i in trimmedCond.indices) {
            // next tile is # -> not possible OR there is no next then
            // range contains . -> not possible
            //
            if ((damagedInfo[0] < trimmedCond.length - i && trimmedCond[damagedInfo[0] + i] != '#')
                    || (damagedInfo[0] == trimmedCond.length - i && damagedInfo.size == 1)) {
                if (!trimmedCond.substring(i..<damagedInfo[0] + i).contains('.')
                        && !trimmedCond.substring(0..<i).contains('#')) {
                    val trim = trimmedCond.drop(damagedInfo[0] + 1 + i)
                    val newInfo = damagedInfo.drop(1)
                    options += calcArrangement(trim, newInfo)
                }
            }
        }
        map[cacheKey] = options
        //println(options)
        return options
    }
}

fun day12Part2(lines: List<String>) {
    val list = mutableListOf<Long>()
    val arr = Arrangement()
    lines.forEach { str ->
        val splitLine = str.split(" ")
        val second = splitLine[1].split(",").mapNotNull { it.toIntOrNull() }
        var condition = splitLine[0]
        val damagedInfo = mutableListOf<Int>()
        damagedInfo.addAll(second)
        repeat(4) {
            condition += "?${splitLine[0]}"
            damagedInfo.addAll(second)
        }
        //println(condition)
        //println(damagedInfo)
        list.add(arr.calcArrangement(condition, damagedInfo))
    }
    //println(list)
    println(list.sumOf { it })
    println(arr.map.size)
}
