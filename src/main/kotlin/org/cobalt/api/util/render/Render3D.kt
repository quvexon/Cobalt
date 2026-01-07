package org.cobalt.api.util.render

import com.mojang.blaze3d.systems.RenderSystem
import java.awt.Color
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.VertexRendering
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.cobalt.api.event.impl.render.WorldRenderContext
import org.cobalt.internal.helper.RenderLayers
import org.joml.Vector3f

object Render3D {

  fun drawBox(context: WorldRenderContext, box: Box, color: Color, esp: Boolean = false) {
    if (!FrustumUtils.isVisible(context.frustum, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)) {
      return
    }

    val matrix = context.matrixStack ?: return
    val bufferSource = context.consumers as? VertexConsumerProvider.Immediate ?: return

    val r = color.red / 255f
    val g = color.green / 255f
    val b = color.blue / 255f

    val fillLayer = if (esp) RenderLayers.TRIANGLE_STRIP_ESP else RenderLayers.TRIANGLE_STRIP
    val lineLayer = if (esp) RenderLayers.LINE_LIST_ESP else RenderLayers.LINE_LIST

    matrix.push()
    with(context.camera.pos) { matrix.translate(-x, -y, -z) }

    VertexRendering.drawFilledBox(
      matrix,
      bufferSource.getBuffer(fillLayer),
      box.minX, box.minY, box.minZ,
      box.maxX, box.maxY, box.maxZ,
      r, g, b, 150 / 255F
    )

    VertexRendering.drawBox(
      matrix.peek(),
      bufferSource.getBuffer(lineLayer),
      box.minX, box.minY, box.minZ,
      box.maxX, box.maxY, box.maxZ,
      r, g, b, 1f
    )

    matrix.pop()
    bufferSource.draw(fillLayer)
    bufferSource.draw(lineLayer)
  }

  fun drawBoxOutlined(
    context: WorldRenderContext,
    box: Box,
    color: Color,
    esp: Boolean = false,
    thickness: Float = 2f
  ) {
    if (!FrustumUtils.isVisible(context.frustum, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)) {
      return
    }

    val matrix = context.matrixStack ?: return
    val bufferSource = context.consumers as? VertexConsumerProvider.Immediate ?: return

    val r = color.red / 255f
    val g = color.green / 255f
    val b = color.blue / 255f
    val a = color.alpha / 255f

    val lineLayer = if (esp) RenderLayers.LINE_LIST_ESP else RenderLayers.LINE_LIST

    RenderSystem.lineWidth(thickness)

    matrix.push()
    with(context.camera.pos) { matrix.translate(-x, -y, -z) }

    VertexRendering.drawBox(
      matrix.peek(),
      bufferSource.getBuffer(lineLayer),
      box.minX, box.minY, box.minZ,
      box.maxX, box.maxY, box.maxZ,
      r, g, b, a
    )

    matrix.pop()
    bufferSource.draw(lineLayer)
  }

  fun drawLine(
    context: WorldRenderContext,
    start: Vec3d,
    end: Vec3d,
    color: Color,
    esp: Boolean = false,
    thickness: Float = 1f,
  ) {
    if (!FrustumUtils.isVisible(
      context.frustum,
      min(start.x, end.x), min(start.y, end.y), min(start.z, end.z),
      max(start.x, end.x), max(start.y, end.y), max(start.z, end.z)
    )) return

    val matrix = context.matrixStack ?: return
    val bufferSource = context.consumers as? VertexConsumerProvider.Immediate ?: return
    val layer = if (esp) RenderLayers.LINE_LIST_ESP else RenderLayers.LINE_LIST
    RenderSystem.lineWidth(thickness)

    matrix.push()
    with(context.camera.pos) { matrix.translate(-x, -y, -z) }

    val startOffset = Vector3f(start.x.toFloat(), start.y.toFloat(), start.z.toFloat())
    val direction = end.subtract(start)

    VertexRendering.drawVector(
      matrix,
      bufferSource.getBuffer(layer),
      startOffset,
      direction,
      color.rgb
    )

    matrix.pop()
    bufferSource.draw(layer)
  }

  fun drawRing(
    context: WorldRenderContext,
    center: Vec3d,
    radius: Double,
    color: Color,
    axis: Axis = Axis.Y,
    segments: Int = 64,
    esp: Boolean = false,
    thickness: Float = 2f
  ) {
    val angleStep = Math.PI * 2.0 / segments

    for (i in 0 until segments) {
      val angle1 = angleStep * i
      val angle2 = angleStep * ((i + 1) % segments)

      val (x1, y1, z1) = when (axis) {
        Axis.X -> Triple(0.0, cos(angle1) * radius, sin(angle1) * radius)
        Axis.Y -> Triple(cos(angle1) * radius, 0.0, sin(angle1) * radius)
        Axis.Z -> Triple(cos(angle1) * radius, sin(angle1) * radius, 0.0)
      }

      val (x2, y2, z2) = when (axis) {
        Axis.X -> Triple(0.0, cos(angle2) * radius, sin(angle2) * radius)
        Axis.Y -> Triple(cos(angle2) * radius, 0.0, sin(angle2) * radius)
        Axis.Z -> Triple(cos(angle2) * radius, sin(angle2) * radius, 0.0)
      }

      val start = center.add(x1, y1, z1)
      val end = center.add(x2, y2, z2)

      drawLine(context, start, end, color, esp, thickness)
    }
  }


  enum class Axis {
    X, Y, Z
  }
}
