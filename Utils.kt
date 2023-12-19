operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.minus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.times(o: Int) = o *first to o * second
operator fun Int.times(o: Pair<Int, Int>) = this *o.first to this * o.second