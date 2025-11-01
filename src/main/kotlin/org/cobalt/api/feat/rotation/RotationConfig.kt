package org.cobalt.api.feat.rotation

//TODO: Not sure if this is what we should do. I'm just doing it like this for now cuz im a bit too lazy to think icl
// I think it should be so users can put anything they want in the config and not be forced into any defaults.
// Maybe only default should be a selection of which implementation you want.
abstract class RotationConfig(
  open val easeFactor: Float,
  open val defaultEndMultiplier: Float,
  open val accurateEndMultiplier: Float,
  open val tickDelay: Long,
  open val rotationThreshold: Float,

  open val overshootDegrees: Pair<Float, Float>,
  open val overshootPause: Pair<Float, Float>,
) {
  open fun validate(): Boolean {
    // Validate slider settings
    if (easeFactor !in 2.0..20.0) return false
    if (tickDelay !in 2..20) return false
    if (rotationThreshold !in 0.1..0.9) return false

    // Validate range settings
    val (overshootLow, overshootHigh) = overshootDegrees
    if (overshootLow !in 1.0..80.0 || overshootHigh !in 1.0 .. 80.0 || overshootLow >= overshootHigh) return false

    val (pauseLow, pauseHigh) = overshootPause
    return !(pauseLow !in 0.0..250.0 || pauseHigh !in 0.0 .. 250.0 || pauseLow >= pauseHigh)
  }

}

