import com.google.common.collect.Iterables.cycle
import java.util.*

open class Person(vararg val enemies: Person) {

  override fun toString() = javaClass.name
}

typealias Group = Set<Person>

typealias River = Pair<Group, Group>

typealias History = LinkedList<River>

fun historyOf(vararg river: River) = river.toList().let(::History)

typealias Router = River.() -> Sequence<River>

fun Group.leave(): Group {
  val isDangerous = any {
    it.enemies.any(::contains)
  }
  if (isDangerous) {
    error("Enemies was found")
  }
  return this
}

fun Group.import(p: Person) = this + p

fun Group.export(p: Person) = (this - p).leave()

fun Group.export(to: Group) = sequence {
  forEach {
    try {
      yield(River(leave(), to))
    } catch (ignored: RuntimeException) {
    }
    try {
      yield(River(export(it), to.import(it)))
    } catch (ignored: RuntimeException) {
    }
  }
}

inline fun swim(river: River, crossinline until: (River) -> Boolean) {
  val exportToSecond: Router = {
    first.export(second).also {
      println("$this 🔜 ${it.toList()}")
    }
  }
  val exportToFirst: Router = {
    second.export(first).also {
      println("$this 🔙 ${it.toList()}")
    }
  }
  var parallelRivers = sequenceOf(river)
  for (route in cycle(exportToSecond, exportToFirst)) {
    parallelRivers = parallelRivers.flatMap(route)
    if (parallelRivers.any(until)) {
      break
    }
  }
}

object `🐺` : Person()

object `🐐` : Person(`🐺`)

object `🥬` : Person(`🐐`)

fun main() {
  val property = setOf(`🐺`, `🐐`, `🥬`)
  swim(River(property, emptySet())) {
    it.second == property
  }
}