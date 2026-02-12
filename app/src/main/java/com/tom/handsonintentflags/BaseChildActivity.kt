package com.tom.handsonintentflags

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Base Activity cho A, B, C — hiển thị thông tin TaskId, hashCode,
 * ghi log lifecycle và cung cấp các nút navigate với các flags khác nhau.
 */
abstract class BaseChildActivity : AppCompatActivity() {

    abstract val activityLabel: String   // "Activity A", "Activity B", "Activity C"
    abstract val bannerColor: Int        // Background color for the name banner
    abstract val selfClass: Class<*>     // Class reference to self

    private val lifecycleLog = mutableListOf<String>()

    // ===================== LIFECYCLE =====================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_child)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logLifecycle("onCreate")

        // Banner
        findViewById<TextView>(R.id.tvActivityName).apply {
            text = activityLabel
            setBackgroundColor(bannerColor)
        }

        setupButtons()
        updateInfo()

        // Auto-chain: Main → A → B → C to build a deep back stack for testing
        if (intent.getBooleanExtra("autoChain", false)) {
            val nextClass = when (this) {
                is ActivityA -> ActivityB::class.java
                is ActivityB -> ActivityC::class.java
                else -> null
            }
            nextClass?.let {
                val chainIntent = Intent(this, it)
                if (this !is ActivityB) chainIntent.putExtra("autoChain", true)
                startActivity(chainIntent)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        logLifecycle("⭐ onNewIntent (instance reused!)")
        updateInfo()
    }

    override fun onStart()   { super.onStart();   logLifecycle("onStart") }
    override fun onResume()  { super.onResume();  logLifecycle("onResume"); updateInfo() }
    override fun onPause()   { super.onPause();   logLifecycle("onPause") }
    override fun onStop()    { super.onStop();    logLifecycle("onStop") }
    override fun onDestroy() { super.onDestroy(); logLifecycle("onDestroy") }

    // ===================== UI =====================

    private fun updateInfo() {
        val info = buildString {
            appendLine("📋 $activityLabel")
            appendLine("─────────────────────────")
            appendLine("taskId      : $taskId")
            appendLine("hashCode    : ${hashCode()}")
            appendLine("instanceId  : ${System.identityHashCode(this@BaseChildActivity)}")
            appendLine("isTaskRoot  : $isTaskRoot")
        }
        findViewById<TextView>(R.id.tvInfo).text = info

        val logText = buildString {
            appendLine("📜 Lifecycle Log:")
            lifecycleLog.forEachIndexed { i, event ->
                appendLine("  ${i + 1}. $event")
            }
        }
        findViewById<TextView>(R.id.tvLifecycle).text = logText
    }

    private fun logLifecycle(event: String) {
        val msg = "$activityLabel [${ Integer.toHexString(System.identityHashCode(this))}] → $event"
        lifecycleLog.add(event)
        Log.d("IntentFlags", msg)
    }

    private fun setupButtons() {
        // Start A, B, C (no flag)
        findViewById<Button>(R.id.btnStartA).setOnClickListener {
            startActivity(Intent(this, ActivityA::class.java))
        }
        findViewById<Button>(R.id.btnStartB).setOnClickListener {
            startActivity(Intent(this, ActivityB::class.java))
        }
        findViewById<Button>(R.id.btnStartC).setOnClickListener {
            startActivity(Intent(this, ActivityC::class.java))
        }

        // CLEAR_TOP → A
        findViewById<Button>(R.id.btnClearTopToA).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // CLEAR_TOP + SINGLE_TOP → A
        findViewById<Button>(R.id.btnClearTopSingleTopToA).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // SINGLE_TOP → Self
        findViewById<Button>(R.id.btnSingleTopSelf).setOnClickListener {
            val intent = Intent(this, selfClass)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // REORDER_TO_FRONT → A
        findViewById<Button>(R.id.btnReorderA).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        // Finish
        findViewById<Button>(R.id.btnGoBack).setOnClickListener {
            finish()
        }
    }
}
