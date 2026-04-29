package com.membership.app.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.membership.app.data.repository.MemberRepository
import com.membership.app.domain.model.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MemberEditViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _member = MutableStateFlow<Member?>(null)
    val member: StateFlow<Member?> = _member

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadMember(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedMember = memberRepository.getMemberById(id)
                _member.value = loadedMember
            } catch (e: Exception) {
                _error.value = "加载会员数据失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateMember(updatedMember: Member) {
        _member.value = updatedMember
    }

    fun saveMember() {
        val currentMember = _member.value
        if (currentMember == null) {
            _error.value = "会员数据为空"
            return
        }

        if (currentMember.name.isBlank()) {
            _error.value = "请输入姓名"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val memberToSave = currentMember.copy(
                    updatedAt = Date()
                )

                if (memberToSave.id == 0L) {
                    memberRepository.insertMember(memberToSave)
                } else {
                    memberRepository.updateMember(memberToSave)
                }

                _saveSuccess.value = true
            } catch (e: Exception) {
                _error.value = "保存失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }
}
