package boat

import Multiverse
import Riverside

/**
 * Квантовая лодка находится в неопределенной состоянии между двумя берегами
 */
abstract class QuantumBoat(val left: Riverside, val right: Riverside) {

  /**
   * Покинуть текущий берег и направиться к противоположному
   * @return все возможные состояния лодки
   */
  abstract fun invert(): List<QuantumBoat>


  /**
   * Отправиться в плавание по мультивселенной
   */
  fun where(condition: Riverside.() -> Boolean, selector: QuantumBoat.() -> Boolean) =
    Multiverse(this, condition).search(selector)

  // generated code

  override fun hashCode(): Int {
    var result = left.hashCode()
    result = 31 * result + right.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as QuantumBoat

    if (left != other.left) return false
    if (right != other.right) return false

    return true
  }

  // generated code
}