package com.example.model

data class RecipeIngredient(
    val name: String,
    val amount: Double,
    val unit: String,
    val notes: String = ""
) {
    fun scaledAmount(baseServings: Int, targetServings: Int): Double {
        if (baseServings <= 0) return amount
        return (amount / baseServings.toDouble()) * targetServings.toDouble()
    }

    fun formattedQuantity(baseServings: Int, targetServings: Int): String {
        val scaled = scaledAmount(baseServings, targetServings)
        return formatAmount(scaled, unit)
    }

    companion object {
        fun formatAmount(amount: Double, unit: String): String {
            if (amount <= 0.0) return if (unit.isNotBlank()) unit else ""
            val whole = amount.toInt()
            val remainder = amount - whole

            val fractionStr = when {
                Math.abs(remainder - 0.5) < 0.04 -> "½"
                Math.abs(remainder - 0.25) < 0.04 -> "¼"
                Math.abs(remainder - 0.75) < 0.04 -> "¾"
                Math.abs(remainder - 0.333) < 0.05 -> "⅓"
                Math.abs(remainder - 0.667) < 0.05 -> "⅔"
                Math.abs(remainder - 0.125) < 0.03 -> "⅛"
                remainder < 0.04 -> ""
                else -> String.format(java.util.Locale.US, "%.1f", amount)
            }

            val qtyStr = when {
                fractionStr.isEmpty() -> "$whole"
                whole > 0 && fractionStr in listOf("½", "¼", "¾", "⅓", "⅔", "⅛") -> "$whole $fractionStr"
                whole == 0 && fractionStr in listOf("½", "¼", "¾", "⅓", "⅔", "⅛") -> fractionStr
                else -> String.format(java.util.Locale.US, "%.1f", amount).removeSuffix(".0")
            }

            return if (unit.isNotBlank()) "$qtyStr $unit" else qtyStr
        }
    }
}

enum class DietaryRestriction(
    val id: String,
    val displayName: String,
    val icon: String,
    val description: String
) {
    VEGAN("vegan", "Vegan", "🌱", "100% plant-based, contains no animal products or dairy"),
    GLUTEN_FREE("gluten_free", "Gluten-Free", "🌾", "Prepared without wheat, rye, barley or gluten grains"),
    NUT_FREE("nut_free", "Nut-Free", "🥜", "Free from peanuts and tree nuts"),
    DAIRY_FREE("dairy_free", "Dairy-Free", "🥛", "Prepared without milk, cheese, butter, or cream"),
    LOW_CARB("low_carb", "Low-Carb", "🥑", "Low carbohydrate profile (under 20g carbs)"),
    HIGH_PROTEIN("high_protein", "High-Protein", "💪", "High protein content (20g+ protein per serving)")
}

data class Recipe(
    val id: String,
    val title: String,
    val category: String,
    val isVeg: Boolean,
    val prepTimeMin: Int,
    val cookTimeMin: Int,
    val baseServings: Int,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val cuisine: String,
    val caloriesPerServing: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val description: String,
    val ingredients: List<RecipeIngredient>,
    val instructions: List<String>,
    val tags: List<String>,
    val chefTip: String,
    val dietaryRestrictions: Set<DietaryRestriction> = emptySet()
) {
    val totalTimeMin: Int get() = prepTimeMin + cookTimeMin

    val resolvedDietaryRestrictions: Set<DietaryRestriction> by lazy {
        val result = dietaryRestrictions.toMutableSet()

        // If explicit restrictions weren't provided or to supplement from ingredients/tags:
        val hasDairy = ingredients.any { ing ->
            val n = ing.name.lowercase()
            n.contains("milk") || n.contains("cheese") || n.contains("butter") ||
            n.contains("cream") || n.contains("paneer") || n.contains("yogurt") ||
            n.contains("ghee") || n.contains("mozzarella") || n.contains("parmigiano")
        } || tags.any { it.contains("Dairy", ignoreCase = true) && !it.contains("Dairy-Free", ignoreCase = true) }

        val hasEgg = ingredients.any { it.name.lowercase().contains("egg") }
        val hasMeatOrSeafood = !isVeg

        // VEGAN check: Must be vegetarian, no dairy, no egg, and marked vegan or no animal byproducts
        if (!hasMeatOrSeafood && !hasDairy && !hasEgg) {
            result.add(DietaryRestriction.VEGAN)
        }
        if (tags.any { it.contains("Vegan", ignoreCase = true) }) {
            result.add(DietaryRestriction.VEGAN)
        }

        // GLUTEN-FREE check:
        val hasGluten = ingredients.any { ing ->
            val n = ing.name.lowercase()
            n.contains("flour") || n.contains("wheat") || n.contains("bread") ||
            n.contains("pasta") || n.contains("dough") || n.contains("noodle") ||
            n.contains("soy sauce") || n.contains("breadcrumbs")
        }
        if (!hasGluten || tags.any { it.contains("Gluten-Free", ignoreCase = true) || it.contains("GF", ignoreCase = true) }) {
            result.add(DietaryRestriction.GLUTEN_FREE)
        }

        // NUT-FREE check:
        val hasNuts = ingredients.any { ing ->
            val n = ing.name.lowercase()
            n.contains("nut") || n.contains("almond") || n.contains("cashew") ||
            n.contains("peanut") || n.contains("walnut") || n.contains("pistachio") ||
            n.contains("pecan") || n.contains("hazelnut")
        }
        if (!hasNuts || tags.any { it.contains("Nut-Free", ignoreCase = true) }) {
            result.add(DietaryRestriction.NUT_FREE)
        }

        // DAIRY-FREE check:
        if (!hasDairy || tags.any { it.contains("Dairy-Free", ignoreCase = true) }) {
            result.add(DietaryRestriction.DAIRY_FREE)
        }

        // LOW-CARB check:
        if (carbsGrams <= 20 || tags.any { it.contains("Low Carb", ignoreCase = true) || it.contains("Keto", ignoreCase = true) }) {
            result.add(DietaryRestriction.LOW_CARB)
        }

        // HIGH-PROTEIN check:
        if (proteinGrams >= 20 || tags.any { it.contains("High Protein", ignoreCase = true) }) {
            result.add(DietaryRestriction.HIGH_PROTEIN)
        }

        result
    }

    fun matchesAllRestrictions(filters: Set<DietaryRestriction>): Boolean {
        if (filters.isEmpty()) return true
        val resolved = resolvedDietaryRestrictions
        return filters.all { it in resolved }
    }
}
