package com.example.simplearcore

import android.app.Activity
import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.view.WindowManager
import com.google.ar.core.Session
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer: Renderer {

    private lateinit var activity: Activity

    private var session: Session? = null

    private lateinit var backgroundRenderer: BackgroundRenderer

    private var viewportChanged = false
    private var viewportWidth = 0
    private var viewportHeight = 0

    fun setSession(session: Session) {
        this.session = session
    }

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    override fun onDrawFrame(unuse: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        if (session == null) {
            return
        }

        if (viewportChanged) {
            val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val displayRotation = display.getRotation()
            session!!.setDisplayGeometry(displayRotation, viewportWidth, viewportHeight)
            viewportChanged = false
        }

        session!!.setCameraTextureName(backgroundRenderer.getTextureId())
        val frame = session!!.update()
        backgroundRenderer.draw(frame)
    }

    override fun onSurfaceChanged(unuse: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        viewportWidth = width
        viewportHeight = height
        viewportChanged = true
    }

    override fun onSurfaceCreated(unuse: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        backgroundRenderer = BackgroundRenderer(activity)
    }
}