package com.lay.construcalc

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lay.construcalc.ads.AdManager
import com.lay.construcalc.ads.ConsentManager
import com.lay.construcalc.model.CalculatorEngine
import com.lay.construcalc.model.CalculatorType
import kotlinx.coroutines.delay

private val Ink = Color(0xFF101418)
private val Surface = Color(0xFFF6F7F9)
private val Primary = Color(0xFFE8782E)
private val Muted = Color(0xFF6B737C)

class MainActivity : ComponentActivity() {
    private lateinit var adManager: AdManager
    private lateinit var consentManager: ConsentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adManager = AdManager(this)
        consentManager = ConsentManager(this)
        consentManager.requestConsent(this) { if (consentManager.canRequestAds()) adManager.initialize() }
        setContent { ConstruCalcTheme { ConstruCalcApp(adManager) } }
    }
}

@Composable private fun ConstruCalcTheme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = lightColorScheme(primary = Primary, onPrimary = Color.White, background = Surface, surface = Color.White, onBackground = Ink, onSurface = Ink), content = content
)

@Composable private fun ConstruCalcApp(adManager: AdManager) {
    var selected by remember { mutableStateOf<CalculatorType?>(null) }
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(containerColor = Surface, bottomBar = {
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(tab == 0, { tab = 0; selected = null }, { Icon(Icons.Default.Calculate, null) }, label = { Text("Calcular") })
            NavigationBarItem(tab == 1, { tab = 1; selected = null }, { Icon(Icons.Default.History, null) }, label = { Text("Histórico") })
            NavigationBarItem(tab == 2, { tab = 2; selected = null }, { Icon(Icons.Default.Settings, null) }, label = { Text("Definições") })
        }
    }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                selected != null -> CalculatorScreen(selected!!, { selected = null }, adManager)
                tab == 0 -> HomeScreen { selected = it }
                tab == 1 -> EmptyState(Icons.Default.History, "Histórico", "Os teus cálculos recentes aparecerão aqui.")
                else -> EmptyState(Icons.Default.Settings, "Definições", "Preferências e opções do ConstruCalc.")
            }
        }
    }
}

@Composable private fun HomeScreen(onSelect: (CalculatorType) -> Unit) {
    Column(Modifier.fillMaxSize().background(Surface).padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text("ConstruCalc", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold); Text("Cálculos de construção, sem complicação.", color = Muted, fontSize = 14.sp) }
            Surface(color = Ink, shape = RoundedCornerShape(16.dp), modifier = Modifier.size(48.dp)) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Calculate, null, tint = Primary) } }
        }
        Spacer(Modifier.height(22.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Ink), shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(22.dp)) {
                Text("Começa por aqui", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text("Calcula materiais e medidas em poucos segundos.", color = Color(0xFFB9C0C8))
                Spacer(Modifier.height(16.dp))
                Button(onClick = { onSelect(CalculatorType.BLOCKS) }, colors = ButtonDefaults.buttonColors(containerColor = Primary), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) { Text("Calcular blocos", fontWeight = FontWeight.Bold) }
            }
        }
        Spacer(Modifier.height(22.dp)); Text("Calculadoras", fontSize = 20.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) { items(CalculatorType.entries) { type -> CalculatorCard(type) { onSelect(type) } } }
    }
}

@Composable private fun CalculatorCard(type: CalculatorType, onClick: () -> Unit) {
    Card(onClick = onClick, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.height(145.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Surface(color = Primary.copy(alpha = .12f), shape = RoundedCornerShape(12.dp), modifier = Modifier.size(42.dp)) { Box(contentAlignment = Alignment.Center) { Text(type.icon, color = Primary, fontSize = 22.sp, fontWeight = FontWeight.Bold) } }
            Column { Text(type.title, fontWeight = FontWeight.Bold, fontSize = 17.sp); Text(type.subtitle, color = Muted, fontSize = 12.sp) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun CalculatorScreen(type: CalculatorType, onBack: () -> Unit, adManager: AdManager) {
    val activity = LocalContext.current as? Activity
    val scope = rememberCoroutineScope()
    var values by remember { mutableStateOf(List(4) { "" }) }
    var result by remember { mutableStateOf<String?>(null) }
    val labels = when (type) {
        CalculatorType.BLOCKS -> listOf("Comprimento da parede (m)", "Altura da parede (m)", "Comprimento do bloco (cm)", "Altura do bloco (cm)")
        CalculatorType.CONCRETE -> listOf("Comprimento (m)", "Largura (m)", "Altura (m)", "Margem (%)")
        CalculatorType.AREA -> listOf("Comprimento (m)", "Largura (m)", "Extra (%)", "")
        CalculatorType.VOLUME -> listOf("Comprimento (m)", "Largura (m)", "Altura (m)", "")
    }
    Column(Modifier.fillMaxSize().background(Surface)) {
        TopAppBar(title = { Text(type.title, fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface))
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(type.subtitle, color = Muted)
            labels.forEachIndexed { index, label -> if (label.isNotBlank()) OutlinedTextField(value = values[index], onValueChange = { input -> values = values.toMutableList().also { it[index] = input.filter { c -> c.isDigit() || c == '.' || c == ',' } } }, label = { Text(label) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) }
            Button(onClick = {
                val numbers = values.map { it.replace(',', '.').toDoubleOrNull() ?: 0.0 }
                result = CalculatorEngine.calculate(type, numbers)
                adManager.recordCalculation()
                activity?.let { currentActivity -> scope.launch { delay(350); adManager.maybeShow(currentActivity) {} } }
            }, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(54.dp)) { Text("Calcular", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            result?.let { Card(colors = CardDefaults.cardColors(containerColor = Ink), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp)) { Text("Resultado", color = Color(0xFFB9C0C8), fontSize = 13.sp); Spacer(Modifier.height(5.dp)); Text(it, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold) } } }
        }
    }
}

@Composable private fun EmptyState(icon: ImageVector, title: String, text: String) {
    Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Surface(color = Primary.copy(alpha = .12f), shape = RoundedCornerShape(22.dp), modifier = Modifier.size(76.dp)) { Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = Primary, modifier = Modifier.size(34.dp)) } }
        Spacer(Modifier.height(18.dp)); Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(6.dp)); Text(text, color = Muted)
    }
}
