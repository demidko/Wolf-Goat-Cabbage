package boat

import Person.Farmer
import Riverside

/**
 * Лодка находится слева
 */
class LeftBoat(left: Riverside, right: Riverside = emptySet()) : QuantumBoat(left, right) {

  override fun toString() = "(⚓️$left⌢$right)️"

  override fun invert() =
    left.map {
      RightBoat(left - it - Farmer, right + it + Farmer)
    } + RightBoat(left - Farmer, right + Farmer)
}