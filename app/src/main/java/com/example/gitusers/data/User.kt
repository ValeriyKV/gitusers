package com.example.gitApp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    fun isEmpty(): Boolean {
        return url == null
    }
}