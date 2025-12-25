package org.cobalt.internal.command

import org.cobalt.api.command.Command
import org.cobalt.api.command.annotation.DefaultHandler
import org.cobalt.api.command.annotation.SubCommand
import org.cobalt.internal.rotation.EasingType
import org.cobalt.internal.rotation.RotationExec
import org.cobalt.internal.rotation.strategy.EasingStrategy
import org.cobalt.internal.ui.screen.UIScreen

object MainCommand : Command(
  name = "cobalt",
  aliases = arrayOf("cb")
) {

  @DefaultHandler
  fun main() {
    UIScreen.openUI()
  }

  @SubCommand
  fun rotate(yaw: Double, pitch: Double, duration: Int) {
    RotationExec.rotateTo(
      yaw.toFloat(), pitch.toFloat(),
      EasingStrategy(
        yawEaseType = EasingType.EASE_OUT_EXPO,
        pitchEaseType = EasingType.EASE_OUT_EXPO,
        duration = duration.toLong()
      )
    )
  }

}
