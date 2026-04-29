package com.membership.app.ui.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.membership.app.domain.model.Member
import com.membership.app.domain.model.PartnerRequirements
import com.membership.app.ui.components.AnimatedItemEnter
import com.membership.app.ui.components.AuroraBackground
import com.membership.app.ui.components.GlassPanel
import com.membership.app.ui.components.GlowOrb
import com.membership.app.ui.theme.AuroraAmber
import com.membership.app.ui.theme.AuroraCyan
import com.membership.app.ui.theme.AuroraRose
import com.membership.app.ui.theme.Error
import com.membership.app.ui.theme.Info
import com.membership.app.ui.theme.OnPrimary
import com.membership.app.ui.theme.Primary
import com.membership.app.ui.theme.Success
import com.membership.app.ui.theme.TextCream
import com.membership.app.ui.theme.TextMuted
import com.membership.app.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MemberDetailScreen(
    memberId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToPoster: (Long) -> Unit,
    viewModel: MemberDetailViewModel = hiltViewModel()
) {
    val member by viewModel.member.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPhotoViewer by remember { mutableStateOf(false) }
    var selectedPhotoIndex by remember { mutableStateOf(0) }
    var showFabMenu by remember { mutableStateOf(false) }

    LaunchedEffect(memberId) {
        viewModel.loadMember(memberId)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    AuroraBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        AnimatedVisibility(
                            visible = scrollBehavior.state.collapsedFraction > 0.5f,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            Text(
                                text = member?.name ?: "会员详情",
                                fontWeight = FontWeight.Bold,
                                color = TextCream
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回",
                                tint = TextCream
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "删除",
                                tint = AuroraRose
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color(0x9917131F)
                    )
                )
            },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AnimatedVisibility(
                        visible = showFabMenu,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.Black.copy(alpha = 0.7f)
                            ) {
                                Text(
                                    "生成海报",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                            SmallFloatingActionButton(
                                onClick = {
                                    showFabMenu = false
                                    onNavigateToPoster(memberId)
                                },
                                containerColor = AuroraCyan,
                                contentColor = Color(0xFF00201D)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "生成海报",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = showFabMenu,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.Black.copy(alpha = 0.7f)
                            ) {
                                Text(
                                    "编辑资料",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                            SmallFloatingActionButton(
                                onClick = {
                                    showFabMenu = false
                                    onNavigateToEdit(memberId)
                                },
                                containerColor = AuroraAmber,
                                contentColor = Color(0xFF3A2700)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "编辑",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    val fabRotation by animateFloatAsState(
                        targetValue = if (showFabMenu) 45f else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "fabRotation"
                    )

                    FloatingActionButton(
                        onClick = { showFabMenu = !showFabMenu },
                        containerColor = AuroraRose,
                        contentColor = OnPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "操作",
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer { rotationZ = fabRotation }
                        )
                    }
                }
            }
        ) { paddingValues ->
            member?.let { m ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    item {
                        PhotoCarousel(
                            photos = m.photos,
                            name = m.name,
                            gender = m.gender,
                            onPhotoClick = { index ->
                                selectedPhotoIndex = index
                                showPhotoViewer = true
                            }
                        )
                    }

                    item {
                        AnimatedItemEnter(index = 0) {
                            BasicInfoCard(member = m)
                        }
                    }

                    item {
                        AnimatedItemEnter(index = 1) {
                            MarriageInfoCard(member = m)
                        }
                    }

                    item {
                        AnimatedItemEnter(index = 2) {
                            FamilyInfoCard(member = m)
                        }
                    }

                    item {
                        AnimatedItemEnter(index = 3) {
                            RequirementsCard(requirements = m.requirements)
                        }
                    }
                }
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AuroraCyan)
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("确认删除") },
            text = { Text("确定要删除该会员吗？此操作不可撤销。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        member?.let {
                            viewModel.deleteMember(it)
                            showDeleteDialog = false
                            onNavigateBack()
                        }
                    }
                ) {
                    Text("删除", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    if (showPhotoViewer && member != null) {
        PhotoViewerDialog(
            photos = member!!.photos,
            initialPage = selectedPhotoIndex,
            onDismiss = { showPhotoViewer = false }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PhotoCarousel(
    photos: List<String>,
    name: String,
    gender: Int,
    onPhotoClick: (Int) -> Unit
) {
    val pagerState = rememberPagerState { photos.size.coerceAtLeast(1) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(30.dp))
    ) {
        if (photos.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = photos[page],
                    contentDescription = "照片 ${page + 1}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onPhotoClick(page) },
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(photos.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(width = if (pagerState.currentPage == index) 22.dp else 7.dp, height = 7.dp)
                            .clip(CircleShape)
                            .background(
                                if (pagerState.currentPage == index) AuroraCyan else Color.White.copy(alpha = 0.45f)
                            )
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AuroraRose.copy(alpha = 0.36f),
                                AuroraCyan.copy(alpha = 0.22f),
                                AuroraAmber.copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlowOrb(size = 120.dp, color = if (gender == 1) AuroraCyan else AuroraRose)
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextCream
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "暂无照片",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.78f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextCream
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = if (gender == 1) Icons.Default.Male else Icons.Default.Female,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (gender == 1) Info else Error
            )
        }
    }
}

@Composable
private fun BasicInfoCard(member: Member) {
    InfoCard(title = "基本信息") {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val infoItems = listOfNotNull(
                member.age?.let { "年龄" to "${it}岁" },
                member.height?.let { "身高" to "${it}cm" },
                member.weight?.let { "体重" to "${it}kg" },
                member.education?.let { "学历" to it },
                member.occupation?.let { "职业" to it },
                member.incomeRange?.let { "收入" to it },
                member.city?.let { "现居" to it },
                member.hometown?.let { "籍贯" to it }
            )

            infoItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { (label, value) ->
                        InfoItem(
                            label = label,
                            value = value,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = TextCream
        )
    }
}

@Composable
private fun MarriageInfoCard(member: Member) {
    InfoCard(title = "婚姻情感") {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow(label = "婚姻状况", value = member.maritalStatusText)
            InfoRow(
                label = "子女情况",
                value = if (member.hasChildren) {
                    "有子女" + (member.childrenCount?.let { " (${it}个)" } ?: "")
                } else "无子女"
            )
            member.relationshipHistory?.takeIf { it.isNotBlank() }?.let {
                InfoRow(label = "情感经历", value = it)
            }
        }
    }
}

@Composable
private fun FamilyInfoCard(member: Member) {
    InfoCard(title = "家庭背景") {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            member.parentsInfo?.takeIf { it.isNotBlank() }?.let {
                InfoRow(label = "父母情况", value = it)
            }
            member.siblingsInfo?.takeIf { it.isNotBlank() }?.let {
                InfoRow(label = "兄弟姐妹", value = it)
            }
            member.familyEconomicStatus?.takeIf { it.isNotBlank() }?.let {
                InfoRow(label = "家庭经济", value = it)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RequirementsCard(requirements: PartnerRequirements?) {
    if (requirements == null) return

    InfoCard(title = "择偶要求") {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            requirements.ageRange?.let {
                InfoRow(label = "年龄范围", value = it)
            }
            requirements.heightRange?.let {
                InfoRow(label = "身高范围", value = it)
            }
            requirements.education?.let {
                InfoRow(label = "学历要求", value = it)
            }
            requirements.maritalStatus?.let { status ->
                val statusText = when (status) {
                    0 -> "未婚"
                    1 -> "可接受离异"
                    2 -> "可接受丧偶"
                    else -> "不限"
                }
                InfoRow(label = "婚姻状况", value = statusText)
            }
            requirements.city?.let {
                InfoRow(label = "所在城市", value = it)
            }
            requirements.otherRequirements?.takeIf { it.isNotBlank() }?.let {
                InfoRow(label = "其他要求", value = it)
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AuroraCyan
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label：",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextCream
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PhotoViewerDialog(
    photos: List<String>,
    initialPage: Int,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { photos.size }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = photos[page],
                    contentDescription = "照片 ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "关闭",
                    tint = Color.White
                )
            }

            Text(
                text = "${pagerState.currentPage + 1}/${photos.size}",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
