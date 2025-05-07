package com.android.newuplift.utility

import java.util.Random

object FallbackQuotesProvider {

    // Define your list of ~50 static quotes here
    // Ensure each Quote object has unique IDs (e.g., negative numbers to differentiate from API quotes)
    // and all required fields are populated.
    private val staticQuotes: List<Quote> = listOf(
        Quote(
            id = -1, // Example of a unique ID for static quotes
            quote = "The only way to do great work is to love what you do.",
            author = "Steve Jobs",
            length = 53,
            tags = listOf("inspiration", "motivation", "work"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -2,
            quote = "Strive not to be a success, but rather to be of value.",
            author = "Albert Einstein",
            length = 59,
            tags = listOf("wisdom", "value", "success"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -3,
            quote = "The mind is everything. What you think you become.",
            author = "Buddha",
            length = 52,
            tags = listOf("mindfulness", "wisdom", "philosophy"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -4,
            quote = "Your time is limited, so don’t waste it living someone else’s life.",
            author = "Steve Jobs",
            length = 70,
            tags = listOf("life", "motivation", "time"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -5,
            quote = "An unexamined life is not worth living.",
            author = "Socrates",
            length = 40,
            tags = listOf("philosophy", "wisdom", "life"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        // Added 50 more quotes below
        Quote(
            id = -6,
            quote = "The best way to predict the future is to create it.",
            author = "Peter Drucker",
            length = 53,
            tags = listOf("future", "action", "inspiration"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -7,
            quote = "Happiness is not something readymade. It comes from your own actions.",
            author = "Dalai Lama",
            length = 70,
            tags = listOf("happiness", "action", "wisdom"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -8,
            quote = "The journey of a thousand miles begins with a single step.",
            author = "Lao Tzu",
            length = 60,
            tags = listOf("journey", "perseverance", "motivation"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -9,
            quote = "It does not matter how slowly you go as long as you do not stop.",
            author = "Confucius",
            length = 65,
            tags = listOf("perseverance", "motivation", "progress"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -10,
            quote = "Believe you can and you're halfway there.",
            author = "Theodore Roosevelt",
            length = 42,
            tags = listOf("belief", "inspiration", "motivation"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -11,
            quote = "To live is the rarest thing in the world. Most people exist, that is all.",
            author = "Oscar Wilde",
            length = 75,
            tags = listOf("life", "existence", "philosophy"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -12,
            quote = "The only impossible journey is the one you never begin.",
            author = "Tony Robbins",
            length = 58,
            tags = listOf("journey", "motivation", "action"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -13,
            quote = "In three words I can sum up everything I've learned about life: it goes on.",
            author = "Robert Frost",
            length = 76,
            tags = listOf("life", "wisdom", "resilience"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -14,
            quote = "What lies behind us and what lies before us are tiny matters compared to what lies within us.",
            author = "Ralph Waldo Emerson",
            length = 99,
            tags = listOf("potential", "self", "inspiration"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -15,
            quote = "The purpose of our lives is to be happy.",
            author = "Dalai Lama",
            length = 41,
            tags = listOf("happiness", "purpose", "life"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -16,
            quote = "You must be the change you wish to see in the world.",
            author = "Mahatma Gandhi",
            length = 56,
            tags = listOf("change", "action", "inspiration"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -17,
            quote = "Life is what happens when you're busy making other plans.",
            author = "John Lennon",
            length = 60,
            tags = listOf("life", "plans", "reality"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -18,
            quote = "Get busy living or get busy dying.",
            author = "Stephen King",
            length = 35,
            tags = listOf("life", "action", "choice"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -19,
            quote = "The best revenge is massive success.",
            author = "Frank Sinatra",
            length = 36,
            tags = listOf("success", "revenge", "motivation"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -20,
            quote = "I have not failed. I've just found 10,000 ways that won't work.",
            author = "Thomas A. Edison",
            length = 68,
            tags = listOf("failure", "perseverance", "learning"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -21,
            quote = "A journey of a thousand miles must begin with a single step.",
            author = "Lao Tzu",
            length = 61,
            tags = listOf("journey", "beginnings", "action"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -22,
            quote = "It is never too late to be what you might have been.",
            author = "George Eliot",
            length = 54,
            tags = listOf("dreams", "potential", "hope"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -23,
            quote = "Everything you can imagine is real.",
            author = "Pablo Picasso",
            length = 35,
            tags = listOf("imagination", "creativity", "reality"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -24,
            quote = "Do what you can, with what you have, where you are.",
            author = "Theodore Roosevelt",
            length = 53,
            tags = listOf("action", "resourcefulness", "practicality"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -25,
            quote = "The only true wisdom is in knowing you know nothing.",
            author = "Socrates",
            length = 54,
            tags = listOf("wisdom", "humility", "knowledge"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -26,
            quote = "Act as if what you do makes a difference. It does.",
            author = "William James",
            length = 51,
            tags = listOf("action", "impact", "significance"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -27,
            quote = "Success is not final, failure is not fatal: It is the courage to continue that counts.",
            author = "Winston Churchill",
            length = 88,
            tags = listOf("success", "failure", "courage", "perseverance"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -28,
            quote = "Be yourself; everyone else is already taken.",
            author = "Oscar Wilde",
            length = 45,
            tags = listOf("individuality", "authenticity", "self"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -29,
            quote = "The future belongs to those who believe in the beauty of their dreams.",
            author = "Eleanor Roosevelt",
            length = 72,
            tags = listOf("future", "dreams", "belief", "beauty"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -30,
            quote = "Don't watch the clock; do what it does. Keep going.",
            author = "Sam Levenson",
            length = 53,
            tags = listOf("perseverance", "time", "action"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -31,
            quote = "Keep your face always toward the sunshine, and shadows will fall behind you.",
            author = "Walt Whitman",
            length = 77,
            tags = listOf("positivity", "optimism", "light"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -32,
            quote = "The harder I work, the luckier I get.",
            author = "Samuel Goldwyn",
            length = 37,
            tags = listOf("work", "luck", "effort"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -33,
            quote = "If you want to lift yourself up, lift up someone else.",
            author = "Booker T. Washington",
            length = 56,
            tags = listOf("kindness", "service", "uplift"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -34,
            quote = "What we achieve inwardly will change outer reality.",
            author = "Plutarch",
            length = 51,
            tags = listOf("inner-change", "reality", "transformation"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -35,
            quote = "Life shrinks or expands in proportion to one's courage.",
            author = "Anais Nin",
            length = 57,
            tags = listOf("courage", "life", "growth"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -36,
            quote = "You will face many defeats in life, but never let yourself be defeated.",
            author = "Maya Angelou",
            length = 73,
            tags = listOf("resilience", "defeat", "strength"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -37,
            quote = "The only limit to our realization of tomorrow is our doubts of today.",
            author = "Franklin D. Roosevelt",
            length = 72,
            tags = listOf("limits", "doubts", "realization", "tomorrow"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -38,
            quote = "It is our choices that show what we truly are, far more than our abilities.",
            author = "J.K. Rowling",
            length = 78,
            tags = listOf("choices", "character", "abilities"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -39,
            quote = "The purpose of life is not to be happy. It is to be useful, to be honorable, to be compassionate, to have it make some difference that you have lived and lived well.",
            author = "Ralph Waldo Emerson",
            length = 175,
            tags = listOf("purpose", "life", "compassion", "difference"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -40,
            quote = "Start by doing what's necessary; then do what's possible; and suddenly you are doing the impossible.",
            author = "Francis of Assisi",
            length = 107,
            tags = listOf("action", "possibility", "impossible"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -41,
            quote = "The best and most beautiful things in the world cannot be seen or even touched - they must be felt with the heart.",
            author = "Helen Keller",
            length = 120,
            tags = listOf("beauty", "heart", "feeling"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -42,
            quote = "Nothing is impossible, the word itself says 'I'm possible'!",
            author = "Audrey Hepburn",
            length = 61,
            tags = listOf("possibility", "impossible", "wordplay"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -43,
            quote = "We know what we are, but know not what we may be.",
            author = "William Shakespeare",
            length = 50,
            tags = listOf("potential", "self-discovery", "future"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -44,
            quote = "Perfection is not attainable, but if we chase perfection we can catch excellence.",
            author = "Vince Lombardi",
            length = 84,
            tags = listOf("perfection", "excellence", "chase"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -45,
            quote = "Change your thoughts and you change your world.",
            author = "Norman Vincent Peale",
            length = 47,
            tags = listOf("thoughts", "change", "world", "mindset"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -46,
            quote = "To improve is to change; to be perfect is to change often.",
            author = "Winston Churchill",
            length = 59,
            tags = listOf("improvement", "change", "perfection"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -47,
            quote = "The roots of education are bitter, but the fruit is sweet.",
            author = "Aristotle",
            length = 59,
            tags = listOf("education", "learning", "perseverance"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -48,
            quote = "A creative man is motivated by the desire to achieve, not by the desire to beat others.",
            author = "Ayn Rand",
            length = 90,
            tags = listOf("creativity", "achievement", "motivation"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -49,
            quote = "Our greatest glory is not in never failing, but in rising up every time we fail.",
            author = "Ralph Waldo Emerson",
            length = 84,
            tags = listOf("glory", "failure", "resilience", "rising"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -50,
            quote = "Do not go where the path may lead, go instead where there is no path and leave a trail.",
            author = "Ralph Waldo Emerson",
            length = 90,
            tags = listOf("path", "trailblazing", "individuality", "leadership"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -51,
            quote = "The only person you are destined to become is the person you decide to be.",
            author = "Ralph Waldo Emerson",
            length = 76,
            tags = listOf("destiny", "choice", "self-determination"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -52,
            quote = "Life is 10% what happens to us and 90% how we react to it.",
            author = "Charles R. Swindoll",
            length = 63,
            tags = listOf("life", "reaction", "attitude"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -53,
            quote = "What you get by achieving your goals is not as important as what you become by achieving your goals.",
            author = "Zig Ziglar",
            length = 106,
            tags = listOf("goals", "achievement", "self-improvement", "becoming"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -54,
            quote = "If you are not willing to risk the usual, you will have to settle for the ordinary.",
            author = "Jim Rohn",
            length = 84,
            tags = listOf("risk", "ordinary", "extraordinary", "settling"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        ),
        Quote(
            id = -55,
            quote = "Go confidently in the direction of your dreams! Live the life you've imagined.",
            author = "Henry David Thoreau",
            length = 80,
            tags = listOf("dreams", "confidence", "imagination", "life"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        )
    )

    fun getRandomFallbackQuote(): Quote? {
        if (staticQuotes.isEmpty()) {
            return null // Or a default "error" quote
        }
        val random = Random()
        return staticQuotes[random.nextInt(staticQuotes.size)]
    }

    // You could also add a default quote to return if the list is somehow empty
    // or if an error occurs during random selection, though less likely with a static list.
    fun getDefaultErrorQuote(): Quote {
        return Quote(
            id = -999,
            quote = "Oops! Something went wrong, but here's a bit of wisdom: Even errors can teach us something.",
            author = "The Universe",
            length = 100, // Approximate length
            tags = listOf("error", "wisdom"),
            isFavorite = false,
            isUserMade = false,
            timestamp = System.currentTimeMillis()
        )
    }
}