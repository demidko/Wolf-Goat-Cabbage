import boat.QuantumBoat

/**
 * Мультиверсум для лодки
 */
class Multiverse(boat: QuantumBoat, val condition: Riverside.() -> Boolean) {

  /**
   * Все смоделированные истории передвижений лодки
   */
  private var multiverse = sequenceOf(historyOf(boat))

  /**
   * Найти историю подходящей нам лодки
   * @param selector нужное состояние берегов и лодки
   * @return все найденные варианты достижения состояния
   */
  tailrec fun search(selector: QuantumBoat.() -> Boolean): List<History> {
    multiverse = multiverse.fork().distinct().filter {
      it.last.left.condition()
        && it.last.right.condition()
    }
    val results = multiverse.filter { it.last.selector() }.toList()
    return when {
      results.isNotEmpty() -> results
      else -> search(selector)
    }
  }
}