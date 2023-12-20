import java.io.File
import java.util.Queue

fun main() {
    Day20.part1().let(::println)
}

object Day20: Day<Long, Long>() {

    val moduleMap: Map<String, Module> = buildList {
        File("input/20").readLines().forEach { str ->
            val (from, to) = str.split(" -> ")
            val tos = to.split(", ")
            if (from.first() == '%') {
                add(FlipFlop(from.drop(1), tos))
            } else if (from.first() == '&') {
                add(Conjunction(from.drop(1), tos))
            } else {
                add(Broadcaster(from, tos))
            }
        }
    }.associateBy { it.name }

    override fun part1(): Long {
        // idea:
        // create map for each line with "[module]" to List of destination [modules]
        // have list of SendSignal starting with button -> broadcaster
        // call signal() on the dest

        // fill inputs, this doesn't need to be here, but I have no time :(
        moduleMap.values.forEach {mod ->
            if (mod is Conjunction) {
                // find all values that have mod.name in their to list
                mod.inputs = moduleMap.values.mapNotNull { value ->
                    value.name.takeIf { value.outputs.contains(mod.name) }
                }.associateWith { Signal.LO }.toMutableMap()
            }
        }

        val workList = mutableListOf<SendSignal>()
        var sum = 0 to 0
        repeat(1000) {
            sum += pushButton()
        }
        return sum.first.toLong() * sum.second.toLong()
    }

    private fun pushButton(): Pair<Int, Int> {
        val workList = mutableListOf<SendSignal>()
        workList.add(SendSignal("button", Signal.LO, "broadcaster"))
        var hilo = 0 to 1
        while (workList.isNotEmpty()) {
            val currSignal = workList.removeFirst()
            val genSignal = moduleMap[currSignal.to]?.signal(currSignal)
            if (!genSignal.isNullOrEmpty()) {
                hilo += genSignal.fold(0 to 0) { acc, signal ->
                    acc + if (signal.signal == Signal.HI) (1 to 0) else (0 to 1)
                }
                workList.addAll(genSignal)
            }
        }
        return hilo
    }

    override fun part2(): Long {
        return lcm(listOf(3917, 3931, 3943, 4057))
        // fill inputs, this doesn't need to be here or be double, but I have no time :(
        moduleMap.values.forEach {mod ->
            if (mod is Conjunction) {
                // find all values that have mod.name in their to list
                mod.inputs = moduleMap.values.mapNotNull { value ->
                    value.name.takeIf { value.outputs.contains(mod.name) }
                }.associateWith { Signal.LO }.toMutableMap()
            }
        }
        val tjConj = moduleMap["tj"]
        if (tjConj is Conjunction) {
            val highInputs = tjConj.inputs.map { -1 }.toMutableList()

            for (i in 1..10000000) {
                if (highInputs.all { it > 0 }) break
                pushButton()
                if (i >= 3917) {
                    print("")
                }
                tjConj.inputs.entries.forEachIndexed { index, entry ->
                    if (entry.value == Signal.HI) {
                        highInputs[index] = i
                    }
                }

            }
            lcm(highInputs.map { it.toLong() }.toList())
        }
        return 0L
    }

    abstract class Module(
            val name: String,
            val outputs: List<String>
    ) {
        abstract fun signal(sent: SendSignal): List<SendSignal>
    }
    class Broadcaster(
            name: String,
            outputs: List<String>
    ): Module(name, outputs) {
       override fun signal(sent: SendSignal): List<SendSignal> = outputs.map {
               SendSignal(name, Signal.LO, it)
           }
    }

    class FlipFlop(
            name: String,
            outputs: List<String>,
            var state : State = State.OFF,
    ): Module(name, outputs){
        override fun signal(sent: SendSignal): List<SendSignal> {
            if (sent.signal == Signal.LO) {
                if (state == State.ON) {
                    state = State.OFF
                    return outputs.map {
                        SendSignal(name, Signal.LO, it)
                    }
                } else {
                    state = State.ON
                    return outputs.map {
                        SendSignal(name, Signal.HI, it)
                    }
                }
            } else {
                return listOf()
            }
        }
    }

    class Conjunction(
            name: String,
            outputs: List<String>,
            var inputs: MutableMap<String, Signal> = mutableMapOf()
    ): Module(name, outputs) {
        override fun signal(sent: SendSignal): List<SendSignal> {
            inputs[sent.from] = sent.signal
            return outputs.map {str ->
                SendSignal(name,
                        if (inputs.all { it.value == Signal.HI }) Signal.LO else Signal.HI,
                        str)
            }
        }
    }

    data class SendSignal(
            val from: String,
            val signal: Signal,
            val to: String
    )

    enum class State {
        ON, OFF
    }
    enum class Signal {
        HI, LO
    }
}