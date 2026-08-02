package com.anonymous.drcconnector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.anonymous.drcconnector.ui.screens.ConnectionsScreen
import com.anonymous.drcconnector.ui.screens.RemoteDesktopScreen
import com.anonymous.drcconnector.ui.screens.SettingsScreen
import com.anonymous.drcconnector.ui.screens.SshTerminalScreen
import com.anonymous.drcconnector.ui.theme.DrcConnectorTheme

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Ssh : Screen("ssh", "SSH", Icons.Filled.Terminal)
    object Rdp : Screen("rdp", "Desktop", Icons.Filled.Computer)
    object Connections : Screen("connections", "Saved", Icons.Filled.Storage)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

val screens = listOf(Screen.Ssh, Screen.Rdp, Screen.Connections, Screen.Settings)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrcConnectorTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { padding ->
                    NavigationGraph(
                        navController = navController,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = androidx.compose.ui.graphics.Color(0xFF0A0A0A),
        contentColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Ssh.route,
        modifier = modifier
    ) {
        composable(Screen.Ssh.route) { SshTerminalScreen() }
        composable(Screen.Rdp.route) { RemoteDesktopScreen() }
        composable(Screen.Connections.route) { ConnectionsScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
