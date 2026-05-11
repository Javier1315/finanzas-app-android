package finanzas.app.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finanzas.app.models.Expense

class ExpenseRepository {

    private val firestore =
        FirebaseFirestore.getInstance()

    private val auth =
        FirebaseAuth.getInstance()

    fun addExpense(

        expense: Expense,

        onResult: (Boolean) -> Unit

    ) {

        val userId =
            auth.currentUser?.uid

        Log.d("FIREBASE", "USER ID: $userId")

        if (userId == null) {

            Log.d("FIREBASE", "USUARIO NULL")

            onResult(false)

            return
        }

        firestore
            .collection("users")
            .document(userId)
            .collection("expenses")
            .document(expense.id)
            .set(expense)

            .addOnSuccessListener {

                Log.d(
                    "FIREBASE",
                    "GASTO GUARDADO"
                )

                onResult(true)
            }

            .addOnFailureListener {

                Log.e(
                    "FIREBASE",
                    "ERROR FIREBASE",
                    it
                )

                onResult(false)
            }
    }

    fun deleteExpense(

        expenseId: String,

        onResult: (Boolean) -> Unit

    ) {

        val userId =
            auth.currentUser?.uid

        if (userId == null) {

            onResult(false)

            return
        }

        firestore
            .collection("users")
            .document(userId)
            .collection("expenses")
            .document(expenseId)
            .delete()

            .addOnSuccessListener {

                onResult(true)
            }

            .addOnFailureListener {

                onResult(false)
            }
    }

    fun getExpenses(

        onResult: (List<Expense>) -> Unit

    ) {

        val userId =
            auth.currentUser?.uid

        if (userId == null) {

            onResult(emptyList())

            return
        }

        firestore
            .collection("users")
            .document(userId)
            .collection("expenses")
            .get()

            .addOnSuccessListener { result ->

                val expenses =

                    result.documents.mapNotNull {

                        it.toObject(
                            Expense::class.java
                        )
                    }

                Log.d(
                    "FIREBASE",
                    "GASTOS CARGADOS: ${expenses.size}"
                )

                onResult(expenses)
            }

            .addOnFailureListener {

                Log.e(
                    "FIREBASE",
                    "ERROR CARGANDO",
                    it
                )
            }
    }
    fun updateExpense(

        expense: Expense,

        onResult: (Boolean) -> Unit

    ) {

        val userId =
            auth.currentUser?.uid

        if (userId == null) {

            onResult(false)

            return
        }

        firestore
            .collection("users")
            .document(userId)
            .collection("expenses")
            .document(expense.id)
            .set(expense)

            .addOnSuccessListener {

                onResult(true)
            }

            .addOnFailureListener {

                onResult(false)
            }
    }
}