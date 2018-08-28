package com.mitash.moviepedia.util

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkInfo.State
import android.telephony.TelephonyManager

import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLDecoder
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Network utils
 */
class ConnectionUtils {

    enum class NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    companion object {

        /**
         * Method to check if a device is connected to internet or not
         *
         * @param context application context
         * @return true - if the device connected to internet; false - otherwise
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager
                    .activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        /**
         * Method to check if GPS is enabled or not
         *
         * @param context application context
         * @return if gps is enabled
         */
        fun isGpsEnabled(context: Context): Boolean {
            val lm = context
                    .getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        /**
         * Method to check if network connection type is wifi
         *
         * @param context application context
         * @return if network type is wifi.
         */
        fun isWifi(context: Context): Boolean {
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
        }

        /**
         * Method to check if network connection type is 3G
         *
         * @param context application context
         * @return if network type is 3G.
         */
        fun is3G(context: Context): Boolean {
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE
        }

        /**
         * Method to check if network connection type is 4G
         *
         * @param context application context
         * @return if network type is 4G.
         */
        fun is4G(context: Context): Boolean {
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting) {
                if (activeNetInfo.type == TelephonyManager.NETWORK_TYPE_LTE) {
                    return true
                }
            }
            return false
        }

        fun isWiFiAvailable(context: Context): Boolean {
            val manager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .state
            return wifi == State.CONNECTED || wifi == State.CONNECTING

        }

        fun isIP(ip: String): Boolean {
            val pattern = Pattern
                    .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b")
            val matcher = pattern.matcher(ip)
            return matcher.matches()
        }

        fun ipToInt(addr: String): Int {
            val addrArray = addr.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var num = 0
            for (i in addrArray.indices) {
                val power = 3 - i
                num += (Integer.parseInt(addrArray[i]) % 256 * Math
                        .pow(256.0, power.toDouble())).toInt()
            }
            return num
        }

        fun getUrlParams(url: String): Map<String, String>? {
            var params: MutableMap<String, String>? = null
            try {
                val urlParts = url.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (urlParts.size > 1) {
                    params = HashMap()
                    val query = urlParts[1]
                    for (param in query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                        val pair = param.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val key = URLDecoder.decode(pair[0], "UTF-8")
                        var value = ""
                        if (pair.size > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8")
                        }
                        params[key] = value
                    }
                }
            } catch (ex: UnsupportedEncodingException) {
                ex.printStackTrace()
            }

            return params
        }

        fun isUrl(url: String): Boolean {
            try {
                val url1 = URL(url)
                return true
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return false
            }

        }

        fun isConnected(context: Context): NetState {
            var stateCode = NetState.NET_NO
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            if (ni != null && ni.isConnectedOrConnecting) {
                stateCode = when (ni.type) {
                    ConnectivityManager.TYPE_WIFI -> NetState.NET_WIFI
                    ConnectivityManager.TYPE_MOBILE -> when (ni.subtype) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA
                            , TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT
                            , TelephonyManager.NETWORK_TYPE_IDEN -> NetState.NET_2G
                        TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS
                            , TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA
                            , TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA
                            , TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD
                            , TelephonyManager.NETWORK_TYPE_HSPAP -> NetState.NET_3G
                        TelephonyManager.NETWORK_TYPE_LTE -> NetState.NET_4G
                        else -> NetState.NET_UNKNOWN
                    }
                    else -> NetState.NET_UNKNOWN
                }

            }
            return stateCode
        }
    }
}
