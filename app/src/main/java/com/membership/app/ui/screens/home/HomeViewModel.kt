package com.membership.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.membership.app.data.repository.MemberRepository
import com.membership.app.data.repository.SortOrder
import com.membership.app.domain.model.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedGender = MutableStateFlow<Int?>(null)
    val selectedGender: StateFlow<Int?> = _selectedGender

    private val _sortOrder = MutableStateFlow(SortOrder.UPDATE_TIME)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val totalCount: StateFlow<Int> = memberRepository.observeMemberCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val maleCount: StateFlow<Int> = memberRepository.observeMaleCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val femaleCount: StateFlow<Int> = memberRepository.observeFemaleCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val members: Flow<List<Member>> = combine(
        _searchQuery.debounce(300),
        _selectedGender,
        _sortOrder
    ) { query, gender, sort ->
        Triple(query, gender, sort)
    }.flatMapLatest { (query, gender, sort) ->
        memberRepository.getAllMembers(sort)
    }.combine(
        combine(_searchQuery.debounce(300), _selectedGender) { q, g -> Pair(q, g) }
    ) { allMembers, (query, gender) ->
        allMembers
            .filter { member ->
                gender == null || member.gender == gender
            }
            .filter { member ->
                if (query.isBlank()) true
                else {
                    member.name.contains(query, ignoreCase = true) ||
                    member.city?.contains(query, ignoreCase = true) == true ||
                    member.education?.contains(query, ignoreCase = true) == true ||
                    member.occupation?.contains(query, ignoreCase = true) == true
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _isLoading.value = false
    }

    fun onGenderSelected(gender: Int?) {
        _selectedGender.value = if (_selectedGender.value == gender) null else gender
    }

    fun onSortOrderChanged(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch {
            memberRepository.deleteMember(member)
        }
    }

    fun toggleFavorite(member: Member) {
        viewModelScope.launch {
            memberRepository.toggleFavorite(member)
        }
    }

    fun refresh() {
        _isLoading.value = false
    }
}
