import com.google.common.collect.Iterables.cycle
import java.util.*

data class River<T>(val leftside: Set<T>, val rightside: Set<T>)

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

typealias Rule<T> = River<T>.() -> Boolean

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

fun <T> withRules(vararg rules: Rule<T>): Rule<T> = {
  rules.all { it(this) }
}

fun <T> whileCondition(condition: Rule<T>): Rule<T> = condition

fun repeatAction(action: Sequence<River<T>>.() -> Unit): Sequence<River<T>>.() -> Unit = action

inline fun <T> River<T>.loop(
  noinline filter: Rule<T>,
  crossinline condition: Rule<T>,
  crossinline action: Sequence<River<T>>.() -> Unit
) {
  var sequence = sequenceOf(this)
  cycle(River<T>::leftToRight, River<T>::rightToLeft).forEach {
    sequence = sequence.flatMap(it)
    if (sequence.any(condition)) {
      return
    }
    sequence = sequence.filter(filter).apply(action)
  }
}


fun main() {

  val wolf = "🐺"
  val goat = "🐐"
  val cabbage = "🥬"

  val persons = setOf(wolf, goat, cabbage)

  val river = River(persons, emptySet())


  river.loop<String>(
    withRules(
      enemies(wolf, goat),
      enemies(goat, cabbage),
      original()
    ),
    whileCondition {
      rightside != persons
    },
    repeatAction {
      println(this)
    })
}