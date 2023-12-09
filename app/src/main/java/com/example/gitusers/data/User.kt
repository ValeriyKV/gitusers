package com.example.gitApp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")                var login : String? = null,
    @SerializedName("id")                   var id : Int? = null,
    @SerializedName("node_id")              var nodeId: String? = null,
    @SerializedName("avatar_url")           var avatarUrl: String? = null,
    @SerializedName("gravatar_id")          var gravatarId: String? = null,
    @SerializedName("url")                  var url: String? = null,
    @SerializedName("html_url")             var htmlUrl: String? = null,
    @SerializedName("followers_url")        var followersUrl: String? = null,
    @SerializedName("following_url")        var followingUrl: String? = null,
    @SerializedName("gists_url")            var gistsUrl: String? = null,
    @SerializedName("starred_url")          var starredUrl: String? = null,
    @SerializedName("received_events_url")  var receivedEventsUrl: String? = null,
    @SerializedName("type")                 var type: String? = null,
    @SerializedName("site_admin")           var siteAdmin: Boolean? = null,
    @SerializedName("name")                 var name: String? = null,
    @SerializedName("company")              var company: String? = null,
    @SerializedName("blog")                 var blog: String? = null,
    @SerializedName("location")             var location: String? = null,
    @SerializedName("email")                var email: String? = null,
    @SerializedName("hireable")             var hireable: String? = null,
    @SerializedName("bio")                  var bio: String? = null,
    @SerializedName("twitter_username")     var twitterUsername: String? = null,
    @SerializedName("public_repos")         var publicRepos: Int? = null,
    @SerializedName("public_gists")         var publicGists: Int? = null,
    @SerializedName("followers")            var followers: Int? = null,
    @SerializedName("following")            var following: Int? = null,
    @SerializedName("created_at")           var created_at: String? = null,
    @SerializedName("updated_at")           var updated_at: String? = null,
                                            var follow : Boolean = false
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeValue(id)
        parcel.writeString(nodeId)
        parcel.writeString(avatarUrl)
        parcel.writeString(gravatarId)
        parcel.writeString(url)
        parcel.writeString(htmlUrl)
        parcel.writeString(followersUrl)
        parcel.writeString(followingUrl)
        parcel.writeString(gistsUrl)
        parcel.writeString(starredUrl)
        parcel.writeString(receivedEventsUrl)
        parcel.writeString(type)
        parcel.writeValue(siteAdmin)
        parcel.writeString(name)
        parcel.writeString(company)
        parcel.writeString(blog)
        parcel.writeString(location)
        parcel.writeString(email)
        parcel.writeString(hireable)
        parcel.writeString(bio)
        parcel.writeString(twitterUsername)
        parcel.writeValue(publicRepos)
        parcel.writeValue(publicGists)
        parcel.writeValue(followers)
        parcel.writeValue(following)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeBoolean(follow)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun isEmpty(): Boolean {
        return url == null
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}