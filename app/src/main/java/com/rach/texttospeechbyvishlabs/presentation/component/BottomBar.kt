package com.rach.texttospeechbyvishlabs.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rach.texttospeechbyvishlabs.R

@Composable
fun BottomNavBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        NavigationBar {
            NavigationBarItem(
                selected = selectedItem == 0,
                onClick = { onItemSelected(0) },
                icon = {
                    Image(
                        painter = painterResource(R.drawable.icons8_home),
                        contentDescription = "Home"
                    )
                },
                label = { Text("Home") }
            )

            NavigationBarItem(
                selected = selectedItem == 1,
                onClick = { onItemSelected(1) },
                icon = {
                    Image(
                        painter = painterResource(R.drawable.delete_svgrepo_com),
                        contentDescription = "Clear"
                    )
                },
                label = { Text("Clear") }
            )

            NavigationBarItem(
                selected = selectedItem == 2,
                onClick = { onItemSelected(2) },
                icon = {
                    Image(
                        painter = painterResource(R.drawable.download_outline_svgrepo_com),
                        contentDescription = "Save"
                    )
                },
                label = { Text("Save") }
            )

            NavigationBarItem(
                selected = selectedItem == 3,
                onClick = { onItemSelected(3) },
                icon = {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                },
                label = { Text("Add") }
            )


            NavigationBarItem(
                selected = selectedItem == 4,
                onClick = { onItemSelected(4) },
                icon = {
                    Image(
                        painter = painterResource(R.drawable.setting_svgrepo_com),
                        contentDescription = "Setting"
                    )
                },
                label = { Text("Setting") }
            )
        }
    }
}
