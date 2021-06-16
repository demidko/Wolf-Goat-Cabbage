package boat

import Person.Farmer
import Riverside

/**
 * Лодка находится справа
 */
class RightBoat(left: Riverside, right: Riverside) : QuantumBoat(left, right) {

  override fun toString() = "($left⌢$right⚓️)"

  override fun invert() =
    right.map {
      LeftBoat(left + it + Farmer, right - it - Farmer)
    } + LeftBoat(left + Farmer, right - Farmer)
}