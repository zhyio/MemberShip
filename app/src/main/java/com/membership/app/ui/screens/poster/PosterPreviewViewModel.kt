package com.membership.app.ui.screens.poster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.membership.app.data.repository.MemberRepository
import com.membership.app.domain.model.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosterPreviewViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _member = MutableStateFlow<Member?>(null)
    val member: StateFlow<Member?> = _member

    fun loadMember(id: Long) {
        viewModelScope.launch {
            _member.value = memberRepository.getMemberById(id)
        }
    }
}
