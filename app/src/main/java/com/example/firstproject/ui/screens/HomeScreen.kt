import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Using your Ivonne Orchard Branding
val DeepMaroon = Color(0xFF1A0A0A)
val LuxuryGold = Color(0xFFD4AF37)
val CardBackground = Color(0xFF2A1A1A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IvonneOrchardHome() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("IVONNE ORCHARD", 
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = LuxuryGold, 
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepMaroon)
            )
        },
        containerColor = DeepMaroon
    ) { padding ->
        // CRITICAL: This verticalScroll is what allows you to see content below the carousel
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) 
        ) {
            // --- 1. THE CAROUSEL (Reduced size so it doesn't hide everything) ---
            Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Color.Black)) {
                // This represents your sliding images
                Text(
                    "Featured Styles", 
                    modifier = Modifier.align(Alignment.Center),
                    color = LuxuryGold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. SERVICES SECTION ---
            SectionLabel("Our Services")
            val services = listOf(
                AppService("Hair Design", Icons.Default.Face),
                AppService("Spa & Glow", Icons.Default.Favorite),
                AppService("Nail Care", Icons.Default.Star)
            )
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services) { service ->
                    ServiceBox(service)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 3. ABOUT US SECTION ---
            SectionLabel("About Us")
            Card(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.2f))
            ) {
                Text(
                    "Ivonne Orchard is a premier salon in Makadara, Nairobi. We specialize in high-end beauty transformations for the modern professional.",
                    modifier = Modifier.padding(20.dp),
                    color = Color.LightGray,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 4. CONTACT US SECTION ---
            SectionLabel("Contact Us")
            ContactItem(Icons.Default.LocationOn, "Makadara, Nairobi, Kenya")
            ContactItem(Icons.Default.Call, "+254 7XX XXX XXX")

            Spacer(modifier = Modifier.height(40.dp))
            
            // Booking Button
            Button(
                onClick = { /* M-Pesa STK Push logic */ },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("BOOK NOW", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = LuxuryGold,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun ServiceBox(service: AppService) {
    Box(
        modifier = Modifier
            .size(130.dp, 150.dp)
            .background(CardBackground, RoundedCornerShape(8.dp))
            .border(0.5.dp, LuxuryGold.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(service.icon, contentDescription = null, tint = LuxuryGold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(service.name, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun ContactItem(icon: ImageVector, label: String) {
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = Color.Gray, fontSize = 14.sp)
    }
}

data class AppService(val name: String, val icon: ImageVector)
