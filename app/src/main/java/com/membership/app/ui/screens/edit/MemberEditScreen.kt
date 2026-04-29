package com.membership.app.ui.screens.edit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.membership.app.domain.model.Member
import com.membership.app.domain.model.PartnerRequirements
import com.membership.app.ui.components.AuroraBackground
import com.membership.app.ui.components.GlassPanel
import com.membership.app.ui.components.PhotoPicker
import com.membership.app.ui.components.Stepper
import com.membership.app.ui.theme.AuroraAmber
import com.membership.app.ui.theme.AuroraRose
import com.membership.app.ui.theme.Error
import com.membership.app.ui.theme.OnPrimary
import com.membership.app.ui.theme.Primary
import com.membership.app.ui.theme.Success
import com.membership.app.ui.theme.TextCream
import com.membership.app.ui.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditScreen(
    memberId: Long?,
    onNavigateBack: () -> Unit,
    onSaveComplete: () -> Unit,
    viewModel: MemberEditViewModel = hiltViewModel()
) {
    val member by viewModel.member.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    val error by viewModel.error.collectAsState()

    var currentStep by remember { mutableIntStateOf(0) }
    val steps = listOf("基本信息", "婚姻家庭", "择偶要求")

    LaunchedEffect(memberId) {
        memberId?.let { viewModel.loadMember(it) }
    }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onSaveComplete()
        }
    }

    AuroraBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                if (memberId == null) "添加会员" else "编辑会员",
                                fontWeight = FontWeight.Bold,
                                color = TextCream
                            )
                            Text(
                                text = "三步完成资料、家庭与择偶要求",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = TextCream
                    )
                )
            },
            bottomBar = {
                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (currentStep > 0) {
                            FilledTonalButton(
                                onClick = { currentStep-- },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("上一步")
                            }
                        }

                        if (currentStep < steps.lastIndex) {
                            Button(
                                onClick = { currentStep++ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AuroraRose)
                            ) {
                                Text("下一步")
                            }
                        } else {
                            Button(
                                onClick = { viewModel.saveMember() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Success)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text("保存")
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Stepper(
                    currentStep = currentStep,
                    steps = steps
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    if (isLoading && memberId != null) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = AuroraAmber
                        )
                    } else {
                        val currentMember = member ?: Member(name = "", gender = 1)

                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    slideInHorizontally { it } + fadeIn() togetherWith
                                    slideOutHorizontally { -it } + fadeOut()
                                } else {
                                    slideInHorizontally { -it } + fadeIn() togetherWith
                                    slideOutHorizontally { it } + fadeOut()
                                }
                            },
                            label = "stepTransition"
                        ) { step ->
                            when (step) {
                                0 -> BasicInfoTab(
                                    member = currentMember,
                                    onUpdate = viewModel::updateMember
                                )
                                1 -> MarriageFamilyTab(
                                    member = currentMember,
                                    onUpdate = viewModel::updateMember
                                )
                                2 -> RequirementsTab(
                                    member = currentMember,
                                    onUpdate = viewModel::updateMember
                                )
                            }
                        }
                    }

                    error?.let { errorMsg ->
                        Snackbar(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            action = {
                                TextButton(onClick = { viewModel.clearError() }) {
                                    Text("确定")
                                }
                            }
                        ) {
                            Text(errorMsg)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicInfoTab(
    member: Member,
    onUpdate: (Member) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PhotoPicker(
            photos = member.photos,
            onPhotosChange = { newPhotos ->
                onUpdate(member.copy(photos = newPhotos))
            }
        )

        OutlinedTextField(
            value = member.name,
            onValueChange = { onUpdate(member.copy(name = it)) },
            label = { Text("姓名 *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Text(
            text = "性别 *",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = member.gender == 1,
                onClick = { onUpdate(member.copy(gender = 1)) }
            )
            Text("男")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = member.gender == 0,
                onClick = { onUpdate(member.copy(gender = 0)) }
            )
            Text("女")
        }

        DatePickerField(
            label = "出生日期",
            date = member.birthDate,
            onDateSelected = { onUpdate(member.copy(birthDate = it)) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = member.height?.toString() ?: "",
                onValueChange = { onUpdate(member.copy(height = it.toIntOrNull())) },
                label = { Text("身高(cm)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = member.weight?.toString() ?: "",
                onValueChange = { onUpdate(member.copy(weight = it.toIntOrNull())) },
                label = { Text("体重(kg)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        DropdownField(
            label = "学历",
            value = member.education ?: "",
            options = listOf("高中及以下", "大专", "本科", "硕士", "博士"),
            onValueChange = { onUpdate(member.copy(education = it)) }
        )

        OutlinedTextField(
            value = member.occupation ?: "",
            onValueChange = { onUpdate(member.copy(occupation = it)) },
            label = { Text("职业") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        DropdownField(
            label = "收入范围",
            value = member.incomeRange ?: "",
            options = listOf(
                "3000元以下", "3000-5000元", "5000-8000元",
                "8000-12000元", "12000-20000元", "20000元以上"
            ),
            onValueChange = { onUpdate(member.copy(incomeRange = it)) }
        )

        OutlinedTextField(
            value = member.city ?: "",
            onValueChange = { onUpdate(member.copy(city = it)) },
            label = { Text("现居城市") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = member.hometown ?: "",
            onValueChange = { onUpdate(member.copy(hometown = it)) },
            label = { Text("籍贯") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarriageFamilyTab(
    member: Member,
    onUpdate: (Member) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "婚姻状况",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(0 to "未婚", 1 to "离异", 2 to "丧偶").forEach { (value, label) ->
                RadioButton(
                    selected = member.maritalStatus == value,
                    onClick = { onUpdate(member.copy(maritalStatus = value)) }
                )
                Text(label)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = member.hasChildren,
                onCheckedChange = { onUpdate(member.copy(hasChildren = it)) }
            )
            Text("是否有子女")
        }

        if (member.hasChildren) {
            OutlinedTextField(
                value = member.childrenCount?.toString() ?: "",
                onValueChange = { onUpdate(member.copy(childrenCount = it.toIntOrNull())) },
                label = { Text("子女数量") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        OutlinedTextField(
            value = member.relationshipHistory ?: "",
            onValueChange = { onUpdate(member.copy(relationshipHistory = it)) },
            label = { Text("情感经历") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = member.parentsInfo ?: "",
            onValueChange = { onUpdate(member.copy(parentsInfo = it)) },
            label = { Text("父母情况") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = member.siblingsInfo ?: "",
            onValueChange = { onUpdate(member.copy(siblingsInfo = it)) },
            label = { Text("兄弟姐妹情况") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(12.dp)
        )

        DropdownField(
            label = "家庭经济状况",
            value = member.familyEconomicStatus ?: "",
            options = listOf("普通", "小康", "富裕", "非常富裕"),
            onValueChange = { onUpdate(member.copy(familyEconomicStatus = it)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RequirementsTab(
    member: Member,
    onUpdate: (Member) -> Unit
) {
    val scrollState = rememberScrollState()
    val req = member.requirements ?: PartnerRequirements()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = req.minAge?.toString() ?: "",
                onValueChange = {
                    onUpdate(member.copy(requirements = req.copy(minAge = it.toIntOrNull())))
                },
                label = { Text("最小年龄") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = req.maxAge?.toString() ?: "",
                onValueChange = {
                    onUpdate(member.copy(requirements = req.copy(maxAge = it.toIntOrNull())))
                },
                label = { Text("最大年龄") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = req.minHeight?.toString() ?: "",
                onValueChange = {
                    onUpdate(member.copy(requirements = req.copy(minHeight = it.toIntOrNull())))
                },
                label = { Text("最小身高(cm)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = req.maxHeight?.toString() ?: "",
                onValueChange = {
                    onUpdate(member.copy(requirements = req.copy(maxHeight = it.toIntOrNull())))
                },
                label = { Text("最大身高(cm)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        DropdownField(
            label = "期望学历",
            value = req.education ?: "",
            options = listOf("不限", "高中及以上", "大专及以上", "本科及以上", "硕士及以上", "博士"),
            onValueChange = {
                onUpdate(member.copy(requirements = req.copy(education = it.takeIf { it.isNotEmpty() })))
            }
        )

        Text(
            text = "接受对方婚姻状况",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(null to "不限", 0 to "未婚", 1 to "可接受离异", 2 to "可接受丧偶").forEach { (value, label) ->
                RadioButton(
                    selected = req.maritalStatus == value,
                    onClick = {
                        onUpdate(member.copy(requirements = req.copy(maritalStatus = value)))
                    }
                )
                Text(label, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        OutlinedTextField(
            value = req.city ?: "",
            onValueChange = {
                onUpdate(member.copy(requirements = req.copy(city = it.takeIf { it.isNotEmpty() })))
            },
            label = { Text("期望对方所在城市") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = req.otherRequirements ?: "",
            onValueChange = {
                onUpdate(member.copy(requirements = req.copy(otherRequirements = it.takeIf { it.isNotEmpty() })))
            },
            label = { Text("其他要求") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    label: String,
    date: Date?,
    onDateSelected: (Date?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date?.time
    )

    OutlinedTextField(
        value = date?.let { formatDate(it) } ?: "",
        onValueChange = { },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.CalendarMonth, contentDescription = "选择日期")
            }
        },
        shape = RoundedCornerShape(12.dp)
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(Date(it))
                        }
                        showDialog = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun formatDate(date: Date): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
}
