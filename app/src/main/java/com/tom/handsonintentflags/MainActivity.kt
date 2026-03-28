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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updateInfo()
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        updateInfo()
    }

    private fun updateInfo() {
        val info = buildString {
            appendLine("📋 MainActivity")
            appendLine("─────────────────────────")
            appendLine("taskId      : $taskId")
            appendLine("hashCode    : ${hashCode()}")
            appendLine("instanceId  : ${System.identityHashCode(this@MainActivity)}")
            appendLine("isTaskRoot  : $isTaskRoot")
        }
        Log.i("tamld", "updateInfo: ")
        findViewById<TextView>(R.id.tvInfo).text = info
    }

    private fun setupButtons() {

        // ======================== BASIC FLAGS ========================

        // No flag — always creates a NEW instance of A, pushed on top of stack
        findViewById<Button>(R.id.btnNoFlag).setOnClickListener {
            startActivity(Intent(this, ActivityA::class.java))
        }

        // SINGLE_TOP — if A is already on TOP of stack, reuse it (→ onNewIntent)
        //               if A is NOT on top, creates a new instance as normal
        findViewById<Button>(R.id.btnSingleTop).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // NEW_TASK — start A in a new task (if the activity has a different taskAffinity)
        //            cùng taskAffinity → không tạo task mới, chỉ thêm vào task hiện tại
        findViewById<Button>(R.id.btnNewTask).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        // ======================== CLEAR FLAGS ========================

        // CLEAR_TOP — if A already exists in the stack, destroy everything above it,
        //             then DESTROY A itself and re-create it (onCreate called again!)
        findViewById<Button>(R.id.btnClearTop).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // CLEAR_TOP + SINGLE_TOP — clear everything above A in stack,
        //                          but REUSE the existing A instance (→ onNewIntent)
        //                          Đây là combo phổ biến nhất!
        findViewById<Button>(R.id.btnClearTopSingleTop).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // NEW_TASK + CLEAR_TASK — xoá toàn bộ task cũ, tạo A mới làm root
        findViewById<Button>(R.id.btnClearTask).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // ======================== OTHER FLAGS ========================

        // NO_HISTORY — A sẽ không được giữ lại trong back stack.
        //              Khi user rời khỏi A (ví dụ mở B), A bị destroy ngay lập tức.
        //              Bấm Back từ B sẽ KHÔNG quay lại A.
        findViewById<Button>(R.id.btnNoHistory).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }

        // REORDER_TO_FRONT — nếu A đã tồn tại trong stack, đưa A lên đầu stack
        //                     mà không destroy bất kỳ Activity nào khác
        findViewById<Button>(R.id.btnReorderToFront).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        // ======================== BUILD STACK ========================

        // Start chain A → B → C to build a deep stack, then test flags from C
        findViewById<Button>(R.id.btnBuildStack).setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            intent.putExtra("autoChain", true)
            startActivity(intent)
        }
    }
}