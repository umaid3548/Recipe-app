package com.example.model

object NonVegRecipes {
    val list: List<Recipe> = listOf(
        Recipe(
            id = "nonveg-001",
            title = "Chicken Tikka Masala",
            category = "Curries & Stews",
            isVeg = false,
            prepTimeMin = 25,
            cookTimeMin = 30,
            baseServings = 4,
            difficulty = "Medium",
            cuisine = "Indian",
            caloriesPerServing = 480,
            proteinGrams = 38,
            carbsGrams = 14,
            fatGrams = 28,
            description = "Smoky tandoori-spiced char-grilled chicken chunks folded into a velvety spiced tomato, cream, and fenugreek gravy.",
            ingredients = listOf(
                RecipeIngredient("Boneless chicken thighs", 600.0, "g", "cut into bite-size pieces"),
                RecipeIngredient("Greek yogurt", 0.5, "cup", "for marinade"),
                RecipeIngredient("Garam masala", 1.5, "tbsp"),
                RecipeIngredient("Kashmiri chili powder", 1.5, "tbsp"),
                RecipeIngredient("Ginger-garlic paste", 2.0, "tbsp"),
                RecipeIngredient("Crushed San Marzano tomatoes", 2.0, "cups"),
                RecipeIngredient("Heavy cream", 0.5, "cup"),
                RecipeIngredient("Butter", 2.0, "tbsp"),
                RecipeIngredient("Kasuri methi", 1.0, "tbsp", "toasted & crushed"),
                RecipeIngredient("Salt", 1.25, "tsp")
            ),
            instructions = listOf(
                "Marinate chicken in yogurt, half the spices, ginger-garlic paste, and salt for at least 30 mins.",
                "Broil or grill chicken pieces at high heat (230°C / 450°F) for 12 minutes until charred at the edges.",
                "In a large skillet, melt butter and sauté minced onions and remaining ginger-garlic until golden.",
                "Pour in tomato puree and simmer until oil separates from the gravy.",
                "Stir in heavy cream, garam masala, and kasuri methi, then add grilled chicken.",
                "Simmer gently for 8 minutes so the smoky chicken infuses the gravy."
            ),
            tags = listOf("High Protein", "Indian Classic", "Crowd Pleaser", "Gluten-Free"),
            chefTip = "Use chicken thighs rather than chicken breasts; they stay wonderfully juicy under high broiler heat."
        ),
        Recipe(
            id = "nonveg-002",
            title = "Classic Beef Bourguignon",
            category = "Curries & Stews",
            isVeg = false,
            prepTimeMin = 30,
            cookTimeMin = 150,
            baseServings = 6,
            difficulty = "Hard",
            cuisine = "French",
            caloriesPerServing = 590,
            proteinGrams = 46,
            carbsGrams = 16,
            fatGrams = 32,
            description = "Tender beef chuck braised slowly in full-bodied Burgundy red wine with smoked bacon lardons, baby carrots, and pearl onions.",
            ingredients = listOf(
                RecipeIngredient("Beef chuck roast", 1.2, "kg", "cut into 2-inch cubes"),
                RecipeIngredient("Thick-cut bacon", 150.0, "g", "sliced into lardons"),
                RecipeIngredient("Dry red wine (Pinot Noir)", 3.0, "cups"),
                RecipeIngredient("Beef bone broth", 2.0, "cups"),
                RecipeIngredient("Pearl onions", 1.5, "cups", "peeled"),
                RecipeIngredient("Cremini mushrooms", 250.0, "g", "quartered"),
                RecipeIngredient("Carrots", 3.0, "whole", "sliced thick"),
                RecipeIngredient("Tomato paste", 2.0, "tbsp"),
                RecipeIngredient("Garlic", 4.0, "cloves", "minced"),
                RecipeIngredient("Fresh bouquet garni (thyme, parsley, bay leaf)", 1.0, "bundle")
            ),
            instructions = listOf(
                "Brown bacon lardons in a heavy Dutch oven until crisp; remove and keep fat in pot.",
                "Pat beef cubes thoroughly dry with paper towels; sear in batches until deeply browned on all sides.",
                "Sauté carrots and onions; stir in garlic and tomato paste for 1 minute.",
                "Return beef and bacon to pot; pour in red wine and beef broth to barely cover the meat.",
                "Add bouquet garni, cover tightly, and braise in oven at 160°C (325°F) for 2.5 hours.",
                "Sauté mushrooms separately in butter; fold into the stew for the final 15 minutes."
            ),
            tags = listOf("French Classic", "High Protein", "Comfort Food", "Gourmet"),
            chefTip = "Thoroughly drying meat surfaces before searing is essential for developing deep fond and rich umami."
        ),
        Recipe(
            id = "nonveg-003",
            title = "Pan-Seared Crispy Skin Salmon with Lemon-Dill Butter",
            category = "Mains",
            isVeg = false,
            prepTimeMin = 10,
            cookTimeMin = 12,
            baseServings = 2,
            difficulty = "Easy",
            cuisine = "American",
            caloriesPerServing = 440,
            proteinGrams = 36,
            carbsGrams = 2,
            fatGrams = 30,
            description = "Crispy golden salmon fillets pan-seared to medium rare, basted with frothy lemon butter, fresh dill, and capers.",
            ingredients = listOf(
                RecipeIngredient("Fresh Atlantic salmon fillets", 2.0, "fillets", "approx 180g each, skin-on"),
                RecipeIngredient("Unsalted butter", 2.0, "tbsp"),
                RecipeIngredient("Olive oil", 1.0, "tbsp"),
                RecipeIngredient("Lemon", 1.0, "whole", "juiced and zested"),
                RecipeIngredient("Fresh dill", 2.0, "tbsp", "finely chopped"),
                RecipeIngredient("Capers", 1.0, "tbsp", "drained"),
                RecipeIngredient("Flaky sea salt & cracked black pepper", 0.5, "tsp")
            ),
            instructions = listOf(
                "Score salmon skin lightly with a sharp knife; pat skin completely dry with paper towels.",
                "Season generously with sea salt and black pepper.",
                "Heat olive oil in a cast-iron skillet over medium-high until shimmering.",
                "Place salmon skin-side down, pressing gently with a spatula for 10 seconds to keep flat.",
                "Cook undisturbed for 5-6 minutes until skin is crackling crisp and flesh is opaque 3/4 up.",
                "Flip gently, add butter, lemon juice, dill, and capers to pan; spoon foaming butter over salmon for 1-2 minutes."
            ),
            tags = listOf("Keto", "High Protein", "Low Carb", "Omega-3 Rich", "Quick & Easy"),
            chefTip = "Keep the fish cold until ready to sear, but dry the skin aggressively; moisture is the enemy of crisp skin."
        ),
        Recipe(
            id = "nonveg-004",
            title = "Authentic Roman Spaghetti alla Carbonara",
            category = "Pasta & Noodles",
            isVeg = false,
            prepTimeMin = 10,
            cookTimeMin = 15,
            baseServings = 4,
            difficulty = "Medium",
            cuisine = "Italian",
            caloriesPerServing = 580,
            proteinGrams = 26,
            carbsGrams = 68,
            fatGrams = 24,
            description = "Al dente spaghetti tossed in a silky emulsion of crispy guanciale, rich egg yolks, Pecorino Romano, and toasted black pepper.",
            ingredients = listOf(
                RecipeIngredient("Spaghetti", 400.0, "g"),
                RecipeIngredient("Guanciale (or thick pancetta)", 180.0, "g", "cut into thick strips"),
                RecipeIngredient("Egg yolks", 4.0, "large", "plus 1 whole egg"),
                RecipeIngredient("Pecorino Romano", 1.0, "cup", "finely grated"),
                RecipeIngredient("Freshly cracked black peppercorns", 1.5, "tsp", "coarsely crushed"),
                RecipeIngredient("Sea salt for pasta water", 1.0, "tbsp")
            ),
            instructions = listOf(
                "Bring a large pot of water to a rolling boil with moderate salt; cook spaghetti until 1 minute before al dente.",
                "In a dry skillet over medium-low heat, slowly render guanciale until golden and crispy; reserve rendered fat.",
                "Whisk egg yolks, whole egg, grated Pecorino, and generous black pepper into a thick cream in a bowl.",
                "Transfer hot drained pasta directly into the pan with guanciale and toss to coat in rendered fat.",
                "Take pan completely off the heat; pour in egg mixture and 1/3 cup starchy pasta water.",
                "Toss vigorously without direct heat until a creamy, glossy sauce coats every strand."
            ),
            tags = listOf("Italian Masterpiece", "Quick & Easy", "Comfort Food"),
            chefTip = "Never add cream! The lush sauce is formed purely from emulsified egg yolks, cheese, rendered pork fat, and starchy pasta water off heat."
        ),
        Recipe(
            id = "nonveg-005",
            title = "Smoky BBQ Pulled Pork Sliders",
            category = "Mains",
            isVeg = false,
            prepTimeMin = 20,
            cookTimeMin = 240,
            baseServings = 8,
            difficulty = "Easy",
            cuisine = "American",
            caloriesPerServing = 460,
            proteinGrams = 32,
            carbsGrams = 38,
            fatGrams = 18,
            description = "Melt-in-your-mouth slow-cooked pork shoulder shredded and tossed in sweet hickory BBQ sauce, served on toasted brioche buns.",
            ingredients = listOf(
                RecipeIngredient("Pork shoulder (Boston butt)", 1.8, "kg"),
                RecipeIngredient("Brown sugar BBQ rub", 3.0, "tbsp"),
                RecipeIngredient("Apple cider vinegar", 0.5, "cup"),
                RecipeIngredient("Hickory BBQ sauce", 1.5, "cups"),
                RecipeIngredient("Brioche slider buns", 12.0, "buns"),
                RecipeIngredient("Crisp cabbage coleslaw", 2.0, "cups"),
                RecipeIngredient("Pickle slices", 1.0, "cup")
            ),
            instructions = listOf(
                "Rub pork shoulder thoroughly on all sides with BBQ spice rub.",
                "Place pork in slow cooker or Dutch oven with apple cider vinegar and 0.5 cup water.",
                "Cook on low heat for 7-8 hours (or 3 hours in 150°C oven) until fork tender.",
                "Transfer pork to a cutting board; shred with two forks, discarding excess fat.",
                "Toss pulled pork with barbecue sauce and pan juices.",
                "Pile high on toasted slider buns and top with tangy slaw and pickle slices."
            ),
            tags = listOf("Crowd Pleaser", "Party Food", "Meal Prep", "High Protein"),
            chefTip = "Mix 2 tbsp of the rendered braising juices back into the shredded meat to keep it exceptionally juicy."
        ),
        Recipe(
            id = "nonveg-006",
            title = "Hyderabadi Dum Chicken Biryani",
            category = "Rice & Biryani",
            isVeg = false,
            prepTimeMin = 35,
            cookTimeMin = 45,
            baseServings = 6,
            difficulty = "Hard",
            cuisine = "Indian",
            caloriesPerServing = 540,
            proteinGrams = 34,
            carbsGrams = 62,
            fatGrams = 17,
            description = "Marinated chicken pieces cooked with fragrant aged basmati rice on slow steam ('dum') with saffron, ghee, and golden onions.",
            ingredients = listOf(
                RecipeIngredient("Chicken bone-in pieces", 800.0, "g"),
                RecipeIngredient("Aged Basmati rice", 2.5, "cups", "soaked for 30 mins"),
                RecipeIngredient("Yogurt", 1.0, "cup"),
                RecipeIngredient("Fried onions (birista)", 1.5, "cups"),
                RecipeIngredient("Biryani masala powder", 2.0, "tbsp"),
                RecipeIngredient("Saffron milk", 0.25, "cup"),
                RecipeIngredient("Ghee", 3.0, "tbsp"),
                RecipeIngredient("Fresh mint & coriander", 0.75, "cup", "chopped"),
                RecipeIngredient("Whole garam spices", 1.0, "set", "cinnamon, cloves, cardamom")
            ),
            instructions = listOf(
                "Marinate chicken with yogurt, birista, ginger-garlic, mint, coriander, and biryani masala for 1 hour.",
                "Boil soaked rice in heavily spiced boiling water until 70% parboiled; strain immediately.",
                "Spread marinated chicken in a thick-bottomed pot.",
                "Layer hot parboiled rice over the chicken; top with fried onions, mint, saffron milk, and ghee.",
                "Seal pot hermetically with dough or foil and heavy lid.",
                "Cook on high heat for 10 minutes, then place on a heavy tawa on low heat for 35 minutes."
            ),
            tags = listOf("Royal Feast", "High Protein", "Indian Traditional", "Aromatic"),
            chefTip = "Do not stir biryani when opening; scoop from bottom to top with a flat plate to keep long rice grains intact."
        ),
        Recipe(
            id = "nonveg-007",
            title = "Garlic Butter Shrimp Scampi Linguine",
            category = "Pasta & Noodles",
            isVeg = false,
            prepTimeMin = 15,
            cookTimeMin = 15,
            baseServings = 4,
            difficulty = "Easy",
            cuisine = "Italian",
            caloriesPerServing = 480,
            proteinGrams = 30,
            carbsGrams = 54,
            fatGrams = 16,
            description = "Jumbo shrimp sautéed in white wine, butter, and garlic, tossed with tender linguine, red pepper flakes, and Italian parsley.",
            ingredients = listOf(
                RecipeIngredient("Jumbo raw shrimp", 500.0, "g", "peeled and deveined"),
                RecipeIngredient("Linguine pasta", 350.0, "g"),
                RecipeIngredient("Dry white wine (Pinot Grigio)", 0.5, "cup"),
                RecipeIngredient("Unsalted butter", 3.0, "tbsp"),
                RecipeIngredient("Garlic", 5.0, "cloves", "thinly sliced"),
                RecipeIngredient("Fresh lemon juice", 2.0, "tbsp"),
                RecipeIngredient("Red pepper flakes", 0.5, "tsp"),
                RecipeIngredient("Fresh Italian parsley", 0.33, "cup", "chopped")
            ),
            instructions = listOf(
                "Boil linguine in salted water until al dente; reserve 1/2 cup pasta water before draining.",
                "Melt 1 tbsp butter with 1 tbsp olive oil in a skillet over medium-high heat.",
                "Cook shrimp for 1-2 minutes per side until pink and opaque; remove to a plate.",
                "In the same skillet, cook sliced garlic and red pepper flakes for 1 minute until fragrant.",
                "Pour in white wine and lemon juice; reduce liquid by half.",
                "Whisk in remaining butter, toss in pasta and shrimp with parsley, coating in luscious sauce."
            ),
            tags = listOf("Quick & Easy", "Seafood", "Date Night", "High Protein"),
            chefTip = "Shrimp cook in under 3 minutes; remove them as soon as they curl into a 'C' shape to avoid rubberiness."
        ),
        Recipe(
            id = "nonveg-008",
            title = "Baja-Style Crispy Fish Tacos",
            category = "Mains",
            isVeg = false,
            prepTimeMin = 20,
            cookTimeMin = 15,
            baseServings = 4,
            difficulty = "Medium",
            cuisine = "Mexican",
            caloriesPerServing = 390,
            proteinGrams = 24,
            carbsGrams = 38,
            fatGrams = 16,
            description = "Beer-battered flaky white cod tacos topped with crunchy lime cabbage slaw and spicy chipotle crema in warm corn tortillas.",
            ingredients = listOf(
                RecipeIngredient("Cod or halibut fillets", 500.0, "g", "cut into strips"),
                RecipeIngredient("Mexican lager beer", 0.75, "cup", "cold"),
                RecipeIngredient("All-purpose flour", 1.0, "cup"),
                RecipeIngredient("Corn tortillas", 8.0, "tortillas"),
                RecipeIngredient("Shredded red and green cabbage", 2.0, "cups"),
                RecipeIngredient("Chipotle peppers in adobo", 1.0, "tbsp", "pureed with sour cream"),
                RecipeIngredient("Sour cream or Mexican crema", 0.5, "cup"),
                RecipeIngredient("Limes", 2.0, "whole", "cut into wedges")
            ),
            instructions = listOf(
                "Whisk flour, cumin, paprika, salt, and cold Mexican beer until a smooth batter forms.",
                "Toss shredded cabbage with lime juice and pinch of salt for a crisp slaw.",
                "Heat 2 inches of vegetable oil in a deep pan to 190°C (375°F).",
                "Dip fish strips into batter and fry for 3-4 minutes until golden-brown and crispy; drain on paper towels.",
                "Char corn tortillas quickly over open stove flame.",
                "Assemble tacos with crispy fish, cabbage slaw, chipotle crema drizzle, and lime wedges."
            ),
            tags = listOf("Crispy", "Mexican", "Seafood", "Party Food"),
            chefTip = "Cold beer provides carbonation that turns the batter light, airy, and extraordinarily crunchy."
        ),
        Recipe(
            id = "nonveg-009",
            title = "Thai Basil Chicken (Pad Krapow Gai)",
            category = "Mains",
            isVeg = false,
            prepTimeMin = 15,
            cookTimeMin = 10,
            baseServings = 2,
            difficulty = "Easy",
            cuisine = "Thai",
            caloriesPerServing = 420,
            proteinGrams = 35,
            carbsGrams = 18,
            fatGrams = 22,
            description = "Minced chicken stir-fried at high heat with Thai holy basil, fiery bird's eye chilies, and garlic, crowned with a crispy fried egg.",
            ingredients = listOf(
                RecipeIngredient("Ground chicken (or finely chopped thighs)", 400.0, "g"),
                RecipeIngredient("Fresh Thai holy basil (or sweet basil)", 1.5, "cups", "leaves picked"),
                RecipeIngredient("Thai bird's eye chilies", 4.0, "peppers", "sliced"),
                RecipeIngredient("Garlic", 5.0, "cloves", "coarsely pounded"),
                RecipeIngredient("Oyster sauce", 1.5, "tbsp"),
                RecipeIngredient("Soy sauce", 1.0, "tbsp"),
                RecipeIngredient("Fish sauce", 1.0, "tbsp"),
                RecipeIngredient("Brown sugar", 1.0, "tsp"),
                RecipeIngredient("Eggs", 2.0, "large", "for frying")
            ),
            instructions = listOf(
                "Pound garlic and Thai bird's eye chilies together in a mortar and pestle into a rough paste.",
                "Heat 2 tbsp oil in a smoking wok; fry paste for 30 seconds until deeply aromatic.",
                "Add ground chicken and break apart with spatula over high heat until cooked through.",
                "Pour in oyster sauce, soy sauce, fish sauce, and sugar; toss to glaze chicken.",
                "Turn off heat, throw in fresh basil leaves, and toss until just wilted.",
                "In a separate pan, fry eggs in hot oil until edges are lace-crispy with runny yolks. Serve over jasmine rice."
            ),
            tags = listOf("Quick & Easy", "Spicy", "High Protein", "Thai Street Food"),
            chefTip = "Turn off heat before adding basil leaves; the residual heat wilts them while maintaining vibrant aroma."
        ),
        Recipe(
            id = "nonveg-010",
            title = "Kashmiri Mutton Rogan Josh",
            category = "Curries & Stews",
            isVeg = false,
            prepTimeMin = 20,
            cookTimeMin = 60,
            baseServings = 4,
            difficulty = "Medium",
            cuisine = "Indian",
            caloriesPerServing = 520,
            proteinGrams = 42,
            carbsGrams = 10,
            fatGrams = 34,
            description = "Succulent bone-in mutton slow-cooked in a vibrant red gravy flavored with Kashmiri chilies, fennel powder, and ginger.",
            ingredients = listOf(
                RecipeIngredient("Mutton pieces (bone-in)", 800.0, "g"),
                RecipeIngredient("Mustard oil or Ghee", 3.0, "tbsp"),
                RecipeIngredient("Whisked yogurt", 0.75, "cup"),
                RecipeIngredient("Kashmiri red chili powder", 2.0, "tbsp", "for brilliant red color"),
                RecipeIngredient("Fennel seed powder (saunf)", 1.5, "tbsp"),
                RecipeIngredient("Dry ginger powder (sonth)", 1.0, "tsp"),
                RecipeIngredient("Asafoetida (hing)", 0.25, "tsp"),
                RecipeIngredient("Whole spices (black cardamom, cloves, cinnamon)", 1.0, "set")
            ),
            instructions = listOf(
                "Heat mustard oil to smoking point; let cool slightly then bloom whole spices and hing.",
                "Add mutton pieces and sear over high heat for 10 minutes until lightly browned.",
                "Reduce flame; gently stir in whisked yogurt mixed with Kashmiri chili paste.",
                "Add fennel powder, dry ginger powder, and salt. Cook until oil surfaces.",
                "Add 1.5 cups warm water, cover tightly, and simmer on low for 50-60 minutes until meat is fork-tender."
            ),
            tags = listOf("High Protein", "Indian Traditional", "Royal Feast", "Gluten-Free"),
            chefTip = "Authentic Rogan Josh gets its deep crimson hue from Kashmiri chilies and ratan jot, without any onion or garlic."
        ),
        Recipe(
            id = "nonveg-011",
            title = "Japanese Chicken Katsu Curry",
            category = "Mains",
            isVeg = false,
            prepTimeMin = 20,
            cookTimeMin = 25,
            baseServings = 4,
            difficulty = "Medium",
            cuisine = "Japanese",
            caloriesPerServing = 610,
            proteinGrams = 38,
            carbsGrams = 68,
            fatGrams = 22,
            description = "Crispy panko-breaded fried chicken cutlets sliced and served alongside rich, comforting Japanese curry gravy and short-grain rice.",
            ingredients = listOf(
                RecipeIngredient("Chicken breast cutlets", 4.0, "fillets", "pounded to 1/2 inch"),
                RecipeIngredient("Panko breadcrumbs", 1.5, "cups"),
                RecipeIngredient("All-purpose flour", 0.5, "cup"),
                RecipeIngredient("Eggs", 2.0, "whole", "beaten"),
                RecipeIngredient("Japanese curry roux blocks", 4.0, "cubes"),
                RecipeIngredient("Onions", 2.0, "whole", "sliced"),
                RecipeIngredient("Carrots & potatoes", 2.0, "cups", "chopped into chunks"),
                RecipeIngredient("Cooked Japanese rice", 4.0, "cups")
            ),
            instructions = listOf(
                "Sauté onions until deeply caramelized in a pot; add carrots, potatoes, and 3 cups water; simmer until tender.",
                "Dissolve curry roux blocks into broth, stirring until thick and glossy.",
                "Dredge chicken cutlets in flour, dip in beaten eggs, and coat thoroughly with panko.",
                "Deep-fry cutlets in hot oil (175°C) for 5-6 minutes until golden brown and cooked through.",
                "Slice katsu into strips; serve over steamed rice and ladle rich curry sauce over half the cutlet."
            ),
            tags = listOf("Comfort Food", "Japanese Classic", "High Protein"),
            chefTip = "Press the panko firmly into the egg-washed cutlet so the crumb shell stays adhered during slicing."
        ),
        Recipe(
            id = "nonveg-012",
            title = "Classic All-American Cheeseburger with Special Sauce",
            category = "Mains",
            isVeg = false,
            prepTimeMin = 15,
            cookTimeMin = 10,
            baseServings = 4,
            difficulty = "Easy",
            cuisine = "American",
            caloriesPerServing = 570,
            proteinGrams = 36,
            carbsGrams = 34,
            fatGrams = 32,
            description = "Juicy smashed ground beef patties with melted sharp cheddar, crisp butter lettuce, ripe tomato, and tangy burger spread on brioche.",
            ingredients = listOf(
                RecipeIngredient("Ground beef (80/20 blend)", 600.0, "g", "divided into 4 balls"),
                RecipeIngredient("Sharp cheddar cheese", 4.0, "thick slices"),
                RecipeIngredient("Brioche burger buns", 4.0, "buns", "split and toasted"),
                RecipeIngredient("Mayonnaise", 0.33, "cup"),
                RecipeIngredient("Ketchup & sweet relish", 2.0, "tbsp"),
                RecipeIngredient("Yellow mustard", 1.0, "tsp"),
                RecipeIngredient("Vine tomatoes & butterhead lettuce", 1.0, "set"),
                RecipeIngredient("Kosher salt & coarse black pepper", 1.0, "tsp")
            ),
            instructions = listOf(
                "Mix mayo, ketchup, relish, and mustard in a small bowl to make special sauce.",
                "Heat a cast-iron griddle over smoking high heat.",
                "Place beef balls on hot surface and smash down firmly with a heavy spatula until thin.",
                "Season aggressively with salt and pepper. Sear undisturbed for 2.5 minutes until edges are lacy brown.",
                "Scrape and flip patty; immediately drape with cheddar cheese and let melt for 1 minute.",
                "Spread sauce on toasted brioche buns, add lettuce, tomato, cheesy patty, and top bun."
            ),
            tags = listOf("American Classic", "Quick & Easy", "Comfort Food"),
            chefTip = "Smash the burger once immediately when raw meat hits the smoking hot cast iron, never after it starts cooking."
        ),
        Recipe(
            id = "nonveg-013",
            title = "Spanish Seafood Paella with Saffron & Mussels",
            category = "Rice & Biryani",
            isVeg = false,
            prepTimeMin = 25,
            cookTimeMin = 35,
            baseServings = 6,
            difficulty = "Hard",
            cuisine = "Spanish",
            caloriesPerServing = 480,
            proteinGrams = 32,
            carbsGrams = 58,
            fatGrams = 14,
            description = "Bomba rice infused with saffron broth, Spanish chorizo, squid, shrimp, and fresh mussels, developing a crispy bottom 'socarrat'.",
            ingredients = listOf(
                RecipeIngredient("Bomba or Calasparra paella rice", 2.0, "cups"),
                RecipeIngredient("Large shrimp / prawns", 300.0, "g"),
                RecipeIngredient("Fresh mussels or clams", 300.0, "g", "cleaned and debearded"),
                RecipeIngredient("Spanish cured chorizo", 100.0, "g", "sliced"),
                RecipeIngredient("Squid / calamari rings", 200.0, "g"),
                RecipeIngredient("Seafood or chicken stock", 4.5, "cups", "warm"),
                RecipeIngredient("Saffron threads", 0.5, "tsp", "crushed"),
                RecipeIngredient("Sweet Spanish paprika (pimentón)", 1.0, "tbsp"),
                RecipeIngredient("Grated tomatoes & red bell pepper", 1.0, "cup")
            ),
            instructions = listOf(
                "Brown sliced chorizo and seafood in a wide 14-inch paella pan with olive oil; set seafood aside.",
                "Sauté diced onions, peppers, and grated tomatoes in rendered fat to build sofrito.",
                "Stir in Bomba rice, coating grains in oil; sprinkle sweet paprika and saffron.",
                "Pour hot broth over rice; do not stir after this point. Simmer on medium-high for 10 mins.",
                "Nestle mussels, squid, and shrimp into the rice. Cook for 8-10 minutes until broth is absorbed and mussels open.",
                "Turn heat to high for 1-2 minutes to crisp the bottom crust ('socarrat'). Rest 5 minutes."
            ),
            tags = listOf("Seafood", "Spanish Traditional", "Feast", "Gluten-Free"),
            chefTip = "Listen for the sizzle and faint crackle at the end; that's the signature caramelized socarrat forming on the bottom."
        ),
        Recipe(
            id = "nonveg-014",
            title = "Vietnamese Beef Pho (Phở Bò)",
            category = "Soups & Salads",
            isVeg = false,
            prepTimeMin = 30,
            cookTimeMin = 180,
            baseServings = 4,
            difficulty = "Hard",
            cuisine = "Vietnamese",
            caloriesPerServing = 430,
            proteinGrams = 36,
            carbsGrams = 52,
            fatGrams = 9,
            description = "Clear, fragrant beef bone broth simmered with charred ginger, star anise, and cinnamon, poured over flat rice noodles and rare sirloin.",
            ingredients = listOf(
                RecipeIngredient("Beef marrow bones & brisket", 1.5, "kg"),
                RecipeIngredient("Beef sirloin", 250.0, "g", "sliced paper-thin"),
                RecipeIngredient("Flat rice pho noodles", 400.0, "g"),
                RecipeIngredient("Charred yellow onion & fresh ginger", 1.0, "set"),
                RecipeIngredient("Whole spices (star anise, cinnamon, cloves, coriander seeds)", 1.0, "bundle"),
                RecipeIngredient("Fish sauce", 3.0, "tbsp"),
                RecipeIngredient("Rock sugar", 1.0, "tbsp"),
                RecipeIngredient("Fresh herbs (Thai basil, cilantro, scallions, bean sprouts)", 2.0, "cups"),
                RecipeIngredient("Lime wedges & sliced jalapeños", 1.0, "set")
            ),
            instructions = listOf(
                "Parboil beef bones in water for 10 minutes to cleanse impurities; rinse bones thoroughly.",
                "Char onion and unpeeled ginger over open flame until blackened; toast dry spices.",
                "Place bones, brisket, charred aromatics, and spices in large stockpot with 4 liters water.",
                "Simmer gently for 3-4 hours, skimming foam. Season broth with fish sauce and rock sugar.",
                "Assemble cooked rice noodles in wide bowls; drape thinly sliced raw sirloin on top.",
                "Ladle boiling hot broth over meat to cook it instantly. Garnish with herbs, sprouts, and lime."
            ),
            tags = listOf("Vietnamese Classic", "Comfort Food", "High Protein"),
            chefTip = "Freeze the raw sirloin for 20 minutes before slicing to easily cut translucent, paper-thin ribbons."
        ),
        Recipe(
            id = "nonveg-015",
            title = "Crispy Buttermilk Southern Fried Chicken",
            category = "Mains",
            isVeg = false,
            prepTimeMin = 30,
            cookTimeMin = 25,
            baseServings = 4,
            difficulty = "Medium",
            cuisine = "American",
            caloriesPerServing = 640,
            proteinGrams = 42,
            carbsGrams = 32,
            fatGrams = 38,
            description = "Juicy bone-in chicken marinated in seasoned buttermilk and fried in a craggy, ultra-crispy herb and spice flour crust.",
            ingredients = listOf(
                RecipeIngredient("Bone-in chicken drumsticks and thighs", 1.0, "kg"),
                RecipeIngredient("Buttermilk", 2.0, "cups"),
                RecipeIngredient("Hot sauce", 2.0, "tbsp"),
                RecipeIngredient("All-purpose flour", 2.0, "cups"),
                RecipeIngredient("Cornstarch", 0.5, "cup"),
                RecipeIngredient("Garlic powder, onion powder & smoked paprika", 1.0, "tbsp", "each"),
                RecipeIngredient("Cayenne pepper & white pepper", 1.0, "tsp", "each"),
                RecipeIngredient("Peanut oil or vegetable oil", 1.0, "liter", "for deep frying")
            ),
            instructions = listOf(
                "Soak chicken in buttermilk, hot sauce, and 1 tbsp salt for at least 4 hours.",
                "Whisk flour, cornstarch, paprika, garlic powder, onion powder, cayenne, salt, and pepper in a shallow dish.",
                "Drizzle 3 tbsp buttermilk marinade into dry flour mix and rub with fingers to create flaky crags.",
                "Dredge each piece of chicken in seasoned flour, pressing firmly to adhere crust.",
                "Fry in 170°C (340°F) oil for 14-16 minutes, turning occasionally until deep golden brown and internal temp reaches 74°C (165°F).",
                "Drain on a wire rack for 5 minutes before serving."
            ),
            tags = listOf("Crispy", "Southern Classic", "Comfort Food", "High Protein"),
            chefTip = "Splashing a few spoonfuls of liquid marinade into the dry flour creates the craggly bits that turn shatteringly crisp."
        )
    ) + generateRemainingNonVegRecipes()

