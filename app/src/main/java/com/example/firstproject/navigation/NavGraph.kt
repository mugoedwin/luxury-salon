package com.example.firstproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.firstproject.ui.screens.*
import com.example.firstproject.ui.theme.AddProductScreen
import com.example.firstproject.ui.theme.AdminDashboardContent

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_WELCOME,
        modifier = modifier
    ) {
        composable(ROUTE_WELCOME) {
            WelcomeScreen(
                onNavigateToLogin = { role -> navController.navigate("$ROUTE_LOGIN/$role") },
                onNavigateToRegister = { navController.navigate(ROUTE_REGISTER) }
            )
        }
        composable(
            route = "$ROUTE_LOGIN/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "customer"
            LoginScreen(
                role = role,
                onNavigateToRegister = { navController.navigate(ROUTE_REGISTER) },
                onLoginSuccess = { userRole ->
                    if (userRole == "admin") {
                        navController.navigate(ROUTE_ADMIN_DASHBOARD) {
                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(ROUTE_LUX_STORY) {
                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(ROUTE_REGISTER) {
            RegisterScreen(
                onBackToLogin = { navController.navigate(ROUTE_LOGIN) },
                onRegisterSuccess = { role ->
                    if (role == "admin") {
                        navController.navigate(ROUTE_ADMIN_DASHBOARD) {
                            popUpTo(ROUTE_REGISTER) { inclusive = true }
                        }
                    } else {
                        navController.navigate(ROUTE_LUX_STORY) {
                            popUpTo(ROUTE_REGISTER) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(ROUTE_CAROUSEL) {
            CarouselScreen(onFinished = {
                navController.navigate(ROUTE_DASHBOARD) {
                    popUpTo(ROUTE_CAROUSEL) { inclusive = true }
                }
            })
        }
        composable(ROUTE_LUX_STORY) {
            LuxStoryScreen(onProceed = {
                navController.navigate(ROUTE_DASHBOARD) {
                    popUpTo(ROUTE_LUX_STORY) { inclusive = true }
                }
            })
        }
        composable(ROUTE_DASHBOARD) {
            IvonneOrchardHomeScreen(navController)
        }
        composable(ROUTE_SERVICES) {
            ServicesPage(navController)
        }
        composable(
            route = "service_detail/{serviceId}",
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "hair"
            ServiceDetailPage(
                serviceId = serviceId, 
                onBookNow = { navController.navigate("bookings") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("bookings") {
            BookingsPage(
                onBackClick = { navController.popBackStack() },
                onProceedToPayment = { navController.navigate("payment_screen") }
            )
        }
        composable("payment_screen") {
            PaymentScreen(onPaymentSuccess = { 
                navController.navigate(ROUTE_DASHBOARD) { 
                    popUpTo(0) { inclusive = true } 
                } 
            })
        }
        composable(ROUTE_CONTACT_US) {
            ContactUsScreen(onBack = { navController.popBackStack() })
        }
        composable(ROUTE_ABOUT_US) {
            AboutUsScreen(onBack = { navController.popBackStack() })
        }
        composable(ROUTE_ADMIN_DASHBOARD) {
            AdminDashboardContent(
                onUnauthorized = { navController.navigate(ROUTE_LOGIN) },
                onLogout = { navController.navigate(ROUTE_LOGIN) }
            )
        }
        composable(ROUTE_ADD_PRODUCT) {
            AddProductScreen(
                onProductAdded = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
