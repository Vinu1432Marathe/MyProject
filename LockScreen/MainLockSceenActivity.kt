package com.vinayak.semicolon.securefolderhidefiles.LockScreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Activity.MainActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.BiometricHelper
import com.vinayak.semicolon.securefolderhidefiles.Other.SharePref
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File
import java.io.FileOutputStream
import kotlin.jvm.java

class MainLockSceenActivity : BaseActivity() {

    private lateinit var pinViews: List<TextView>
    private var enteredPin = ""


    private var cameraDevice: CameraDevice? = null
    private var imageReader: ImageReader? = null
    private val cameraThread = HandlerThread("CameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lock_sceen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkFingerprintAvailability(this)


        pinViews = listOf(
            findViewById(R.id.pin1),
            findViewById(R.id.pin2),
            findViewById(R.id.pin3),
            findViewById(R.id.pin4)
        )

        val digits = listOf(
            Pair(R.id.key0, "0"), Pair(R.id.key1, "1"), Pair(R.id.key2, "2"),
            Pair(R.id.key3, "3"), Pair(R.id.key4, "4"), Pair(R.id.key5, "5"),
            Pair(R.id.key6, "6"), Pair(R.id.key7, "7"), Pair(R.id.key8, "8"),
            Pair(R.id.key9, "9")
        )

        digits.forEach { (id, digit) ->
            findViewById<Button>(id).setOnClickListener { onDigitPressed(digit) }
        }

        findViewById<ImageView>(R.id.keyDel).setOnClickListener {
            if (enteredPin.isNotEmpty()) {
                enteredPin = enteredPin.dropLast(1)
                updatePinViews()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun onDigitPressed(digit: String) {
        if (enteredPin.length < 4) {
            enteredPin += digit
            updatePinViews()

            if (enteredPin.length == 4) {

                Toast.makeText(this, "Entered PIN: $enteredPin", Toast.LENGTH_SHORT).show()
                // Proceed to next screen or validation
                val SavedPin = SharePref.getPin(this)
                if (enteredPin.equals(SavedPin)) {
                    val intent = Intent(this, MainActivity::class.java)
                        .putExtra("Password", enteredPin)
                    startActivity(intent)
                } else {

                    Toast.makeText(this, "Entered PIN: $enteredPin", Toast.LENGTH_SHORT).show()
                    capturePhoto()
                    enteredPin = ""
                    updatePinViews()
                }

            }
        }
    }

    private fun updatePinViews() {
        pinViews.forEachIndexed { index, textView ->
            textView.text = if (index < enteredPin.length) enteredPin[index].toString() else ""
        }
    }



    private fun capturePhoto() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = manager.cameraIdList.first {
                manager.getCameraCharacteristics(it)
                    .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        startSession()
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        camera.close()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        camera.close()
                    }
                }, cameraHandler)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startSession() {
        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
        imageReader?.setOnImageAvailableListener({
            val image = it.acquireLatestImage()
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            saveImage(bytes)
            image.close()
        }, cameraHandler)

        val surface = imageReader!!.surface
        cameraDevice?.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                val request = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                request.addTarget(surface)
                request.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                request.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                request.set(CaptureRequest.JPEG_QUALITY, 100.toByte()) // Max quality

                session.capture(request.build(), null, cameraHandler)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {}
        }, cameraHandler)
    }



    private fun saveImage(bytes: ByteArray) {
        try {
            // Decode JPEG byte array to bitmap
            var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            // Rotate bitmap based on device orientation (90 or 270 for front camera)
            val matrix = Matrix()
            matrix.postRotate(270f) // Try 270 or 90 depending on test result

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            // Save the rotated bitmap
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ".HideSecureFolder/IntruderSelfie")
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, "intruder_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            Log.d("Camera", "Image saved at: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraDevice?.close()
        imageReader?.close()
        cameraThread.quitSafely()
    }


    fun checkFingerprintAvailability(context: Context) {
        val biometricManager = BiometricManager.from(context)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                checkFingerpr()
//                Toast.makeText(context, "Fingerprint available and enrolled", Toast.LENGTH_SHORT).show()

//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
////                checkFingerpr()
////                Toast.makeText(context, "No biometric hardware available", Toast.LENGTH_SHORT).show()
//
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
////                checkFingerpr()
////                Toast.makeText(context, "Biometric hardware currently unavailable", Toast.LENGTH_SHORT).show()
//
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
////                checkFingerpr()
//                Toast.makeText(context, "No fingerprint enrolled", Toast.LENGTH_SHORT).show()
        }
    }
    fun checkFingerpr() {
        BiometricHelper(
            activity = this,
            onSuccess = {
                Toast.makeText(this, "Unlocked!", Toast.LENGTH_SHORT).show()
                // navigate to home screen or unlock app content
                val intent = Intent(this, MainActivity::class.java)
                    .putExtra("Password", enteredPin)
                startActivity(intent)
            },
            onError = {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        ).showBiometricCustomDialog()

    }
}