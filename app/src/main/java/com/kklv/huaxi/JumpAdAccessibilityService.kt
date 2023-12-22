package com.kklv.huaxi

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class JumpAdAccessibilityService : AccessibilityService() {
    val TAG = "kklv"

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        Log.d(TAG, "onAccessibilityEvent：$event")
        try {
            rootInActiveWindow?.let {
                val doctorHomeList = it.findAccessibilityNodeInfosByText("彭兵医生")
                Log.i("kklv", "list.size:${doctorHomeList.size}")
                if (doctorHomeList.isNullOrEmpty().not()) {
                    doctorHome(it)
                }

                val selectCardList = it.findAccessibilityNodeInfosByText("选择就诊卡")
                Log.i("kklv", "list.size:${selectCardList.size}")
                if (selectCardList.isNullOrEmpty().not()) {
                    selectCard(it)
                }

                val reservation = it.findAccessibilityNodeInfosByText("就诊地点")
                Log.i("kklv", "list.size:${reservation.size}")
                if (reservation.isNullOrEmpty().not()) {
                    reservation(it)
                }


            }

            Log.i("kklv", "is null ${rootInActiveWindow == null}")
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 医生主页，预约
     */
    private fun doctorHome(it: AccessibilityNodeInfo) {
        val tvList = it.findAccessibilityNodeInfosByViewId("info.cd120:id/tvStatus")
        Log.i("kklv", "tvList size:${tvList.size}")
        tvList?.let {
            tvList[1].click()
        }
//        tvList?.forEach { item ->
//            Log.i("kklv", "text:${item.text}")
//            if (item.text == "预约") item.click()
//            return@forEach
//        }
    }

    /**
     * 医生主页，预约
     */
    private fun selectCard(it: AccessibilityNodeInfo) {
        val tvList = it.findAccessibilityNodeInfosByText("选择此卡")
        Log.i("kklv", "选择此卡 tvList size:${tvList.size}")
        tvList?.let {
            it[0].parent.click()
        }
    }

    /**
     * 医生主页，预约
     */
    private fun reservation(it: AccessibilityNodeInfo) {
        val tvList = it.findAccessibilityNodeInfosByText("确认预约")
        Log.i("kklv", "挂号 tvList size:${tvList.size}")
        tvList?.let {
            val current = System.currentTimeMillis()
            Log.i("kklv", "click:$click")
            Log.i("kklv", "current:$current")
            Log.i("kklv", "差值:${current - click}")
            if (click == 0L || (current - click > 100)) {
                it[0].click()
            }
            click = current
        }
    }

    private var click = 0L

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

    /**
     * 遍历AccessibilityNodeInfo并输出
     * @param nodeInfo
     */
    private fun recycle(nodeInfo: AccessibilityNodeInfo) {
        Log.i("kklv", "------------------------------------------")
        Log.i("kklv", "nodeInfo.getChildCount()----->${nodeInfo.childCount}")
        Log.i("kklv", "nodeInfo.getWindowId()----->${nodeInfo.windowId}")
        Log.i("kklv", "nodeInfo.getClassName()----->${nodeInfo.className}")
        Log.i("kklv", "nodeInfo.getContentDescription()----->${nodeInfo.contentDescription}")
        Log.i("kklv", "nodeInfo.getPackageName()----->${nodeInfo.packageName}")
        Log.i("kklv", "nodeInfo.getText()----->${nodeInfo.text}")
        Log.i("kklv", "nodeInfo.getViewIdResourceName()----->" + nodeInfo.viewIdResourceName)
        Log.i("kklv", "nodeInfo.getActions()----->" + nodeInfo.actions)
        Log.i("kklv", "nodeInfo.getMovementGranularities()----->" + nodeInfo.movementGranularities)
        Log.i("kklv", "nodeInfo.getgetTextSelectionEnd()----->" + nodeInfo.textSelectionEnd)
        Log.i("kklv", "nodeInfo.getTextSelectionStart()----->" + nodeInfo.textSelectionStart)
        Log.i("kklv", "nodeInfo.getLabeledBy()----->" + nodeInfo.labeledBy)
        Log.i("kklv", "nodeInfo.getLabelFor()----->" + nodeInfo.labelFor)
        if (nodeInfo.childCount > 0) {
            for (i in 0 until nodeInfo.childCount) {
                recycle(nodeInfo.getChild(i))
            }
        }
    }

}