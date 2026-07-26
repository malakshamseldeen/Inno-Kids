package com.example.data.repository

import com.example.data.api.GeminiClient
import com.example.data.dao.InnoKidsDao
import com.example.data.model.BadgeEntity
import com.example.data.model.ChallengeEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.LessonEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class InnoKidsRepository(private val dao: InnoKidsDao) {

    val userProfile: Flow<UserEntity?> = dao.getUserProfile()
    val allLessons: Flow<List<LessonEntity>> = dao.getAllLessons()
    val allChallenges: Flow<List<ChallengeEntity>> = dao.getAllChallenges()
    val allBadges: Flow<List<BadgeEntity>> = dao.getAllBadges()
    val chatMessages: Flow<List<ChatMessageEntity>> = dao.getChatMessages()

    suspend fun initializeDefaultDataIfEmpty() {
        // Initialize User if missing
        val existingUser = dao.getUserProfile().firstOrNull()
        if (existingUser == null) {
            dao.insertOrUpdateUser(
                UserEntity(
                    name = "Omar",
                    age = 9,
                    level = 3,
                    xp = 340,
                    xpForNextLevel = 500,
                    coins = 120,
                    streakDays = 5,
                    selectedAvatar = "Cyber Hero",
                    selectedRobot = "Inno Classic"
                )
            )
        }

        // Initialize Lessons if empty
        val existingLessons = dao.getAllLessons().firstOrNull()
        if (existingLessons.isNullOrEmpty()) {
            dao.insertLessons(getInitialLessons())
        }

        // Initialize Challenges if empty
        val existingChallenges = dao.getAllChallenges().firstOrNull()
        if (existingChallenges.isNullOrEmpty()) {
            dao.insertChallenges(getInitialChallenges())
        }

        // Initialize Badges if empty
        val existingBadges = dao.getAllBadges().firstOrNull()
        if (existingBadges.isNullOrEmpty()) {
            dao.insertBadges(getInitialBadges())
        }

        // Initialize welcome chat if empty
        val existingChat = dao.getChatMessages().firstOrNull()
        if (existingChat.isNullOrEmpty()) {
            dao.insertChatMessage(
                ChatMessageEntity(
                    sender = "INNO",
                    text = "Hi Omar! 🤖 I'm Inno, your AI learning buddy! What secret about AI would you like to explore today? Ask me anything or try a fun prompt!",
                    category = "WELCOME"
                )
            )
        }
    }

    suspend fun completeLesson(lessonId: String) {
        val lessons = dao.getAllLessons().firstOrNull() ?: return
        val lesson = lessons.find { it.id == lessonId } ?: return

        if (!lesson.isCompleted) {
            val updatedLesson = lesson.copy(isCompleted = true)
            dao.updateLesson(updatedLesson)
            addXpAndCoins(lesson.xpReward, lesson.coinReward)
        }
    }

    suspend fun completeChallenge(challengeId: String) {
        val challenges = dao.getAllChallenges().firstOrNull() ?: return
        val challenge = challenges.find { it.id == challengeId } ?: return

        if (!challenge.isCompleted) {
            dao.updateChallenge(challenge.copy(isCompleted = true))
            addXpAndCoins(challenge.xpReward, challenge.xpReward / 2)
        }
    }

    suspend fun addXpAndCoins(xpGained: Int, coinsGained: Int) {
        val user = dao.getUserProfile().firstOrNull() ?: return
        var newXp = user.xp + xpGained
        var newCoins = user.coins + coinsGained
        var newLevel = user.level
        var xpTarget = user.xpForNextLevel

        while (newXp >= xpTarget) {
            newLevel += 1
            newXp -= xpTarget
            xpTarget += 250
        }

        dao.insertOrUpdateUser(
            user.copy(
                xp = newXp,
                coins = newCoins,
                level = newLevel,
                xpForNextLevel = xpTarget
            )
        )
    }

    suspend fun updateUserAvatar(avatarName: String, robotName: String) {
        val user = dao.getUserProfile().firstOrNull() ?: return
        dao.insertOrUpdateUser(
            user.copy(
                selectedAvatar = avatarName,
                selectedRobot = robotName
            )
        )
    }

    suspend fun updateParentSettings(pin: String, maxMinutes: Int) {
        val user = dao.getUserProfile().firstOrNull() ?: return
        dao.insertOrUpdateUser(
            user.copy(
                parentPin = pin,
                maxDailyMinutes = maxMinutes,
                isParentPinSet = true
            )
        )
    }

    suspend fun sendMessageToInno(userPrompt: String) {
        // Insert user message
        dao.insertChatMessage(
            ChatMessageEntity(
                sender = "USER",
                text = userPrompt
            )
        )

        // Get AI response
        val aiResponse = GeminiClient.askInno(userPrompt)

        // Insert AI response
        dao.insertChatMessage(
            ChatMessageEntity(
                sender = "INNO",
                text = aiResponse
            )
        )
    }

    suspend fun clearChat() {
        dao.clearChatHistory()
        dao.insertChatMessage(
            ChatMessageEntity(
                sender = "INNO",
                text = "Beep Boop! Chat cleared! What new topic shall we learn today? 🤖",
                category = "WELCOME"
            )
        )
    }

    private fun getInitialLessons() = listOf(
        LessonEntity(
            id = "lesson_1",
            title = "What is AI?",
            category = "Beginner",
            description = "Discover how smart computers use code and patterns to learn like humans!",
            storyText = "Imagine giving a robot a bucket of legos! Human intelligence builds things step-by-step. Artificial Intelligence (AI) allows computers to notice patterns, learn from thousands of examples, and solve problems all by themselves!",
            estMinutes = 5,
            isCompleted = true,
            xpReward = 50,
            coinReward = 20,
            quizQuestionsJson = """[
                {"question": "What does AI stand for?", "options": ["Awesome Internet", "Artificial Intelligence", "Automated Insects"], "correctIndex": 1},
                {"question": "How do AI computers learn?", "options": ["By eating carrots", "By recognizing patterns in big data", "By sleeping at night"], "correctIndex": 1}
            ]""",
            iconName = "psychology"
        ),
        LessonEntity(
            id = "lesson_2",
            title = "Machines vs Humans",
            category = "Beginner",
            description = "Compare what humans do best with what machines do best!",
            storyText = "Humans have feelings, imagination, and empathy! Machines are super fast at math, searching huge libraries, and spotting tiny visual details. When humans and AI work together, magical things happen!",
            estMinutes = 7,
            isCompleted = false,
            xpReward = 60,
            coinReward = 25,
            quizQuestionsJson = """[
                {"question": "Which of these is a special human quality?", "options": ["Empathy and creativity", "Calculating 100,000 math problems per second", "Running on lithium batteries"], "correctIndex": 0}
            ]""",
            iconName = "compare"
        ),
        LessonEntity(
            id = "lesson_3",
            title = "Pattern Recognition",
            category = "Beginner",
            description = "Learn how AI sees cat photos, sound waves, and patterns!",
            storyText = "How does your phone recognize a cat? By looking at thousands of pictures of whiskers, pointy ears, and fur! AI turns pictures into tiny pixel numbers to spot matching patterns.",
            estMinutes = 8,
            isCompleted = false,
            xpReward = 70,
            coinReward = 30,
            quizQuestionsJson = """[
                {"question": "How does AI recognize cats in pictures?", "options": ["By smelling the phone", "By analyzing patterns like ears and whiskers", "By asking a mouse"], "correctIndex": 1}
            ]""",
            iconName = "pattern"
        ),
        LessonEntity(
            id = "lesson_4",
            title = "Machine Learning Basics",
            category = "Intermediate",
            description = "Discover how AI gets smarter with more training data!",
            storyText = "When you learn to ride a bicycle, you wobble at first! Machine learning works the same way: AI makes predictions, learns from its mistakes, and improves over time with training data.",
            estMinutes = 10,
            isCompleted = false,
            xpReward = 90,
            coinReward = 40,
            quizQuestionsJson = """[
                {"question": "What happens when AI gets more training data?", "options": ["It gets slower", "It makes better predictions and fewer mistakes", "It explodes"], "correctIndex": 1}
            ]""",
            iconName = "auto_awesome"
        ),
        LessonEntity(
            id = "lesson_5",
            title = "Generative AI & Prompts",
            category = "Intermediate",
            description = "Learn how to craft magic prompts to create stories, art, and code!",
            storyText = "Generative AI can create brand new drawings, music, and stories! A 'Prompt' is your set of instructions. The clearer and more detailed your prompt is, the cooler the creation!",
            estMinutes = 12,
            isCompleted = false,
            xpReward = 100,
            coinReward = 50,
            quizQuestionsJson = """[
                {"question": "What is a 'Prompt' in Generative AI?", "options": ["The power button", "Instructions or descriptions given to AI", "A type of robot leg"], "correctIndex": 1}
            ]""",
            iconName = "brush"
        ),
        LessonEntity(
            id = "lesson_6",
            title = "Neural Networks",
            category = "Advanced",
            description = "Explore virtual brain cells called neurons that help AI think!",
            storyText = "Inside human brains are billions of connected cells called neurons. AI engineers created Artificial Neural Networks with layered nodes to mimic how brain signals process complex thoughts!",
            estMinutes = 15,
            isCompleted = false,
            xpReward = 150,
            coinReward = 75,
            quizQuestionsJson = """[
                {"question": "What are Artificial Neural Networks inspired by?", "options": ["Spider webs", "The human brain and neurons", "Car engines"], "correctIndex": 1}
            ]""",
            iconName = "hub"
        ),
        LessonEntity(
            id = "lesson_7",
            title = "AI Ethics & Safety",
            category = "Advanced",
            description = "Understand fairness, privacy, and building safe AI for everyone!",
            storyText = "AI is super powerful! That's why inventors must ensure AI is fair, respects user privacy, doesn't spread false rumors, and helps make the world a better, safer place for all kids.",
            estMinutes = 10,
            isCompleted = false,
            xpReward = 120,
            coinReward = 60,
            quizQuestionsJson = """[
                {"question": "Why is AI ethics important?", "options": ["To ensure AI is fair, safe, and helpful", "So robots can take over the world", "It is not important"], "correctIndex": 0}
            ]""",
            iconName = "verified_user"
        )
    )

    private fun getInitialChallenges() = listOf(
        ChallengeEntity(
            id = "ch_1",
            title = "Sort AI vs Human",
            type = "SORT_AI",
            description = "Can you classify which tasks require human empathy vs machine speed?",
            xpReward = 100,
            isCompleted = true
        ),
        ChallengeEntity(
            id = "ch_2",
            title = "Pattern Detective",
            type = "PATTERN",
            description = "Spot the missing sequence numbers in the neural network train!",
            xpReward = 120,
            isCompleted = false
        ),
        ChallengeEntity(
            id = "ch_3",
            title = "Build Inno Robot",
            type = "BUILD_ROBOT",
            description = "Assemble sensor modules, vision cameras, and AI core logic!",
            xpReward = 150,
            isCompleted = false
        ),
        ChallengeEntity(
            id = "ch_4",
            title = "Prompt Master Lab",
            type = "PROMPT",
            description = "Turn vague prompts into super specific AI art and story prompts!",
            xpReward = 200,
            isCompleted = false
        )
    )

    private fun getInitialBadges() = listOf(
        BadgeEntity(
            id = "b_1",
            title = "AI Explorer",
            description = "Completed your first AI lesson!",
            iconName = "explore",
            isUnlocked = true,
            unlockedDate = "Yesterday"
        ),
        BadgeEntity(
            id = "b_2",
            title = "Robot Builder",
            description = "Assembled your custom companion robot!",
            iconName = "smart_toy",
            isUnlocked = true,
            unlockedDate = "Today"
        ),
        BadgeEntity(
            id = "b_3",
            title = "Prompt Master",
            description = "Crafted 5 magical AI prompts with Inno!",
            iconName = "auto_fix_high",
            isUnlocked = false
        ),
        BadgeEntity(
            id = "b_4",
            title = "ML Hero",
            description = "Mastered Machine Learning Basics!",
            iconName = "military_tech",
            isUnlocked = false
        ),
        BadgeEntity(
            id = "b_5",
            title = "5-Day Streak",
            description = "Learned with Inno 5 days in a row!",
            iconName = "local_fire_department",
            isUnlocked = true,
            unlockedDate = "Today"
        ),
        BadgeEntity(
            id = "b_6",
            title = "Quiz Whiz",
            description = "Scored 100% on 3 AI quizzes!",
            iconName = "workspace_premium",
            isUnlocked = false
        )
    )
}
