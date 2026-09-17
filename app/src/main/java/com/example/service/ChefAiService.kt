package com.example.service

import com.example.model.Recipe
import com.example.model.RecipeRepository
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val relatedRecipeId: String? = null,
    val suggestedActions: List<String> = emptyList()
)

object ChefAiService {

    fun generateWelcomeMessage(): ChatMessage {
        return ChatMessage(
            text = "Hello! I am your AI Culinary Assistant. I know all 200 vegetarian and non-vegetarian recipes in this app.\n\nYou can ask me about ingredient substitutions, tips on scaling recipes for dinner parties, cooking techniques, or recipe recommendations based on ingredients in your pantry!",
            isUser = false,
            suggestedActions = listOf(
                "How do I scale spices when doubling a recipe?",
                "Suggest high protein recipes under 30 mins",
                "Substitute for buttermilk or heavy cream",
                "Best recipes for a party of 8"
            )
        )
    }

    fun answerQuery(query: String, currentRecipe: Recipe? = null): ChatMessage {
        val q = query.trim().lowercase()

        // If context is about current recipe
        if (currentRecipe != null && (q.contains("this recipe") || q.contains("scale") || q.contains("substitute") || q.contains("tip"))) {
            return generateContextualRecipeAdvice(query, currentRecipe)
        }

        // Check for scaling questions
        if (q.contains("scale") || q.contains("scaling") || q.contains("double") || q.contains("halve") || q.contains("servings") || q.contains("crowd")) {
            return ChatMessage(
                text = "Culinary Rules for Scaling Recipes:\n\n" +
                        "1. **Direct 1:1 Scaling**: Main proteins, vegetables, broths, and grains (rice, pasta, flour) scale linearly with serving count.\n\n" +
                        "2. **The 1.5x Spice & Salt Rule**: When doubling or tripling a recipe, do NOT immediately double pungent spices (chili, cayenne, cloves) or salt. Start with 1.5x the original amount, taste during the simmer, and adjust.\n\n" +
                        "3. **Pan Size & Evaporation**: A larger batch cooked in a wide pan evaporates liquid faster. When cooking for 8+ servings, increase liquid slightly (10-15%) and watch the simmer.\n\n" +
                        "4. **Baking Precision**: In baking (cakes, breads), maintain exact weight ratios for leavening agents (baking powder/soda) rather than pure volume multipliers.\n\n" +
                        "💡 *Tip: On any recipe page in this app, you can enter any custom serving number in the Servings Scaler to get exact recalculations!*",
                isUser = false,
                suggestedActions = listOf("How to adjust cooking time?", "Show 6-serving recipes", "Explore Paneer Butter Masala")
            )
        }

        // Check for substitution questions
        if (q.contains("substitute") || q.contains("replacement") || q.contains("replace") || q.contains("instead of")) {
            val subText = when {
                q.contains("buttermilk") -> "• **Buttermilk Replacement**: Add 1 tbsp lemon juice or white vinegar to 1 cup of whole milk. Let sit for 5 minutes until lightly curdled."
                q.contains("heavy cream") -> "• **Heavy Cream Replacement**: Mix ¾ cup whole milk + ⅓ cup melted butter, or use full-fat coconut cream for dairy-free curries and soups."
                q.contains("egg") -> "• **Egg Replacement**: In baking: ¼ cup unsweetened applesauce per egg, or 1 tbsp ground flaxseed mixed with 3 tbsp water (flax egg)."
                q.contains("wine") -> "• **White/Red Wine in cooking**: Replace white wine with equal parts chicken/vegetable broth + 1 tsp lemon juice. Replace red wine with beef/mushroom broth + 1 tsp balsamic vinegar."
                q.contains("paneer") -> "• **Paneer Replacement**: Extra-firm tofu (pressed and lightly pan-seared) or Halloumi cheese works wonderfully in any curry recipe."
                q.contains("sour cream") -> "• **Sour Cream Replacement**: Greek whole-milk yogurt is a 1:1 substitute that provides the exact same tang and richness with more protein."
                else -> "• **Common Culinary Substitutions**:\n- **Buttermilk**: 1 cup milk + 1 tbsp lemon juice\n- **Heavy Cream**: ¾ cup milk + ⅓ cup melted butter\n- **Fresh Herbs**: 1 tsp dried herbs = 1 tbsp fresh herbs\n- **Cornstarch**: 2 tbsp all-purpose flour for every 1 tbsp cornstarch"
            }
            return ChatMessage(
                text = "Here are tested chef substitutions for your kitchen:\n\n$subText",
                isUser = false,
                suggestedActions = listOf("Substitute for eggs", "Substitute for heavy cream", "Substitute for wine")
            )
        }

        // Check for high protein or diet queries
        if (q.contains("protein") || q.contains("healthy") || q.contains("diet") || q.contains("gym") || q.contains("keto")) {
            val topProteinVeg = RecipeRepository.vegRecipes.sortedByDescending { it.proteinGrams }.take(2)
            val topProteinNonVeg = RecipeRepository.nonVegRecipes.sortedByDescending { it.proteinGrams }.take(2)

            val text = buildString {
                append("Here are our top high-protein recipe recommendations:\n\n")
                append("🌱 **Vegetarian Powerhouses**:\n")
                topProteinVeg.forEach { r ->
                    append("• **${r.title}**: ${r.proteinGrams}g protein per serving (${r.caloriesPerServing} kcal)\n")
                }
                append("\n🍗 **Non-Vegetarian High Protein**:\n")
                topProteinNonVeg.forEach { r ->
                    append("• **${r.title}**: ${r.proteinGrams}g protein per serving (${r.caloriesPerServing} kcal)\n")
                }
                append("\nWould you like to open one of these recipes?")
            }
            return ChatMessage(
                text = text,
                isUser = false,
                relatedRecipeId = topProteinNonVeg.firstOrNull()?.id,
                suggestedActions = listOf("Show " + (topProteinVeg.firstOrNull()?.title ?: "Veg"), "Show " + (topProteinNonVeg.firstOrNull()?.title ?: "Chicken"))
            )
        }

        // Search in the 200 recipe database
        val matchedRecipes = RecipeRepository.allRecipes.filter { recipe ->
            val words = q.split(" ", ",", "and").map { it.trim() }.filter { it.length > 3 }
            words.any { word ->
                recipe.title.lowercase().contains(word) ||
                        recipe.cuisine.lowercase().contains(word) ||
                        recipe.ingredients.any { it.name.lowercase().contains(word) } ||
                        recipe.tags.any { it.lowercase().contains(word) }
            }
        }.take(3)

        if (matchedRecipes.isNotEmpty()) {
            val topMatch = matchedRecipes.first()
            val text = buildString {
                append("I found delicious recipes matching your request:\n\n")
                matchedRecipes.forEachIndexed { i, r ->
                    val vegIcon = if (r.isVeg) "🌱 [Veg]" else "🍗 [Non-Veg]"
                    append("${i + 1}. **${r.title}** $vegIcon\n")
                    append("   *Cuisine: ${r.cuisine} | Total: ${r.totalTimeMin}m | Base: ${r.baseServings} servings*\n")
                    append("   ${r.description}\n\n")
                }
                append("💡 Tap below to open **${topMatch.title}** and scale it to your exact party size!")
            }
            return ChatMessage(
                text = text,
                isUser = false,
                relatedRecipeId = topMatch.id,
                suggestedActions = matchedRecipes.map { "View ${it.title}" }
            )
        }

        // Generic friendly culinary response
        return ChatMessage(
            text = "I'd love to help you cook! You can ask me:\n" +
                    "• **Recipe Search**: e.g., 'Do you have Thai curry or Salmon?'\n" +
                    "• **Ingredient Match**: e.g., 'What can I cook with mushrooms and garlic?'\n" +
                    "• **Recipe Scaling**: e.g., 'How do I scale a 4-person curry to 10 people?'\n" +
                    "• **Substitutions**: e.g., 'Can I use Greek yogurt instead of sour cream?'\n" +
                    "• **Cooking Techniques**: e.g., 'How do I get crispy chicken skin?'",
            isUser = false,
            suggestedActions = listOf(
                "Vegetarian pasta recipes",
                "Non-veg quick dinner under 20 mins",
                "How to scale biryani for 12 guests"
            )
        )
    }

