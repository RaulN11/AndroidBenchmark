package com.example.myapplication.gpu;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private static final int NUM_VERTICES = 3000;
    private FloatBuffer vertexBuffer;

    private int program;
    private int aPos;

    private boolean measure = false;
    private long startTime;

    private GraphicsCompletionListener listener;
    private InitializationListener initListener;

    public interface GraphicsCompletionListener {
        void onCompletion(long nanoTime);
    }

    public interface InitializationListener {
        void onInitialized();
    }

    public void setListener(GraphicsCompletionListener listener) {
        this.listener = listener;
    }

    public void setInitializationListener(InitializationListener listener) {
        this.initListener = listener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        program = createProgram();
        aPos = GLES20.glGetAttribLocation(program, "aPosition");
        setupVertices();
        if (initListener != null) {
            initListener.onInitialized();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (measure) {
            startTime = System.nanoTime();
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPos);
        GLES20.glVertexAttribPointer(aPos, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, NUM_VERTICES);
        GLES20.glDisableVertexAttribArray(aPos);

        if (measure) {
            GLES20.glFinish();
            long end = System.nanoTime();
            long result = end - startTime;
            if (listener != null) {
                listener.onCompletion(result);
            }

            measure = false;
        }
    }

    public void startMeasurement() {
        measure = true;
    }

    private void setupVertices() {
        float[] data = new float[NUM_VERTICES * 3];
        for (int i = 0; i < data.length; i++) {
            data[i] = (float) (Math.random() * 2f - 1f);
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4)
                .order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(data).position(0);
    }

    private int createProgram() {
        String vs =
                "attribute vec4 aPosition;" +
                        "void main(){ gl_Position = aPosition; }";

        String fs =
                "precision mediump float;" +
                        "void main(){ gl_FragColor = vec4(1.0); }";

        int v = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(v, vs);
        GLES20.glCompileShader(v);

        int f = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(f, fs);
        GLES20.glCompileShader(f);

        int p = GLES20.glCreateProgram();
        GLES20.glAttachShader(p, v);
        GLES20.glAttachShader(p, f);
        GLES20.glLinkProgram(p);

        return p;
    }
}