package org.cobalt.api.module.setting.impl

import org.cobalt.api.module.setting.Setting

class SliderSetting(
  name: String,
  description: String,
  defaultValue: Double,
  val min: Double,
  val max: Double
) : Setting<Double>(name, description, defaultValue)
