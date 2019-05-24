package jp.ac.titech.itpro.sdl.die;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Icosahedron implements Obj {

    private final static float[] VERTICES = {
            0.0f,         0.766994f,          0.0f,       // #0, Top-most vertex

            0.0f,         0.346410f,          0.680521f,  // #1, (See the scratch paper)
            -0.647214f,    0.346410f,          0.210293f,  // #2, (See the scratch paper)
            -0.4f,         0.346410f,         -0.550553f,  // #3, (See the scratch paper)
            0.4f,         0.346410f,         -0.550553f,  // #4, (See the scratch paper)
            0.647214f,    0.346410f,          0.210293f,  // #5, (See the scratch paper)

            -0.4f,        -0.346410f,          0.550553f,  // #6, (See the scratch paper)
            0.4f,        -0.346410f,          0.550553f,  // #7, (See the scratch paper)
            -0.647214f,   -0.346410f,         -0.210293f,  // #8, (See the scratch paper)
            0.647214f,   -0.346410f,         -0.210293f,  // #9, (See the scratch paper)
            0.0f,        -0.346410f,         -0.680521f,  // #10, (See the scratch paper)

            0.0f,        -0.766994f,         0.0f         // #11, Bottom-most vertex
    };

    private final static int[] INDEX = {
            0, 1, 5, // Face of #0
            0, 5, 4, // Face of #1
            0, 4, 3, // Face of #2
            0, 3, 2, // Face of #3
            0, 2, 1, // Face of #4

            1, 6, 7,
            1, 2, 6,
            2, 8, 6,
            2, 3, 8,
            3, 10, 8,
            3, 4, 10,
            4, 9, 10,
            4, 5, 9,
            5, 7, 9,
            1, 7, 5,

            7, 6, 11,
            6, 8, 11,
            8, 10, 11,
            10, 9, 11,
            9, 7, 11
    };

    private final FloatBuffer vbuf;
    private float[] surfaceNormalVector;

    Icosahedron() {

        // Create vertex array
        float coefficient = 2.0f;
        float[] verticesRaw = new float[INDEX.length * 3];
        for(int i = 0; i < INDEX.length; ++i) {
            verticesRaw[i * 3] = VERTICES[3 * INDEX[i]] * coefficient;
            verticesRaw[i * 3 + 1] = VERTICES[3 * INDEX[i] + 1] * coefficient;
            verticesRaw[i * 3 + 2] = VERTICES[3 * INDEX[i] + 2] * coefficient;
        }

        // Bind vertex buffer
        vbuf = ByteBuffer
                .allocateDirect(verticesRaw.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vbuf.put(verticesRaw);
        vbuf.position(0);


        // Calculate the Normal of EACH Surface
        surfaceNormalVector = new float[3 * INDEX.length];
        for (int i = 0; i < (INDEX.length / 3); ++i)
        {
            // Normal
            float[] normalVector = new float[3];
            float[] diff1 = new float[3];
            float[] diff2 = new float[3];
            int v0PI = INDEX[i * 3];
            int v1PI = INDEX[i * 3 + 1];
            int v2PI = INDEX[i * 3 + 2];
            // Difference 1 = v1 - v0
            diff1[0] = VERTICES[v1PI * 3] - VERTICES[v0PI * 3];
            diff1[1] = VERTICES[v1PI * 3 + 1] - VERTICES[v0PI * 3 + 1];
            diff1[2] = VERTICES[v1PI * 3 + 2] - VERTICES[v0PI * 3 + 2];
            // Difference 2 = v2 - v0
            diff2[0] = VERTICES[v2PI * 3] - VERTICES[v0PI * 3];
            diff2[1] = VERTICES[v2PI * 3 + 1] - VERTICES[v0PI * 3 + 1];
            diff2[2] = VERTICES[v2PI * 3 + 2] - VERTICES[v0PI * 3 + 2];
            cross(diff1, diff2, normalVector);

            // Store the Result
            surfaceNormalVector[3 * i] = normalVector[0];
            surfaceNormalVector[3 * i + 1] = normalVector[1];
            surfaceNormalVector[3 * i + 2] = normalVector[2];
        }
    }

    @Override
    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vbuf);

        // Draw each face
        for (int i = 0; i < INDEX.length / 3; ++i) {
            float nx = surfaceNormalVector[3 * i];
            float ny = surfaceNormalVector[3 * i + 1];
            float nz = surfaceNormalVector[3 * i + 2];
            gl.glNormal3f(nx, ny, nz);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 3 * i, 3);
        }

    }


    /**
     * Calculate cross product
     * @param p1
     * @param p2
     * @param result
     */
    private static void cross(float[] p1, float[] p2, float[] result) {
        result[0] = p1[1] * p2[2] - p2[1] * p1[2];
        result[1] = p1[2] * p2[0] - p2[2] * p1[0];
        result[2] = p1[0] * p2[1] - p2[0] * p1[1];
    }
}
