import boat.QuantumBoat
import java.util.*

/**
 * История представляет список состояний лодки
 */
typealias History = LinkedList<QuantumBoat>

/**
 * @return новая история от первого состояния лодки
 */
fun historyOf(new: QuantumBoat) = History().apply { add(new) }

fun History.prettyPrint() {
  for (boat in this) {
    print("→ $boat ")
  }
  println("º")
  println()
}

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