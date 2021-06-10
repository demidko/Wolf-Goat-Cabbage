import com.google.common.collect.Iterables.cycle
import java.util.*

data class River<T>(val leftside: Set<T>, val rightside: Set<T> = emptySet())

typealias Rule<T> = River<T>.() -> Boolean

typealias Action<T> = River<T>.() -> Unit

fun <T> River<T>.leftToRight() = sequence {
  yield(this@leftToRight)
  leftside.forEach {
    yield(River(leftside - it, rightside + it))
  }
}

fun <T> River<T>.rightToLeft() = sequence {
  yield(this@rightToLeft)
  leftside.forEach {
    yield(River(leftside + it, rightside - it))
  }
}


fun <T> enemies(vararg e: T): Rule<T> = {
  leftside.count(e::contains) <= 1
    && rightside.count(e::contains) <= 1
}

fun <T> original(): Rule<T> {
  val history = LinkedHashSet<River<T>>()
  return {
    history.add(this)
  }
}

fun <T> withFilters(vararg rules: Rule<T>): Rule<T> = {
  rules.all { it(this) }
}

fun <T> until(condition: Rule<T>) = condition

fun <T> repeatAction(action: Action<T>) = action

inline fun <T> River<T>.swim(
  noinline until: Rule<T>,
  noinline rules: Rule<T>,
  crossinline repeat: Action<T>
) {
  var sequence = sequenceOf(this)
  for(next in cycle(River<T>::leftToRight, River<T>::rightToLeft)) {

    sequence = sequence.flatMap(next)

    sequence.filterNot(until).apply { forEach(repeat) }

    if (stop.any()) {
      return
    }
    sequence = sequence.filter(rules).apply {
      forEach(repeat)
    }
  }
}


fun main() {

  val wolf = "🐺"
  val goat = "🐐"
  val cabbage = "🥬"

  River(setOf(wolf, goat, cabbage)).swim(
    until {
      rightside.size < 3
    },
    withFilters(
      enemies(wolf, goat),
      enemies(goat, cabbage)
    ),
    repeatAction {
      println(this)
    }
  )
}