package com.feliplvz.ecommfl.utils

// Clase para manejar el estado de validación
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object FormValidator {

    // Validar nombre de producto
    fun validateProductName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre no puede estar vacío")
            name.length < 3 -> ValidationResult(false, "El nombre debe tener al menos 3 caracteres")
            name.length > 50 -> ValidationResult(false, "El nombre no puede exceder 50 caracteres")
            else -> ValidationResult(true)
        }
    }

    // Validar descripción
    fun validateDescription(description: String): ValidationResult {
        return when {
            description.isBlank() -> ValidationResult(false, "La descripción no puede estar vacía")
            description.length < 10 -> ValidationResult(false, "La descripción debe tener al menos 10 caracteres")
            description.length > 200 -> ValidationResult(false, "La descripción no puede exceder 200 caracteres")
            else -> ValidationResult(true)
        }
    }

    // Validar precio
    fun validatePrice(priceText: String): ValidationResult {
        return when {
            priceText.isBlank() -> ValidationResult(false, "El precio no puede estar vacío")
            else -> {
                val price = priceText.toDoubleOrNull()
                when {
                    price == null -> ValidationResult(false, "El precio debe ser un número válido")
                    price <= 0 -> ValidationResult(false, "El precio debe ser mayor a 0")
                    price > 1000000 -> ValidationResult(false, "El precio es demasiado alto")
                    else -> ValidationResult(true)
                }
            }
        }
    }

    // Validar stock
    fun validateStock(stockText: String): ValidationResult {
        return when {
            stockText.isBlank() -> ValidationResult(false, "El stock no puede estar vacío")
            else -> {
                val stock = stockText.toIntOrNull()
                when {
                    stock == null -> ValidationResult(false, "El stock debe ser un número entero")
                    stock < 0 -> ValidationResult(false, "El stock no puede ser negativo")
                    stock > 10000 -> ValidationResult(false, "El stock es demasiado alto")
                    else -> ValidationResult(true)
                }
            }
        }
    }

    // Validar categoría
    fun validateCategory(category: String): ValidationResult {
        return when {
            category.isBlank() -> ValidationResult(false, "Debe seleccionar una categoría")
            else -> ValidationResult(true)
        }
    }

    // Validar URL de imagen
    fun validateImageUrl(url: String): ValidationResult {
        return when {
            url.isBlank() -> ValidationResult(false, "La URL de la imagen no puede estar vacía")
            !url.startsWith("http://") && !url.startsWith("https://") ->
                ValidationResult(false, "La URL debe comenzar con http:// o https://")
            else -> ValidationResult(true)
        }
    }

    // Validar todos los campos del producto
    fun validateProduct(
        name: String,
        description: String,
        price: String,
        stock: String,
        category: String,
        imageUrl: String
    ): Map<String, ValidationResult> {
        return mapOf(
            "name" to validateProductName(name),
            "description" to validateDescription(description),
            "price" to validatePrice(price),
            "stock" to validateStock(stock),
            "category" to validateCategory(category),
            "imageUrl" to validateImageUrl(imageUrl)
        )
    }

    // Verificar si todos los campos son válidos
    fun isFormValid(validationResults: Map<String, ValidationResult>): Boolean {
        return validationResults.all { it.value.isValid }
    }
}

