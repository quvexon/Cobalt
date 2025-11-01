package org.cobalt.internal.feat.rotation

import org.cobalt.api.feat.rotation.RotationConfig

object DefaultRotationConfig : RotationConfig(
  // TODO: Remove. For testing.
  easeFactor = 8f,
  tickDelay = 10,
  defaultEndMultiplier = .5f,
  accurateEndMultiplier = 1.2f,
  rotationThreshold = .5f,
  overshootDegrees = Pair(5f, 15f),
  overshootPause = Pair(50f, 100f)

  /* TODO: Please make it work like this:
  easeFactor by SliderSetting("Easing factor", 8.0, 2.0, 20.0, true),
  rotationDelay by SliderSetting("Rotation update delay", 10.0, 2.0, 20.0, true),
  rotationThreshold by SliderSetting("Rotation Complete Threshold", 0.5, 0.1, 0.9, true),

  overshootDegrees by RangeSetting("Overshoot Degrees", Pair(5.0, 15.0), 1.0, 80.0, true),
  overshootPause by RangeSetting("Overshoot Pause Time (ms)", Pair(50.0, 100.0), 0.0, 250.0, false),
  */
)
