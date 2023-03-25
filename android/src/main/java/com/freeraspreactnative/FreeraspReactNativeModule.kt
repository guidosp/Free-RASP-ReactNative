package com.freeraspreactnative

import com.aheaditec.talsec_security.security.api.Talsec
import com.aheaditec.talsec_security.security.api.TalsecConfig
import com.aheaditec.talsec_security.security.api.ThreatListener
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import android.os.Build

class FreeraspReactNativeModule(val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), ThreatListener.ThreatDetected, FreeraspDeviceStateListener.DeviceStateListener {

  private val listener = ThreatListener(this, FreeraspDeviceStateListener)

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun talsecStart(
    options: ReadableMap
  ) {

    try {
      val model = Build.MODEL
      val listAllowj5 = arrayOf("SM-G610F", "SM-G610Y", "SM-G610M", "SM-G610", "SM-G570F", "SM-G570F/DD", "SM-G570F", "SM-G570Y", "SM-G570M", "SM-J530F", "SM-J530Y", "SM-J530FM", "SM-J530G", "SM-J530YM", "SM-J530L", "SM-J530S", "SM-J530K", "SM-J530GM", "SM-J510F", "SM-J510G", "SM-J510FN", "SM-J510Y", "SM-J510M", "SM-J510GN", "SM-J510H", "SM-J510MN", "SM-J5108", "SM-J510UN", "SM-J510L", "SM-J510S", "SM-J510FQ", "SM-J510K", "SM-G571F")
      val listAllowj6 = arrayOf("SM-G570F", "SM-G570F/DD", "SM-G570F", "SM-G570Y", "SM-G570M", "SM-J530F", "SM-J530Y", "SM-J530FM", "SM-J530G", "SM-J530YM", "SM-J530L", "SM-J530S", "SM-J530K", "SM-J530GM", "SM-J510F", "SM-J510G", "SM-J510FN", "SM-J510Y", "SM-J510M", "SM-J510GN", "SM-J510H", "SM-J510MN", "SM-J5108", "SM-J510UN", "SM-J510L", "SM-J510S", "SM-J510FQ", "SM-J510K", "SM-G571F", "SM-J600G", "SM-J600F", "SM-J600G", "SM-J600FN", "SM-J600GF", "SM-J600GT", "SM-J600L", "SM-J600N")
      val listAllowj7=arrayOf("SM-G610F","SM-G610Y", "SM-G610M", "SM-G610","SM-J700F", "SM-J700H", "SM-J700M"," SM-J700T", "J7", "SM-J700T1", "SM-J700K"," SM-J700P","SM-j7008","SM-J730G","SM-J730GM","SM-J710FN","SM-J710F","SM-J710H"," SM-J710M", "SM-J710GN"," SM-J710MN", "SM-J710K"," SM-J7108", "SM-J710FQ","SM-J701F", "SM-J701F"," SM-J701M", "SM-J701MT","SM-G615F", "SM-G615FU","SM-J730F", "SM-J730FM", "SM-S727VL","SM-J730K","SM-J720F", "SM-J720F/DS", "SM-J720M", "SM-J720M/DS","SM-J720F", "SM-J720F/DS", "SM-J720M", "SM-J720M/DS","SM-J737F","SM-J737V", "SM-J737T", "SM-J737A"," SM-J737P", "SM-J737T1", "SM-J737U", "SM-J737S")
      val listAllowj8 = arrayOf("SM-G570F", "SM-G570F/DD", "SM-G570F", "SM-G570Y", "SM-G570M", "SM-J530F", "SM-J530Y", "SM-J530FM", "SM-J530G", "SM-J530YM", "SM-J530L", "SM-J530S", "SM-J530K", "SM-J530GM", "SM-J510F", "SM-J510G", "SM-J510FN", "SM-J510Y", "SM-J510M", "SM-J510GN", "SM-J510H", "SM-J510MN", "SM-J5108", "SM-J510UN", "SM-J510L", "SM-J510S", "SM-J510FQ", "SM-J510K", "SM-G571F", "SM-J600G", "SM-J600F", "SM-J600G", "SM-J600FN", "SM-J600GF", "SM-J600GT", "SM-J600L", "SM-J600N", "SM-J810G", "SM-J810F", "J810Y", "SM-J810Y", "SM-J810GF", "SM-J810M")

      val listAllow: Array<Array<String>> = arrayOf(listAllowj5, listAllowj6, listAllowj7, listAllowj8)
      if (!listAllow.any { it.contains(model) }) {
        val config = parseTalsecConfig(options)
        FreeraspDeviceStateListener.listener = this
        listener.registerListener(reactContext)
        Talsec.start(reactContext, config)
        sendOngoingPluginResult("started", null)
      }
    } catch (e: Exception) {
      val params = Arguments.createMap().apply {
        putString("message", e.message)
      }
      sendOngoingPluginResult("initializationError", params)
    }
  }

  @ReactMethod
  fun addListener(@Suppress("UNUSED_PARAMETER") eventName: String) {
    // Set up any upstream listeners or background tasks as necessary
  }

  @ReactMethod
  fun removeListeners(@Suppress("UNUSED_PARAMETER") count: Int) {
    // Remove upstream listeners, stop unnecessary background tasks
  }

  override fun onRootDetected() {
    sendOngoingPluginResult("privilegedAccess", null)
  }

  override fun onDebuggerDetected() {
    sendOngoingPluginResult("debug", null)
  }

  override fun onEmulatorDetected() {
    sendOngoingPluginResult("simulator", null)
  }

  override fun onTamperDetected() {
    sendOngoingPluginResult("appIntegrity", null)
  }

  override fun onUntrustedInstallationSourceDetected() {
    sendOngoingPluginResult("unofficialStore", null)
  }

  override fun onHookDetected() {
    sendOngoingPluginResult("hooks", null)
  }

  override fun onDeviceBindingDetected() {
    sendOngoingPluginResult("device binding", null)
  }

  override fun deviceStateChangeDetected(threatType: String) {
    sendOngoingPluginResult(threatType, null)
  }

  private fun sendOngoingPluginResult(eventName: String, params: WritableMap?) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }

  private fun parseTalsecConfig(config: ReadableMap): TalsecConfig {
    val androidConfig = config.getMap("androidConfig")!!
    val packageName = androidConfig.getString("packageName")!!
    val certificateHashes = mutableListOf<String>()
    val hashes = androidConfig.getArray("certificateHashes")!!
    for (i in 0 until hashes.size()) {
      certificateHashes.add(hashes.getString(i))
    }
    val watcherMail = config.getString("watcherMail")
    val alternativeStores = mutableListOf<String>()
    if (androidConfig.hasKey("supportedAlternativeStores")) {
      val stores = androidConfig.getArray("supportedAlternativeStores")!!
      for (i in 0 until stores.size()) {
        alternativeStores.add(stores.getString(i))
      }
    }
    return TalsecConfig(packageName, certificateHashes.toTypedArray(), watcherMail, alternativeStores.toTypedArray())
  }

  companion object {
    const val NAME = "FreeraspReactNative"
  }
}
