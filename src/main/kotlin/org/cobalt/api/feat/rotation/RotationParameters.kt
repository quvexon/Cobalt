package org.cobalt.api.feat.rotation

abstract class RotationParameters(
  open var yawMaxOffset: Float = 0f,
  open var pitchMaxOffset: Float = 0f,
  open var canOvershoot: Boolean = true,
) {
  open fun validate() {
    TODO("Implement")
  }
}

