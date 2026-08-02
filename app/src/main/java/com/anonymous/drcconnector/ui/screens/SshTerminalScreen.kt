package com.anonymous.drcconnector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jcraft.jsch.*
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

@Composable
fun SshTerminalScreen() {
    var host by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("22") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    var terminalOutput by remember { mutableStateOf(listOf<String>("> DrcConnector SSH Terminal v1.0", "> Ready to connect...", "")) }
    var commandInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var session by remember { mutableStateOf<Session?>(null) }
    var channel by remember { mutableStateOf<ChannelShell?>(null) }
    var writer by remember { mutableStateOf<PrintWriter?>(null) }
    var readerJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(terminalOutput.size) {
        if (terminalOutput.size > 0) {
            listState.animateScrollToItem(terminalOutput.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp)
    ) {
        // Connection form
        if (!isConnected) {
            Text(
                text = "SSH Connection",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFFF3333),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = host,
                onValueChange = { host = it },
                label = { Text("Host", color = Color(0xFF707070)) },
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
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = Color(0xFF707070)) },
                    modifier = Modifier.weight(0.6f),
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color(0xFF707070)) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (host.isBlank() || username.isBlank()) return@Button
                    isLoading = true
                    scope.launch(Dispatchers.IO) {
                        try {
                            val jsch = JSch()
                            val newSession = jsch.getSession(username, host, port.toIntOrNull() ?: 22)
                            newSession.setPassword(password)
                            newSession.setConfig("StrictHostKeyChecking", "no")
                            newSession.timeout = 15000
                            newSession.connect()

                            val newChannel = newSession.openChannel("shell") as ChannelShell
                            newChannel.connect()

                            val outWriter = PrintWriter(newChannel.outputStream, true)
                            val inReader = BufferedReader(InputStreamReader(newChannel.inputStream))

                            session = newSession
                            channel = newChannel
                            writer = outWriter

                            withContext(Dispatchers.Main) {
                                isConnected = true
                                isLoading = false
                                terminalOutput = terminalOutput + listOf(
                                    "",
                                    "> Connected to $host:${port}",
                                    "> Session established",
                                    ""
                                )
                            }

                            readerJob = scope.launch(Dispatchers.IO) {
                                var line: String?
                                while (isConnected && inReader.readLine().also { line = it } != null) {
                                    line?.let {
                                        withContext(Dispatchers.Main) {
                                            terminalOutput = terminalOutput + it
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                isLoading = false
                                terminalOutput = terminalOutput + listOf(
                                    "",
                                    "> ERROR: ${e.message}",
                                    ""
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3333)),
                enabled = !isLoading && host.isNotBlank() && username.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Connect", color = Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
        } else {
            // Terminal view
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "● $host:${port}",
                    color = Color(0xFF00E676),
                    fontSize = 14.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                TextButton(
                    onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                readerJob?.cancel()
                                channel?.disconnect()
                                session?.disconnect()
                            }
                            isConnected = false
                            terminalOutput = terminalOutput + listOf(
                                "",
                                "> Disconnected from $host",
                                ""
                            )
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF3333))
                ) {
                    Icon(Icons.Filled.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Disconnect")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Terminal output
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF0A0A0A))
                    .padding(8.dp),
                state = listState
            ) {
                items(terminalOutput) { line ->
                    Text(
                        text = line,
                        color = if (line.startsWith(">")) Color(0xFFFF3333) else Color(0xFFB0B0B0),
                        fontSize = 12.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Command input
            BasicTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF141414), MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                cursorBrush = SolidColor(Color(0xFFFF3333)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (commandInput.isNotBlank() && writer != null) {
                            val cmd = commandInput
                            terminalOutput = terminalOutput + ">>> $cmd"
                            writer?.println(cmd)
                            writer?.flush()
                            commandInput = ""
                        }
                    }
                ),
                decorationBox = { innerTextField ->
                    if (commandInput.isEmpty()) {
                        Text("Type command and press Enter...", color = Color(0xFF707070), fontSize = 14.sp)
                    }
                    innerTextField()
                }
            )
        }
    }
}
