const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');

const PORT = 3000;
const HOST = '0.0.0.0';

// Load prebuilt recipes
let recipes = [];
try {
  const recipesPath = path.join(__dirname, 'public', 'recipes.json');
  if (fs.existsSync(recipesPath)) {
    recipes = JSON.parse(fs.readFileSync(recipesPath, 'utf8'));
    console.log(`[Recipe App] Loaded ${recipes.length} recipes (${recipes.filter(r => r.isVeg).length} Veg, ${recipes.filter(r => !r.isVeg).length} Non-Veg).`);
  }
} catch (err) {
  console.error('[Recipe App] Error loading recipes.json:', err);
}

// Check for compiled Android APK
const apkPath = path.join(__dirname, 'app', 'build', 'outputs', 'apk', 'debug', 'app-debug.apk');

// Server
const server = http.createServer(async (req, res) => {
  const parsedUrl = url.parse(req.url, true);
  const pathname = parsedUrl.pathname;

  // CORS headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  // Health endpoint
  if (pathname === '/health' || pathname === '/api/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', recipesCount: recipes.length }));
    return;
  }

  // APK download endpoint
  if (pathname === '/app-debug.apk') {
    if (fs.existsSync(apkPath)) {
      const stat = fs.statSync(apkPath);
      res.writeHead(200, {
        'Content-Type': 'application/vnd.android.package-archive',
        'Content-Length': stat.size,
        'Content-Disposition': 'attachment; filename="RecipeApp-debug.apk"'
      });
      fs.createReadStream(apkPath).pipe(res);
      return;
    } else {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('APK not found. Run compile_applet to build it.');
      return;
    }
  }

  // API: Get recipes with filtering
  if (pathname === '/api/recipes' && req.method === 'GET') {
    const { veg, search, cuisine, category, dietary } = parsedUrl.query;
    let filtered = [...recipes];

    if (veg === 'true') {
      filtered = filtered.filter(r => r.isVeg);
    } else if (veg === 'false') {
      filtered = filtered.filter(r => !r.isVeg);
    }

    if (cuisine) {
      filtered = filtered.filter(r => r.cuisine.toLowerCase() === cuisine.toLowerCase());
    }

    if (category) {
      filtered = filtered.filter(r => r.category.toLowerCase() === category.toLowerCase());
    }

    if (dietary) {
      const requested = dietary.split(',').map(d => d.trim().toUpperCase());
      filtered = filtered.filter(r => {
        const restrictions = r.dietaryRestrictions || [];
        return requested.every(req => restrictions.includes(req));
      });
    }

    if (search) {
      const q = search.toLowerCase().trim();
      filtered = filtered.filter(r => 
        r.title.toLowerCase().includes(q) ||
        r.description.toLowerCase().includes(q) ||
        r.cuisine.toLowerCase().includes(q) ||
        (r.tags && r.tags.some(t => t.toLowerCase().includes(q))) ||
        (r.ingredients && r.ingredients.some(i => i.name.toLowerCase().includes(q)))
      );
    }

    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify(filtered));
    return;
  }

  // API: Chef AI Chat
  if (pathname === '/api/chat' && req.method === 'POST') {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', async () => {
      try {
        const { message, recipeContext } = JSON.parse(body || '{}');
        const apiKey = process.env.GEMINI_API_KEY;

        let reply = '';
        if (apiKey) {
          try {
            const promptText = `You are "Chef AI", a friendly, knowledgeable, Michelin-caliber executive chef and culinary consultant.
Context:
${recipeContext ? `The user is asking about or viewing recipe: "${recipeContext.title}" (${recipeContext.isVeg ? 'Vegetarian' : 'Non-Vegetarian'}, ${recipeContext.cuisine} cuisine, base servings: ${recipeContext.baseServings}). Ingredients: ${recipeContext.ingredients.map(i => i.name).join(', ')}.` : 'The user is browsing the 200 prebuilt recipe collection (100 veg, 100 non-veg).'}

User query: ${message}

Instructions:
Provide clear, actionable, culinary advice. If discussing scaling, remember cooking times do not always scale linearly. If discussing substitutions, explain both flavor and texture impacts. Keep responses concise, warm, and structured with bullet points where helpful.`;

            const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                contents: [{ parts: [{ text: promptText }] }]
              })
            });

            if (response.ok) {
              const data = await response.json();
              reply = data?.candidates?.[0]?.content?.parts?.[0]?.text || '';
            }
          } catch (e) {
            console.error('[Chef AI] Gemini error:', e);
          }
        }

        // Fallback intelligent culinary knowledge base if API key unavailable or failed
        if (!reply) {
          const lower = (message || '').toLowerCase();
          if (lower.includes('scale') || lower.includes('double') || lower.includes('triple') || lower.includes('serving')) {
            reply = `👨‍🍳 **Scaling Advice from Chef AI:**\n\n- **Direct Proportions**: Ingredients scale linearly with your serving multiplier. If doubling from 4 to 8, double all liquids, produce, and proteins.\n- **Spices & Heat**: Scale spices by 1.5x to 1.75x initially, then taste and adjust. Full 2x heat can overpower!\n- **Cooking Times**: Simmering liquid takes slightly longer to reach a boil in larger pots, but internal cooking temperatures remain the same.`;
          } else if (lower.includes('substitute') || lower.includes('egg') || lower.includes('dairy') || lower.includes('replace')) {
            reply = `👨‍🍳 **Culinary Substitution Guide:**\n\n- **For Eggs in Baking**: 1/4 cup applesauce or 1 tbsp ground flaxseed mixed with 3 tbsp warm water (flax egg).\n- **For Heavy Cream**: Coconut cream (1:1 for richness) or blended soaked cashews with vegetable broth for a savory velvety finish.\n- **For Butter**: Equal parts olive oil or refined coconut oil for dairy-free cooking.`;
          } else if (lower.includes('wine') || lower.includes('pair')) {
            reply = `👨‍🍳 **Sommelier Pairing Note:**\n\n- **Rich Vegetarian Dishes** (like Paneer Butter Masala): A dry or off-dry Riesling, Gewürztraminer, or crisp Viognier cuts through the richness.\n- **Hearty Non-Veg Stews & Braises**: A full-bodied Syrah, Cabernet Sauvignon, or Malbec complements the savory caramelized proteins.`;
          } else if (lower.includes('paneer') || lower.includes('butter masala')) {
            reply = `👨‍🍳 **Chef Secret for Paneer Butter Masala:**\n\nSoak your paneer cubes in warm, salted water for 10 minutes before adding them to the simmering gravy. It keeps the cheese silky soft and prevents it from turning rubbery!`;
          } else {
            reply = `👨‍🍳 **Chef AI at your service!**\n\nI can help you scale recipes for parties, suggest ingredient substitutions for dietary needs, optimize cooking techniques, and recommend perfect pairings. What are you cooking today?`;
          }
        }

        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ reply }));
      } catch (err) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: err.message }));
      }
    });
    return;
  }

  // Serve static files from public/ or root
  if (pathname === '/recipes.json') {
    const jsonPath = path.join(__dirname, 'public', 'recipes.json');
    if (fs.existsSync(jsonPath)) {
      res.writeHead(200, { 'Content-Type': 'application/json' });
      fs.createReadStream(jsonPath).pipe(res);
      return;
    }
  }

  // Root Web Application
  if (pathname === '/' || pathname === '/index.html') {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    res.end(getHtmlApp());
    return;
  }

  res.writeHead(404, { 'Content-Type': 'text/plain' });
  res.end('Not Found');
});

