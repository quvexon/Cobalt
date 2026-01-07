package org.cobalt.api.util.render

import java.awt.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.util.math.Vec3d
import org.cobalt.api.event.impl.render.WorldRenderContext
import org.cobalt.internal.helper.RenderLayers

object ExtraRender3D {

  fun addWaypoint(
    context: WorldRenderContext,
    pos: Vec3d,
    color: Color,
    height: Int = 300,
    esp: Boolean = true
  ) {
    val matrix = context.matrixStack ?: return
    val bufferSource = context.consumers as? VertexConsumerProvider.Immediate ?: return
    val mc = MinecraftClient.getInstance()

    if (color.alpha == 0) return

    val bottomY = 0.0
    val topY = bottomY + height

    matrix.push()
    with(context.camera.pos) { matrix.translate(-x, -y, -z) }

    val time = (mc.world?.time ?: 0L).toDouble()
    val baseAngle = time * 0.0375

    val x = pos.x
    val y = pos.y
    val z = pos.z

    val r = color.red / 255f
    val g = color.green / 255f
    val b = color.blue / 255f
    val alpha = color.alpha / 255f
    val innerAlpha = alpha * 0.25f

    val beaconLayer = if (esp) RenderLayers.TRIANGLE_STRIP_ESP else RenderLayers.TRIANGLE_STRIP
    val buffer = bufferSource.getBuffer(beaconLayer)

    val posMatrix = matrix.peek().positionMatrix

    fun emit(px: Double, py: Double, pz: Double, a: Float) {
      buffer.vertex(posMatrix, px.toFloat(), py.toFloat(), pz.toFloat()).color(r, g, b, a)
    }

    fun rotatedSquareOffsets(
      center: Double,
      radius: Double,
      angle: Double
    ): DoubleArray {
      val out = DoubleArray(8)
      for (i in 0 until 4) {
        val t = angle + (i * (PI / 2.0))
        out[i * 2] = center + cos(t) * radius
        out[i * 2 + 1] = center + sin(t) * radius
      }
      return out
    }

    val outerRadius = 0.2
    val outerCenter = 0.5
    val outer = rotatedSquareOffsets(outerCenter, outerRadius, baseAngle)

    for (i in 0 until 4) {
      val next = (i + 1) and 3
      val x1 = x + outer[i * 2]
      val z1 = z + outer[i * 2 + 1]
      val x2 = x + outer[next * 2]
      val z2 = z + outer[next * 2 + 1]

      emit(x1, y + topY, z1, alpha)
      emit(x1, y + bottomY, z1, alpha)
      emit(x2, y + bottomY, z2, alpha)
      emit(x2, y + topY, z2, alpha)
    }

    val innerMin = 0.2
    val innerMax = 0.8
    val innerCorners = arrayOf(
      doubleArrayOf(innerMin, innerMin),
      doubleArrayOf(innerMax, innerMin),
      doubleArrayOf(innerMax, innerMax),
      doubleArrayOf(innerMin, innerMax),
    )

    for (i in 0 until 4) {
      val next = (i + 1) and 3
      val x1 = x + innerCorners[i][0]
      val z1 = z + innerCorners[i][1]
      val x2 = x + innerCorners[next][0]
      val z2 = z + innerCorners[next][1]

      emit(x1, y + topY, z1, innerAlpha)
      emit(x1, y + bottomY, z1, innerAlpha)
      emit(x2, y + bottomY, z2, innerAlpha)
      emit(x2, y + topY, z2, innerAlpha)
    }

    bufferSource.draw(beaconLayer)
    matrix.pop()
  }
}
