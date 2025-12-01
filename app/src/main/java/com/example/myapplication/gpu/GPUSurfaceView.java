package com.example.myapplication.gpu;

import android.content.Context;
import android.opengl.GLSurfaceView;

// 1. GPUSurfaceView MUST extend GLSurfaceView to function as an OpenGL rendering surface.
public class GPUSurfaceView extends GLSurfaceView {

    // 2. The Renderer is necessary to define the drawing operations (your benchmark).
    private final MyGLRenderer renderer;

    // 3. The constructor must take a Context and set up the GL environment.
    public GPUSurfaceView(Context context) {
        super(context);

        // Use OpenGL ES 2.0 (or 3.0 if you need more advanced features)
        setEGLContextClientVersion(2);

        // Instantiate your custom renderer (assuming it's in the same package)
        renderer = new MyGLRenderer();
        setRenderer(renderer);

        // This is crucial for benchmarking: Draw only when explicitly requested.
        // This stops the continuous rendering loop and makes measurement repeatable.
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    // 4. Provide a getter to access the renderer instance from MainActivity/BenchmarkUtil.
    public MyGLRenderer getRenderer() {
        return renderer;
    }
}