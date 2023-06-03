package ru.rybak

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib

class AppsflyerManager(private val context: Context) {
    private lateinit var conversionDataListener: ConversionDataListener

    fun init() {
        val appsFlyerConfiguration = AppsFlyerLib.getInstance().init("UfzmD9J3NRVH9AcyuBEvf8", appsFlyerConversionListener, context)
        appsFlyerConfiguration.setDebugLog(true)
        AppsFlyerLib.getInstance().start(context)
    }

    fun setConversionDataListener(listener: ConversionDataListener) {
        conversionDataListener = listener
    }

    fun getSubIds(): Map<String, String> {
        val sharedPrefs = context.getSharedPreferences("AppsflyerData", Context.MODE_PRIVATE)
        val sub1 = sharedPrefs.getString("sub1", "None")!!
        val sub2 = sharedPrefs.getString("sub2", "None")!!
        val sub3 = sharedPrefs.getString("sub3", "None")!!
        val sub4 = sharedPrefs.getString("sub4", "None")!!
        val sub5 = sharedPrefs.getString("sub5", "None")!!

        return mapOf(
            "sub1" to sub1,
            "sub2" to sub2,
            "sub3" to sub3,
            "sub4" to sub4,
            "sub5" to sub5
        )
    }

    interface ConversionDataListener {
        fun onConversionDataReceived(sub1: String, sub2: String, sub3: String, sub4: String, sub5: String)
    }

    private val appsFlyerConversionListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(conversionData: MutableMap<String, Any>?) {
            Log.d("AppsflyerManager", "Conversion data received: $conversionData")

            val sub1 = conversionData?.get("sub1") as? String ?: "None"
            val sub2 = conversionData?.get("sub2") as? String ?: "None"
            val sub3 = conversionData?.get("sub3") as? String ?: "None"
            val sub4 = conversionData?.get("sub4") as? String ?: "None"
            val sub5 = conversionData?.get("sub5") as? String ?: "None"

            conversionDataListener.onConversionDataReceived(sub1, sub2, sub3, sub4, sub5)
        }

        override fun onConversionDataFail(errorMessage: String?) {
            Log.e("AppsflyerManager", "Failed to receive conversion data: $errorMessage")
        }

        override fun onAppOpenAttribution(conversionData: MutableMap<String, String>?) {
            Log.d("AppsflyerManager", "App open attribution received: $conversionData")
        }

        override fun onAttributionFailure(errorMessage: String?) {
            Log.e("AppsflyerManager", "Failed to receive attribution: $errorMessage")
        }
    }
}