package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.InnoKidsDatabase
import com.example.data.model.BadgeEntity
import com.example.data.model.ChallengeEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.LessonEntity
import com.example.data.model.UserEntity
import com.example.data.repository.InnoKidsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InnoKidsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InnoKidsRepository

    val userProfile: StateFlow<UserEntity?>
    val lessons: StateFlow<List<LessonEntity>>
    val challenges: StateFlow<List<ChallengeEntity>>
    val badges: StateFlow<List<BadgeEntity>>
    val chatMessages: StateFlow<List<ChatMessageEntity>>

    private val _selectedLesson = MutableStateFlow<LessonEntity?>(null)
    val selectedLesson: StateFlow<LessonEntity?> = _selectedLesson.asStateFlow()

    private val _selectedChallenge = MutableStateFlow<ChallengeEntity?>(null)
    val selectedChallenge: StateFlow<ChallengeEntity?> = _selectedChallenge.asStateFlow()

    private val _isParentUnlocked = MutableStateFlow(false)
    val isParentUnlocked: StateFlow<Boolean> = _isParentUnlocked.asStateFlow()

    private val _parentPinError = MutableStateFlow<String?>(null)
    val parentPinError: StateFlow<String?> = _parentPinError.asStateFlow()

    private val _isSendingMessage = MutableStateFlow(false)
    val isSendingMessage: StateFlow<Boolean> = _isSendingMessage.asStateFlow()

    init {
        val database = InnoKidsDatabase.getDatabase(application)
        repository = InnoKidsRepository(database.innoKidsDao())

        viewModelScope.launch {
            repository.initializeDefaultDataIfEmpty()
        }

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        lessons = repository.allLessons.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        challenges = repository.allChallenges.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        badges = repository.allBadges.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        chatMessages = repository.chatMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun selectLesson(lesson: LessonEntity?) {
        _selectedLesson.value = lesson
    }

    fun completeLesson(lessonId: String) {
        viewModelScope.launch {
            repository.completeLesson(lessonId)
        }
    }

    fun selectChallenge(challenge: ChallengeEntity?) {
        _selectedChallenge.value = challenge
    }

    fun completeChallenge(challengeId: String) {
        viewModelScope.launch {
            repository.completeChallenge(challengeId)
        }
    }

    fun sendMessageToInno(prompt: String) {
        if (prompt.isBlank()) return
        _isSendingMessage.value = true
        viewModelScope.launch {
            try {
                repository.sendMessageToInno(prompt)
            } finally {
                _isSendingMessage.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    fun unlockParentDashboard(pinInput: String) {
        val user = userProfile.value
        val correctPin = user?.parentPin ?: "1234"
        if (pinInput == correctPin) {
            _isParentUnlocked.value = true
            _parentPinError.value = null
        } else {
            _parentPinError.value = "Incorrect PIN. Default PIN is 1234"
        }
    }

    fun lockParentDashboard() {
        _isParentUnlocked.value = false
        _parentPinError.value = null
    }

    fun updateParentPinAndLimit(newPin: String, maxMinutes: Int) {
        viewModelScope.launch {
            repository.updateParentSettings(newPin, maxMinutes)
        }
    }

    fun updateAvatarAndRobot(avatar: String, robot: String) {
        viewModelScope.launch {
            repository.updateUserAvatar(avatar, robot)
        }
    }
}
