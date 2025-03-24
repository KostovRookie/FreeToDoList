package com.kostov.freetodolist.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String,
    onFabClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("main", Icons.Default.Star, "Tasks"),
        BottomNavItem("kitten", Icons.Default.Favorite, "Kitten"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    Box {
        NavigationBar(
            containerColor = Color(0xFFF2EBFE),
            tonalElevation = 4.dp,
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    },
                    label = null,
                    alwaysShowLabel = false
                )
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            containerColor = Color(0xFF6A3DE8),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
                .size(64.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}