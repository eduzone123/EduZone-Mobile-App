package com.example.data.repository

data class LessonQuiz(
    val lessonId: Int,
    val title: String,
    val explanation: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val successExplanation: String
)

object CourseLessonsData {
    fun getLessonsForCourse(courseId: Int): List<LessonQuiz> {
        return when (courseId) {
            1 -> listOf( // Kotlin Basics
                LessonQuiz(
                    1,
                    "Variables and val vs var",
                    "In Kotlin, we declare variables using two main keywords. `val` creates an immutable (read-only) variable whose reference cannot be changed once assigned. `var` creates a mutable variable that can be reassigned later.",
                    "Which keyword is used to define an immutable (read-only) variable in Kotlin?",
                    listOf("var", "val", "const", "let"),
                    1,
                    "Awesome! val makes variables read-only. Think of it as 'value'."
                ),
                LessonQuiz(
                    2,
                    "Kotlin Null Safety",
                    "Kotlin's type system is designed to eliminate NullPointerExceptions. By default, variables are non-nullable. If you want to allow null values, append a ? to the type. To invoke a property safely on a nullable value, use the safe call operator.",
                    "Which operator performs a safe call in Kotlin, returning null instead of throwing an exception?",
                    listOf("!!", "?", "?.", "?:"),
                    2,
                    "Spot on! The safe call operator ?. only executes the call if the object is non-null."
                ),
                LessonQuiz(
                    3,
                    "Functions and the Unit Type",
                    "Functions are defined using the `fun` keyword. When a function executes actions without returning a specific data value, its return type is Unit. Unlike void in other languages, Unit is a real object.",
                    "What is the return type of a Kotlin function that doesn't return any meaningful value?",
                    listOf("void", "Null", "Unit", "Nothing"),
                    2,
                    "Correct! Unit is the equivalent of void, but it is an actual Singleton object."
                ),
                LessonQuiz(
                    4,
                    "Singletons and the object Keyword",
                    "Kotlin provides a very clean syntax to declare Singletons. Instead of writing a class with standard static methods and private constructors, you simply use the `object` keyword.",
                    "Which keyword declares an elegant Singleton instance directly in Kotlin?",
                    listOf("singleton", "object", "static", "class"),
                    1,
                    "Perfect! The object keyword declares a class and creates a single instance of it concurrently."
                )
            )
            2 -> listOf( // Jetpack Compose
                LessonQuiz(
                    1,
                    "Declarative Interfaces",
                    "Jetpack Compose uses a declarative paradigm. You build UI by defining a set of composable functions that transform data into visual components. Every composables must be annotated appropriately so the compiler can process them.",
                    "Which annotation is mandatory for every Compose UI function?",
                    listOf("@Compose", "@Composable", "@UI", "@Component"),
                    1,
                    "Correct! @Composable tells the Kotlin compiler plugin that this function is for building layouts."
                ),
                LessonQuiz(
                    2,
                    "Hoisting State with remember",
                    "Recomposition runs composable functions again when data changes. To prevent local variables from being recreated on every recomposition, you must wrap them with a special caching block.",
                    "Which helper function is used inside composables to remember state values across recompositions?",
                    listOf("remember", "mutableStateOf", "rememberSaveable", "deriveState"),
                    0,
                    "Yes! 'remember' stores values inside the composition tree so they survive recomposition."
                ),
                LessonQuiz(
                    3,
                    "Modifiers and Layouts",
                    "Modifiers allow you to decorate or augment a composable. You can set heights, widths, backgrounds, paddings, and add click listeners in a chained, type-safe builder.",
                    "Which Modifier method expands a component to occupy all available horizontal layout width?",
                    listOf("Modifier.fillWidth()", "Modifier.fillMaxWidth()", "Modifier.width(MAX)", "Modifier.expandHorizontally()"),
                    1,
                    "Spot on! Modifier.fillMaxWidth() makes the view occupy the maximum boundary width."
                ),
                LessonQuiz(
                    4,
                    "Efficient Lists with LazyColumn",
                    "For large scrollable feeds, standard Columns would load all elements at once, degrading performance. To build highly responsive recycling feeds, Compose provides dedicated Lazy components.",
                    "Which Compose component acts similarly to RecyclerView, loading items only as they scroll into view?",
                    listOf("ScrollView", "LazyColumn", "Column", "LazyRow"),
                    1,
                    "Excellent! LazyColumn is the idiomatic list implementation for efficient scrolling datasets."
                )
            )
            3 -> listOf( // Python Basics
                LessonQuiz(
                    1,
                    "Dynamic Typing & Variables",
                    "Python is dynamically typed. This means you do not declare variable types explicitly before using them; Python infers the type at runtime based on the value assigned.",
                    "How do you assign the integer 5 to a variable x in Python?",
                    listOf("int x = 5", "x := 5", "x = 5", "var x = 5"),
                    2,
                    "Super simple and correct! Python variables are initialized with a single equals sign."
                ),
                LessonQuiz(
                    2,
                    "Lists and Indexing",
                    "Python Lists are ordered, mutable collections of items. You can reference elements using bracket-indexing, which starts at index 0. You can also index from the back using negative offsets.",
                    "What index refers to the absolute last item in a Python list?",
                    listOf("list[0]", "list[-1]", "list[len(list)]", "list.last()"),
                    1,
                    "Correct! list[-1] is Python's elegant shorthand to fetch the last element of a list."
                ),
                LessonQuiz(
                    3,
                    "Loops and Iteration",
                    "Python uses simple syntax to traverse collections without index counters. The `for...in` loop binds each element sequence member directly inside the block.",
                    "Which keyword iterates directly over elements in collections or a range?",
                    listOf("foreach", "for", "while", "loop"),
                    1,
                    "Correct! Python uses standard for loops combined with the 'in' membership statement."
                ),
                LessonQuiz(
                    4,
                    "Defining Functions",
                    "Functions in Python are defined with a short keyword representing definition. Code block indentation determines function scope instead of curly braces.",
                    "Which keyword starts a function definition in Python?",
                    listOf("func", "function", "def", "define"),
                    2,
                    "Perfect! Python uses 'def' followed by the function name and a colon."
                )
            )
            4 -> listOf( // JavaScript
                LessonQuiz(
                    1,
                    "Variables: let vs const vs var",
                    "Modern JavaScript (ES6) recommends using block-scoped variables instead of legacy function-scoped variables. Use const for references that won't be reassigned, and let for mutable ones.",
                    "Which modern keyword declares a block-scoped mutable variable in JavaScript?",
                    listOf("var", "let", "const", "def"),
                    1,
                    "Spot on! 'let' is block-scoped and lets you update the values safely."
                ),
                LessonQuiz(
                    2,
                    "Arrow Functions",
                    "Arrow functions provide a shorter syntax to express functional callbacks and naturally bind the parent standard lexical execution context of 'this'.",
                    "Which symbol represents an arrow declaration in JavaScript?",
                    listOf("->", "=>", "==>", "=>>"),
                    1,
                    "Correct! The => symbol defines modern arrow lambda expressions."
                ),
                LessonQuiz(
                    3,
                    "React Hooks & state",
                    "React components re-render when their states or props change. functional components declare state variables using hooks to hook into core React lifecycle APIs.",
                    "Which hook is standard to declare local states in React functional components?",
                    listOf("useState", "useEffect", "useRef", "useAction"),
                    0,
                    "Spot on! useState is the fundamental hook used to trigger visual state reactions."
                ),
                LessonQuiz(
                    4,
                    "Asynchronous Promises",
                    "JavaScript uses single-threaded event loops. Long Operations (like API fetches) are handled asynchronously using Promises and modern declarative syntax.",
                    "Which keyword is prefixed before a function definition to allow the usage of the 'await' keyword?",
                    listOf("async", "defer", "promise", "thread"),
                    0,
                    "Perfect! An async function always returns a Promise, allowing cleaner sequential await calls."
                )
            )
            else -> listOf( // AI Systems
                LessonQuiz(
                    1,
                    "Large Language Model Basics",
                    "Large Language Models (LLMs) are deep-learning models trained on massive text corpora to predict the next token given a context sequence of preceding tokens.",
                    "What is the basic unit of input text parsed and resolved by an LLM called?",
                    listOf("Word", "Character", "Token", "Pixel"),
                    2,
                    "Spot on! Text is parsed into numeric fractions called tokens, which are smaller than words."
                ),
                LessonQuiz(
                    2,
                    "System Instructions",
                    "System instructions set the tone, goals, guardrails, and context persona of the assistant. They take absolute priority and guide all downstream conversational bounds.",
                    "Which system role defines the background context, guidelines, and behavioral rules for an LLM?",
                    listOf("user", "system", "assistant", "instructor"),
                    1,
                    "Correct! The system role configures master behaviors and guardrails."
                ),
                LessonQuiz(
                    3,
                    "Few-Shot Prompting",
                    "Few-shot prompting is a technique where you supply explicit training example trials of inputs and desired outputs in your prompt to show the model how to reply.",
                    "Providing 2-3 input-output examples in a prompt to show the model how to reply is called which technique?",
                    listOf("Zero-shot", "Few-shot", "Fine-tuning", "Chain-of-thought"),
                    1,
                    "Excellent! Few-shot prompting provides demonstrations to align predictions without weights training."
                ),
                LessonQuiz(
                    4,
                    "Structured Outputs",
                    "Modern enterprise AI pipelines require standard parsed outputs instead of arbitrary conversational strings. This optimizes machine-to-machine exchange.",
                    "Which string format is widely used for structured data exchange with model API responses?",
                    listOf("XML", "JSON", "YAML", "TXT"),
                    1,
                    "Perfect! JSON schema constraints guarantee beautiful, robust typed APIs."
                )
            )
        }
    }
}
