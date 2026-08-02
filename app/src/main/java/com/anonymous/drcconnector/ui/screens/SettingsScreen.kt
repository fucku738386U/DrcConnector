package com.anonymous.drcconnector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFFFF3333),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Theme section
        SettingsSection(title = "Appearance") {
            SettingsRow(
                icon = Icons.Filled.DarkMode,
                title = "Dark theme",
                subtitle = "Always on — OLED optimized",
                trailing = {
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        enabled = false,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFFFF3333),
                            checkedTrackColor = Color(0x33FF3333)
                        )
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About section
        SettingsSection(title = "About") {
            SettingsRow(
                icon = Icons.Filled.Info,
                title = "Version",
                subtitle = "1.0.0 (Anonymous Build)",
                trailing = {}
            )
            SettingsRow(
                icon = Icons.Filled.Info,
                title = "Built by",
                subtitle = "anonymous | Powered by Clipher 🔥💀👑",
                trailing = {}
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "💀 DrcConnector 💀\nRemote Desktop + SSH Terminal\nDark UI | Smooth | Android 14",
                color = Color(0xFF707070),
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            color = Color(0xFF707070),
            fontSize = 12.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0A0A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222222))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFFFF3333), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = Color.White, fontSize = 14.sp)
                Text(text = subtitle, color = Color(0xFF707070), fontSize = 12.sp)
            }
        }
        trailing()
    }
}
