package org.cobalt.internal.module

data class ModuleData(
  val id: String,
  val logo: String,
  val version: String,
  val name: String,
  val description: String,
  val authors: Array<String>,

  val entryPoint: String,
  val mixinsFile: String,
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ModuleData

    if (id != other.id) return false
    if (logo != other.logo) return false
    if (version != other.version) return false
    if (name != other.name) return false
    if (description != other.description) return false
    if (!authors.contentEquals(other.authors)) return false
    if (entryPoint != other.entryPoint) return false
    if (mixinsFile != other.mixinsFile) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + logo.hashCode()
    result = 31 * result + version.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + description.hashCode()
    result = 31 * result + authors.contentHashCode()
    result = 31 * result + entryPoint.hashCode()
    result = 31 * result + mixinsFile.hashCode()
    return result
  }

}
