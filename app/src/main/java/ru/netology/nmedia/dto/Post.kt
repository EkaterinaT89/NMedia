package ru.netology.nmedia.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import ru.netology.nmedia.enum.AttachmentType


@Parcelize

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    var likedByMe: Boolean,

    @SerializedName("likes")
    var likesCount: Long = 0,

    @SerializedName("published")
    val date: String,

    var shareCount: Long = 0,
    var video: String? = null,
    val authorAvatar: String = "",
    var attachment: Attachment? = null,
    var show: Boolean

) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        author = parcel.readString().toString(),
//        parcel.readLong(),
        content = parcel.readString().toString(),
        likedByMe = parcel.readByte() != 0.toByte(),
        likesCount = parcel.readLong(),
        date = parcel.readString().toString(),
        shareCount = parcel.readLong(),
//        parcel.readByte() != 0.toByte(),
        video = parcel.readString(),
        authorAvatar = parcel.readString().toString(),
//      parcel.readLong(),
//        parcel.readString(),
//        parcel.readString().toString(),
        show = parcel.readByte() != 0.toByte()
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



