package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var canAddOperation=false
    private var canAddDecimal=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun numberAction(view: View) {
        if(view is Button){
            if(view.text=="."){
                if(canAddDecimal)
                    binding.workingTV.append(view.text)
                canAddDecimal=false
            }
            else
                binding.workingTV.append(view.text)

            canAddOperation=true
        }
    }

    fun operationAction(view: View) {
        if(view is Button && canAddOperation){
            binding.workingTV.append(view.text)
            canAddOperation=false
            canAddDecimal=true
        }
    }

    fun allClearAction(view: View) {
        binding.workingTV.text=""
        binding.resultTV.text=""
    }

    fun backSpaceAction(view: View) {
        val length=binding.workingTV.length()
        if(length>0)
           binding.workingTV.text= binding.workingTV.text.subSequence(0,length-1)
    }

    fun equalsAction(view: View) {
        binding.resultTV.text=calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators=digitsOperators()
        if (digitsOperators.isEmpty())
            return ""
        val timesDivision=timesDivisionCalculator(digitsOperators)
        if (timesDivision.isEmpty())
            return ""
        val result=addSubCal(timesDivision)

        return result.toString()
    }

    private fun addSubCal(passedList: MutableList<Any>): Float {
        var result=passedList[0] as Float

        for (i in passedList.indices){
            if (passedList[i] is Char && i !=passedList.lastIndex){
                val operator=passedList[i];
                val nextDigit=passedList[i+1] as Float
                if (operator=='+')
                    result += nextDigit
                if (operator=='-')
                    result -=nextDigit
            }
        }

        return result

    }

    private fun timesDivisionCalculator(passedList: MutableList<Any>):MutableList<Any> {
        var list=passedList
        while (list.contains('x')|| list.contains('/')){
            list=calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newlist=mutableListOf<Any>()

        var restartIndex=passedList.size

        for (i in passedList.indices){
            if(passedList[i]is Char && i !=passedList.lastIndex &&i < restartIndex){
                val operator=passedList[i]
                val prevDigit=passedList[i-1] as Float
                val nextDigit=passedList[i+1] as Float
                when (operator)
                {
                    'x' ->{
                        newlist.add(prevDigit*nextDigit)
                        restartIndex=i+1
                    }
                    '/'->{
                        newlist.add(prevDigit/nextDigit)
                        restartIndex=i+1
                    }
                    else->
                    {
                        newlist.add(prevDigit)
                        newlist.add(operator)
                    }
                }
            }
            if (i>restartIndex)
                newlist.add(passedList[i])
        }

        return newlist
    }

    private fun digitsOperators():MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit=""
        for (char in binding.workingTV.text){
            if(char.isDigit()||char=='.')
                currentDigit+=char
            else{
                list.add(currentDigit.toFloat())
                currentDigit=""
                list.add(char)
            }
        }
        if(currentDigit!="")
            list.add(currentDigit.toFloat())
        return list
    }
}