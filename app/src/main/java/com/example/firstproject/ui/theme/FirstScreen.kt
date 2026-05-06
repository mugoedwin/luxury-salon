package com.example.firstproject.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

// =============================================
// DATA CLASS
// =============================================

data class OnboardingPage(
    val title: String,
    val description: String,
    val image: String
)

// =============================================
// SAMPLE PAGES
// =============================================

val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to Lux Salon",
        description = "Experience luxury beauty services, premium styling, and world-class salon care.",
        image = "https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f"
    ),
    OnboardingPage(
        title = "Book Appointments Easily",
        description = "Schedule salon visits, beauty treatments, and consultations in seconds.",
        image = "https://images.unsplash.com/photo-1560066984-138dadb4c035"
    ),
    OnboardingPage(
        title = "Luxury Boutique Shop",
        description = "Shop premium beauty products, hair collections, and skincare essentials.",
        image = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9"
    ),
    OnboardingPage(
        title = "Track Your Experience",
        description = "Manage appointments, orders, and personalized beauty services effortlessly.",
        image = "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f"
    )
)

// =============================================
// MAIN SCREEN (Updated to ShowcaseScreen for consistency)
// =============================================

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowcaseScreen(
    userRole: String = "user",
    onProceed: () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = {
        onboardingPages.size
    })

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF000000),
                        Color(0xFF111111),
                        Color(0xFF1A1A1A)
                    )
                )
            )
    ) {

        // =============================================
        // SKIP BUTTON
        // =============================================

        TextButton(
            onClick = {
                onProceed()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
        ) {

            Text(
                text = "Skip",
                color = Color(0xFFFFB347),
                fontSize = 16.sp
            )
        }

        // =============================================
        // PAGER
        // =============================================

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            val currentPage = onboardingPages[page]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // =============================================
                // IMAGE
                // =============================================

                AsyncImage(
                    model = currentPage.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                        .clip(RoundedCornerShape(30.dp))
                )

                Spacer(modifier = Modifier.height(35.dp))

                // =============================================
                // TITLE
                // =============================================

                Text(
                    text = currentPage.title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // =============================================
                // DESCRIPTION
                // =============================================

                Text(
                    text = currentPage.description,
                    fontSize = 17.sp,
                    color = Color.LightGray,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // =============================================
                // PAGE INDICATORS
                // =============================================

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    repeat(onboardingPages.size) { index ->

                        val isSelected = pagerState.currentPage == index

                        val width by animateFloatAsState(
                            targetValue = if (isSelected) 30f else 10f,
                            label = ""
                        )

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .height(10.dp)
                                .width(width.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected)
                                        Color(0xFFFFB347)
                                    else
                                        Color.Gray
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // =============================================
                // NEXT / FINISH BUTTON
                // =============================================

                AnimatedVisibility(
                    visible = true
                ) {

                    Button(
                        onClick = {
                            if (pagerState.currentPage == onboardingPages.lastIndex) {
                                onProceed()
                            } else {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB347)
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {

                        Text(
                            text =
                            if (pagerState.currentPage == onboardingPages.lastIndex)
                                "Proceed to Home"
                            else
                                "Next",
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShowcaseScreen() {
    ShowcaseScreen(onProceed = {})
}
