package com.example.calculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private enum class Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, NONE
    }

    private var selectedOperation = Operation.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        // Operation button clicks
        binding.btnAdd.setOnClickListener {
            selectOperation(Operation.ADD, binding.btnAdd)
        }

        binding.btnSubtract.setOnClickListener {
            selectOperation(Operation.SUBTRACT, binding.btnSubtract)
        }

        binding.btnMultiply.setOnClickListener {
            selectOperation(Operation.MULTIPLY, binding.btnMultiply)
        }

        binding.btnDivide.setOnClickListener {
            selectOperation(Operation.DIVIDE, binding.btnDivide)
        }

        // Calculate button click
        binding.btnCalculate.setOnClickListener {
            calculateResult()
        }

        // Help button click
        binding.btnHelp.setOnClickListener {
            showHelpDialog()
        }
    }

    private fun selectOperation(operation: Operation, selectedButton: MaterialButton) {
        selectedOperation = operation
        resetOperationButtonStyles()

        // Highlight selected operation button
        selectedButton.setBackgroundColor(
            ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_primary),
        )
        selectedButton.setTextColor(
            ContextCompat.getColor(this, android.R.color.white),
        )

        // Reset result display until Calculate button is pressed
        binding.tvResult.text = getString(R.string.result_placeholder)
    }

    private fun resetOperationButtonStyles() {
        val buttons = listOf(
            binding.btnAdd,
            binding.btnSubtract,
            binding.btnMultiply,
            binding.btnDivide
        )

        for (button in buttons) {
            button.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.transparent)
            )
            button.setTextColor(
                ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_primary)
            )
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        binding.tilFirstNumber.error = null
        binding.tilSecondNumber.error = null

        val text1 = binding.etFirstNumber.text?.toString()?.trim()
        val text2 = binding.etSecondNumber.text?.toString()?.trim()

        val num1 = text1?.toDoubleOrNull()
        val num2 = text2?.toDoubleOrNull()

        if ((text1.isNullOrEmpty()) || (num1 == null)) {
            binding.tilFirstNumber.error = getString(R.string.error_invalid_input)
            isValid = false
        }

        if ((text2.isNullOrEmpty()) || (num2 == null)) {
            binding.tilSecondNumber.error = getString(R.string.error_invalid_input)
            isValid = false
        }

        return isValid
    }

    private fun calculateResult() {
        if (!validateInputs()) {
            return
        }

        if (selectedOperation == Operation.NONE) {
            binding.tvResult.text = getString(R.string.error_no_operation)
            return
        }

        val num1 = binding.etFirstNumber.text.toString().trim().toDouble()
        val num2 = binding.etSecondNumber.text.toString().trim().toDouble()

        when (selectedOperation) {
            Operation.ADD -> {
                val result = num1 + num2
                displayResult(formatResult(result))
            }
            Operation.SUBTRACT -> {
                val result = num1 - num2
                displayResult(formatResult(result))
            }
            Operation.MULTIPLY -> {
                val result = num1 * num2
                displayResult(formatResult(result))
            }
            Operation.DIVIDE -> {
                if (num2 == 0.0) {
                    binding.tvResult.text = getString(R.string.error_divide_by_zero)
                } else {
                    val result = num1 / num2
                    displayResult(formatResult(result))
                }
            }
            Operation.NONE -> {
                // Handled above
            }
        }
    }

    private fun displayResult(formattedResult: String) {
        binding.tvResult.text = getString(R.string.result_label, formattedResult)
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0 && value <= Long.MAX_VALUE && value >= Long.MIN_VALUE) {
            value.toLong().toString()
        } else {
            String.format(Locale.getDefault(), "%.4f", value).trimEnd('0').trimEnd('.')
        }
    }

    private fun showHelpDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.help_title))
            .setMessage(getString(R.string.help_message))
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_menu_help)
            .show()
    }
}