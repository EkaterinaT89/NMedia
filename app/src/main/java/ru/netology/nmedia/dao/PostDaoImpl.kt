package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.SyncStateContract
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    companion object {
        const val TABLE_NAME = "posts"

        val DDL = """
            CREATE TABLE $TABLE_NAME (
            ${Column.Id.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.Author.columnName} TEXT NOT NULL,
            ${Column.Content.columnName} TEXT NOT NULL,
            ${Column.LikedByMe.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.LikesCount.columnName}  INTEGER NOT NULL DEFAULT 0,
            ${Column.Date.columnName} TEXT NOT NULL,
            ${Column.ShareCount.columnName}  INTEGER NOT NULL DEFAULT 0,
            ${Column.Video.columnName} TEXT
            );
            """.trimIndent()
    }

    enum class Column(val columnName: String) {
        Id("id"),
        Author("author"),
        Content("content"),
        LikedByMe("likedByMe"),
        LikesCount("likesCount"),
        Date("date"),
        ShareCount("shareCount"),
        Video("video")
    }


    init {
        Column.values().map { it.columnName }
    }


    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                    likesCount = likesCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                    likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE id = ?;
                """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                  shareCount = shareCount + CASE WHEN shareCount THEN -1 ELSE 1 END
                WHERE id = ?;
                """.trimIndent(), arrayOf(id)
        )
    }

    override fun getAll(): List<Post> {
        var nextId = 1L
        val posts = mutableListOf(Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            date = "21 мая в 18:36",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях",
            date = "18 сентября в 10:12",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Языков программирования много, и выбрать какой-то один бывает нелегко. Собрали подборку статей, которая поможет вам начать, если вы остановили свой выбор на JavaScript.",
            date = "19 сентября в 10:24",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Большая афиша мероприятий осени: конференции, выставки и хакатоны для жителей Москвы, Ульяновска и Новосибирска \uD83D\uDE09",
            date = "19 сентября в 14:12",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Диджитал давно стал частью нашей жизни: мы общаемся в социальных сетях и мессенджерах, заказываем еду, такси и оплачиваем счета через приложения.",
            date = "20 сентября в 10:14",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = " 24 сентября стартует новый поток бесплатного курса «Диджитал-старт: первый шаг к востребованной профессии» — за две недели вы попробуете себя в разных профессиях и определите, что подходит именно вам → http://netolo.gy/fQ",
            date = "21 сентября в 10:12",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Таймбоксинг — отличный способ навести порядок в своём календаре и разобраться с делами, которые долго откладывали на потом. Его главный принцип — на каждое дело заранее выделяется определённый отрезок времени. В это время вы работаете только над одной задачей, не переключаясь на другие. Собрали советы, которые помогут внедрить таймбоксинг \uD83D\uDC47\uD83C\uDFFB",
            date = "22 сентября в 10:12",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Делиться впечатлениями о любимых фильмах легко, а что если рассказать так, чтобы все заскучали ",
            date = "22 сентября в 10:14",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Освоение новой профессии — это не только открывающиеся возможности и перспективы, но и настоящий вызов самому себе. Приходится выходить из зоны комфорта и перестраивать привычный образ жизни: менять распорядок дня, искать время для занятий, быть готовым к возможным неудачам в начале пути. В блоге рассказали, как избежать стресса на курсах профпереподготовки → http://netolo.gy/fPD",
            date = "23 сентября в 10:12",
            likedByMe = false,
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )
    )

        db.query(
            TABLE_NAME,
            Column.values().map { it.columnName }.toTypedArray(),
            null,
            null,
            null,
            null,
            "${Column.Id.columnName} DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                posts.add(map(cursor))
            }
        }

        return posts
    }

    override fun removeById(id: Long) {
        db.delete(
            TABLE_NAME,
            "${Column.Id.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if(post.id !=0L) {
                put(Column.Id.columnName, post.id)
            }
            put(Column.Author.columnName, post.author)
            put(Column.Content.columnName, post.content)
            put(Column.LikedByMe.columnName, post.likedByMe)
            put(Column.LikesCount.columnName, post.likesCount)
            put(Column.Date.columnName, post.date)
            put(Column.ShareCount.columnName, post.shareCount)
            put(Column.Video.columnName, post.video)
        }
        val id = db.replace(TABLE_NAME, null, values)
        db.query(
            TABLE_NAME,
            Column.values().map { it.columnName }.toTypedArray(),
            "${Column.Id.columnName} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }

    }

    override fun video() {

    }

    private fun map(cursor: Cursor) = with(cursor) {
        Post(
            id = getLong(getColumnIndexOrThrow(Column.Id.columnName)),
            author = getString(getColumnIndexOrThrow(Column.Author.columnName)),
            content = getString(getColumnIndexOrThrow(Column.Content.columnName)),
            likedByMe = getInt(getColumnIndexOrThrow(Column.LikedByMe.columnName)) != 0,
            likesCount = getLong(getColumnIndexOrThrow(Column.LikesCount.columnName)),
            shareCount = getLong(getColumnIndexOrThrow(Column.ShareCount.columnName)),
            date = getString(getColumnIndexOrThrow(Column.Date.columnName)),
            video = getString(getColumnIndexOrThrow(Column.Video.columnName)),
        )
    }
}