    private fun generateContextualRecipeAdvice(query: String, recipe: Recipe): ChatMessage {
        val q = query.lowercase()
        return when {
            q.contains("scale") || q.contains("serving") -> {
                ChatMessage(
                    text = "Scaling **${recipe.title}** (Base: ${recipe.baseServings} servings):\n\n" +
                            "• The app's built-in **Servings Scaler** automatically adjusts all ${recipe.ingredients.size} ingredients with mathematically precise ratios.\n" +
                            "• **Cook time advice**: When cooking double the quantity, increase the searing/browning time by 20-30%, but keep simmering time similar.\n" +
                            "• Chef tip for this dish: *\"${recipe.chefTip}\"*",
                    isUser = false,
                    relatedRecipeId = recipe.id
                )
            }
            q.contains("substitute") -> {
                val firstIng = recipe.ingredients.firstOrNull()?.name ?: "main ingredient"
                ChatMessage(
                    text = "Substitutions for **${recipe.title}**:\n\n" +
                            "• **$firstIng**: Check your pantry for similar protein or vegetable bases.\n" +
                            "• **Spices & Herbs**: Dried herbs can replace fresh at a 1:3 ratio.\n" +
                            "• Would you like a specific ingredient replacement in this list?",
                    isUser = false,
                    relatedRecipeId = recipe.id
                )
            }
            else -> {
                ChatMessage(
                    text = "Expert Chef Advice for **${recipe.title}**:\n\n" +
                            "• **Prep Note**: ${recipe.instructions.firstOrNull() ?: "Prepare all ingredients ahead."}\n" +
                            "• **Key Secret**: ${recipe.chefTip}\n" +
                            "• **Nutrition**: ${recipe.caloriesPerServing} kcal, ${recipe.proteinGrams}g Protein per serving.",
                    isUser = false,
                    relatedRecipeId = recipe.id
                )
            }
        }
    }
}
