package org.cobalt.internal.module

import com.google.gson.JsonParser
import java.io.File
import java.io.InputStreamReader
import java.net.URLClassLoader
import java.util.jar.JarFile
import org.cobalt.api.module.Module

internal object ModuleManager {

  private val modules = hashMapOf<ModuleData, Module>()
  private val directory = File("./config/cobalt/modules")

  fun loadModules() {
    if (!directory.exists()) directory.mkdirs()

    val jars = directory.listFiles { it.extension == "jar" } ?: return
    if (jars.isEmpty()) return

    for (file in jars) {
      val classLoader = URLClassLoader(
        arrayOf(file.toURI().toURL()),
        ModuleManager::class.java.classLoader,
      )

      val jar = JarFile(file)
      val entry = jar.getEntry("cobalt.module.json")
        ?: continue

      val json = InputStreamReader(jar.getInputStream(entry)).use {
        JsonParser.parseReader(it).asJsonObject
      }

      try {
        val moduleData = ModuleData(
          id = json.get("id").asString,
          logo = json.get("logo").asString,
          version = json.get("version").asString,
          name = json.get("name").asString,
          description = json.get("description").asString,
          authors = json.getAsJsonArray("authors").map { it.asString }.toTypedArray(),
          entryPoint = json.get("entryPoint").asString,
          mixinsFile = json.get("mixinsFile").asString,
        )

        val clazz = classLoader.loadClass(moduleData.entryPoint)
        val instance = clazz.getDeclaredConstructor().newInstance()

        if (instance !is Module)
          continue

        modules[moduleData] = instance
      } catch (ex: Exception) {
        throw IllegalArgumentException("Invalid cobalt.module.json in ${file.name}", ex)
      }
    }
  }

  fun getModules(): HashMap<ModuleData, Module> {
    return modules
  }

}
