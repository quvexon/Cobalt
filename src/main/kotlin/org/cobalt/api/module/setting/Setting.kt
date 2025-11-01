package org.cobalt.api.module.setting

abstract class Setting<T>(
  val name: String,
  val description: String,
  private var value: T,
) {

  fun getValue(): T {
    return value
  }

  fun setValue(value: T) {
    this.value = value
  }

}
