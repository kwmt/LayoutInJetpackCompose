package net.kwmt27.layoutinjetpackcompose

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.kwmt27.layoutinjetpackcompose.ui.theme.LayoutInJetpackComposeTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutInJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LayoutInJetpackComposeTheme {
        Greeting("Android")
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = {})
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.preferredSize(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            // image goes here
        }
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }

}

sealed class Screen(val route: String, val resourceId: String) {
    object Profile : Screen("profile", "a")
    object FriendsList : Screen("friendslist", "b")
}

val items = listOf(
    Screen.Profile,
    Screen.FriendsList,
)

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("LayoutsCodeLab")
                },
                actions = {
                    IconButton(onClick = {/*TODO*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigation {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icons.Filled.Favorite },
                        label = { Text(screen.resourceId) },
                        selected = true,
//                        selected = currentRoute == screen.route,
                        onClick = {
//                            navController.navigate(screen.route) {
//                                // Pop up to the start destination of the graph to
//                                // avoid building up a large stack of destinations
//                                // on the back stack as users select items
//                                popUpTo = navController.graph.startDestination
//                                // Avoid multiple copies of the same destination when
//                                // reselecting the same item
//                                launchSingleTop = true
//                            }
                        },
                    )

                }
            }
        }
    ) {
        BodyContent()
    }


}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = Modifier.layout {measurable, constraints ->
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.toIntPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        //...
        placeable.placeRelative(0, placeableY)
    }
}

@Preview
@Composable
fun PhotographerCardPreview() {
    LayoutInJetpackComposeTheme {
        LayoutsCodelab()
    }
}

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    LayoutInJetpackComposeTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    LayoutInJetpackComposeTheme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}