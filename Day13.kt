import java.io.File
import kotlin.math.min

fun main() {
    day13Part2(File("input/13").readLines())
}

fun day13Part1(lines: List<String>) {
    var horizLines: MutableList<String> = mutableListOf()
    var verticalLines: List<String> = buildList<String>() {
        repeat(lines[0].length) { add("") }
    }
    var sum = 0
    lines.forEachIndexed { lineIdx, str ->
        if (str.isEmpty()) {
            val horizontal = findMirror(horizLines).firstOrNull() ?: 0
            val vertical = findMirror(verticalLines).firstOrNull() ?: 0
            sum += horizontal * 100 + vertical
            horizLines = mutableListOf()
            verticalLines = buildList() {
                if (lineIdx+1 < lines.size) {
                    repeat(lines[lineIdx+1].length) { add("") }
                }
            }
            println(sum)
            // get to work and find shits
        } else {
            horizLines.add(str)
            verticalLines = verticalLines.mapIndexed { idx, vLine ->
                vLine + str[idx]
            }
        }

    }
}

fun findMirror(lines: List<String>) : List<Int> {
    var results: MutableList<Int> = mutableListOf()
    val midIdx = lines.size / 2
    val sameMidLines = lines.mapIndexedNotNull { idx, str ->
        idx.takeIf { str == lines[midIdx] && it != midIdx }
    }
    val sameFirstLine = lines.mapIndexedNotNull { idx, str ->
        idx.takeIf { str == lines[0] && it != 0 }
    }
    val sameLastLine = lines.mapIndexedNotNull { idx, str ->
        idx.takeIf { str == lines.last() && it != (lines.size -1) }
    }
    sameMidLines.forEach { sameIdx ->
        val mirrorIdx = (sameIdx + midIdx) / 2
        findMirrorDetail(lines, mirrorIdx).apply {
            if (this > 0) results.add(this)
        }
    }
    sameFirstLine.forEach { sameIdx ->
        findMirrorDetail(lines, sameIdx / 2).apply {
            if (this > 0) results.add(this)
        }
    }
    sameLastLine.forEach { sameIdx ->
        findMirrorDetail(lines, (sameIdx + lines.size-1) / 2).apply {
            if (this > 0) results.add(this)
        }
    }
    return results
}

fun findMirrorDetail(lines: List<String>, mirrorIdx: Int): Int {
    val sizeToEnd = min(mirrorIdx, lines.size - mirrorIdx - 2)
    val beforeMirror = lines.subList(mirrorIdx - sizeToEnd, mirrorIdx + 1)
    val afterMirror = lines.subList(mirrorIdx + 1, mirrorIdx+1 + sizeToEnd+1).reversed()
    if (beforeMirror == afterMirror) {
        return mirrorIdx +1
    }
    return 0
}

fun changedMirror(lines: MutableList<String>, realLine: Int, isHorizontal: Boolean = false): Int {
    var adjustedRealLine = if (isHorizontal) realLine/100 else realLine
    lines.forEachIndexed { lineIdx, line ->
        line.forEachIndexed { columnIdx, char ->
            val editedLines = lines.mapIndexed { idx, mapLine ->
                if (idx == lineIdx) {
                    changeLine(columnIdx, line)
                } else {
                    mapLine
                }
            }

            val result = findMirror(editedLines).firstOrNull { it != adjustedRealLine }
            if (result != null && result > 0) {
                return result
            }
        }
    }
    return 0

    /*var total = 0
    for (i in lines[0].indices) {
        val midIdx = lines.size / 2
        val changedLine = changeLine(i, lines[midIdx])
        val sameMidLines = lines.mapIndexedNotNull { idx, str ->
            idx.takeIf { str == changedLine && it != midIdx }
        }

        sameMidLines.forEach { sameIdx ->
            val mirrorIdx = (sameIdx + midIdx) / 2
            total += findMirrorDetail(lines, mirrorIdx)
            if (total > 0) return total
        }
    }

    for (i in lines[0].indices) {
        val changedLine = changeLine(i, lines[0])
        val sameFirstLine = lines.mapIndexedNotNull { idx, str ->
            idx.takeIf { str == changedLine && it != 0 }
        }

        sameFirstLine.forEach { sameIdx ->
            total += findMirrorDetail(lines, sameIdx / 2)
            if (total > 0) return total
        }
    }

    for (i in lines[0].indices) {
        val changedLine = changeLine(i, lines.last())
        val sameLastLine = lines.mapIndexedNotNull { idx, str ->
            idx.takeIf { str == changedLine && it != (lines.size - 1) }
        }
        sameLastLine.forEach { sameIdx ->
            total += findMirrorDetail(lines, (sameIdx + lines.size - 1) / 2)
            if (total > 0) return total
        }
    }
    return total*/
}

fun changeLine(idx: Int, line: String): String {
    var changedLine: String = line.substring(0..<idx)
    if (line[idx] == '#') {
        changedLine += '.'
    } else {
        changedLine += '#'
    }
    if (idx+1 < line.length) {
        changedLine += line.substring(idx+1..<line.length)
    }
    return changedLine
}


fun day13Part2(lines: List<String>) {
    var horizLines: MutableList<String> = mutableListOf()
    var verticalLines: List<String> = buildList<String>() {
        repeat(lines[0].length) { add("") }
    }
    var sum = 0
    lines.forEachIndexed { lineIdx, str ->
        if (str.isEmpty()) {
            val horizontal = findMirror(horizLines).firstOrNull() ?: 0
            val vertical = findMirror(verticalLines).firstOrNull() ?: 0
            val result = horizontal * 100 + vertical
            println(result)

            sum += changedMirror(verticalLines.toMutableList(), result)
            sum += changedMirror(horizLines, result, true) * 100
            print("$lineIdx ")
            println(sum)
            horizLines = mutableListOf()
            verticalLines = buildList() {
                if (lineIdx+1 < lines.size) {
                    repeat(lines[lineIdx+1].length) { add("") }
                }
            }
            // get to work and find shits
        } else {
            horizLines.add(str)
            verticalLines = verticalLines.mapIndexed { idx, vLine ->
                vLine + str[idx]
            }
        }

    }
}