package com.android.newuplift.utility

object QuoteCache {
    // Map of quotes by category/mood
    private val quotesByTag = mapOf(
        "happiness" to listOf(
            Quote(101, "Happiness is not something ready-made. It comes from your own actions.", "Dalai Lama", 76, listOf("happiness")),
            Quote(102, "The most important thing is to enjoy your life—to be happy—it's all that matters.", "Audrey Hepburn", 80, listOf("happiness")),
            Quote(103, "Happiness is when what you think, what you say, and what you do are in harmony.", "Mahatma Gandhi", 81, listOf("happiness")),
            Quote(104, "Count your age by friends, not years. Count your life by smiles, not tears.", "John Lennon", 74, listOf("happiness")),
            Quote(105, "The purpose of our lives is to be happy.", "Dalai Lama", 40, listOf("happiness")),
            Quote(106, "For every minute you are angry you lose sixty seconds of happiness.", "Ralph Waldo Emerson", 65, listOf("happiness")),
            Quote(107, "Happiness is a warm puppy.", "Charles M. Schulz", 28, listOf("happiness")),
            Quote(108, "The secret of happiness is not in doing what one likes, but in liking what one does.", "James M. Barrie", 82, listOf("happiness")),
            Quote(109, "Be happy for this moment. This moment is your life.", "Omar Khayyam", 46, listOf("happiness")),
            Quote(110, "If you want to be happy, be.", "Leo Tolstoy", 24, listOf("happiness")),
            Quote(111, "The happiness of your life depends upon the quality of your thoughts.", "Marcus Aurelius", 70, listOf("happiness")),
            Quote(112, "True happiness arises, in the first place, from the enjoyment of one's self.", "Joseph Addison", 75, listOf("happiness")),
            Quote(113, "Happiness is not in the mere possession of money; it lies in the joy of achievement.", "Franklin D. Roosevelt", 87, listOf("happiness")),
            Quote(114, "Happiness is a choice that requires effort at times.", "Aeschylus", 50, listOf("happiness")),
            Quote(115, "The best way to cheer yourself up is to try to cheer somebody else up.", "Mark Twain", 69, listOf("happiness")),
            Quote(116, "Whoever is happy will make others happy too.", "Anne Frank", 50, listOf("happiness")),
            Quote(117, "The present moment is filled with joy and happiness. If you are attentive, you will see it.", "Thich Nhat Hanh", 97, listOf("happiness")),
            Quote(118, "Happiness is a journey, not a destination.", "Ben Sweetland", 45, listOf("happiness")),
            Quote(119, "Resolve to keep happy, and your joy and you shall form an invincible host against difficulties.", "Helen Keller", 100, listOf("happiness")),
            Quote(120, "A calm and modest life brings more happiness than the pursuit of success combined with constant restlessness.", "Albert Einstein", 106, listOf("happiness")),
            Quote(121, "If you want to be happy, do not dwell in the past, do not worry about the future, focus on living fully in the present.", "Roy T. Bennett", 112, listOf("happiness")),
            Quote(122, "Happiness lies within your own life, within a single moment.", "Daisaku Ikeda", 65, listOf("happiness")),
            Quote(123, "Happiness is not something far away. It is to be found neither in fame nor in popularity.", "Daisaku Ikeda", 93, listOf("happiness")),
            Quote(124, "The power of finding beauty in the humblest things makes home happy and life lovely.", "Louisa May Alcott", 95, listOf("happiness")),
            Quote(125, "Happiness is the spiritual experience of living every minute with love, grace, and gratitude.", "Denis Waitley", 95, listOf("happiness")),
            Quote(126, "Happiness is when what you think, what you say, and what you do are in harmony.", "Mahatma Gandhi", 81, listOf("happiness")),
            Quote(127, "The secret of happiness is freedom, the secret of freedom is courage.", "Carrie Jones", 65, listOf("happiness")),
            Quote(128, "Happiness is not something ready made. It comes from your own actions.", "Dalai Lama", 76, listOf("happiness")),
            Quote(129, "Happiness is a choice that requires effort at times.", "Aeschylus", 50, listOf("happiness")),
            Quote(130, "Happiness is a warm puppy.", "Charles M. Schulz", 28, listOf("happiness")),
            Quote(131, "The purpose of our lives is to be happy.", "Dalai Lama", 40, listOf("happiness")),
            Quote(132, "If you want to be happy, be.", "Leo Tolstoy", 24, listOf("happiness")),
            Quote(133, "Be happy for this moment. This moment is your life.", "Omar Khayyam", 46, listOf("happiness")),
            Quote(134, "The happiness of your life depends upon the quality of your thoughts.", "Marcus Aurelius", 70, listOf("happiness")),
            Quote(135, "True happiness arises, in the first place, from the enjoyment of one's self.", "Joseph Addison", 75, listOf("happiness"))
        ),
        "motivation" to listOf(
            Quote(301, "It does not matter how slowly you go as long as you do not stop.", "Confucius", 63, listOf("motivation")),
            Quote(302, "The secret of getting ahead is getting started.", "Mark Twain", 49, listOf("motivation")),
            Quote(303, "Don't watch the clock; do what it does. Keep going.", "Sam Levenson", 54, listOf("motivation")),
            Quote(304, "Believe you can and you're halfway there.", "Theodore Roosevelt", 42, listOf("motivation")),
            Quote(305, "Whether you think you can or think you can't, you're right.", "Henry Ford", 58, listOf("motivation")),
            Quote(306, "Your talent determines what you can do. Your motivation determines how much you're willing to do.", "Lou Holtz", 95, listOf("motivation")),
            Quote(307, "The harder you work for something, the greater you'll feel when you achieve it.", "Thomas Jefferson", 78, listOf("motivation")),
            Quote(308, "Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill", 89, listOf("motivation")),
            Quote(309, "Start where you are. Use what you have. Do what you can.", "Arthur Ashe", 58, listOf("motivation")),
            Quote(310, "The only way to do great work is to love what you do.", "Steve Jobs", 55, listOf("motivation")),
            Quote(311, "The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", 70, listOf("motivation")),
            Quote(312, "I have not failed. I've just found 10,000 ways that won't work.", "Thomas Edison", 68, listOf("motivation")),
            Quote(313, "The only limit to our realization of tomorrow will be our doubts of today.", "Franklin D. Roosevelt", 83, listOf("motivation")),
            Quote(314, "You are never too old to set another goal or to dream a new dream.", "C.S. Lewis", 67, listOf("motivation")),
            Quote(315, "If you can dream it, you can do it.", "Walt Disney", 32, listOf("motivation")),
            Quote(316, "Push yourself, because no one else is going to do it for you.", "Unknown", 55, listOf("motivation")),
            Quote(317, "Sometimes later becomes never. Do it now.", "George Herbert", 41, listOf("motivation")),
            Quote(318, "Great things never come from comfort zones.", "Neale Donald Walsch", 45, listOf("motivation")),
            Quote(319, "Dream it. Wish it. Do it.", "Unknown", 26, listOf("motivation")),
            Quote(320, "Success doesn’t just find you. You have to go out and get it.", "Unknown", 61, listOf("motivation")),
            Quote(321, "The harder you work, the luckier you get.", "Gary Player", 45, listOf("motivation")),
            Quote(322, "Don’t stop when you’re tired. Stop when you’re done.", "Marilyn Monroe", 54, listOf("motivation")),
            Quote(323, "Wake up with determination. Go to bed with satisfaction.", "George Lorimer", 60, listOf("motivation")),
            Quote(324, "Do something today that your future self will thank you for.", "Sean Patrick Flanery", 64, listOf("motivation")),
            Quote(325, "Little things make big days.", "Isabel Marant", 36, listOf("motivation")),
            Quote(326, "It’s going to be hard, but hard does not mean impossible.", "Unknown", 54, listOf("motivation")),
            Quote(327, "Don’t wait for opportunity. Create it.", "George Bernard Shaw", 41, listOf("motivation")),
            Quote(328, "Sometimes we’re tested not to show our weaknesses, but to discover our strengths.", "Unknown", 82, listOf("motivation")),
            Quote(329, "The key to success is to focus on goals, not obstacles.", "Unknown", 55, listOf("motivation")),
            Quote(330, "Keep going. Everything you need will come to you at the perfect time.", "Unknown", 70, listOf("motivation")),
            Quote(331, "Doubt kills more dreams than failure ever will.", "Suzy Kassem", 50, listOf("motivation")),
            Quote(332, "Don’t be afraid to give up the good to go for the great.", "John D. Rockefeller", 58, listOf("motivation")),
            Quote(333, "Discipline is doing what needs to be done, even if you don’t want to do it.", "Unknown", 75, listOf("motivation")),
            Quote(334, "Do what you can with all you have, wherever you are.", "Theodore Roosevelt", 60, listOf("motivation")),
            Quote(335, "Act as if what you do makes a difference. It does.", "William James", 55, listOf("motivation"))
        ),
        "lost" to listOf(
            Quote(401, "Not until we are lost do we begin to understand ourselves.", "Henry David Thoreau", 64, listOf("lost")),
            Quote(402, "Sometimes when you lose your way, you find yourself.", "Mandy Hale", 51, listOf("lost")),
            Quote(403, "The way to find yourself is to lose yourself in the service of others.", "Mahatma Gandhi", 69, listOf("lost")),
            Quote(404, "We must be willing to let go of the life we planned so as to have the life that is waiting for us.", "Joseph Campbell", 96, listOf("lost")),
            Quote(405, "When you feel lost, remember that the point of being lost is to find your way again.", "Robert Frost", 79, listOf("lost")),
            Quote(406, "Not all those who wander are lost.", "J.R.R. Tolkien", 35, listOf("lost")),
            Quote(407, "Sometimes you have to get lost to find yourself.", "John Green", 59, listOf("lost")),
            Quote(408, "In the middle of difficulty lies opportunity.", "Albert Einstein", 47, listOf("lost")),
            Quote(409, "When you're feeling lost, take a deep breath and remember why you started.", "Steve Jobs", 58, listOf("lost")),
            Quote(410, "The soul which has no fixed purpose in life is lost.", "Michel de Montaigne", 58, listOf("lost")),
            Quote(411, "Maps can be useful, but sometimes you have to find your own way.", "J.R.R. Tolkien", 35, listOf("lost")),
            Quote(412, "The hardest part about being lost is admitting you have no idea where you're going.", "Toni Morrison", 68, listOf("lost")),
            Quote(413, "If you feel lost, disappointed, hesitant, or weak, return to yourself.", "Amélie Nothomb", 73, listOf("lost")),
            Quote(414, "It's okay to be lost. It just means your journey is about to begin.", "T.S. Eliot", 72, listOf("lost")),
            Quote(415, "Getting lost is not a waste of time; it's a way to find yourself.", "Ralph Waldo Emerson", 65, listOf("lost")),
            Quote(416, "Not all those who wander are lost.", "J.R.R. Tolkien", 35, listOf("lost")),
            Quote(417, "We must be willing to let go of the life we planned, so as to have the life that is waiting for us.", "Joseph Campbell", 96, listOf("lost")),
            Quote(418, "The best way out is always through.", "Robert Frost", 79, listOf("lost")),
            Quote(419, "The way to know life is to love many things.", "Vincent van Gogh", 60, listOf("lost")),
            Quote(420, "In the middle of difficulty lies opportunity.", "Albert Einstein", 47, listOf("lost")),
            Quote(421, "Sometimes you have to get lost to find yourself.", "John Green", 59, listOf("lost")),
            Quote(422, "When you are lost, you are not lost to the world, you are lost to yourself.", "Toni Morrison", 68, listOf("lost")),
            Quote(423, "In the process of letting go, we discover our true selves.", "Pema Chödrön", 75, listOf("lost")),
            Quote(424, "The soul which has no fixed purpose in life is lost.", "Michel de Montaigne", 58, listOf("lost")),
            Quote(425, "You have to get lost before you can be found.", "John Green", 59, listOf("lost")),
            Quote(426, "The world is full of magic things, patiently waiting for our senses to grow sharper.", "W.B. Yeats", 88, listOf("lost")),
            Quote(427, "It is not length of life, but depth of life.", "Ralph Waldo Emerson", 65, listOf("lost")),
            Quote(428, "The only journey is the one within.", "Rainer Maria Rilke", 60, listOf("lost")),
            Quote(429, "Not until we are lost do we begin to understand ourselves.", "Henry David Thoreau", 64, listOf("lost")),
            Quote(430, "What we call the beginning is often the end. And to make an end is to make a beginning. The end is where we start from.", "T.S. Eliot", 72, listOf("lost")),
            Quote(431, "The only way to deal with this life meaningfully is to find one's passion and pursue it with all the enthusiasm one can muster.", "Albert Einstein", 86, listOf("lost")),
            Quote(432, "The unexamined life is not worth living.", "Socrates", 67, listOf("lost")),
            Quote(433, "You have within you right now, everything you need to deal with whatever the world can throw at you.", "Brian Tracy", 95, listOf("lost")),
            Quote(434, "What you get by achieving your goals is not as important as what you become by achieving your goals.", "Zig Ziglar", 81, listOf("lost")),
            Quote(435, "The journey of a thousand miles begins with one step.", "Lao Tzu", 65, listOf("lost"))
        ),

        "bored" to listOf(
            Quote(501, "Boredom always precedes a period of great creativity.", "Robert M. Pirsig", 57, listOf("bored")),
            Quote(502, "The cure for boredom is curiosity. There is no cure for curiosity.", "Dorothy Parker", 72, listOf("bored")),
            Quote(503, "When you're bored, it means that your mind is asking to be challenged.", "Christopher Paolini", 67, listOf("bored")),
            Quote(504, "Boredom is simply the absence of an interesting perspective.", "Brandon A. Trean", 60, listOf("bored")),
            Quote(505, "The man who lets himself be bored is even more contemptible than the bore.", "Samuel Butler", 79, listOf("bored")),
            Quote(506, "Boredom is the feeling that everything is a waste of time; serenity, that nothing is.", "Thomas Szasz", 85, listOf("bored")),
            Quote(507, "The life of the creative man is led, directed and controlled by boredom.", "Saul Bellow", 67, listOf("bored")),
            Quote(508, "Boredom is the root of all evil - the despairing refusal to be oneself.", "Søren Kierkegaard", 77, listOf("bored")),
            Quote(509, "When hit with boredom, let yourself be crushed by it; submerge, hit bottom.", "Andrei Tarkovsky", 79, listOf("bored")),
            Quote(510, "Boredom is the dream bird that hatches the egg of experience.", "Walter Benjamin", 63, listOf("bored")),
            Quote(511, "Against boredom even gods struggle in vain.", "Friedrich Nietzsche", 47, listOf("bored")),
            Quote(512, "Perhaps the world's second-worst crime is boredom; the first is being a bore.", "Cecil Beaton", 75, listOf("bored")),
            Quote(513, "Boredom is the deadliest poison.", "William F. Buckley, Jr.", 37, listOf("bored")),
            Quote(514, "When people are bored, it is primarily with themselves.", "Eric Hoffer", 53, listOf("bored")),
            Quote(515, "I am never bored; to be bored is an insult to oneself.", "Jules Renard", 54, listOf("bored")),
            Quote(516, "Boredom is the feeling that everything is a waste of time; creativity is finding new things to do.", "Albert Einstein", 85, listOf("bored")),
            Quote(517, "The only thing worse than being bored is being bored with yourself.", "Oscar Wilde", 63, listOf("bored")),
            Quote(518, "Boredom is a state of mind that can only be defeated by creative action.", "Michelangelo", 62, listOf("bored")),
            Quote(519, "Boredom is the enemy of creativity.", "Shirley Temple", 88, listOf("bored")),
            Quote(520, "The ability to be bored is the key to creativity.", "David Foster Wallace", 56, listOf("bored")),
            Quote(521, "Boredom is the curse of the creative mind.", "Ralph Waldo Emerson", 75, listOf("bored")),
            Quote(522, "Boredom can be the soil in which creativity flourishes.", "Truman Capote", 72, listOf("bored")),
            Quote(523, "Boredom is the absence of self-reflection.", "Albert Camus", 76, listOf("bored")),
            Quote(524, "Boredom is the price you pay for a full mind.", "Voltaire", 60, listOf("bored")),
            Quote(525, "Boredom is just a mental block between you and your next idea.", "Eleanor Roosevelt", 66, listOf("bored"))
        )

    )

