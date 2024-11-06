package com.hope.socialmediaemotiondetection.viewmodel

import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.follows.UserFollowing
import com.hope.socialmediaemotiondetection.repository.AuthRepository
import com.hope.socialmediaemotiondetection.repository.PostRepository
import com.hope.socialmediaemotiondetection.repository.UserFollowsRepository
import com.hope.socialmediaemotiondetection.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository,
    private val userFollowsRepository: UserFollowsRepository
) : ViewModel() {

    private val _postResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val postResult : StateFlow<Resource<Boolean>> get() = _postResult

    private val _getAllPostResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val getAllPostResult : StateFlow<Resource<Boolean>> get() = _getAllPostResult

    private val _getAllPost = MutableStateFlow<Resource<Map<String,List<Post>>>>(Resource.Loading())
    val getAllPost : StateFlow<Resource<Map<String,List<Post>>>> get() = _getAllPost


    fun addPost(emotion : String,content : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = postRepository.addPost(emotion,content)
                _postResult.value = if (result.isSuccess){
                    Resource.Success(true)
                }else{
                    Resource.Failure("Post Atılamadı")
                }
            }catch (e :Exception){
                _postResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    private fun fetchPostsForAllUsers(userIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postsMap = mutableMapOf<String, List<Post>>()
                val postsList = userIds.map { userId ->
                    async {
                        val usernameResult = userRepository.getUsernameByUserId(userId)
                        val postsResult = postRepository.getPostsByUser(userId)

                        if (usernameResult.isSuccess && postsResult.isSuccess) {
                            val username = usernameResult.getOrNull()
                            val posts = postsResult.getOrNull()
                            if (username != null && posts != null) {
                                username to posts
                            } else {
                                null
                            }
                        } else {
                            null
                        }
                    }
                }.awaitAll()

                postsList.filterNotNull().forEach { (username, posts) ->
                    postsMap[username] = posts
                }
                _getAllPost.value = Resource.Success(postsMap)

            } catch (e: Exception) {
                _getAllPost.value = Resource.Failure(e.message ?: "An error occurred")
            }
        }
    }
    fun getFollowingAllPost() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _getAllPost.value = Resource.Loading()
                val result = authRepository.currentUser()?.let { user ->
                    userFollowsRepository.getFollowingList(user.uid)
                }
                result?.onSuccess { listOfUserIds ->
                    fetchPostsForAllUsers(listOfUserIds)
                }?.onFailure { exception ->
                    println("Error fetching following list: ${exception.message}")
                }

            } catch (e: Exception) {
                println("Exception: $e")
            }
        }
    }
}