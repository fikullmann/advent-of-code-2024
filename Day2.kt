import java.io.File

fun main() {
    task2()
}

fun task1() {
    val redLimit = 12
    val blueLimit = 14
    val greenLimit = 13
    var sum = 0
    File("input/2").forEachLine {
        var possible = true
        val listOfStrings = it.split(": ", "; ", ", ")
        for (i in 1..<listOfStrings.size) {
            listOfStrings[i].apply {
                        when {
                            "red" in this -> possible = possible && (this.dropLast(4).toInt() <= redLimit)
                            "blue" in this -> possible = possible && (this.dropLast(5).toInt() <= blueLimit)
                            "green" in this -> possible = possible && (this.dropLast(6).toInt() <= greenLimit)
                        }
                    }
        }
        if (possible) {
            sum += listOfStrings[0].drop(5).toInt()
        }
    }
    println(sum)
}

fun task2() {
    var sum = 0
    File("input/2").forEachLine {
        val listOfStrings = it.split(": ", "; ", ", ")
        var redLimit = 0
        var blueLimit = 0
        var greenLimit = 0
        for (i in 1..<listOfStrings.size) { // Game Analysis
            listOfStrings[i].apply {
                when {
                    "red" in this -> redLimit = kotlin.math.max(this.dropLast(4).toInt(), redLimit)
                    "blue" in this -> blueLimit = kotlin.math.max(this.dropLast(5).toInt(), blueLimit)
                    "green" in this -> greenLimit = kotlin.math.max(this.dropLast(6).toInt(), greenLimit)
                }
            }
        }
        println(redLimit * blueLimit * greenLimit)
        sum += (redLimit * blueLimit * greenLimit)
    }
    println(sum)
}