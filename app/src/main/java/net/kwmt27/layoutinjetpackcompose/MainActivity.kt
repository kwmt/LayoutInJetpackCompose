package net.kwmt27.layoutinjetpackcompose

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.kwmt27.layoutinjetpackcompose.ui.theme.LayoutInJetpackComposeTheme
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutInJetpackComposeTheme() {
                BodyContent()
            }
//            MyOwnColumn(modifier = Modifier.background(Color.Red)) {
//                Text("MyOwnColumn")
//                Text("places items")
//                Text("vertically.")
//                Text("We've done it by hand!")
//            }
//            LayoutInJetpackComposeTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("Android")
//                }
//            }
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
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        StaggeredGrid {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = Modifier.layout { measurable, constraints ->
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

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = children) { measurables, constraints ->
        // 子供Viewをさらに制約しないでください。
        // 測定された子のリスト
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints = constraints)
        }
        var yPosition = 0
        Log.d("MyOwnColumn", "maxWidth:${constraints.maxWidth}, maxHeight:${constraints.maxHeight}")
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            Log.d("MyOwnColumn", "yPosition:$yPosition")
            placeables.forEach { placeable ->
                // Position item on the screen
                Log.d("MyOwnColumn", "placeable.height:" + placeable.height.toString())
                placeable.placeRelative(x = 0, y = yPosition)
                // Record the y co-ord placed up to
                yPosition += placeable.height
                Log.d("MyOwnColumn", "yPosition:$yPosition")
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    children: @Composable() () -> Unit
) {
    Layout(
        modifier = modifier,
        content = children
    ) { measurables, constraints ->

        // 行の各アイテムの幅
        val rowWidths = IntArray(rows) { 0 }
        // 各行の最大の高さ
        val rowMaxHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            val row = index % rows
            rowWidths[row] = rowWidths[row] + placeable.width
            rowMaxHeights[row] = max(rowMaxHeights[row], placeable.height)
            placeable
        }

        // Int.coerceIn(範囲)はIntの値を範囲内に強制します。
        // Gridの幅が最大のRowの幅
        val width =
            rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth))
                ?: constraints.minWidth

        // 各業の高さを合計したものが、constraints.minHeightとconstraints.maxHeightの範囲内に収めたときの高さ
        val height = rowMaxHeights.sumBy { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        Log.d("StaggeredGrid", "$width, $height")

        // 各業の高さ
        // 前の行の高さから計算する
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowMaxHeights[i - 1]
        }
        layout(width, height) {
            val rowX = IntArray(rows) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .preferredSize(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.preferredWidth(4.dp))
            Text(text = text)
        }
    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Preview
@Composable
fun ChipPreview() {
    LayoutInJetpackComposeTheme() {

        BodyContent()
    }
}

@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {

        val (button1, button2, text) = createRefs()

        Button(onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }) {
            Text("Button")
        }


        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })

        val barrier = createEndBarrier(button1, text)
        Button(onClick = { /*TODO*/ },
        modifier = Modifier.constrainAs(button2) {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(barrier)
        }) {
            Text("Button2")
        }
    }
}

@Preview
@Composable
fun ConstraintLayoutContentPreview() {
    LayoutInJetpackComposeTheme {
        ConstraintLayoutContent()
    }
}