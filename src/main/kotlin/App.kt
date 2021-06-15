import java.util.*

/**
 * Персонаж нашей ситуации может быть любым
 */
sealed class Person(private val name: String) {

  override fun toString() = name
}

object Farmer : Person("👩🏻‍🌾")

object Wolf : Person("🐺")

object Goat : Person("🐐")

object Cabbage : Person("🥬")

/**
 * Определим берег как набор персонажей
 */
typealias Riverside = Set<Person>

/**
 * Определим правила
 */
fun Riverside.isValid(): Boolean {
  if (contains(Farmer)) {
    return true
  }
  if (contains(Wolf) && contains(Goat)) {
    return false
  }
  if (contains(Goat) && contains(Cabbage)) {
    return false
  }
  return true
}

/**
 * Квантовая лодка находится в неопределенной состоянии между двумя берегами
 */
sealed class QuantumBoat(val leftside: Riverside, val rightside: Riverside) {

  /**
   * Покинуть текущий берег и направиться к противоположному
   * @return все возможные состояния лодки
   */
  abstract fun invert(): List<QuantumBoat>
}

/**
 * Лодка находится слева
 */
class LeftBoat(leftside: Riverside, rightside: Riverside = emptySet()) : QuantumBoat(leftside, rightside) {

  override fun toString() = "(⚓️$leftside⌢$rightside️)"

  override fun invert() =
    leftside.map {
      RightBoat(leftside - it - Farmer, rightside + it + Farmer)
    } + RightBoat(leftside, rightside)
}

/**
 * Лодка находится справа
 */
class RightBoat(leftside: Riverside = emptySet(), rightside: Riverside) : QuantumBoat(leftside, rightside) {

  override fun toString() = "(️$leftside⌢$rightside⚓️)"

  override fun invert() =
    rightside.map {
      LeftBoat(leftside + it + Farmer, rightside - it - Farmer)
    } + LeftBoat(leftside, rightside)
}

/**
 * История представляет список состояний лодки
 */
typealias History = LinkedList<QuantumBoat>

/**
 * @return новая история от первого состояния лодки
 */
fun historyOf(new: QuantumBoat) = History().apply { add(new) }

/**
 * Развилка мультивселенной состояний лодки на новый набор вселенных
 */
@Suppress("UNCHECKED_CAST")
fun Sequence<History>.fork() = sequence {
  for (history in this@fork) {
    for (forked in history.last.invert()) {
      yield((history.clone() as History).apply {
        add(forked)
      })
    }
  }
}

/**
 * Хронист будет думать за нас
 * @param boat квантовая лодка фермера
 */
class Chronicler(boat: QuantumBoat) {

  /**
   * Мультивселенная в голове хрониста
   */
  private var multiverse = sequenceOf(historyOf(boat))

  /**
   * Поиск желаемого состояния в мультивселенной
   * @param result желаемое состояние обеих берегов
   */
  fun search(result: QuantumBoat.() -> Boolean) = multiverse.filter { it.last.result() }.toList()

  /**
   * Плыть по мультивселенной дальше
   * @param to произвольное описание
   */
  fun sail(): Chronicler {
    multiverse = multiverse.fork().filter {
      it.last.leftside.isValid() && it.last.rightside.isValid()
    }
    return this
  }
}

fun main() {
  val property = setOf(Wolf, Goat, Cabbage)
  val chronicler = Chronicler(LeftBoat(property))
  var multiverse = listOf<History>()
  while (multiverse.isEmpty()) {
    multiverse = chronicler.sail().search {
      rightside.containsAll(property)
    }
  }
  for (history in multiverse) {
    for (boat in history) {
      print("→ $boat ")
    }
    println()
  }
}