package com.developerjp.composablescreens.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.developerjp.composablescreens.BottomAppBarWithFAB
import com.developerjp.composablescreens.SwitchItem
import com.developerjp.composablescreens.VolumeKnob

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainScreen"){
        composable("MainScreen") {
            MainScreen(navController = navController)
        }
        composable( "DetailScreen") {
            DetailScreen(navController = navController)
        }
        composable( "DetailScreen2/{status}", arguments = listOf(navArgument("status") {type = NavType.StringType})) {backStackEntry ->
            DetailScreen2(navController, backStackEntry.arguments?.getString("status"))
        }
    }
}


@Composable
fun MainScreen(navController: NavController){

    var selected by rememberSaveable { mutableStateOf(false) }
    val color = if (selected) MaterialTheme.colorScheme.secondary
    else MaterialTheme.colorScheme.secondaryContainer
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())){

        SwitchItem(name = "Mój switch 1", modifier = Modifier.fillMaxWidth(), false)
        Spacer(modifier = Modifier.size(10.dp))
        SwitchItem(name = "Mój switch 2", modifier = Modifier.fillMaxWidth(), true)
        Spacer(modifier = Modifier.size(10.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly, Alignment.CenterVertically, ){
            Button(onClick = { /*TODO*/ }) {
                Text(text = "RA2")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "RA2")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "RA2")
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly,
        ){
            Button(onClick = { selected = !selected },
                modifier = Modifier.size(100.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = color)) {
                Text(text = "RA5 $selected")
            }
            Button(onClick = { navController.navigate("DetailScreen2/$selected") },
                modifier = Modifier.size(100.dp),
                shape = RectangleShape
            ) {
                Text(text = "RA6")
            }
            Button(onClick = { navController.navigate("DetailScreen")},
                modifier = Modifier.size(100.dp),
                shape = RectangleShape
            ) {
                Text(text = "RB4")
            }
        }
        //ImageButton()
        Spacer(modifier = Modifier.size(20.dp))
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.Red)
        Spacer(modifier = Modifier.size(20.dp))
        Column(modifier = Modifier
            .fillMaxHeight(),
            Arrangement.SpaceEvenly) {
            Text(text = "Text1 $selected", modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.size(20.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                //.border(
                //    2.dp, Color.Red, RectangleShape
                //),
                ,contentAlignment = Alignment.Center){
                VolumeKnob(modifier = Modifier
                    .width(300.dp)
                    .align(Alignment.Center)
                    .padding(20.dp))
                //.padding(50.dp))
            }
            Spacer(modifier = Modifier.size(40.dp))
            Text(text = "Text 2", modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.size(40.dp))
        }
        BottomAppBarWithFAB()
    }


}

@Composable
fun DetailScreen(navController: NavController){
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
        Text(text = "Hello, name")
        Button(onClick = { navController.navigate("MainScreen") }) {
            Text(text = "Moj przycisk")
        }
    }
}

@Composable
fun DetailScreen2(navController: NavController, string: String?){
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
        Text(text = "Hello, name")
        Button(onClick = { navController.navigate("MainScreen") }) {
            Text(text = "Moj przycisk 2 + $string")
        }
    }
}