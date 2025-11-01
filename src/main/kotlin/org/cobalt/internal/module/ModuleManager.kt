package org.cobalt.internal.module

import java.io.File
import org.cobalt.api.module.Module

internal object ModuleManager {

  private val modules = mutableListOf<Module>()
  private val directory = File("./config/cobalt/modules")

  fun loadModules() {
    if (!directory.exists()) {
      directory.mkdirs()
      return
    }

    val moduleFiles = directory.listFiles { file -> file.extension == "jar" } ?: return

    for (module in moduleFiles) {
      println(module.name)
    }
  }

  fun getModules(): List<Module> {
    return modules
  }

}
