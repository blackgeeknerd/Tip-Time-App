package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // added this line after creating the binding lateinit variable
        binding = ActivityMainBinding.inflate(layoutInflater)

        // setContentView(R.layout.activity_main)// initial
        setContentView(binding.root) //new line after initialising the binding lateinit variable
        /** example further use of 'binding' variable been set to setContentView(binding.root)

        // Old way with findViewById()
        val myButton: Button = findViewById(R.id.my_button)
        myButton.text = "A button"

        // Better way with view binding
        val myButton: Button = binding.myButton
        myButton.text = "A button"

        // Best way with view binding and no extra variable
        binding.myButton.text = "A button"
         */

        binding.calculateButton.setOnClickListener{calculateTip()}

        binding.costOfServiceEditText.setOnKeyListener{ view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }

    }

    private fun calculateTip(){
        //get the cost of the service which is in the EditText
        //convert text to string
        // Adding .text on the end says to take that result (an EditText object),
        // and get the text property from it. This is known as chaining,
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        //convert to double
        //val cost = stringInTextField.toDouble()
        //val cost = stringInTextField.toDoubleOrNull() ?: return // using the elvis operator
        // or
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null) {
            binding.tipResult.text = " "
            return
        }


        //get percent of the id selected into a variable
        val tipPercent = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        //get tip percentage method 2
        /**
         * inline variable
         *   val tipPercent = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
            }
         */

       //calculate the tip and round it up
        //tip = cost * percent tip
        var tip = tipPercent * cost

        //check if switch is flipped
        //val roundUp = binding.roundUpSwitch.isChecked
        //if switch is checked, then round up tip to nearest number
        if(binding.roundUpSwitch.isChecked){
            tip = kotlin.math.ceil(tip)
        }

        //now format the currency base on the language and other settings
        // users have chosen on their phone
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)

        /** display the tip..
             first change the value of the tip_amount in the strings.xml file to
                'Tip Amount: %s', the %s is where the formatted currency will be inserted
             Second: set/bind the text of tipResult
        */
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

    }

    //helper function that helps hide the soft keyboard once a user press Enter key
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if(keyCode == KEYCODE_ENTER){
           //Hide the keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}