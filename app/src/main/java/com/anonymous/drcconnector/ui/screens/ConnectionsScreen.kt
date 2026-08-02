package com.anonymous.drcconnector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConnectionsScreen() {
    var connections by remember { mutableStateOf(listOf<ConnectionItem>()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved Connections",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFFF3333)
            )
            IconButton(
                onClick = { showAddDialog = true },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFFFF3333))
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (connections.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No saved connections\nTap + to add one",
                    color = Color(0xFF707070),
                    fontSize = 14.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(connections) { conn ->
                    ConnectionCard(
                        connection = conn,
                        onDelete = {
                            connections = connections.filter { it.id != conn.id }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddConnectionDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { conn ->
                connections = connections + conn
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ConnectionCard(connection: ConnectionItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0A0A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222222))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (connection.type == "ssh") Icons.Filled.Terminal else Icons.Filled.Computer,
                    contentDescription = null,
                    tint = Color(0xFFFF3333),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = connection.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    )
                    Text(
                        text = "${connection.host}:${connection.port}",
                        color = Color(0xFF707070),
                        fontSize = 12.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
            IconButton(onClick = onDelete, colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF707070))) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun AddConnectionDialog(onDismiss: () -> Unit, onAdd: (ConnectionItem) -> Unit) {
    var name by remember { mutableStateOf("") }
    var host by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("22") }
    var type by remember { mutableStateOf("ssh") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0A0A0A),
        title = { Text("New Connection", color = Color(0xFFFF3333)) },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name", color = Color(0xFF707070)) }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFFFF3333), unfocusedBorderColor = Color(0xFF222222), focusedContainerColor = Color(0xFF141414), unfocusedContainerColor = Color(0xFF141414)))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = host, onValueChange = { host = it }, label = { Text("Host", color = Color(0xFF707070)) }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFFFF3333), unfocusedBorderColor = Color(0xFF222222), focusedContainerColor = Color(0xFF141414), unfocusedContainerColor = Color(0xFF141414)))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = port, onValueChange = { port = it.filter { c -> c.isDigit() } }, label = { Text("Port", color = Color(0xFF707070)) }, colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFFFF3333), unfocusedBorderColor = Color(0xFF222222), focusedContainerColor = Color(0xFF141414), unfocusedContainerColor = Color(0xFF141414)))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && host.isNotBlank()) {
                        onAdd(ConnectionItem(
                            id = System.currentTimeMillis(),
                            name = name,
                            host = host,
                            port = port.toIntOrNull() ?: 22,
                            type = type
                        ))
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF3333))
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF707070))) { Text("Cancel") }
        }
    )
}

data class ConnectionItem(
    val id: Long,
    val name: String,
    val host: String,
    val port: Int,
    val type: String
)
