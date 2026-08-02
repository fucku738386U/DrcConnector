package com.anonymous.drcconnector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RemoteDesktopScreen() {
    var host by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("5900") }
    var password by remember { mutableStateOf("") }
    var isConnecting by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Idle") }
    var isConnected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Remote Desktop",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFFFF3333),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "VNC Connection",
            color = Color(0xFF707070),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = host,
            onValueChange = { host = it },
            label = { Text("VNC Host", color = Color(0xFF707070)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF3333),
                unfocusedBorderColor = Color(0xFF222222),
                focusedContainerColor = Color(0xFF0A0A0A),
                unfocusedContainerColor = Color(0xFF0A0A0A)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = port,
                onValueChange = { port = it.filter { c -> c.isDigit() } },
                label = { Text("Port", color = Color(0xFF707070)) },
                modifier = Modifier.weight(0.4f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFF3333),
                    unfocusedBorderColor = Color(0xFF222222),
                    focusedContainerColor = Color(0xFF0A0A0A),
                    unfocusedContainerColor = Color(0xFF0A0A0A)
                ),
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("VNC Password", color = Color(0xFF707070)) },
                modifier = Modifier.weight(0.6f),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFF3333),
                    unfocusedBorderColor = Color(0xFF222222),
                    focusedContainerColor = Color(0xFF0A0A0A),
                    unfocusedContainerColor = Color(0xFF0A0A0A)
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (host.isBlank()) return@Button
                isConnecting = true
                status = "Connecting to $host:$port..."
                // VNC connection logic would go here
                // For now, simulate connection
                isConnecting = false
                isConnected = true
                status = "Connected to $host:$port"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3333)),
            enabled = !isConnecting && host.isNotBlank()
        ) {
            if (isConnecting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Connect VNC", color = Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0A0A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222222))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Computer,
                        contentDescription = null,
                        tint = if (isConnected) Color(0xFF00E676) else Color(0xFF707070),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Status: $status",
                        color = if (isConnected) Color(0xFF00E676) else Color(0xFFB0B0B0),
                        fontSize = 14.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Placeholder for VNC view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF0A0A0A))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isConnected) {
                Text(
                    text = "VNC View Placeholder\n(Integrate VNC viewer library here)",
                    color = Color(0xFF707070),
                    fontSize = 14.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                Text(
                    text = "Connect to a VNC server to view remote desktop",
                    color = Color(0xFF707070),
                    fontSize = 14.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Note: Full VNC integration requires native VNC client library.\nCurrent version provides connection UI with SSH tunnel support.",
            color = Color(0xFF707070),
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    }
}
