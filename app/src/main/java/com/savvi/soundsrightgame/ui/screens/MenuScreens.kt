package com.savvi.soundsrightgame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.savvi.soundsrightgame.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import android.widget.Toast
import com.savvi.soundsrightgame.ui.components.GeneralButton

@Composable
fun MenuScreen(onPlayClicked: () -> Unit, onHelpClicked: () -> Unit) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val buyCoffeeThanks = stringResource(R.string.buy_coffee_thanks)

    var isGreek by remember {
        mutableStateOf(configuration.locales[0].language == "el")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Image(
            painter = painterResource(id = R.drawable.sounds_right_logo),
            contentDescription = stringResource(id = R.string.app_name)
        )
        
        Spacer(modifier = Modifier.weight(1f))

        GeneralButton(
            text = stringResource(R.string.menu_play),
            onClick = onPlayClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        GeneralButton(
            text = stringResource(R.string.menu_help),
            onClick = onHelpClicked

        )
        
        Spacer(modifier = Modifier.weight(1.5f))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag selection button
            Card(
                onClick = {
                    isGreek = !isGreek
                    val locale = if (isGreek) "el" else "en"
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
                },
                modifier = Modifier.size(width = 48.dp, height = 32.dp),
                shape = MaterialTheme.shapes.small,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Image(
                    painter = painterResource(id = if (isGreek) R.drawable.flag_greece else R.drawable.flag_uk),
                    contentDescription = if (isGreek) "Ελληνικά" else "English",
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Buy me coffee placeholder button
            Button(
                onClick = {
                    Toast.makeText(context, buyCoffeeThanks, Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(stringResource(R.string.buy_coffee))
            }
        }
    }
}

@Composable
fun HelpScreen(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = stringResource(R.string.menu_help), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.help_rules), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onBackClicked, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(stringResource(R.string.ok))
        }
    }
}
