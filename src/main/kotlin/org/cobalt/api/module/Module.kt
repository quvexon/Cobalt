package org.cobalt.api.module

import org.cobalt.api.module.setting.Setting

abstract class Module {

  abstract fun getSettings(): List<Setting<*>>
  abstract fun onInitialize()

}
