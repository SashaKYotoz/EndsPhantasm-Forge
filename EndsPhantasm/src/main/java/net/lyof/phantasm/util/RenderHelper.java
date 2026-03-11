package net.lyof.phantasm.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class RenderHelper {
    public static void renderFace(PoseStack matrices, VertexConsumer vertices, int light,
                                  Point p1, Point p2, Point p3, Point p4) {

        matrices.pushPose();

        PoseStack.Pose entry = matrices.last();
        Matrix4f position = entry.pose();
        Matrix3f normal = entry.normal();


        // Front face
        vertices.vertex(position, p1.x, p1.y, p1.z)
                .color(255, 255, 255, 255)
                .uv(p1.u, p1.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();
        vertices.vertex(position, p2.x, p2.y, p2.z)
                .color(255, 255, 255, 255)
                .uv(p2.u, p2.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();
        vertices.vertex(position, p3.x, p3.y, p3.z)
                .color(255, 255, 255, 255)
                .uv(p3.u, p3.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();
        vertices.vertex(position, p4.x, p4.y, p4.z)
                .color(255, 255, 255, 255)
                .uv(p4.u, p4.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();


        matrices.rotateAround(p1.getRotation(p2),
                (p1.x + p2.x + p3.x + p4.x) / 4,
                (p1.y + p2.y + p3.y + p4.y) / 4,
                (p1.z + p2.z + p3.z + p4.z) / 4);

        entry = matrices.last();
        position = entry.pose();
        normal = entry.normal();

        // Back face
        vertices.vertex(position, p1.x, p1.y, p1.z)
                .color(255, 255, 255, 255)
                .uv(p1.u, p1.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();
        vertices.vertex(position, p2.x, p2.y, p2.z)
                .color(255, 255, 255, 255)
                .uv(p2.u, p2.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();
        vertices.vertex(position, p3.x, p3.y, p3.z)
                .color(255, 255, 255, 255)
                .uv(p3.u, p3.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();
        vertices.vertex(position, p4.x, p4.y, p4.z)
                .color(255, 255, 255, 255)
                .uv(p4.u, p4.v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();

        matrices.popPose();
    }

    public static void renderCube(PoseStack matrices, MultiBufferSource vertexConsumers, ResourceLocation texture, int light,
                                  float x0, float x1, float y0, float y1, float z0, float z1) {
        renderCube(matrices, vertexConsumers.getBuffer(RenderType.entitySolid(texture)), light,
                x0, x1, y0, y1, z0, z1);
    }

    public static void renderCube(PoseStack matrices, VertexConsumer vertices, int light,
                                  float x0, float x1, float y0, float y1, float z0, float z1) {
        renderCube(matrices, vertices, light,
                x0, x1, y0, y1, z0, z1, false);
    }

    public static void renderCube(PoseStack matrices, VertexConsumer vertices, int light,
                           float x0, float x1, float y0, float y1, float z0, float z1, boolean scaleTexture) {

        float dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0), dz = Math.abs(z1 - z0);
        if (scaleTexture) { dx = 1; dy = 1; dz = 1; }

        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z0, dx, dy),
                Point.of(x0, y1, z0, dx, 0),
                Point.of(x1, y1, z0, 0, 0), 
                Point.of(x1, y0, z0, 0, dy));
                //y0, y1, x0, x1, z0, z0);
        renderFace(matrices, vertices, light,
                Point.of(x1, y0, z0, dz, dy),
                Point.of(x1, y1, z0, dz, 0),
                Point.of(x1, y1, z1, 0, 0),
                Point.of(x1, y0, z1, 0, dy));
                //y0, y1, x1, x1, z0, z1);
        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z1, dx, dy),
                Point.of(x0, y1, z1, dx, 0),
                Point.of(x1, y1, z1, 0, 0),
                Point.of(x1, y0, z1, 0, dy));
                //y0, y1, x1, x0, z1, z1);
        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z0, dz, dy),
                Point.of(x0, y1, z0, dz, 0),
                Point.of(x0, y1, z1, 0, 0),
                Point.of(x0, y0, z1, 0, dy));
                //y0, y1, x0, x0, z1, z0);
        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z0, 0, dz),
                Point.of(x1, y0, z0, dx, dz),
                Point.of(x1, y0, z1, dx, 0),
                Point.of(x0, y0, z1, 0, 0));
        renderFace(matrices, vertices, light,
                Point.of(x0, y1, z0, 0, dz),
                Point.of(x1, y1, z0, dx, dz),
                Point.of(x1, y1, z1, dx, 0),
                Point.of(x0, y1, z1, 0, 0));
    }


    public static class Point {
        public float x, y, z, u, v;
        private static final Map<String, Quaternionf> rotationCache = new HashMap<>();
        private static final Map<String, Point> instanceCache = new HashMap<>();

        public static Point of(float x, float y, float z, float u, float v) {
            String key = x + " " + y + " " + z + " " + u + " " + v;
            if (instanceCache.containsKey(key)) return instanceCache.get(key);

            Point p = new Point();
            p.x = x; p.y = y; p.z = z; p.u = u; p.v = v;
            instanceCache.put(key, p);
            return p;
        }

        public Quaternionf getRotation(Point other) {
            String key = this.x + " " + this.y + " " + this.z + "/" + other.x + " " + other.y + " " + other.z;
            if (rotationCache.containsKey(key)) return rotationCache.get(key);

            rotationCache.put(key, Axis.of(new Vector3f(this.x - other.x, this.y - other.y, this.z - other.z))
                    .rotationDegrees(180));
            return rotationCache.get(key);
        }
    }
}
