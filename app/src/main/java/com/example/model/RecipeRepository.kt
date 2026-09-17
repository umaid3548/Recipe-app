package com.example.model

object RecipeRepository {
    val allRecipes: List<Recipe> by lazy {
        VegRecipes.list + NonVegRecipes.list
    }

    val vegRecipes: List<Recipe> by lazy {
        allRecipes.filter { it.isVeg }
    }

    val nonVegRecipes: List<Recipe> by lazy {
        allRecipes.filter { !it.isVeg }
    }

    val categories: List<String> by lazy {
        listOf("All") + allRecipes.map { it.category }.distinct().sorted()
    }

    val cuisines: List<String> by lazy {
        listOf("All") + allRecipes.map { it.cuisine }.distinct().sorted()
    }

    fun getRecipeById(id: String): Recipe? {
        return allRecipes.find { it.id == id }
    }

    fun searchRecipes(
        query: String,
        isVegFilter: Boolean?, // null for all, true for veg, false for non-veg
        category: String = "All",
        dietaryFilters: Set<DietaryRestriction> = emptySet(),
        maxTimeMin: Int? = null,
        sortBy: SortOption = SortOption.POPULAR
    ): List<Recipe> {
        return allRecipes.filter { recipe ->
            val matchesQuery = query.isBlank() ||
                recipe.title.contains(query, ignoreCase = true) ||
                recipe.cuisine.contains(query, ignoreCase = true) ||
                recipe.ingredients.any { it.name.contains(query, ignoreCase = true) } ||
                recipe.tags.any { it.contains(query, ignoreCase = true) }

            val matchesVeg = isVegFilter == null || recipe.isVeg == isVegFilter
            val matchesCategory = category == "All" || recipe.category.equals(category, ignoreCase = true)
            val matchesTime = maxTimeMin == null || recipe.totalTimeMin <= maxTimeMin
            val matchesDietary = recipe.matchesAllRestrictions(dietaryFilters)

            matchesQuery && matchesVeg && matchesCategory && matchesTime && matchesDietary
        }.let { list ->
            when (sortBy) {
                SortOption.POPULAR -> list
                SortOption.TIME_LOW -> list.sortedBy { it.totalTimeMin }
                SortOption.CALORIES_LOW -> list.sortedBy { it.caloriesPerServing }
                SortOption.PROTEIN_HIGH -> list.sortedByDescending { it.proteinGrams }
                SortOption.ALPHABETICAL -> list.sortedBy { it.title }
            }
        }
    }

    fun countRecipesMatchingDietary(restriction: DietaryRestriction): Int {
        return allRecipes.count { restriction in it.resolvedDietaryRestrictions }
    }
}

enum class SortOption(val displayName: String) {
    POPULAR("Recommended"),
    TIME_LOW("Fastest (Time)"),
    CALORIES_LOW("Lowest Calories"),
    PROTEIN_HIGH("Highest Protein"),
    ALPHABETICAL("A - Z")
}
