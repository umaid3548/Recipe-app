package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.Recipe
import com.example.model.RecipeRepository
import com.example.ui.screens.ChefAiScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.RecipeListScreen
import com.example.ui.screens.ServingScalerToolScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TerracottaContainer
import com.example.ui.theme.TerracottaPrimary

enum class AppTab(val label: String) {
    RECIPES("Recipes"),
    SCALER("Scaler"),
    CHEF_AI("Chef AI"),
    SAVED("Saved")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RecipeApp()
            }
        }
    }
}

@Composable
fun RecipeApp() {
    var currentTab by remember { mutableStateOf(AppTab.RECIPES) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var currentServings by remember { mutableStateOf(4) }
    var chefAiRecipeContext by remember { mutableStateOf<Recipe?>(null) }

    // Remember scaled servings per recipe ID so users don't lose adjustments
    val scaledServingsMap = remember { mutableStateMapOf<String, Int>() }

    // Initial favorite recipes (e.g. veg-001 Paneer Butter Masala, nonveg-001 Butter Chicken, veg-002 Biryani, nonveg-002 Salmon)
    val favorites = remember {
        val initialFavs = listOf("veg-001", "nonveg-001", "veg-002", "nonveg-002")
        mutableStateMapOf<String, Boolean>().apply {
            initialFavs.forEach { put(it, true) }
        }
    }

    fun toggleFavorite(recipeId: String) {
        if (favorites.containsKey(recipeId)) {
            favorites.remove(recipeId)
        } else {
            favorites[recipeId] = true
        }
    }

    // Handle system back navigation
    BackHandler(enabled = selectedRecipe != null) {
        selectedRecipe = null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (selectedRecipe == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    NavigationBarItem(
                        selected = currentTab == AppTab.RECIPES,
                        onClick = { currentTab = AppTab.RECIPES },
                        icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = "Recipes") },
                        label = { Text("Recipes") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = TerracottaContainer
                        )
                    )
                    NavigationBarItem(
                        selected = currentTab == AppTab.SCALER,
                        onClick = { currentTab = AppTab.SCALER },
                        icon = { Icon(Icons.Default.Tune, contentDescription = "Serving Scaler") },
                        label = { Text("Scaler") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = TerracottaContainer
                        )
                    )
                    NavigationBarItem(
                        selected = currentTab == AppTab.CHEF_AI,
                        onClick = { currentTab = AppTab.CHEF_AI },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Chef AI") },
                        label = { Text("Chef AI") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = TerracottaContainer
                        )
                    )
                    NavigationBarItem(
                        selected = currentTab == AppTab.SAVED,
                        onClick = { currentTab = AppTab.SAVED },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
                        label = { Text("Saved") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            selectedTextColor = TerracottaPrimary,
                            indicatorColor = TerracottaContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)

        if (selectedRecipe != null) {
            val recipe = selectedRecipe!!
            RecipeDetailScreen(
                recipe = recipe,
                currentServings = currentServings,
                onServingsChanged = { newServings ->
                    currentServings = newServings
                    scaledServingsMap[recipe.id] = newServings
                },
                isFavorite = favorites.containsKey(recipe.id),
                onToggleFavorite = { toggleFavorite(recipe.id) },
                onBack = { selectedRecipe = null },
                onAskChefAi = { r ->
                    chefAiRecipeContext = r
                    selectedRecipe = null
                    currentTab = AppTab.CHEF_AI
                },
                modifier = contentModifier
            )
        } else {
            when (currentTab) {
                AppTab.RECIPES -> {
                    RecipeListScreen(
                        onRecipeSelected = { recipe ->
                            selectedRecipe = recipe
                            currentServings = scaledServingsMap[recipe.id] ?: recipe.baseServings
                        },
                        favorites = favorites.keys,
                        onToggleFavorite = { id -> toggleFavorite(id) },
                        scaledServingsMap = scaledServingsMap,
                        modifier = contentModifier
                    )
                }
                AppTab.SCALER -> {
                    ServingScalerToolScreen(
                        onOpenRecipeDetail = { recipe, servings ->
                            selectedRecipe = recipe
                            currentServings = servings
                            scaledServingsMap[recipe.id] = servings
                        },
                        modifier = contentModifier
                    )
                }
                AppTab.CHEF_AI -> {
                    ChefAiScreen(
                        onOpenRecipe = { recipe ->
                            selectedRecipe = recipe
                            currentServings = scaledServingsMap[recipe.id] ?: recipe.baseServings
                        },
                        initialRecipeContext = chefAiRecipeContext,
                        modifier = contentModifier
                    )
                }
                AppTab.SAVED -> {
                    FavoritesScreen(
                        favorites = favorites.keys,
                        onToggleFavorite = { id -> toggleFavorite(id) },
                        onRecipeSelected = { recipe ->
                            selectedRecipe = recipe
                            currentServings = scaledServingsMap[recipe.id] ?: recipe.baseServings
                        },
                        scaledServingsMap = scaledServingsMap,
                        onExploreRecipes = { currentTab = AppTab.RECIPES },
                        modifier = contentModifier
                    )
                }
            }
        }
    }
}
