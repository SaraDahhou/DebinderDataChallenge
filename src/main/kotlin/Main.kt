import kotlinx.serialization.*
import kotlinx.serialization.json.*

fun main(args: Array<String>) {
    val inputJson = readLine()!!.trim()
    debinderData(inputJson)
}

fun debinderData(input : String){

    // Decode the input to json into dictionary of ISBN -> List<String>
    val data = Json.decodeFromString<Map<String, List<String>>>(input)

    // Iterate on data and create a new intermediate dictionary with the new format
    val authorDictionary = mutableMapOf<String, MutableList<Book>>()
    data.forEach { t ->
        val book = Book(title = t.value[1], isbn = t.key, text = t.value!!.subList(2, t.value!!.size))
        if (authorDictionary.containsKey(t.value[0])) authorDictionary[t.value[0]]!!.add(book)
        else authorDictionary[t.value[0]] = mutableListOf(book)
    }

    // Sort the books by title for each author
    authorDictionary.forEach { author ->
        author.value.sortBy { it.title }
    }

    // Convert the author Dictionary to a list of authors and sort it by name
    val authorsList = mutableListOf<Author>()
    authorDictionary.forEach { it ->
        authorsList.add(Author(name = it.key, books = it.value))
    }
    authorsList.sortBy { it.name }

    // Encode the authors list to Json and Print it
    println(Json.encodeToString(authorsList))
}

@Serializable
data class Book(val title: String, val isbn: String, val text: List<String>)

@Serializable
data class Author(val name: String, val books: List<Book>)
