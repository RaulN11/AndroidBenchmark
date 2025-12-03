package com.example.myapplication.gpu;

import android.content.Context;
import android.opengl.GLSurfaceView;
public class GPUSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;
    public GPUSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new MyGLRenderer();
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    public MyGLRenderer getRenderer() {
        return renderer;
    }
}