    // Additional tag mappings for UI convenience
    private val tagMappings = mapOf(
        "Happy" to "happiness",
        "Motivated" to "motivation",
        "Lost" to "lost",
        "Bored" to "bored"
    )

    // General quotes to use when tag isn't found
    private val generalQuotes = listOf(
        Quote(701, "Life is what happens when you're busy making other plans.", "John Lennon", 59, listOf("general")),
        Quote(702, "The greatest glory in living lies not in never falling, but in rising every time we fall.", "Nelson Mandela", 92, listOf("general")),
        Quote(703, "In the end, it's not the years in your life that count. It's the life in your years.", "Abraham Lincoln", 84, listOf("general")),
        Quote(704, "The way to get started is to quit talking and begin doing.", "Walt Disney", 64, listOf("general")),
        Quote(705, "If life were predictable it would cease to be life, and be without flavor.", "Eleanor Roosevelt", 79, listOf("general")),
        Quote(706, "The purpose of our lives is to be happy.", "Dalai Lama", 39, listOf("general")),
        Quote(707, "Life is really simple, but we insist on making it complicated.", "Confucius", 60, listOf("general")),
        Quote(708, "Get busy living or get busy dying.", "Stephen King", 35, listOf("general")),
        Quote(709, "You only live once, but if you do it right, once is enough.", "Mae West", 61, listOf("general")),
        Quote(710, "Many of life's failures are people who did not realize how close they were to success when they gave up.", "Thomas A. Edison", 109, listOf("general")),
        Quote(711, "Life is short, and it is up to you to make it sweet.", "Sarah Louise Delany", 58, listOf("general")),
        Quote(712, "Life isn't about finding yourself. Life is about creating yourself.", "George Bernard Shaw", 72, listOf("general")),
        Quote(713, "Life is either a daring adventure or nothing at all.", "Helen Keller", 54, listOf("general")),
        Quote(714, "Life is 10% what happens to us and 90% how we react to it.", "Charles R. Swindoll", 66, listOf("general")),
        Quote(715, "You have within you right now, everything you need to deal with whatever the world can throw at you.", "Brian Tracy", 95, listOf("general"))
    )

    // Tracks used quotes to avoid repetition
    private val usedQuotes = mutableMapOf<String, MutableSet<Int>>()

    // Get a random quote for a specific tag
    fun getRandomQuote(tag: String): Quote {
        // Map UI mood to actual tag if needed
        val mappedTag = tagMappings[tag] ?: tag.lowercase()

        // Try to find quotes for the exact tag
        val quotes = quotesByTag[mappedTag] ?: generalQuotes

        // Initialize tracking for this tag if needed
        if (mappedTag !in usedQuotes) {
            usedQuotes[mappedTag] = mutableSetOf()
        }

        // Reset if all quotes have been used
        if (usedQuotes[mappedTag]!!.size >= quotes.size) {
            usedQuotes[mappedTag]!!.clear()
        }

        // Find an unused quote
        var selectedQuote: Quote
        do {
            selectedQuote = quotes.random()
        } while (selectedQuote.id in usedQuotes[mappedTag]!!)

        // Mark this quote as used
        usedQuotes[mappedTag]!!.add(selectedQuote.id)

        return selectedQuote
    }
}