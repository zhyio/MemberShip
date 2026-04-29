package com.membership.app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.membership.app.data.repository.SortOrder
import com.membership.app.domain.model.Member
import com.membership.app.ui.components.AnimatedItemEnter
import com.membership.app.ui.components.AuroraBackground
import com.membership.app.ui.components.GlassPanel
import com.membership.app.ui.components.GlowOrb
import com.membership.app.ui.components.StatCard
import com.membership.app.ui.components.scaleOnPress
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val members by viewModel.members.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGender by viewModel.selectedGender.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val totalCount by viewModel.totalCount.collectAsState()
    val maleCount by viewModel.maleCount.collectAsState()
    val femaleCount by viewModel.femaleCount.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    AuroraBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToAdd,
                    containerColor = AuroraRose,
                    contentColor = OnPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "添加会员"
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .statusBarsPadding()
            ) {
                HomeTopStrip(
                    showSortMenu = showSortMenu,
                    currentSort = sortOrder,
                    onShowSortMenu = { showSortMenu = true },
                    onDismissSortMenu = { showSortMenu = false },
                    onSortSelected = {
                        viewModel.onSortOrderChanged(it)
                        showSortMenu = false
                    }
                )

                StatCard(
                    totalCount = totalCount,
                    maleCount = maleCount,
                    femaleCount = femaleCount
                )

                SearchBar(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onClearQuery = { viewModel.onSearchQueryChange("") }
                )

                FilterBar(
                    selectedGender = selectedGender,
                    onGenderSelected = viewModel::onGenderSelected
                )

                if (members.isEmpty() && !isLoading) {
                    EmptyState(onAddClick = onNavigateToAdd)
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 96.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalItemSpacing = 12.dp
                    ) {
                        itemsIndexed(
                            items = members,
                            key = { _, member -> member.id }
                        ) { index, member ->
                            AnimatedItemEnter(index = index) {
                                MemberGridCard(
                                    member = member,
                                    onClick = { onNavigateToDetail(member.id) },
                                    onFavoriteClick = { viewModel.toggleFavorite(member) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTopStrip(
    showSortMenu: Boolean,
    currentSort: SortOrder,
    onShowSortMenu: () -> Unit,
    onDismissSortMenu: () -> Unit,
    onSortSelected: (SortOrder) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "相亲会员管理",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextCream
            )
            Text(
                text = "高质量资料库与海报工作流",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }

        Box {
            IconButton(
                onClick = onShowSortMenu,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.11f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "排序",
                    tint = TextCream
                )
            }
            SortDropdownMenu(
                expanded = showSortMenu,
                currentSort = currentSort,
                onSortSelected = onSortSelected,
                onDismiss = onDismissSortMenu
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "搜索姓名、城市、学历或职业",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = AuroraCyan
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    IconButton(onClick = onClearQuery) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "清除",
                            tint = TextMuted
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = TextCream,
                unfocusedTextColor = TextCream,
                focusedPlaceholderColor = TextMuted,
                unfocusedPlaceholderColor = TextMuted,
                cursorColor = AuroraRose
            ),
            shape = RoundedCornerShape(28.dp)
        )
    }
}

@Composable
private fun FilterBar(
    selectedGender: Int?,
    onGenderSelected: (Int?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedGender == null,
            onClick = { onGenderSelected(null) },
            label = { Text("全部") },
            colors = filterColors(AuroraAmber)
        )
        FilterChip(
            selected = selectedGender == 1,
            onClick = { onGenderSelected(1) },
            label = { Text("男士") },
            leadingIcon = if (selectedGender == 1) {
                {
                    Icon(
                        imageVector = Icons.Default.Male,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null,
            colors = filterColors(AuroraCyan)
        )
        FilterChip(
            selected = selectedGender == 0,
            onClick = { onGenderSelected(0) },
            label = { Text("女士") },
            leadingIcon = if (selectedGender == 0) {
                {
                    Icon(
                        imageVector = Icons.Default.Female,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null,
            colors = filterColors(AuroraRose)
        )
    }
}

@Composable
private fun filterColors(accent: Color) = FilterChipDefaults.filterChipColors(
    containerColor = Color.White.copy(alpha = 0.08f),
    labelColor = TextMuted,
    iconColor = accent,
    selectedContainerColor = accent.copy(alpha = 0.22f),
    selectedLabelColor = TextCream,
    selectedLeadingIconColor = accent
)

@Composable
private fun MemberGridCard(
    member: Member,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .scaleOnPress()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (member.photos.isNotEmpty()) 0.76f else 0.94f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                if (member.photos.isNotEmpty()) {
                    AsyncImage(
                        model = member.photos.first(),
                        contentDescription = member.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        AuroraRose.copy(alpha = 0.36f),
                                        AuroraCyan.copy(alpha = 0.24f),
                                        AuroraAmber.copy(alpha = 0.22f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        GlowOrb(color = if (member.gender == 1) AuroraCyan else AuroraRose)
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(52.dp),
                            tint = TextCream.copy(alpha = 0.86f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.12f),
                                    Color.Black.copy(alpha = 0.62f)
                                )
                            )
                        )
                )

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.36f))
                ) {
                    Icon(
                        imageVector = if (member.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (member.isFavorite) "取消收藏" else "收藏",
                        tint = if (member.isFavorite) AuroraRose else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp),
                    shape = CircleShape,
                    color = when (member.maritalStatus) {
                        0 -> Success.copy(alpha = 0.92f)
                        else -> AuroraAmber.copy(alpha = 0.92f)
                    }
                ) {
                    Text(
                        text = member.maritalStatusText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name.ifBlank { "未命名会员" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextCream,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (member.gender == 1) Icons.Default.Male else Icons.Default.Female,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                        tint = if (member.gender == 1) Info else Error
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    member.age?.let { ChipText("${it}岁") }
                    member.city?.takeIf { it.isNotBlank() }?.let { ChipText(it) }
                }

                member.occupation?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ChipText(text: String) {
    Surface(
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SortDropdownMenu(
    expanded: Boolean,
    currentSort: SortOrder,
    onSortSelected: (SortOrder) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        SortOrderItem("按更新时间", SortOrder.UPDATE_TIME, currentSort, onSortSelected)
        SortOrderItem("按创建时间", SortOrder.CREATE_TIME, currentSort, onSortSelected)
        SortOrderItem("按姓名", SortOrder.NAME, currentSort, onSortSelected)
        SortOrderItem("按年龄", SortOrder.AGE, currentSort, onSortSelected)
    }
}

@Composable
private fun SortOrderItem(
    label: String,
    sortOrder: SortOrder,
    currentSort: SortOrder,
    onSortSelected: (SortOrder) -> Unit
) {
    DropdownMenuItem(
        text = { Text(label) },
        onClick = { onSortSelected(sortOrder) },
        trailingIcon = {
            if (currentSort == sortOrder) {
                Text("✓", color = Primary)
            }
        }
    )
}

@Composable
private fun EmptyState(
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        GlassPanel(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAddClick),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center) {
                    GlowOrb(size = 110.dp, color = AuroraRose)
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(76.dp),
                        tint = TextCream
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "暂无会员数据",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextCream
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "点击右下角按钮，创建第一份可生成海报的会员资料",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        }
    }
}
