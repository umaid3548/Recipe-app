package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Recipe
import com.example.model.RecipeIngredient
import com.example.model.RecipeRepository
import com.example.ui.components.ServingScalerCard
import com.example.ui.components.VegBadge
import com.example.ui.theme.TerracottaContainer
import com.example.ui.theme.TerracottaPrimary

@Composable
fun ServingScalerToolScreen(
    onOpenRecipeDetail: (Recipe, Int) -> Unit,
    initialRecipeId: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedRecipe by remember {
        mutableStateOf(
            if (initialRecipeId != null) RecipeRepository.getRecipeById(initialRecipeId) ?: RecipeRepository.allRecipes.first()
            else RecipeRepository.allRecipes.first()
        )
    }

    var currentServings by remember(selectedRecipe.id) {
        mutableStateOf(selectedRecipe.baseServings)
    }

    var recipeSearchQuery by remember { mutableStateOf("") }
    var recipeDropdownExpanded by remember { mutableStateOf(false) }

    val matchedRecipes = remember(recipeSearchQuery) {
        if (recipeSearchQuery.isBlank()) RecipeRepository.allRecipes.take(15)
        else RecipeRepository.allRecipes.filter {
            it.title.contains(recipeSearchQuery, ignoreCase = true) ||
            it.cuisine.contains(recipeSearchQuery, ignoreCase = true)
        }.take(15)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Ingredient Scaler & Calculator",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Select any prebuilt recipe to automatically scale ingredient quantities",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Recipe Selector Box
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .clickable { recipeDropdownExpanded = true }
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VegBadge(isVeg = selectedRecipe.isVeg, showLabel = false)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = selectedRecipe.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Base: ${selectedRecipe.baseServings} servings • ${selectedRecipe.cuisine}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = "Change",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        DropdownMenu(
            expanded = recipeDropdownExpanded,
            onDismissRequest = { recipeDropdownExpanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            OutlinedTextField(
                value = recipeSearchQuery,
                onValueChange = { recipeSearchQuery = it },
                placeholder = { Text("Filter 200 recipes...", fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            matchedRecipes.forEach { recipe ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VegBadge(isVeg = recipe.isVeg, showLabel = false)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = recipe.title,
                                fontWeight = if (recipe.id == selectedRecipe.id) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    },
                    onClick = {
                        selectedRecipe = recipe
                        currentServings = recipe.baseServings
                        recipeDropdownExpanded = false
                        recipeSearchQuery = ""
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Serving Scaler Component
        ServingScalerCard(
            baseServings = selectedRecipe.baseServings,
            targetServings = currentServings,
            onServingsChanged = { currentServings = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Scaled Ingredients Table Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Scaled Ingredients (${selectedRecipe.ingredients.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            IconButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val textList = buildString {
                        append("${selectedRecipe.title} (Scaled to $currentServings servings):\n\n")
                        selectedRecipe.ingredients.forEach { ing ->
                            append("• ${ing.formattedQuantity(selectedRecipe.baseServings, currentServings)} ${ing.name}\n")
                        }
                    }
                    clipboard.setPrimaryClip(ClipData.newPlainText("Recipe Ingredients", textList))
                    Toast.makeText(context, "Shopping list copied to clipboard!", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy List",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Ingredients Lazy List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(selectedRecipe.ingredients) { ing ->
                val originalQty = RecipeIngredient.formatAmount(ing.amount, ing.unit)
                val scaledQty = ing.formattedQuantity(selectedRecipe.baseServings, currentServings)

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ing.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (currentServings != selectedRecipe.baseServings) {
                                Text(
                                    text = "Original: $originalQty",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Scaled Quantity Box
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (currentServings != selectedRecipe.baseServings) TerracottaContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = scaledQty,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (currentServings != selectedRecipe.baseServings) TerracottaPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onOpenRecipeDetail(selectedRecipe, currentServings) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Restaurant, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open Cooking Instructions for $currentServings Servings")
        }
    }
}