function getHtmlApp() {
  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Culinary Recipe App • 200 Recipes & Chef AI</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Playfair+Display:ital,wght@0,600;0,700;1,600&display=swap" rel="stylesheet">
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      theme: {
        extend: {
          colors: {
            brand: {
              50: '#fdf4f0',
              100: '#fbe8df',
              500: '#c85a32',
              600: '#b04924',
              700: '#8e3719',
              900: '#43190b',
            },
            sage: {
              50: '#f2f7f4',
              100: '#e1ede5',
              600: '#2d7a52',
              700: '#225d3e',
            }
          },
          fontFamily: {
            sans: ['"Plus Jakarta Sans"', 'sans-serif'],
            serif: ['"Playfair Display"', 'serif'],
          }
        }
      }
    }
  </script>
  <style>
    body { font-family: 'Plus Jakarta Sans', sans-serif; }
    .serif-title { font-family: 'Playfair Display', serif; }
    ::-webkit-scrollbar { width: 6px; height: 6px; }
    ::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 9999px; }
    ::-webkit-scrollbar-thumb:hover { background: #94a3b8; }
  </style>
</head>
<body class="bg-[#faf8f5] text-stone-800 min-h-screen flex flex-col">

  <!-- Top Announcement / Android APK Download Banner -->
  <header class="bg-stone-900 text-white text-xs sm:text-sm py-2 px-4 border-b border-stone-800">
    <div class="max-w-7xl mx-auto flex flex-wrap items-center justify-between gap-2">
      <div class="flex items-center gap-2">
        <span class="inline-block w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
        <span class="font-medium text-stone-200">200 Prebuilt Recipes Active: 100 Vegetarian & 100 Non-Vegetarian</span>
      </div>
      <div class="flex items-center gap-3">
        <a href="/app-debug.apk" class="inline-flex items-center gap-1.5 bg-brand-500 hover:bg-brand-600 text-white px-3 py-1 rounded-full font-semibold transition text-xs shadow-sm">
          <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 24 24"><path d="M17.523 15.3414c-.5511 0-.9993-.4486-.9993-.9997s.4482-.9993.9993-.9993c.551 0 .9993.4482.9993.9993.0001.5511-.4483.9997-.9993.9997m-11.046 0c-.5511 0-.9993-.4486-.9993-.9997s.4482-.9993.9993-.9993c.5511 0 .9993.4482.9993.9993 0 .5511-.4482.9997-.9993.9997m11.4045-6.02l1.996-3.4572c.156-.2704.063-.6158-.207-.7719-.2705-.156-.6159-.0631-.772.2073l-2.0223 3.5028c-1.4475-.6625-3.0768-1.0407-4.8262-1.0407-1.7493 0-3.3787.3782-4.8262 1.0407l-2.0223-3.5028c-.1561-.2704-.5015-.3633-.772-.2073-.2704.1561-.3633.5015-.2073.7719l1.996 3.4572C4.3031 11.0664 2.019 14.502 1.637 18.57h20.726c-.382-4.068-2.6661-7.5036-4.4815-9.2486"/></svg>
          Download Android APK
        </a>
      </div>
    </div>
  </header>

  <!-- Main Navigation Bar -->
  <nav class="sticky top-0 z-30 bg-white/95 backdrop-blur border-b border-stone-200 px-4 py-3 shadow-xs">
    <div class="max-w-7xl mx-auto flex items-center justify-between gap-4">
      <div class="flex items-center gap-3 cursor-pointer" onclick="switchTab('recipes')">
        <div class="w-10 h-10 rounded-xl bg-brand-500 text-white flex items-center justify-center font-serif text-xl font-bold shadow-sm">
          S
        </div>
        <div>
          <h1 class="serif-title text-xl font-bold text-stone-900 leading-tight">Savor & Scale</h1>
          <p class="text-xs text-stone-500 font-medium">Culinary Assistant & Scaling Engine</p>
        </div>
      </div>

      <!-- Navigation Tabs -->
      <div class="flex items-center gap-1 bg-stone-100 p-1 rounded-xl border border-stone-200 text-xs sm:text-sm font-medium">
        <button id="tab-btn-recipes" onclick="switchTab('recipes')" class="px-3 py-1.5 rounded-lg transition bg-white text-stone-900 shadow-xs font-semibold">
          📖 Recipes (<span id="total-count-badge">200</span>)
        </button>
        <button id="tab-btn-scaler" onclick="switchTab('scaler')" class="px-3 py-1.5 rounded-lg transition text-stone-600 hover:text-stone-900">
          ⚖️ Serving Scaler
        </button>
        <button id="tab-btn-chef" onclick="switchTab('chef')" class="px-3 py-1.5 rounded-lg transition text-stone-600 hover:text-stone-900 flex items-center gap-1">
          ✨ Chef AI
        </button>
      </div>
    </div>
  </nav>

  <!-- Main Content Container -->
  <main class="max-w-7xl mx-auto p-4 sm:p-6 w-full flex-1">

    <!-- VIEW 1: RECIPES BROWSER -->
    <div id="view-recipes" class="space-y-6">

      <!-- Search & Primary Filters Card -->
      <div class="bg-white rounded-2xl p-4 sm:p-5 border border-stone-200 shadow-xs space-y-4">
        <div class="flex flex-col md:flex-row gap-3">
          <!-- Search input -->
          <div class="relative flex-1">
            <svg class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-stone-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
            <input id="search-input" type="text" placeholder="Search 200 recipes by title, ingredients, cuisine..." class="w-full pl-10 pr-4 py-2.5 bg-stone-50 border border-stone-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-brand-500 focus:border-brand-500 transition" oninput="debounceSearch()">
          </div>

          <!-- Diet filter buttons (All / Veg / Non-Veg) -->
          <div class="flex items-center gap-1 bg-stone-100 p-1 rounded-xl border border-stone-200 self-start">
            <button id="diet-btn-all" onclick="setDietFilter('all')" class="px-3 py-1.5 rounded-lg text-xs font-semibold bg-white text-stone-900 shadow-xs">
              All (200)
            </button>
            <button id="diet-btn-veg" onclick="setDietFilter('veg')" class="px-3 py-1.5 rounded-lg text-xs font-semibold text-stone-600 hover:text-stone-900 flex items-center gap-1">
              <span class="w-2 h-2 rounded-full bg-emerald-600"></span> Veg (100)
            </button>
            <button id="diet-btn-nonveg" onclick="setDietFilter('nonveg')" class="px-3 py-1.5 rounded-lg text-xs font-semibold text-stone-600 hover:text-stone-900 flex items-center gap-1">
              <span class="w-2 h-2 rounded-full bg-red-600"></span> Non-Veg (100)
            </button>
          </div>
        </div>

        <!-- Multi-Criteria Dietary Restrictions Bar -->
        <div class="pt-3 border-t border-stone-100">
          <div class="flex flex-wrap items-center justify-between gap-2 mb-2">
            <div class="flex items-center gap-2">
              <span class="text-xs font-bold uppercase tracking-wider text-stone-500">Dietary Profile Filters:</span>
              <span id="dietary-matches-all-badge" class="hidden text-xs bg-brand-50 text-brand-700 font-semibold px-2 py-0.5 rounded-full border border-brand-200">
                Matches All Selected
              </span>
            </div>
            <button id="clear-dietary-btn" onclick="clearDietaryFilters()" class="hidden text-xs text-brand-600 hover:text-brand-700 font-semibold hover:underline">
              Clear Filters
            </button>
          </div>

          <div class="flex flex-wrap gap-2" id="dietary-chip-container">
            <!-- Dietary Chips dynamically generated or static -->
            <button onclick="toggleDietary('VEGAN')" id="chip-VEGAN" class="dietary-chip px-3 py-1.5 rounded-full text-xs font-medium border border-stone-200 bg-stone-50 text-stone-700 hover:bg-stone-100 transition flex items-center gap-1.5">
              <span>🌱</span> Vegan
            </button>
            <button onclick="toggleDietary('GLUTEN_FREE')" id="chip-GLUTEN_FREE" class="dietary-chip px-3 py-1.5 rounded-full text-xs font-medium border border-stone-200 bg-stone-50 text-stone-700 hover:bg-stone-100 transition flex items-center gap-1.5">
              <span>🌾</span> Gluten-Free
            </button>
            <button onclick="toggleDietary('NUT_FREE')" id="chip-NUT_FREE" class="dietary-chip px-3 py-1.5 rounded-full text-xs font-medium border border-stone-200 bg-stone-50 text-stone-700 hover:bg-stone-100 transition flex items-center gap-1.5">
              <span>🥜</span> Nut-Free
            </button>
            <button onclick="toggleDietary('DAIRY_FREE')" id="chip-DAIRY_FREE" class="dietary-chip px-3 py-1.5 rounded-full text-xs font-medium border border-stone-200 bg-stone-50 text-stone-700 hover:bg-stone-100 transition flex items-center gap-1.5">
              <span>🥛</span> Dairy-Free
            </button>
            <button onclick="toggleDietary('LOW_CARB')" id="chip-LOW_CARB" class="dietary-chip px-3 py-1.5 rounded-full text-xs font-medium border border-stone-200 bg-stone-50 text-stone-700 hover:bg-stone-100 transition flex items-center gap-1.5">
              <span>🥑</span> Low-Carb
            </button>
            <button onclick="toggleDietary('HIGH_PROTEIN')" id="chip-HIGH_PROTEIN" class="dietary-chip px-3 py-1.5 rounded-full text-xs font-medium border border-stone-200 bg-stone-50 text-stone-700 hover:bg-stone-100 transition flex items-center gap-1.5">
              <span>🥩</span> High-Protein
            </button>
          </div>
        </div>
      </div>

      <!-- Results Count & Active Status -->
      <div class="flex items-center justify-between px-1">
        <p class="text-xs sm:text-sm font-medium text-stone-600">
          Showing <span id="results-count" class="font-bold text-stone-900">200</span> recipes
        </p>
      </div>

      <!-- Recipe Grid -->
      <div id="recipe-grid" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
        <!-- Recipe cards rendered here -->
      </div>

      <!-- Empty State -->
      <div id="empty-state" class="hidden text-center py-16 bg-white rounded-2xl border border-stone-200 p-8">
        <div class="text-4xl mb-3">🔍</div>
        <h3 class="text-lg font-bold text-stone-900 mb-1">No matching recipes found</h3>
        <p class="text-sm text-stone-500 max-w-md mx-auto mb-4">
          Try relaxing some of your search terms or dietary restriction filters. Recipes must match all active criteria simultaneously.
        </p>
        <button onclick="resetAllFilters()" class="bg-brand-500 hover:bg-brand-600 text-white text-xs font-bold px-4 py-2 rounded-xl transition">
          Reset All Filters
        </button>
      </div>

    </div>

    <!-- VIEW 2: SERVING SCALER TOOL -->
    <div id="view-scaler" class="hidden max-w-3xl mx-auto space-y-6">
      <div class="bg-white rounded-2xl p-6 border border-stone-200 shadow-xs space-y-6">
        <div>
          <h2 class="serif-title text-2xl font-bold text-stone-900">Dynamic Serving Scaler</h2>
          <p class="text-sm text-stone-500">Select any of the 200 prebuilt recipes to automatically recalibrate all ingredient measurements and nutritional calculations.</p>
        </div>

        <!-- Recipe Selector -->
        <div>
          <label class="block text-xs font-bold uppercase tracking-wider text-stone-500 mb-2">Select Recipe to Scale</label>
          <select id="scaler-recipe-select" onchange="onScalerRecipeChange()" class="w-full p-3 bg-stone-50 border border-stone-200 rounded-xl text-sm font-medium focus:ring-2 focus:ring-brand-500 focus:outline-none">
            <!-- options populated via JS -->
          </select>
        </div>

        <!-- Scaling Controls Box -->
        <div class="bg-stone-50 rounded-xl p-5 border border-stone-200 flex flex-wrap items-center justify-between gap-4">
          <div>
            <span class="text-xs font-bold uppercase tracking-wider text-stone-500 block mb-1">Desired Servings</span>
            <div class="flex items-center gap-3">
              <button onclick="adjustScalerServings(-1)" class="w-10 h-10 rounded-xl bg-white border border-stone-200 text-stone-700 font-bold hover:bg-stone-100 flex items-center justify-center transition shadow-xs text-lg">−</button>
              <input id="scaler-servings-input" type="number" min="1" max="50" value="4" onchange="onScalerInputDirect(this.value)" class="w-16 text-center py-2 bg-white border border-stone-200 rounded-xl font-bold text-stone-900 text-lg shadow-xs focus:ring-2 focus:ring-brand-500 focus:outline-none">
              <button onclick="adjustScalerServings(1)" class="w-10 h-10 rounded-xl bg-white border border-stone-200 text-stone-700 font-bold hover:bg-stone-100 flex items-center justify-center transition shadow-xs text-lg">+</button>
              <span id="scaler-ratio-badge" class="text-xs font-bold text-brand-600 bg-brand-50 border border-brand-200 px-2.5 py-1 rounded-full">
                1.0x Scale
              </span>
            </div>
          </div>

          <div class="flex items-center gap-4 text-xs">
            <div class="text-right">
              <span class="text-stone-500 block">Base Servings</span>
              <span id="scaler-base-servings-display" class="font-bold text-stone-800 text-sm">4</span>
            </div>
            <div class="w-px h-8 bg-stone-200"></div>
            <div class="text-right">
              <span class="text-stone-500 block">Calories / Serving</span>
              <span id="scaler-calories-display" class="font-bold text-stone-800 text-sm">380 kcal</span>
            </div>
          </div>
        </div>

        <!-- Scaled Ingredients List -->
        <div>
          <div class="flex items-center justify-between mb-3">
            <h3 class="font-bold text-stone-900 text-base">Scaled Ingredients</h3>
            <button onclick="copyScaledIngredients()" class="text-xs font-semibold text-brand-600 hover:text-brand-700 hover:underline flex items-center gap-1">
              📋 Copy Ingredients
            </button>
          </div>
          <div id="scaler-ingredients-list" class="divide-y divide-stone-100 border border-stone-200 rounded-xl bg-white overflow-hidden">
            <!-- populated by JS -->
          </div>
        </div>

      </div>
    </div>

    <!-- VIEW 3: CHEF AI CHATBOT -->
    <div id="view-chef" class="hidden max-w-3xl mx-auto space-y-4">
      <div class="bg-white rounded-2xl border border-stone-200 shadow-xs flex flex-col h-[700px] overflow-hidden">
        
        <!-- Chat Header -->
        <div class="p-4 border-b border-stone-200 bg-stone-50 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 rounded-full bg-brand-500 text-white flex items-center justify-center text-xl shadow-xs">
              👨‍🍳
            </div>
            <div>
              <h2 class="font-bold text-stone-900 text-sm sm:text-base">Chef AI Culinary Assistant</h2>
              <p class="text-xs text-stone-500">Expert on all 200 catalog recipes, scaling tips, and ingredient swaps</p>
            </div>
          </div>
          <span class="text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200 px-2.5 py-1 rounded-full flex items-center gap-1">
            <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span> Online
          </span>
        </div>

        <!-- Active Context Banner (if asking about a specific recipe) -->
        <div id="chef-context-banner" class="hidden px-4 py-2 bg-brand-50 border-b border-brand-100 text-xs text-brand-800 flex items-center justify-between">
          <span>Active Context: <strong id="chef-context-title">Paneer Butter Masala</strong></span>
          <button onclick="clearChefContext()" class="font-bold hover:underline">Clear</button>
        </div>

        <!-- Chat Message Log -->
        <div id="chat-messages" class="flex-1 overflow-y-auto p-4 space-y-4 bg-[#fcfbfa]">
          <div class="flex items-start gap-3">
            <div class="w-8 h-8 rounded-full bg-brand-500 text-white flex items-center justify-center text-sm shrink-0">
              👨‍🍳
            </div>
            <div class="bg-white border border-stone-200 rounded-2xl rounded-tl-none p-3.5 max-w-[85%] text-xs sm:text-sm text-stone-800 shadow-2xs leading-relaxed">
              Hello! I'm your dedicated <strong>Chef AI</strong>. Ask me anything about preparing or modifying any of our 200 recipes, safe portion scaling, wine pairings, or dietary substitutions.
            </div>
          </div>
        </div>

        <!-- Prompt Suggestions -->
        <div class="px-4 py-2 bg-white border-t border-stone-100 flex items-center gap-2 overflow-x-auto text-xs">
          <span class="text-stone-400 shrink-0 font-medium">Try:</span>
          <button onclick="sendQuickPrompt('How do I scale spices when doubling a recipe?')" class="whitespace-nowrap bg-stone-100 hover:bg-stone-200 px-2.5 py-1 rounded-full text-stone-700 transition">
            Scaling spices advice
          </button>
          <button onclick="sendQuickPrompt('What are good dairy-free substitutes for heavy cream?')" class="whitespace-nowrap bg-stone-100 hover:bg-stone-200 px-2.5 py-1 rounded-full text-stone-700 transition">
            Dairy-free cream swaps
          </button>
          <button onclick="sendQuickPrompt('Suggest a wine pairing for Paneer Butter Masala')" class="whitespace-nowrap bg-stone-100 hover:bg-stone-200 px-2.5 py-1 rounded-full text-stone-700 transition">
            Wine pairings
          </button>
        </div>

        <!-- Input Box -->
        <form onsubmit="handleChatSubmit(event)" class="p-3 bg-white border-t border-stone-200 flex items-center gap-2">
          <input id="chat-input" type="text" placeholder="Ask Chef AI for culinary advice, swaps, or cooking steps..." class="flex-1 px-4 py-2.5 bg-stone-50 border border-stone-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-brand-500 focus:border-brand-500 transition">
          <button type="submit" id="chat-send-btn" class="bg-brand-500 hover:bg-brand-600 text-white font-bold px-4 py-2.5 rounded-xl text-sm transition flex items-center gap-1 shadow-xs">
            Send
          </button>
        </form>

      </div>
    </div>

  </main>

  <!-- Recipe Detail Modal -->
  <div id="recipe-modal" class="fixed inset-0 z-50 bg-stone-900/60 backdrop-blur-xs hidden items-center justify-center p-4">
    <div class="bg-white rounded-2xl max-w-2xl w-full max-h-[90vh] flex flex-col shadow-2xl border border-stone-200 overflow-hidden">
      <!-- Modal Header -->
      <div class="p-5 border-b border-stone-200 flex items-start justify-between bg-stone-50">
        <div>
          <div class="flex items-center gap-2 mb-1.5">
            <span id="modal-veg-badge" class="px-2 py-0.5 rounded text-[11px] font-bold"></span>
            <span id="modal-cuisine" class="text-xs text-stone-500 font-semibold uppercase tracking-wider"></span>
            <span class="text-stone-300">•</span>
            <span id="modal-category" class="text-xs text-stone-500 font-medium"></span>
          </div>
          <h3 id="modal-title" class="serif-title text-2xl font-bold text-stone-900"></h3>
        </div>
        <button onclick="closeRecipeModal()" class="w-8 h-8 rounded-full bg-stone-200 hover:bg-stone-300 text-stone-700 font-bold flex items-center justify-center transition">✕</button>
      </div>

      <!-- Modal Body -->
      <div class="p-5 overflow-y-auto space-y-6 flex-1 text-sm text-stone-700">
        <p id="modal-desc" class="text-stone-600 leading-relaxed"></p>

        <!-- Quick Metrics Bar -->
        <div class="grid grid-cols-4 gap-2 bg-stone-50 p-3 rounded-xl border border-stone-200 text-center text-xs">
          <div>
            <span class="text-stone-400 block">Prep</span>
            <strong id="modal-prep" class="text-stone-900"></strong>
          </div>
          <div>
            <span class="text-stone-400 block">Cook</span>
            <strong id="modal-cook" class="text-stone-900"></strong>
          </div>
          <div>
            <span class="text-stone-400 block">Difficulty</span>
            <strong id="modal-difficulty" class="text-stone-900"></strong>
          </div>
          <div>
            <span class="text-stone-400 block">Calories</span>
            <strong id="modal-calories" class="text-stone-900"></strong>
          </div>
        </div>

        <!-- Serving Scaler in Modal -->
        <div class="bg-brand-50/70 border border-brand-200 rounded-xl p-4 flex items-center justify-between">
          <div>
            <span class="text-xs font-bold uppercase tracking-wider text-brand-900 block mb-0.5">Scale This Recipe</span>
            <p class="text-xs text-brand-700">Ingredient quantities auto-adjust</p>
          </div>
          <div class="flex items-center gap-2">
            <button onclick="adjustModalServings(-1)" class="w-8 h-8 rounded-lg bg-white border border-brand-200 font-bold text-brand-900 hover:bg-brand-100 flex items-center justify-center">−</button>
            <input id="modal-servings-input" type="number" min="1" max="50" class="w-14 text-center py-1 bg-white border border-brand-200 rounded-lg font-bold text-brand-900 text-sm" onchange="onModalServingsChange(this.value)">
            <button onclick="adjustModalServings(1)" class="w-8 h-8 rounded-lg bg-white border border-brand-200 font-bold text-brand-900 hover:bg-brand-100 flex items-center justify-center">+</button>
          </div>
        </div>

        <!-- Dietary Profiles -->
        <div>
          <h4 class="font-bold text-xs uppercase tracking-wider text-stone-400 mb-2">Dietary Badges & Certifications</h4>
          <div id="modal-dietary-tags" class="flex flex-wrap gap-1.5"></div>
        </div>

        <!-- Ingredients List -->
        <div>
          <h4 class="font-bold text-stone-900 mb-2.5">Ingredients (<span id="modal-servings-label"></span>)</h4>
          <ul id="modal-ingredients" class="space-y-1.5 bg-stone-50 p-3.5 rounded-xl border border-stone-200 text-xs sm:text-sm"></ul>
        </div>

        <!-- Step by Step Instructions -->
        <div>
          <h4 class="font-bold text-stone-900 mb-2.5">Instructions</h4>
          <ol id="modal-instructions" class="space-y-2 list-decimal list-inside text-xs sm:text-sm text-stone-700 leading-relaxed"></ol>
        </div>

        <!-- Chef Tip -->
        <div class="bg-amber-50 border border-amber-200 rounded-xl p-3.5 text-xs text-amber-900 flex items-start gap-2.5">
          <span class="text-base">💡</span>
          <div>
            <strong class="font-bold block mb-0.5">Chef's Secret Technique:</strong>
            <span id="modal-tip"></span>
          </div>
        </div>
      </div>

      <!-- Modal Footer -->
      <div class="p-4 border-t border-stone-200 bg-stone-50 flex items-center justify-between">
        <button onclick="askChefAiAboutModalRecipe()" class="bg-brand-500 hover:bg-brand-600 text-white text-xs font-bold px-4 py-2.5 rounded-xl transition flex items-center gap-1.5 shadow-xs">
          👨‍🍳 Ask Chef AI About This Recipe
        </button>
        <button onclick="closeRecipeModal()" class="text-xs font-semibold text-stone-600 hover:text-stone-900 px-3 py-2">
          Close
        </button>
      </div>
    </div>
  </div>

  <script>
    let allRecipes = [];
    let activeDiet = 'all'; // 'all', 'veg', 'nonveg'
    let selectedDietary = new Set();
    let currentModalRecipe = null;
    let currentModalServings = 4;
    let scalerRecipe = null;
    let scalerServings = 4;
    let activeChefContextRecipe = null;

    // Load initial recipes
    async function init() {
      try {
        const res = await fetch('/api/recipes');
        allRecipes = await res.json();
        document.getElementById('total-count-badge').textContent = allRecipes.length;
        populateScalerDropdown();
        renderRecipes();
      } catch (e) {
        console.error('Failed to load recipes', e);
      }
    }

    // Tabs
    function switchTab(tab) {
      ['recipes', 'scaler', 'chef'].forEach(t => {
        document.getElementById('view-' + t).classList.add('hidden');
        document.getElementById('tab-btn-' + t).classList.remove('bg-white', 'text-stone-900', 'shadow-xs', 'font-semibold');
        document.getElementById('tab-btn-' + t).classList.add('text-stone-600');
      });

      document.getElementById('view-' + tab).classList.remove('hidden');
      document.getElementById('tab-btn-' + tab).classList.add('bg-white', 'text-stone-900', 'shadow-xs', 'font-semibold');
      document.getElementById('tab-btn-' + tab).classList.remove('text-stone-600');

      if (tab === 'scaler' && allRecipes.length > 0 && !scalerRecipe) {
        scalerRecipe = allRecipes[0];
        scalerServings = scalerRecipe.baseServings;
        updateScalerUI();
      }
    }

    // Filter Logic
    function setDietFilter(type) {
      activeDiet = type;
      ['all', 'veg', 'nonveg'].forEach(t => {
        const btn = document.getElementById('diet-btn-' + t);
        if (t === type) {
          btn.classList.add('bg-white', 'text-stone-900', 'shadow-xs');
          btn.classList.remove('text-stone-600');
        } else {
          btn.classList.remove('bg-white', 'text-stone-900', 'shadow-xs');
          btn.classList.add('text-stone-600');
        }
      });
      renderRecipes();
    }

    function toggleDietary(restriction) {
      const chip = document.getElementById('chip-' + restriction);
      if (selectedDietary.has(restriction)) {
        selectedDietary.delete(restriction);
        chip.classList.remove('bg-brand-500', 'text-white', 'border-brand-600');
        chip.classList.add('bg-stone-50', 'text-stone-700', 'border-stone-200');
      } else {
        selectedDietary.add(restriction);
        chip.classList.add('bg-brand-500', 'text-white', 'border-brand-600');
        chip.classList.remove('bg-stone-50', 'text-stone-700', 'border-stone-200');
      }

      const hasActive = selectedDietary.size > 0;
      document.getElementById('dietary-matches-all-badge').classList.toggle('hidden', !hasActive);
      document.getElementById('clear-dietary-btn').classList.toggle('hidden', !hasActive);
      if (hasActive) {
        document.getElementById('dietary-matches-all-badge').textContent = 'Matches All ' + selectedDietary.size + ' Selected';
      }

      renderRecipes();
    }

    function clearDietaryFilters() {
      selectedDietary.clear();
      document.querySelectorAll('.dietary-chip').forEach(chip => {
        chip.classList.remove('bg-brand-500', 'text-white', 'border-brand-600');
        chip.classList.add('bg-stone-50', 'text-stone-700', 'border-stone-200');
      });
      document.getElementById('dietary-matches-all-badge').classList.add('hidden');
      document.getElementById('clear-dietary-btn').classList.add('hidden');
      renderRecipes();
    }

    function resetAllFilters() {
      document.getElementById('search-input').value = '';
      clearDietaryFilters();
      setDietFilter('all');
    }

    let searchTimeout;
    function debounceSearch() {
      clearTimeout(searchTimeout);
      searchTimeout = setTimeout(renderRecipes, 200);
    }

    // Render Recipes
    function renderRecipes() {
      const search = document.getElementById('search-input').value.toLowerCase().trim();
      const grid = document.getElementById('recipe-grid');
      const empty = document.getElementById('empty-state');

      let filtered = allRecipes.filter(r => {
        // Veg / Non-Veg
        if (activeDiet === 'veg' && !r.isVeg) return false;
        if (activeDiet === 'nonveg' && r.isVeg) return false;

        // Dietary Restrictions (matches ALL selected)
        if (selectedDietary.size > 0) {
          const restrictions = r.dietaryRestrictions || [];
          for (let req of selectedDietary) {
            if (!restrictions.includes(req)) return false;
          }
        }

        // Search text
        if (search) {
          const matchTitle = r.title.toLowerCase().includes(search);
          const matchDesc = r.description.toLowerCase().includes(search);
          const matchCuisine = r.cuisine.toLowerCase().includes(search);
          const matchIng = r.ingredients && r.ingredients.some(i => i.name.toLowerCase().includes(search));
          const matchTag = r.tags && r.tags.some(t => t.toLowerCase().includes(search));
          if (!matchTitle && !matchDesc && !matchCuisine && !matchIng && !matchTag) return false;
        }

        return true;
      });

      document.getElementById('results-count').textContent = filtered.length;

      if (filtered.length === 0) {
        grid.innerHTML = '';
        empty.classList.remove('hidden');
        return;
      }

      empty.classList.add('hidden');
      grid.innerHTML = filtered.map(r => {
        const isVeg = r.isVeg;
        const badgeColor = isVeg ? 'text-emerald-700 bg-emerald-50 border-emerald-200' : 'text-red-700 bg-red-50 border-red-200';
        const badgeDot = isVeg ? 'bg-emerald-600' : 'bg-red-600';
        const dietChips = (r.dietaryRestrictions || []).slice(0, 3).map(d => {
          let label = d.replace('_', ' ').toLowerCase();
          if (d === 'GLUTEN_FREE') label = 'gluten-free';
          return '<span class="px-2 py-0.5 rounded text-[10px] font-medium bg-stone-100 text-stone-600 border border-stone-200 capitalize">' + label + '</span>';
        }).join('');

        return \`
          <div class="bg-white rounded-2xl border border-stone-200 hover:border-stone-300 hover:shadow-md transition flex flex-col justify-between overflow-hidden group">
            <div class="p-4 sm:p-5">
              <div class="flex items-center justify-between gap-2 mb-2.5">
                <span class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold border \${badgeColor}">
                  <span class="w-2 h-2 rounded-full \${badgeDot}"></span>
                  \${isVeg ? 'Vegetarian' : 'Non-Veg'}
                </span>
                <span class="text-xs text-stone-400 font-medium">\${r.cuisine}</span>
              </div>
              <h4 class="font-bold text-stone-900 text-base mb-1.5 group-hover:text-brand-600 transition leading-snug cursor-pointer" onclick="openRecipeModal('\${r.id}')">\${r.title}</h4>
              <p class="text-xs text-stone-500 line-clamp-2 leading-relaxed mb-3">\${r.description}</p>
              
              <div class="flex flex-wrap gap-1 mb-3">
                \${dietChips}
              </div>

              <div class="grid grid-cols-3 gap-2 pt-3 border-t border-stone-100 text-[11px] text-stone-500">
                <div>
                  <span class="block text-stone-400">Time</span>
                  <strong class="text-stone-800">\${r.prepTimeMin + r.cookTimeMin}m</strong>
                </div>
                <div>
                  <span class="block text-stone-400">Servings</span>
                  <strong class="text-stone-800">\${r.baseServings}</strong>
                </div>
                <div>
                  <span class="block text-stone-400">Calories</span>
                  <strong class="text-stone-800">\${r.caloriesPerServing}</strong>
                </div>
              </div>
            </div>

            <div class="px-4 py-3 bg-stone-50 border-t border-stone-100 flex items-center justify-between gap-2">
              <button onclick="openRecipeScaler('\${r.id}')" class="text-xs font-semibold text-brand-600 hover:text-brand-700 hover:underline flex items-center gap-1">
                ⚖️ Scale
              </button>
              <button onclick="openRecipeModal('\${r.id}')" class="bg-stone-900 hover:bg-stone-800 text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition shadow-2xs">
                View Details
              </button>
            </div>
          </div>
        \`;
      }).join('');
    }

    // Modal
    function openRecipeModal(recipeId) {
      const r = allRecipes.find(x => x.id === recipeId);
      if (!r) return;
      currentModalRecipe = r;
      currentModalServings = r.baseServings;

      document.getElementById('modal-title').textContent = r.title;
      document.getElementById('modal-desc').textContent = r.description;
      document.getElementById('modal-cuisine').textContent = r.cuisine;
      document.getElementById('modal-category').textContent = r.category;
      document.getElementById('modal-prep').textContent = r.prepTimeMin + ' min';
      document.getElementById('modal-cook').textContent = r.cookTimeMin + ' min';
      document.getElementById('modal-difficulty').textContent = r.difficulty;
      document.getElementById('modal-calories').textContent = r.caloriesPerServing + ' kcal';
      document.getElementById('modal-tip').textContent = r.chefTip || 'Taste and adjust salt and acidity right before taking off heat.';

      const vegBadge = document.getElementById('modal-veg-badge');
      if (r.isVeg) {
        vegBadge.textContent = 'VEGETARIAN';
        vegBadge.className = 'px-2 py-0.5 rounded text-[11px] font-bold text-emerald-700 bg-emerald-50 border border-emerald-200';
      } else {
        vegBadge.textContent = 'NON-VEGETARIAN';
        vegBadge.className = 'px-2 py-0.5 rounded text-[11px] font-bold text-red-700 bg-red-50 border border-red-200';
      }

      // Dietary tags
      const tagsContainer = document.getElementById('modal-dietary-tags');
      const tags = (r.dietaryRestrictions || []).map(d => {
        let label = d.replace('_', ' ').toLowerCase();
        return '<span class="px-2.5 py-1 rounded-full text-xs font-semibold bg-brand-50 text-brand-800 border border-brand-200 capitalize">✓ ' + label + '</span>';
      });
      tagsContainer.innerHTML = tags.length > 0 ? tags.join('') : '<span class="text-xs text-stone-400">Standard culinary profile</span>';

      updateModalIngredients();

      const modal = document.getElementById('recipe-modal');
      modal.classList.remove('hidden');
      modal.classList.add('flex');
    }

    function closeRecipeModal() {
      const modal = document.getElementById('recipe-modal');
      modal.classList.add('hidden');
      modal.classList.remove('flex');
    }

    function adjustModalServings(delta) {
      const newServings = Math.max(1, Math.min(50, currentModalServings + delta));
      if (newServings !== currentModalServings) {
        currentModalServings = newServings;
        updateModalIngredients();
      }
    }

    function onModalServingsChange(val) {
      const num = parseInt(val, 10);
      if (!isNaN(num) && num >= 1 && num <= 50) {
        currentModalServings = num;
        updateModalIngredients();
      }
    }

    function updateModalIngredients() {
      if (!currentModalRecipe) return;
      document.getElementById('modal-servings-input').value = currentModalServings;
      document.getElementById('modal-servings-label').textContent = currentModalServings + ' servings';

      const factor = currentModalServings / currentModalRecipe.baseServings;
      const list = document.getElementById('modal-ingredients');

      list.innerHTML = (currentModalRecipe.ingredients || []).map(i => {
        const scaledQty = i.quantity * factor;
        const formattedQty = formatQuantity(scaledQty);
        return \`
          <li class="flex items-center justify-between py-1 border-b border-stone-100 last:border-0">
            <span class="font-medium text-stone-800">\${i.name} \${i.notes ? '<em class="text-stone-400 font-normal">(' + i.notes + ')</em>' : ''}</span>
            <span class="font-bold text-brand-600 shrink-0 ml-2">\${formattedQty} \${i.unit || ''}</span>
          </li>
        \`;
      }).join('');

      // Instructions
      const instList = document.getElementById('modal-instructions');
      instList.innerHTML = (currentModalRecipe.instructions || []).map(step => {
        return '<li class="pl-1 leading-relaxed">' + step + '</li>';
      }).join('');
    }

    function askChefAiAboutModalRecipe() {
      if (!currentModalRecipe) return;
      activeChefContextRecipe = currentModalRecipe;
      document.getElementById('chef-context-banner').classList.remove('hidden');
      document.getElementById('chef-context-title').textContent = currentModalRecipe.title;
      closeRecipeModal();
      switchTab('chef');
      sendChatMessage("Tell me chef secrets and preparation advice for " + currentModalRecipe.title);
    }

    // Serving Scaler Tool
    function populateScalerDropdown() {
      const select = document.getElementById('scaler-recipe-select');
      select.innerHTML = allRecipes.map(r => {
        return '<option value="' + r.id + '">' + (r.isVeg ? '🟢 [Veg] ' : '🔴 [Non-Veg] ') + r.title + ' (' + r.cuisine + ')</option>';
      }).join('');
    }

    function openRecipeScaler(recipeId) {
      const r = allRecipes.find(x => x.id === recipeId);
      if (!r) return;
      scalerRecipe = r;
      scalerServings = r.baseServings;
      document.getElementById('scaler-recipe-select').value = r.id;
      switchTab('scaler');
      updateScalerUI();
    }

    function onScalerRecipeChange() {
      const id = document.getElementById('scaler-recipe-select').value;
      scalerRecipe = allRecipes.find(r => r.id === id);
      if (scalerRecipe) {
        scalerServings = scalerRecipe.baseServings;
        updateScalerUI();
      }
    }

    function adjustScalerServings(delta) {
      const next = Math.max(1, Math.min(50, scalerServings + delta));
      if (next !== scalerServings) {
        scalerServings = next;
        updateScalerUI();
      }
    }

    function onScalerInputDirect(val) {
      const num = parseInt(val, 10);
      if (!isNaN(num) && num >= 1 && num <= 50) {
        scalerServings = num;
        updateScalerUI();
      }
    }

    function updateScalerUI() {
      if (!scalerRecipe) return;
      document.getElementById('scaler-servings-input').value = scalerServings;
      document.getElementById('scaler-base-servings-display').textContent = scalerRecipe.baseServings;
      document.getElementById('scaler-calories-display').textContent = scalerRecipe.caloriesPerServing + ' kcal';

      const ratio = (scalerServings / scalerRecipe.baseServings).toFixed(2);
      document.getElementById('scaler-ratio-badge').textContent = ratio + 'x Scale Factor';

      const list = document.getElementById('scaler-ingredients-list');
      const factor = scalerServings / scalerRecipe.baseServings;

      list.innerHTML = (scalerRecipe.ingredients || []).map(i => {
        const scaled = i.quantity * factor;
        return \`
          <div class="p-3 sm:p-3.5 flex items-center justify-between hover:bg-stone-50 transition">
            <div>
              <span class="font-semibold text-stone-800 text-sm">\${i.name}</span>
              \${i.notes ? '<span class="text-xs text-stone-400 ml-1">(' + i.notes + ')</span>' : ''}
            </div>
            <div class="text-right shrink-0">
              <span class="text-sm font-bold text-brand-600">\${formatQuantity(scaled)}</span>
              <span class="text-xs text-stone-500 font-medium ml-1">\${i.unit || ''}</span>
            </div>
          </div>
        \`;
      }).join('');
    }

    function copyScaledIngredients() {
      if (!scalerRecipe) return;
      const factor = scalerServings / scalerRecipe.baseServings;
      const text = scalerRecipe.title + " (Scaled for " + scalerServings + " servings):\n\n" +
        scalerRecipe.ingredients.map(i => {
          return "- " + formatQuantity(i.quantity * factor) + " " + (i.unit || '') + " " + i.name + (i.notes ? " (" + i.notes + ")" : "");
        }).join("\n");

      navigator.clipboard.writeText(text).then(() => {
        alert("Scaled ingredients copied to clipboard!");
      }).catch(err => {
        console.error("Clipboard error:", err);
      });
    }

    // Helper: fractional quantity formatting (e.g. 1 1/2, 1/4)
    function formatQuantity(val) {
      if (val === 0) return '0';
      const whole = Math.floor(val);
      const frac = val - whole;

      let fracStr = '';
      if (Math.abs(frac - 0.25) < 0.05) fracStr = '¼';
      else if (Math.abs(frac - 0.333) < 0.05) fracStr = '⅓';
      else if (Math.abs(frac - 0.5) < 0.05) fracStr = '½';
      else if (Math.abs(frac - 0.666) < 0.05) fracStr = '⅔';
      else if (Math.abs(frac - 0.75) < 0.05) fracStr = '¾';
      else if (frac > 0.05) fracStr = (Math.round(frac * 10) / 10).toString().replace(/^0/, '');

      if (whole > 0 && fracStr) return whole + ' ' + fracStr;
      if (whole > 0) return whole.toString();
      return fracStr || '0';
    }

    // Chef AI Chat
    function clearChefContext() {
      activeChefContextRecipe = null;
      document.getElementById('chef-context-banner').classList.add('hidden');
    }

    function sendQuickPrompt(text) {
      document.getElementById('chat-input').value = text;
      handleChatSubmit(new Event('submit'));
    }

    function handleChatSubmit(e) {
      if (e && e.preventDefault) e.preventDefault();
      const input = document.getElementById('chat-input');
      const text = input.value.trim();
      if (!text) return;
      input.value = '';
      sendChatMessage(text);
    }

    async function sendChatMessage(text) {
      const chatLog = document.getElementById('chat-messages');

      // Append user bubble
      const userBubble = document.createElement('div');
      userBubble.className = 'flex items-start justify-end gap-3';
      userBubble.innerHTML = \`
        <div class="bg-brand-500 text-white rounded-2xl rounded-tr-none p-3.5 max-w-[85%] text-xs sm:text-sm shadow-xs leading-relaxed">
          \${text}
        </div>
      \`;
      chatLog.appendChild(userBubble);
      chatLog.scrollTop = chatLog.scrollHeight;

      // Loading bubble
      const chefBubble = document.createElement('div');
      chefBubble.className = 'flex items-start gap-3';
      chefBubble.innerHTML = \`
        <div class="w-8 h-8 rounded-full bg-brand-500 text-white flex items-center justify-center text-sm shrink-0">
          👨‍🍳
        </div>
        <div class="bg-white border border-stone-200 rounded-2xl rounded-tl-none p-3.5 max-w-[85%] text-xs sm:text-sm text-stone-800 shadow-2xs leading-relaxed animate-pulse">
          Chef AI is thinking...
        </div>
      \`;
      chatLog.appendChild(chefBubble);
      chatLog.scrollTop = chatLog.scrollHeight;

      try {
        const res = await fetch('/api/chat', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            message: text,
            recipeContext: activeChefContextRecipe
          })
        });

        const data = await res.json();
        chefBubble.querySelector('.animate-pulse').classList.remove('animate-pulse');
        chefBubble.querySelector('div:last-child').innerHTML = formatChefMarkdown(data.reply || 'Happy cooking!');
      } catch (err) {
        chefBubble.querySelector('.animate-pulse').classList.remove('animate-pulse');
        chefBubble.querySelector('div:last-child').textContent = 'Apologies, my kitchen timer ran out! Please try again.';
      }
      chatLog.scrollTop = chatLog.scrollHeight;
    }

    function formatChefMarkdown(md) {
      return md
        .replace(/\\*\\*(.*?)\\*\\*/g, '<strong>$1</strong>')
        .replace(/\\*(.*?)\\*/g, '<em>$1</em>')
        .replace(/\\n/g, '<br>');
    }

    // Startup
    init();
  </script>
</body>
</html>`;
}

server.listen(PORT, HOST, () => {
  console.log(`[Recipe App] Dev server listening on http://${HOST}:${PORT}`);
});
