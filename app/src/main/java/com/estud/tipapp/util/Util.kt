package com.estud.tipapp.util

fun calculateTotalPerPerson(totalValue: Double, splitBy: Int, tipPercentage: Double): Double {
    val total = totalValue * (1 + tipPercentage)
    return total / splitBy
}