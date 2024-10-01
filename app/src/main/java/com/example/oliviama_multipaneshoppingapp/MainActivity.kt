package com.example.oliviama_multipaneshoppingapp
// code taken and inspired from ContentPanes1 from lecture 9/24/24
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oliviama_multipaneshoppingapp.ui.theme.OliviaMaMultiPaneShoppingAppTheme
import androidx.compose.material3.Button
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OliviaMaMultiPaneShoppingAppTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())) { innerPadding ->
                    ShoppingApp(modifier =
                    Modifier
                        .displayCutoutPadding()
                        .padding(innerPadding))
                }
            }
        }
    }
}

data class WindowInfo(
    val isWideScreen: Boolean
)

@Composable
fun calculateCurrentWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    // Set a breakpoint for wide vs narrow screens (600dp is commonly used)
    val isWideScreen = screenWidth >= 600

    return WindowInfo(
        isWideScreen = isWideScreen
    )
}

@Composable
fun ProductCard(image: Int, product: String, selectedProduct: String?, onItemSelected: (String) -> Unit){
    // only one product card can be selected at a time
    val isSelected = product == selectedProduct
    val borderWidth = if (isSelected) 4.dp else 1.dp

    val cardHeight = if (calculateCurrentWindowInfo().isWideScreen) 50.dp else 150.dp

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(borderWidth, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(cardHeight)
            .clickable {
                onItemSelected(product)
            }
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product,
                modifier = Modifier
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold

            )
            if (!calculateCurrentWindowInfo().isWideScreen) {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ProductList(products: List<Pair<String, Int>>, onItemSelected: (String) -> Unit, modifier: Modifier = Modifier, selectedProduct: String?) {
    // simple product cards displayed in a column in the products list pane
    LazyColumn (
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // List Title
            Text(
                text = "Products",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Display product cards for each product in the list
        items(products.size) { index ->
            val (product, image) = products[index]
            ProductCard(
                image = image,
                product = product,
                selectedProduct = selectedProduct,
                onItemSelected = onItemSelected
            )
        }
    }
}


@Composable
fun ProductDetails(product: String?, products: List<Pair<String, Int>>, modifier: Modifier = Modifier, onBack: () -> Unit) {
    // List of products + descriptions
    val productDetails = listOf(
        Pair("Apple", "An apple is a round, edible fruit produced by an apple tree."),
        Pair("Banana", "A banana is an elongated, edible fruit – botanically a berry."),
        Pair("Orange", "The orange, also called sweet orange to distinguish it from the bitter orange (Citrus × aurantium), is the fruit of a tree in the family Rutaceae."),
        Pair("Pear", "Pears are fruits produced and consumed around the world, growing on a tree and harvested in late summer into mid-autumn.")
    )

    // List of products + prices
    val productPrices = listOf(
        Pair("Apple", "$1.50/lb"),
        Pair("Banana", "$0.50/lb"),
        Pair("Orange", "$1.30/lb"),
        Pair("Pear", "$1.70/lb")
    )

    // Task details pane used when the user selects a particular task
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (product != null) {
            if (!calculateCurrentWindowInfo().isWideScreen) {
                Image(
                    painter = painterResource(products.find { it.first == product }?.second ?: 0),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(20.dp)
                        // makes images same size on screen
                        .height(200.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            Text(
                text = "$product" + "s",
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = productPrices.find {it.first == product}?.second?: "",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = productDetails.find {it.first == product}?.second?: "No details available for the selected product",
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

        } else {
            // No task selected
            Text(
                text = "Select a product to view details.",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // if in portrait mode -> have a back button to go back to product list
        if (!calculateCurrentWindowInfo().isWideScreen) {
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Button(onClick = onBack) {  // Call the onBack function
                Text("Back to Product List")
            }
        }
    }
}


@Composable
fun ShoppingApp(modifier: Modifier = Modifier) {
    val windowInfo = calculateCurrentWindowInfo()
    val products = listOf(
        Pair("Apple", R.drawable.apple),
        Pair("Banana", R.drawable.banana),
        Pair("Orange", R.drawable.orange),
        Pair("Pear", R.drawable.pear)
    )
    // ensure that the state survives configuration changes
    var selectedProduct by rememberSaveable { mutableStateOf<String?>(null) }

    if (windowInfo.isWideScreen) {
        // Two-pane layout for wide screens, one for the task list
        // the other for the task details
        Row(modifier = Modifier.fillMaxSize()) {
            ProductList(products, onItemSelected = { selectedProduct = it }, modifier = Modifier.weight(1f), selectedProduct)
            Spacer(modifier = Modifier.width(16.dp))
            ProductDetails(
                product = selectedProduct,
                products = products,
                modifier = Modifier.weight(1f),
                onBack = { selectedProduct = null }
            )
        }
    } else {
        // Single-pane layout for narrow screens
        if (selectedProduct == null) {
            ProductList(products, onItemSelected = { selectedProduct = it }, modifier = Modifier.fillMaxSize(), selectedProduct)
        } else {
            ProductDetails(
                product = selectedProduct,
                products = products,
                modifier = Modifier.fillMaxSize(),
                onBack = { selectedProduct = null }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OliviaMaMultiPaneShoppingAppTheme {
        ShoppingApp()
    }
}