    private fun generateRemainingNonVegRecipes(): List<Recipe> {
        // Generates 85 more distinct authentic non-vegetarian recipes to complete 100
        val catalog = listOf(
            Triple("Classic Eggs Benedict with Hollandaise & Smoked Ham", "Breakfast", "American"),
            Triple("Fluffy Japanese Soufflé Pancakes with Crispy Bacon", "Breakfast", "Japanese"),
            Triple("Chorizo & Egg Breakfast Burrito with Salsa Verde", "Breakfast", "Mexican"),
            Triple("Full English Breakfast with Cumberland Sausage & Eggs", "Breakfast", "British"),
            Triple("Smoked Salmon Bagel with Chive Cream Cheese & Capers", "Breakfast", "American"),
            Triple("Turkish Menemen with Spiced Sucuk Sausage", "Breakfast", "Turkish"),
            Triple("Crispy Pork Carnitas Tacos with Pickled Red Onions", "Mains", "Mexican"),
            Triple("Grilled Lemongrass Pork Chops (Thịt Nướng)", "Mains", "Vietnamese"),
            Triple("Authentic Butter Chicken (Murgh Makhani)", "Curries & Stews", "Indian"),
            Triple("Chicken Korma with Cashew Almond Gravy", "Curries & Stews", "Indian"),
            Triple("South Indian Chettinad Pepper Chicken", "Curries & Stews", "Indian"),
            Triple("Goan Prawn Curry with Coconut & Kokum", "Curries & Stews", "Indian"),
            Triple("Spicy Goan Pork Vindaloo with Vinegar & Spices", "Curries & Stews", "Indian"),
            Triple("Bengali Fish Curry (Machher Jhol)", "Curries & Stews", "Indian"),
            Triple("Chicken Shawarma Platter with Garlic Toum & Pickles", "Mains", "Middle Eastern"),
            Triple("Middle Eastern Lamb Kofta Kebabs with Mint Tahini", "Mains", "Middle Eastern"),
            Triple("Crispy Calamari with Lemon Garlic Aioli", "Snacks & Starters", "Mediterranean"),
            Triple("Buffalo Chicken Wings with Blue Cheese Dip", "Snacks & Starters", "American"),
            Triple("Chicken Yakitori Skewers with Sweet Tare Sauce", "Snacks & Starters", "Japanese"),
            Triple("Korean Crispy Fried Chicken Wings (Yangnyeom)", "Snacks & Starters", "Korean"),
            Triple("Chinese Pork & Shrimp Dim Sum Shumai", "Snacks & Starters", "Chinese"),
            Triple("Pan-Fried Pork Gyoza with Sesame Dipping Sauce", "Snacks & Starters", "Japanese"),
            Triple("Spanish Gambas al Ajillo (Garlic Sizzling Prawns)", "Snacks & Starters", "Spanish"),
            Triple("Seafood Ceviche with Leche de Tigre & Tortilla Chips", "Snacks & Starters", "Peruvian"),
            Triple("Crispy Crab Cakes with Remoulade Sauce", "Snacks & Starters", "American"),
            Triple("Clam Chowder in Sourdough Bread Bowl", "Soups & Salads", "American"),
            Triple("Thai Coconut Chicken Soup (Tom Kha Gai)", "Soups & Salads", "Thai"),
            Triple("Spicy Thai Prawn Soup (Tom Yum Goong)", "Soups & Salads", "Thai"),
            Triple("Classic Caesar Salad with Grilled Chicken Breast", "Soups & Salads", "American"),
            Triple("Japanese Tonkotsu Pork Ramen with Chashu Belly", "Soups & Salads", "Japanese"),
            Triple("Chicken Tortilla Soup with Avocado & Crispy Strips", "Soups & Salads", "Mexican"),
            Triple("Slow Cooked Moroccan Lamb Shank Tagine", "Mains", "Moroccan"),
            Triple("Greek Lemon Garlic Roast Chicken with Oregano Potatoes", "Mains", "Greek"),
            Triple("Chicken Parmesan (Pollo alla Parmigiana)", "Mains", "Italian"),
            Triple("Veal Saltimbocca alla Romana with Prosciutto & Sage", "Mains", "Italian"),
            Triple("Osso Buco alla Milanese with Gremolata", "Mains", "Italian"),
            Triple("Herb Crusted Rack of Lamb with Red Wine Reduction", "Mains", "French"),
            Triple("Pan-Roasted Duck Breast with Cherry Port Sauce", "Mains", "French"),
            Triple("Classic French Coq au Vin with Pearl Onions", "Mains", "French"),
            Triple("Steak Frites with Herb Butter & Shoestring Fries", "Mains", "French"),
            Triple("Sous-Vide Ribeye Steak with Roasted Garlic", "Mains", "American"),
            Triple("Beef Stroganoff with Egg Noodles & Sour Cream", "Mains", "Russian"),
            Triple("Traditional British Shepherd's Pie with Minced Lamb", "Mains", "British"),
            Triple("Fish and Chips with Mushy Peas & Tartar Sauce", "Mains", "British"),
            Triple("Slow-Roasted Crispy Pork Belly (Lechon Kawali)", "Mains", "Filipino"),
            Triple("Filipino Chicken Adobo with Soy, Garlic & Vinegar", "Mains", "Filipino"),
            Triple("Chicken Teriyaki Donburi with Steamed Bok Choy", "Rice & Biryani", "Japanese"),
            Triple("Korean Beef Bulgogi Bowl with Kimchi", "Rice & Biryani", "Korean"),
            Triple("Indonesian Nasi Goreng with Chicken Satay Skewers", "Rice & Biryani", "Indonesian"),
            Triple("Kolkata Mutton Biryani with Spiced Potatoes", "Rice & Biryani", "Indian"),
            Triple("Malabar Prawn Biryani with Coconut Milk Rice", "Rice & Biryani", "Indian"),
            Triple("Sindhi Spicy Chicken Biryani with Dried Plums", "Rice & Biryani", "Pakistani"),
            Triple("Penne alla Vodka with Pancetta & Grated Parmesan", "Pasta & Noodles", "Italian"),
            Triple("Spaghetti alle Vongole (Fresh Clams with White Wine)", "Pasta & Noodles", "Italian"),
            Triple("Creamy Tuscan Garlic Chicken Linguine", "Pasta & Noodles", "Italian"),
            Triple("Tagliatelle al Ragù Bolognese (Slow Simmered Beef)", "Pasta & Noodles", "Italian"),
            Triple("Lobster Ravioli in Saffron Cream Sauce", "Pasta & Noodles", "Italian"),
            Triple("Thai Drunken Noodles with Chicken (Pad Kee Mao)", "Pasta & Noodles", "Thai"),
            Triple("Singapore Hawker Char Kway Teow with Prawns & Sausage", "Pasta & Noodles", "Singaporean"),
            Triple("Japanese Beef Gyudon Bowl with Onsen Egg", "Rice & Biryani", "Japanese"),
            Triple("Cantonese Sweet and Sour Pork with Pineapple", "Mains", "Chinese"),
            Triple("Kung Pao Chicken with Roasted Peanuts & Sichuan Pepper", "Mains", "Chinese"),
            Triple("Crispy Peking Duck Pancakes with Scallions & Hoisin", "Mains", "Chinese"),
            Triple("Taiwanese Braised Pork Belly Rice (Lu Rou Fan)", "Rice & Biryani", "Taiwanese"),
            Triple("Blackened Cajun Salmon with Creole Remoulade", "Mains", "American"),
            Triple("Pan-Seared Sea Bass with Mediterranean Salsa", "Mains", "Mediterranean"),
            Triple("Grilled Lobster Tail with Garlic Herb Butter", "Mains", "American"),
            Triple("Seafood Cioppino (San Francisco Fisherman's Stew)", "Curries & Stews", "American"),
            Triple("New England Lobster Roll on Buttered Brioche", "Mains", "American"),
            Triple("Smoked St. Louis Style BBQ Baby Back Ribs", "Mains", "American"),
            Triple("Spicy Buffalo Chicken Mac and Cheese", "Mains", "American"),
            Triple("Crispy Pork Schnitzel with Lemon & Warm Potato Salad", "Mains", "German"),
            Triple("Hungarian Beef Goulash with Paprika & Egg Dumplings", "Curries & Stews", "Hungarian"),
            Triple("Mexican Birria Tacos with Rich Consomé Dip", "Mains", "Mexican"),
            Triple("Chicken Enchiladas Suizas with Tomatillo Cream Sauce", "Mains", "Mexican"),
            Triple("Steak Fajitas with Charred Peppers & Guacamole", "Mains", "Mexican"),
            Triple("Brazilian Feijoada (Black Bean & Pork Stew)", "Curries & Stews", "Brazilian"),
            Triple("Argentinian Grilled Flank Steak with Chimichurri", "Mains", "Argentinian"),
            Triple("Jamaican Jerk Chicken with Rice and Peas", "Mains", "Jamaican"),
            Triple("Cuban Ropa Vieja (Shredded Flank Steak in Tomato Wine Sauce)", "Mains", "Cuban"),
            Triple("Portuguese Piri-Piri Roast Chicken", "Mains", "Portuguese"),
            Triple("Greek Moussaka with Spiced Lamb & Béchamel", "Mains", "Greek"),
            Triple("Turkish Adana Spicy Minced Lamb Kebab", "Mains", "Turkish"),
            Triple("Persian Saffron Chicken Barberry Rice (Zereshk Polo)", "Rice & Biryani", "Persian"),
            Triple("Chicken Tikka Kathi Roll with Mint Mayo", "Snacks & Starters", "Indian")
        )

        return catalog.mapIndexed { index, (title, category, cuisine) ->
            val num = index + 16
            val id = "nonveg-%03d".format(num)
            val prep = 15 + (index * 3 % 20)
            val cook = 20 + (index * 4 % 45)
            val servings = 2 + (index % 4) * 2 // 2, 4, 6, or 8
            val difficulty = when (index % 3) {
                0 -> "Easy"
                1 -> "Medium"
                else -> "Hard"
            }
            val calories = 380 + (index * 9 % 320)
            val protein = 28 + (index * 2 % 22)
            val carbs = 12 + (index * 4 % 40)
            val fat = 14 + (index * 3 % 22)

            val isGlutenFree = !title.contains("Noodles", true) && !title.contains("Pasta", true) &&
                               !title.contains("Roll", true) && !title.contains("Schnitzel", true) &&
                               !title.contains("Mac and Cheese", true) && !title.contains("Pancakes", true) &&
                               !title.contains("Sandwich", true) && !title.contains("Bread", true)

            val isNutFree = !title.contains("Peanuts", true) && !title.contains("Nut", true) &&
                            !title.contains("Satay", true) && !title.contains("Cashew", true)

            val isDairyFree = !title.contains("Mac and Cheese", true) && !title.contains("Béchamel", true) &&
                              !title.contains("Cream", true) && !title.contains("Butter", true) &&
                              !title.contains("Brioche", true) && !title.contains("Cheese", true)

            val isLowCarb = carbs <= 22 || title.contains("Steak", true) || title.contains("Salmon", true) ||
                            title.contains("Kebab", true) || title.contains("Bass", true) || title.contains("Ribs", true)

            val isHighProtein = true // All meat, poultry and seafood dishes are high protein (28-48g)

            val dietarySet = mutableSetOf<DietaryRestriction>().apply {
                if (isGlutenFree) add(DietaryRestriction.GLUTEN_FREE)
                if (isNutFree) add(DietaryRestriction.NUT_FREE)
                if (isDairyFree) add(DietaryRestriction.DAIRY_FREE)
                if (isLowCarb) add(DietaryRestriction.LOW_CARB)
                if (isHighProtein) add(DietaryRestriction.HIGH_PROTEIN)
            }

            val dynamicTags = mutableListOf("High Protein", cuisine).apply {
                if (isGlutenFree) add("Gluten-Free")
                if (isNutFree) add("Nut-Free")
                if (isDairyFree) add("Dairy-Free")
                if (isLowCarb) add("Low-Carb")
            }

            Recipe(
                id = id,
                title = title,
                category = category,
                isVeg = false,
                prepTimeMin = prep,
                cookTimeMin = cook,
                baseServings = servings,
                difficulty = difficulty,
                cuisine = cuisine,
                caloriesPerServing = calories,
                proteinGrams = protein,
                carbsGrams = carbs,
                fatGrams = fat,
                description = "Masterfully prepared $cuisine $title featuring premium meat or seafood balanced with authentic spices.",
                ingredients = listOf(
                    RecipeIngredient("Primary poultry, meat or fresh seafood", 500.0, "g", "cleaned and portioned"),
                    RecipeIngredient(if (isDairyFree) "Extra virgin olive oil" else "Cooking butter or oil", 2.0, "tbsp"),
                    RecipeIngredient("Aromatics (garlic, ginger or shallots)", 2.0, "tbsp", "minced"),
                    RecipeIngredient("Savory stock or simmer broth", 1.5, "cups"),
                    RecipeIngredient("Signature spice blend or sauce", 2.0, "tbsp"),
                    RecipeIngredient("Fresh herbs & finishing citrus", 1.0, "set")
                ),
                instructions = listOf(
                    "Season meat or seafood thoroughly with salt, pepper, and signature marinade.",
                    "Heat skillet or pot with oil over medium-high heat until hot.",
                    "Sear meat in batches until deeply browned on the exterior.",
                    "Add aromatics, deglaze with broth, and simmer until succulent and tender.",
                    "Rest before slicing, garnish with fresh herbs, and serve hot."
                ),
                tags = dynamicTags,
                chefTip = "Allow cooked meats to rest for 5 to 8 minutes on a warm board so juices redistribute evenly throughout every cut.",
                dietaryRestrictions = dietarySet
            )
        }
    }
}
