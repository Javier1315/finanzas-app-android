package finanzas.app.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import finanzas.app.models.Expense
import finanzas.app.repository.ExpenseRepository

class ExpenseViewModel : ViewModel() {

    private val repository =
        ExpenseRepository()

    private val _expenses =
        mutableStateListOf<Expense>()

    val expenses: List<Expense> =
        _expenses

    init {

        loadExpenses()
    }

    fun loadExpenses() {

        _expenses.clear()

        repository.getExpenses { expenseList ->

            _expenses.clear()

            _expenses.addAll(expenseList)
        }
    }

    fun addExpense(
        expense: Expense
    ) {

        repository.addExpense(expense) { success ->

            if (success) {

                _expenses.add(expense)
            }
        }
    }

    fun getTotalExpenses(): Double {

        return _expenses.sumOf {
            it.amount
        }
    }

    fun getExpensesByCategory():
            Map<String, Double> {

        return _expenses
            .groupBy { it.category }
            .mapValues { entry ->

                entry.value.sumOf {
                    it.amount
                }
            }
    }

    fun getCategoryPercentage(
        categoryTotal: Double
    ): Float {

        val total =
            getTotalExpenses()

        if (total == 0.0) {

            return 0f
        }

        return (
                (categoryTotal / total) * 100
                ).toFloat()
    }

    fun getFilteredExpenses(

        category: String,

        month: String

    ): List<Expense> {

        return _expenses.filter { expense ->

            val expenseMonth =

                if (expense.date.contains("/")) {

                    expense.date.split("/")[1]

                } else {

                    ""
                }

            val matchesCategory =

                category == "Todas" ||
                        expense.category == category

            val matchesMonth =

                month == "Todos" ||
                        expenseMonth == month

            matchesCategory &&
                    matchesMonth
        }
    }

    fun deleteExpense(
        expense: Expense
    ) {

        repository.deleteExpense(
            expense.id
        ) { success ->

            if (success) {

                _expenses.remove(expense)
            }
        }
    }

    fun getExpensesByMonth():
            Map<String, Double> {

        return _expenses
            .groupBy {

                val parts =
                    it.date.split("/")

                if (parts.size >= 2) {

                    parts[1]

                } else {

                    "0"
                }
            }

            .mapValues { entry ->

                entry.value.sumOf {
                    it.amount
                }
            }
    }
    fun clearExpenses() {

        _expenses.clear()
    }

    fun updateExpense(
        updatedExpense: Expense
    ) {

        repository.updateExpense(
            updatedExpense
        ) { success ->

            if (success) {

                val index =

                    _expenses.indexOfFirst {

                        it.id ==
                                updatedExpense.id
                    }

                if (index != -1) {

                    _expenses[index] =
                        updatedExpense
                }
            }
        }
    }

    fun getExpenseById(
        expenseId: String
    ): Expense? {

        return _expenses.find {

            it.id == expenseId
        }
    }

}