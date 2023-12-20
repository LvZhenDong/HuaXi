package com.kklv.huaxi

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class JumpAdAccessibilityService: AccessibilityService() {
    val TAG = "kklv"

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        Log.d(TAG, "onAccessibilityEvent：$event")
        try {
            rootInActiveWindow?.let {
                val list = it.findAccessibilityNodeInfosByText("在线门诊")
                Log.i("kklv","list.size:${list.size}")
                if(list.isNullOrEmpty().not()){
                    list[0].click()
                }
            }

            Log.i("kklv","is null ${rootInActiveWindow==null}")
        }catch (e:Throwable){
            e.printStackTrace()
        }

    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.DEFAULT
            packageNames = arrayOf("info.cd120") //监听的应用包名，支持多个
            notificationTimeout = 10
        }
        setServiceInfo(serviceInfo)
    }

    // 点击
    fun AccessibilityNodeInfo.click() = performAction(AccessibilityNodeInfo.ACTION_CLICK)

    // 长按
    fun AccessibilityNodeInfo.longClick() =
        performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)

    // 向下滑动一下
    fun AccessibilityNodeInfo.scrollForward() =
        performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)

    // 向上滑动一下
    fun AccessibilityNodeInfo.scrollBackward() =
        performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)

    // 填充文本
    fun AccessibilityNodeInfo.input(content: String) = performAction(
        AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content)
        }
    )
}