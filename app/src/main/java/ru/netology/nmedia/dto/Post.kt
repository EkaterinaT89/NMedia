package ru.netology.nmedia.dto

import android.os.Parcel
import android.os.Parcelable
import ru.netology.nmedia.enum.AttachmentType


@Parcelize

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    var likedByMe: Boolean,
    var likesCount: Long = 0,
    val date: String,
    var shareCount: Long = 0,
    var video: String? = null,
    val authorAvatar: String = "",
    var attachment: Attachment? = null

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(author)
        parcel.writeString(content)
        parcel.writeByte(if (likedByMe) 1 else 0)
        parcel.writeLong(likesCount)
        parcel.writeString(date)
        parcel.writeLong(shareCount)
        parcel.writeString(video)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }

}

annotation class Parcelize

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
)


