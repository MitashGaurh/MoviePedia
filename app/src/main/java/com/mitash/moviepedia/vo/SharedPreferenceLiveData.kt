package com.mitash.moviepedia.vo

import android.arch.lifecycle.LiveData
import android.content.SharedPreferences

/**
 * Created by Mitash Gaurh on 8/23/2018.
 */
abstract class SharedPreferenceLiveData<T>(
        val mSharedPrefs: SharedPreferences, private val mKey: String, private val mDefValue: T) : LiveData<T>() {

    private val mPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == this.mKey) {
            postValue(getValueFromPreferences(key, mDefValue))
        }
    }

    abstract fun getValueFromPreferences(key: String, defValue: T): T

    override fun onActive() {
        super.onActive()
        //postValue(getValueFromPreferences(mKey, mDefValue))
        mSharedPrefs.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener)
    }

    override fun onInactive() {
        mSharedPrefs.unregisterOnSharedPreferenceChangeListener(mPreferenceChangeListener)
        super.onInactive()
    }
}

class SharedPreferenceIntLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Int) :
        SharedPreferenceLiveData<Int>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Int): Int = mSharedPrefs.getInt(key, defValue)
}

class SharedPreferenceStringLiveData(sharedPrefs: SharedPreferences, key: String, defValue: String) :
        SharedPreferenceLiveData<String>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: String): String = mSharedPrefs.getString(key, defValue)!!
}

class SharedPreferenceBooleanLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Boolean) :
        SharedPreferenceLiveData<Boolean>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Boolean): Boolean = mSharedPrefs.getBoolean(key, defValue)
}

class SharedPreferenceFloatLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Float) :
        SharedPreferenceLiveData<Float>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Float): Float = mSharedPrefs.getFloat(key, defValue)
}

class SharedPreferenceLongLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Long) :
        SharedPreferenceLiveData<Long>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Long): Long = mSharedPrefs.getLong(key, defValue)
}

class SharedPreferenceStringSetLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Set<String>) :
        SharedPreferenceLiveData<Set<String>>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Set<String>): Set<String> = mSharedPrefs.getStringSet(key, defValue)!!
}

fun SharedPreferences.intLiveData(key: String, defValue: Int): SharedPreferenceLiveData<Int> {
    return SharedPreferenceIntLiveData(this, key, defValue)
}

fun SharedPreferences.stringLiveData(key: String, defValue: String): SharedPreferenceLiveData<String> {
    return SharedPreferenceStringLiveData(this, key, defValue)
}

fun SharedPreferences.booleanLiveData(key: String, defValue: Boolean): SharedPreferenceLiveData<Boolean> {
    return SharedPreferenceBooleanLiveData(this, key, defValue)
}

fun SharedPreferences.floatLiveData(key: String, defValue: Float): SharedPreferenceLiveData<Float> {
    return SharedPreferenceFloatLiveData(this, key, defValue)
}

fun SharedPreferences.longLiveData(key: String, defValue: Long): SharedPreferenceLiveData<Long> {
    return SharedPreferenceLongLiveData(this, key, defValue)
}

fun SharedPreferences.stringSetLiveData(key: String, defValue: Set<String>): SharedPreferenceLiveData<Set<String>> {
    return SharedPreferenceStringSetLiveData(this, key, defValue)
}