package com.membership.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.membership.app.ui.screens.detail.MemberDetailScreen
import com.membership.app.ui.screens.edit.MemberEditScreen
import com.membership.app.ui.screens.home.HomeScreen
import com.membership.app.ui.screens.poster.PosterPreviewScreen

object Routes {
    const val HOME = "home"
    const val MEMBER_DETAIL = "member_detail/{memberId}"
    const val MEMBER_EDIT = "member_edit?memberId={memberId}"
    const val POSTER_PREVIEW = "poster_preview/{memberId}"

    fun memberDetail(memberId: Long) = "member_detail/$memberId"
    fun memberEdit(memberId: Long? = null) = if (memberId != null) "member_edit?memberId=$memberId" else "member_edit"
    fun posterPreview(memberId: Long) = "poster_preview/$memberId"
}

private const val TRANSITION_DURATION = 300

@Composable
fun MembershipNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
        }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToDetail = { memberId ->
                    navController.navigate(Routes.memberDetail(memberId))
                },
                onNavigateToAdd = {
                    navController.navigate(Routes.memberEdit())
                }
            )
        }

        composable(
            route = Routes.MEMBER_DETAIL,
            arguments = listOf(
                navArgument("memberId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            MemberDetailScreen(
                memberId = memberId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Routes.memberEdit(id))
                },
                onNavigateToPoster = { id ->
                    navController.navigate(Routes.posterPreview(id))
                }
            )
        }

        composable(
            route = Routes.MEMBER_EDIT,
            arguments = listOf(
                navArgument("memberId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId")
            MemberEditScreen(
                memberId = if (memberId == -1L) null else memberId,
                onNavigateBack = { navController.popBackStack() },
                onSaveComplete = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.POSTER_PREVIEW,
            arguments = listOf(
                navArgument("memberId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            PosterPreviewScreen(
                memberId = memberId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
