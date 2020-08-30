package com.example.simplearcore

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ar.core.ArCoreApk
import com.google.ar.core.ArCoreApk.InstallStatus
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.*

class MainActivity : AppCompatActivity() {

    private val TAG = BackgroundRenderer::class.java.simpleName

    private val CAMERA_PERMISSION_CODE = 0
    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private var installRequested = false

    private lateinit var surfaceView: GLSurfaceView
    private lateinit var glRenderer: GLRenderer
    private var session: Session? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        glRenderer = GLRenderer()
        glRenderer.setActivity(this)

        surfaceView = findViewById(R.id.surfaceview)
        surfaceView.preserveEGLContextOnPause = true
        surfaceView.setEGLContextClientVersion(2)
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        surfaceView.setRenderer(glRenderer)
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        surfaceView.setWillNotDraw(false)

        installRequested = false
    }

    override fun onResume() {
        super.onResume()

        when (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
            InstallStatus.INSTALL_REQUESTED -> {
                installRequested = true
                return
            }
            InstallStatus.INSTALLED -> { }
        }

        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf<String>(CAMERA_PERMISSION), CAMERA_PERMISSION_CODE)
            return
        }

        try {
            session = Session(this)
            val config: Config = session!!.getConfig()
            if (session!!.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                config.depthMode = Config.DepthMode.AUTOMATIC
            } else {
                config.depthMode = Config.DepthMode.DISABLED
            }
            session!!.configure(config)
        } catch (e: UnavailableArcoreNotInstalledException) {
            Log.e(TAG, "Please install ARCore")
        } catch (e: UnavailableUserDeclinedInstallationException) {
            Log.e(TAG, "Please install ARCore")
        } catch (e: UnavailableApkTooOldException) {
            Log.e(TAG, "Please update ARCore")
        } catch (e: UnavailableSdkTooOldException) {
            Log.e(TAG, "Please update this app")
        } catch (e: UnavailableDeviceNotCompatibleException) {
            Log.e(TAG, "This device does not support AR")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create AR session")
        }

        try {
            session!!.resume()
        } catch (e: CameraNotAvailableException) {
            Log.e(TAG, "Camera not available. Try restarting the app.")
            session = null
            return
        }
        glRenderer.setSession(session!!)

        surfaceView.onResume()
    }
}
