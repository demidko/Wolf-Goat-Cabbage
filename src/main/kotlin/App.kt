import Person.Farmer
import boat.LeftBoat


object Wolf : Person("🐺")

object Goat : Person("🐐")

object Cabbage : Person("🥬")

fun Riverside.rule() =
  contains(Farmer) ||
    (!contains(Wolf) || !contains(Goat)) &&
    (!contains(Goat) || !contains(Cabbage))

fun main() {

  val property = setOf(Wolf, Goat, Cabbage)

  val scripts = LeftBoat(property).where(Riverside::rule) {
    right.containsAll(property)
  }

  scripts.forEach(History::prettyPrint)
}

