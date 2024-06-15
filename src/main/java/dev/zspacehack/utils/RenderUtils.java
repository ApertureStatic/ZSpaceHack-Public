/**
 * A class that contains all of the drawing functions.
 */
package dev.zspacehack.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zspacehack.gui.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_BLEND;

public class RenderUtils {
	public static java.awt.Color injectAlpha(final java.awt.Color color, final int alpha) {
		int alph = MathHelper.clamp(alpha, 0, 255);
		return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), alph);
	}

	public static float animate(float in, float target, float delta) {

		float out = (target - in) / Math.max((float) MinecraftClient.getInstance().getCurrentFps(), 5.0f) * 15.0f;

		if (out > 0.0f) {
			out = Math.max(delta, out);
			out = Math.min(target - in, out);
		} else if (out < 0.0f) {
			out = Math.min(-delta, out);
			out = Math.max(target - in, out);
		}

		return in + out;
	}

	public void drawBox(MatrixStack matrixStack, int x, int y, int width, int height, float r, float g, float b,
						float alpha) {

		RenderSystem.setShaderColor(r, g, b, alpha);

		GL11.glEnable(GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();

		tessellator.draw();

		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL_BLEND);
	}

	public void drawBox(MatrixStack matrixStack, int x, int y, int width, int height, Color color, float alpha) {

		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);

		GL11.glEnable(GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();

		tessellator.draw();

		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL_BLEND);
	}

	public void drawOutlinedBox(MatrixStack matrixStack, int x, int y, int width, int height, Color color,
								float alpha) {

		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);

		GL11.glEnable(GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();

		tessellator.draw();

		RenderSystem.setShaderColor(0, 0, 0, alpha);
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y, 0).next();

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL_BLEND);
	}

	public void drawOutline(MatrixStack matrixStack, int x, int y, int width, int height) {

		RenderSystem.setShaderColor(0, 0, 0, 1);

		GL11.glEnable(GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y, 0).next();

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL_BLEND);
	}

	public void draw3DBox(MatrixStack matrixStack, Box box, Color color, float alpha) {
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), 1.0f);

		GL11.glEnable(GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();

		tessellator.draw();

		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL_BLEND);
	}

	public void drawLine3D(MatrixStack matrixStack, Vec3d pos, Vec3d pos2, Color color) {
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), 1.0f);

		GL11.glEnable(GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z).next();
		bufferBuilder.vertex(matrix, (float) pos2.x, (float) pos2.y, (float) pos2.z).next();

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL_BLEND);
	}

	public void drawString(DrawContext drawContext, String text, float x, float y, Color color) {
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(2.0f, 2.0f, 1.0f);
		matrixStack.translate(-x / 2, -y / 2, 0.0f);
		drawContext.drawText(mc.textRenderer, text, (int) x, (int) y, color.getColorAsInt(), true);
		matrixStack.pop();
	}

	public void drawString(DrawContext drawContext, String text, double x, double y, int color) {
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(2.0f, 2.0f, 1.0f);
		matrixStack.translate(-x / 2, -y / 2, 0.0f);
		drawContext.drawText(mc.textRenderer, text, (int) x, (int) y, color, true);
		matrixStack.pop();
	}

	public void drawStringWithScale(DrawContext drawContext, String text, float x, float y, Color color, float scale) {
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(scale, scale, 1.0f);
		if (scale > 1.0f) {
			matrixStack.translate(-x / scale, -y / scale, 0.0f);
		} else {
			matrixStack.translate((x / scale) - x, (y * scale) - y, 0.0f);
		}
		drawContext.drawText(mc.textRenderer, text, (int) x, (int) y, color.getColorAsInt(), true);
		matrixStack.pop();
	}

	public void drawStringWithScale(DrawContext drawContext, String text, float x, float y, int color, float scale) {
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(scale, scale, 1.0f);
		if (scale > 1.0f) {
			matrixStack.translate(-x / scale, -y / scale, 0.0f);
		} else {
			matrixStack.translate(x / scale, y * scale, 0.0f);
		}
		drawContext.drawText(mc.textRenderer, text, (int) x, (int) y, color, true);
		matrixStack.pop();
	}

	public static void applyRenderOffset(MatrixStack matrixStack) {
		Vec3d camPos = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getPos();
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
	}

	public static void applyRegionalRenderOffset(MatrixStack matrixStack) {
		Vec3d camPos = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getPos();
		BlockPos camBlockPos = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getBlockPos();

		int regionX = (camBlockPos.getX() >> 9) * 512;
		int regionZ = (camBlockPos.getZ() >> 9) * 512;

		matrixStack.translate(regionX - camPos.x, -camPos.y, regionZ - camPos.z);
	}


	public static void renderRoundedOutlineInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double radC1, double radC2,
													double radC3, double radC4, double width, double samples) {
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

		double[][] map = new double[][]{new double[]{toX - radC4, toY - radC4, radC4}, new double[]{toX - radC2, fromY + radC2, radC2},
				new double[]{fromX + radC1, fromY + radC1, radC1}, new double[]{fromX + radC3, toY - radC3, radC3}};
		for (int i = 0; i < 4; i++) {
			double[] current = map[i];
			double rad = current[2];
			for (double r = i * 90d; r < 360 / 4d + i * 90d; r += 90 / samples) {
				float rad1 = (float) Math.toRadians(r);
				double sin1 = Math.sin(rad1);
				float sin = (float) (sin1 * rad);
				double cos1 = Math.cos(rad1);
				float cos = (float) (cos1 * rad);
				bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
				bufferBuilder.vertex(matrix, (float) (current[0] + sin + sin1 * width), (float) (current[1] + cos + cos1 * width), 0.0F).color(cr, cg, cb, ca).next();
			}
			float rad1 = (float) Math.toRadians(360 / 4d + i * 90d);
			double sin1 = Math.sin(rad1);
			float sin = (float) (sin1 * rad);
			double cos1 = Math.cos(rad1);
			float cos = (float) (cos1 * rad);
			bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
			bufferBuilder.vertex(matrix, (float) (current[0] + sin + sin1 * width), (float) (current[1] + cos + cos1 * width), 0.0F).color(cr, cg, cb, ca).next();
		}
		int i = 0;
		double[] current = map[i];
		double rad = current[2];
		float cos = (float) rad;
		bufferBuilder.vertex(matrix, (float) current[0], (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
		bufferBuilder.vertex(matrix, (float) current[0], (float) (current[1] + cos + width), 0.0F).color(cr, cg, cb, ca).next();
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}



	public void drawRoundedOutlinedBox(MatrixStack matrixStack, int x, int y, int width, int height, float radius, Color color, float alpha) {
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);

		GL11.glEnable(GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		// Draw filled rounded rectangle
		for (int cornerX = x; cornerX < x + width; cornerX += 2) {
			for (int cornerY = y; cornerY < y + height; cornerY += 2) {
				drawCircleSector(matrix, cornerX, cornerY, radius);
			}
		}

		// Connect corners with lines
		RenderSystem.setShaderColor(0, 0, 0, alpha);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		// Top and bottom edges
		for (float i = x + radius; i <= x + width - radius; i++) {
			bufferBuilder.vertex(matrix, i, y, 0).next();
			bufferBuilder.vertex(matrix, i, y + height, 0).next();
		}
		for (float i = y + radius; i <= y + height - radius; i++) {
			bufferBuilder.vertex(matrix, x, i, 0).next();
			bufferBuilder.vertex(matrix, x + width, i, 0).next();
		}

		// Corners
		connectCornersWithLines(matrix, x, y, width, height, radius, bufferBuilder);

		tessellator.draw();

		RenderSystem.setShaderColor(1, 1, 1, 1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL_BLEND);
	}

		// Helper method to draw a circular sector
		private void drawCircleSector(Matrix4f matrix, float centerX, float centerY, float radius) {
			BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
			Tessellator tessellator = RenderSystem.renderThreadTesselator();
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
			final int circleSteps = 32; // Adjust this value for more or fewer circle segments
			for (int i = 0; i <= circleSteps; i++) {
				double angle = 2 * Math.PI * i / circleSteps;
				float x = (float) (centerX + radius * Math.cos(angle));
				float y = (float) (centerY + radius * Math.sin(angle));
				bufferBuilder.vertex(matrix, x, y, 0).next();
			}

			tessellator.draw();
		}

		// Helper method to connect corners with lines
		private void connectCornersWithLines(Matrix4f matrix, int x, int y, int width, int height, float radius, BufferBuilder bufferBuilder) {
			bufferBuilder.vertex(matrix, x + radius, y, 0).next();
			bufferBuilder.vertex(matrix, x + width - radius, y, 0).next();
			bufferBuilder.vertex(matrix, x + width, y + radius, 0).next();
			bufferBuilder.vertex(matrix, x + width, y + height - radius, 0).next();
			bufferBuilder.vertex(matrix, x + width - radius, y + height, 0).next();
			bufferBuilder.vertex(matrix, x + radius, y + height, 0).next();
			bufferBuilder.vertex(matrix, x, y + height - radius, 0).next();
			bufferBuilder.vertex(matrix, x, y + radius, 0).next();
			bufferBuilder.vertex(matrix, x + radius, y, 0).next(); // Close the line strip
		}

		public static void drawRect(MatrixStack matrices, float x, float y, float width, float height, Color c,float alpha) {
			Matrix4f matrix = matrices.peek().getPositionMatrix();
			BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
			RenderSystem.setShader(GameRenderer::getPositionColorProgram);
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
			bufferBuilder.vertex(matrix, x, y + height, 0.0F).color(c.getRedFloat(), c.getGreenFloat(), c.getBlueFloat(),alpha).next();
			bufferBuilder.vertex(matrix, x + width, y + height, 0.0F).color(c.getRedFloat(), c.getGreenFloat(), c.getBlueFloat(), alpha).next();
			bufferBuilder.vertex(matrix, x + width, y, 0.0F).color(c.getRedFloat(), c.getGreenFloat(), c.getBlueFloat(), alpha).next();
			bufferBuilder.vertex(matrix, x, y, 0.0F).color(c.getRedFloat(), c.getGreenFloat(), c.getBlueFloat(), alpha).next();
			Tessellator.getInstance().draw();
			RenderSystem.disableBlend();
		}
	public static void renderRoundedQuadInternal2(Matrix4f matrix, float cr, float cg, float cb, float ca, float cr1, float cg1, float cb1, float ca1, float cr2, float cg2, float cb2, float ca2, float cr3, float cg3, float cb3, float ca3, double fromX, double fromY, double toX, double toY, double radC1) {
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

		double[][] map = new double[][]{new double[]{toX - radC1, toY - radC1, radC1}, new double[]{toX - radC1, fromY + radC1, radC1}, new double[]{fromX + radC1, fromY + radC1, radC1}, new double[]{fromX + radC1, toY - radC1, radC1}};

		for (int i = 0; i < 4; i++) {
			double[] current = map[i];
			double rad = current[2];
			for (double r = i * 90; r < (90 + i * 90); r += 10) {
				float rad1 = (float) Math.toRadians(r);
				float sin = (float) (Math.sin(rad1) * rad);
				float cos = (float) (Math.cos(rad1) * rad);
				switch (i) {
					case 0 ->
							bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr1, cg1, cb1, ca1).next();
					case 1 ->
							bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
					case 2 ->
							bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr2, cg2, cb2, ca2).next();
					default ->
							bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr3, cg3, cb3, ca3).next();
				}/*
                if (i == 1) {
                    bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
                } else if (i == 0) {
                    bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr1, cg1, cb1, ca1).next();
                } else if (i == 2) {
                    bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr2, cg2, cb2, ca2).next();
                } else {
                    bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr3, cg3, cb3, ca3).next();
                }*/
			}
		}
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}

	public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca1, float cr1, float cg1, float cb1, float ca, double fromX, double fromY, double toX, double toY, double radius, double samples) {
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

		double[][] map = new double[][]{new double[]{toX - radius, toY - radius, radius}, new double[]{toX - radius, fromY + radius, radius}, new double[]{fromX + radius, fromY + radius, radius}, new double[]{fromX + radius, toY - radius, radius}};
		for (int i = 0; i < 4; i++) {
			double[] current = map[i];
			double rad = current[2];
			for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
				float rad1 = (float) Math.toRadians(r);
				float sin = (float) (Math.sin(rad1) * rad);
				float cos = (float) (Math.cos(rad1) * rad);
				if (i == 1 || i == 0) {
					bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
				} else {
					bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr1, cg1, cb1, ca1).next();
				}
			}
		}
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}
	public static void renderRoundedQuad2(MatrixStack matrices, java.awt.Color c, java.awt.Color c2, java.awt.Color c3, java.awt.Color c4, double fromX, double fromY, double toX, double toY, double radius) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		renderRoundedQuadInternal2(matrices.peek().getPositionMatrix(), c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f, c2.getRed() / 255f, c2.getGreen() / 255f, c2.getBlue() / 255f, c2.getAlpha() / 255f, c3.getRed() / 255f, c3.getGreen() / 255f, c3.getBlue() / 255f, c3.getAlpha() / 255f, c4.getRed() / 255f, c4.getGreen() / 255f, c4.getBlue() / 255f, c4.getAlpha() / 255f, fromX, fromY, toX, toY, radius);
		RenderSystem.disableBlend();
	}



}





