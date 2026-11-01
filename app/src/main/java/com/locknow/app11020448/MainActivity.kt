package com.locknow.app11020448

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import rikka.shizuku.Shizuku
import java.util.concurrent.Executors

class MainActivity : Activity() {
    companion object {
        private const val REQUEST_CODE = 11020448
    }

    private val executor = Executors.newSingleThreadExecutor()
    private var requestInFlight = false

    private val permissionListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode != REQUEST_CODE) return@OnRequestPermissionResultListener
        requestInFlight = false
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            lockScreen()
        } else {
            toastAndClose("未获得 Shizuku 授权")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shizuku.addRequestPermissionResultListener(permissionListener)

        if (!Shizuku.pingBinder()) {
            toastAndClose("请先启动 Shizuku")
            return
        }

        when (Shizuku.checkSelfPermission()) {
            PackageManager.PERMISSION_GRANTED -> lockScreen()
            else -> {
                if (!requestInFlight) {
                    requestInFlight = true
                    Shizuku.requestPermission(REQUEST_CODE)
                }
            }
        }
    }

    private fun lockScreen() {
        executor.execute {
            try {
                val process = Shizuku.newProcess(arrayOf("sh", "-c", "input keyevent 223"), null, null)
                val exitCode = process.waitFor()
                runOnUiThread {
                    if (exitCode != 0) Toast.makeText(this, "锁屏命令执行失败", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (_: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "锁屏失败，请检查 Shizuku 状态和授权", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun toastAndClose(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onDestroy() {
        Shizuku.removeRequestPermissionResultListener(permissionListener)
        executor.shutdown()
        super.onDestroy()
    }
}
