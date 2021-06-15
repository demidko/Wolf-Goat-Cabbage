/**
 * Персонаж нашей ситуации может быть любым
 */
open class Person(private val name: String) {

  override fun toString() = name

  /**
   * Всегда перемещается вместе с лодкой
   */
  object Farmer : Person("👩🏻‍🌾")
}
