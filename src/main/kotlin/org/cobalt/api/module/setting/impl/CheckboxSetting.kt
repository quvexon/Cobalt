package org.cobalt.api.module.setting.impl

import org.cobalt.api.module.setting.Setting

class CheckboxSetting(
  name: String,
  description: String,
  defaultValue: Boolean
) : Setting<Boolean>(name, description, defaultValue)
