package finanzas.app.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finanzas.app.models.Expense

class ExpenseRepository {

    // instancia de firestore para almacenar datos
    private val firestore =
        FirebaseFirestore.getInstance()

    // instancia de autenticacion firebase
    private val auth =
        FirebaseAuth.getInstance()

    // agrega un nuevo gasto en firestore
    fun addExpense(

        expense: Expense,

        onResult: (Boolean) -> Unit

    ) {

        // obtiene el id del usuario actual
        val userId =
            auth.currentUser?.uid

        Log.d("FIREBASE", "USER ID: $userId")

        // verifica si existe usuario autenticado
        if (userId == null) {

            Log.d("FIREBASE", "USUARIO NULL")

            onResult(false)

            return
        }

        // guarda el gasto en firestore
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

    // elimina un gasto segun su id
    fun deleteExpense(

        expenseId: String,

        onResult: (Boolean) -> Unit

    ) {

        val userId =
            auth.currentUser?.uid

        // valida usuario autenticado
        if (userId == null) {

            onResult(false)

            return
        }

        // elimina el gasto seleccionado
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

    // obtiene todos los gastos del usuario
    fun getExpenses(

        onResult: (List<Expense>) -> Unit

    ) {

        val userId =
            auth.currentUser?.uid

        // valida usuario autenticado
        if (userId == null) {

            onResult(emptyList())

            return
        }

        // consulta gastos almacenados en firestore
        firestore
            .collection("users")
            .document(userId)
            .collection("expenses")
            .get()

            .addOnSuccessListener { result ->

                // convierte documentos en objetos expense
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

    // actualiza un gasto existente
    fun updateExpense(

        expense: Expense,

        onResult: (Boolean) -> Unit

    ) {

        val userId =
            auth.currentUser?.uid

        // valida usuario autenticado
        if (userId == null) {

            onResult(false)

            return
        }

        // reemplaza informacion del gasto
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