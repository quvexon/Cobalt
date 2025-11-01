package org.cobalt.api.module.setting.impl

import org.cobalt.api.module.setting.Setting

class ModeSetting(
  name: String,
  description: String,
  defaultValue: Int,
  val options: Array<String>
) : Setting<Int>(name, description, defaultValue)
