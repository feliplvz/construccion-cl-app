package com.feliplvz.ecommfl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feliplvz.ecommfl.data.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val supabase = SupabaseClient.client

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                val session = supabase.auth.currentSessionOrNull()
                _authState.value = AuthState(isAuthenticated = session != null)
            } catch (e: Exception) {
                _authState.value = AuthState(isAuthenticated = false)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState(isLoading = true)

                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                _authState.value = AuthState(isAuthenticated = true)
            } catch (e: Exception) {
                _authState.value = AuthState(
                    isAuthenticated = false,
                    error = when {
                        e.message?.contains("Invalid login credentials") == true ->
                            "Credenciales incorrectas"
                        e.message?.contains("Email not confirmed") == true ->
                            "Email no confirmado"
                        else -> "Error al iniciar sesión: ${e.message}"
                    }
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                _authState.value = AuthState(isAuthenticated = false)
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    error = "Error al cerrar sesión: ${e.message}"
                )
            }
        }
    }
}

