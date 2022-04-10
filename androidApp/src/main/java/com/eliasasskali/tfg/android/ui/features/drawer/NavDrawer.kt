package com.eliasasskali.tfg.android.ui.features.drawer

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.android.navigation.goToLogin
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
        NavDrawerItem.ClubListItem,
        NavDrawerItem.LogOutItem,
    )
    Column() {
        /*Image(
            painter = painterResource(id = R.drawable.img_header_login),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )*/
        Column(
            modifier = Modifier
                //.background(color = MaterialTheme.colors.background)
                .padding(vertical = 12.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // List of navigation items
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val activity = (LocalContext.current as? Activity)
            val authRepository: AuthRepository = get()

            items.forEach { item ->
                DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                    if (item.route == "NOT IMPLEMENTED") {
                        activity?.let {
                            // TODO: Toast
                            println("NOT IMPLEMENTED")
                        }
                    }  else if (item.route == "LogOut") {
                        authRepository.signOut()
                        activity?.let {
                            goToLogin(it)
                        }
                    } else {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }

                    // Close drawer
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        // TODO: Add app logo
        /*Image(
            painter = painterResource(id = R.drawable.img_logo_worldline_base),
            contentDescription = stringResource(
                R.string.worldline_logo_description
            ),
            Modifier.align(Alignment.CenterHorizontally)
        )*/
    }
}

@Composable
fun DrawerItem(item: NavDrawerItem, selected: Boolean, onItemClick: (NavDrawerItem) -> Unit) {
    val background = if (selected) Color.LightGray else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = { onItemClick(item) })
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = stringResource(item.title),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = stringResource(id = item.title),
            style = if (selected) MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
            else MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SelectedDrawerItemPreview() {
    AppTheme {
        DrawerItem(item = NavDrawerItem.ClubListItem, selected = true, onItemClick = {})
    }
}

@Composable
@Preview(showBackground = true)
fun NonSelectedDrawerItemPreview() {
    AppTheme {
        DrawerItem(item = NavDrawerItem.ClubListItem, selected = false, onItemClick = {})
    }
}