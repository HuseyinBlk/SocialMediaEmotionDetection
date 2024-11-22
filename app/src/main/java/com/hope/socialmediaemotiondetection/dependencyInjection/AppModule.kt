package com.hope.socialmediaemotiondetection.dependencyInjection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.repository.AuthRepository
import com.hope.socialmediaemotiondetection.repository.EmotionRepository
import com.hope.socialmediaemotiondetection.repository.PostCommentRepository
import com.hope.socialmediaemotiondetection.repository.PostLikesRepository
import com.hope.socialmediaemotiondetection.repository.PostRepository
import com.hope.socialmediaemotiondetection.repository.UserCommentsRepository
import com.hope.socialmediaemotiondetection.repository.UserDailyEmotionRepository
import com.hope.socialmediaemotiondetection.repository.UserFollowsRepository
import com.hope.socialmediaemotiondetection.repository.UserLikedPostRepository
import com.hope.socialmediaemotiondetection.repository.UserRepository
import com.hope.socialmediaemotiondetection.service.EmotionApiService
import com.hope.socialmediaemotiondetection.utils.Constants.BASE_URL_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
BU MODULE VE YAPI KÜTÜPHANE TAMAMEN BAĞIMLILIKLARI KOLAYLAŞTIRMAYA YARIYOR MİSAL DİYELİM APİ CLASSIM VAR
VE BU CLASS SÜREKLİ OLUŞTURMA DERDİM VAR BURAYA ONLARI BELİRTİRSEM HİLT BUNU KOLAYCA ARADA KÖPRÜ OLUŞTURUP
TANIMLAR
@SİNGLETON İLE TÜM UYGULAMADA TEK BİR TANE OLUŞTURMAYA YARA BUDA BAYA FAYDALI OLUYOR VERİMLİ OLUYOR
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth,firestore: FirebaseFirestore):AuthRepository{
        return AuthRepository(auth,firestore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): UserRepository {
        return UserRepository(auth, firestore)
    }

    @Provides
    @Singleton
    fun providePostCommentRepository(auth: FirebaseAuth,firestore: FirebaseFirestore): PostCommentRepository{
        return PostCommentRepository(auth,firestore)
    }

    @Provides
    @Singleton
    fun providePostLikesRepository(auth: FirebaseAuth,firestore: FirebaseFirestore) : PostLikesRepository{
        return PostLikesRepository(auth,firestore)
    }

    @Provides
    @Singleton
    fun providePostRepository(auth: FirebaseAuth,firestore: FirebaseFirestore) : PostRepository{
        return PostRepository(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideUserCommentsRepository(auth: FirebaseAuth,firestore: FirebaseFirestore) : UserCommentsRepository{
        return UserCommentsRepository(auth,firestore)
    }

    @Provides
    @Singleton
    fun provideDailyEmotionRepository(auth: FirebaseAuth,firestore: FirebaseFirestore) : UserDailyEmotionRepository{
        return UserDailyEmotionRepository(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideUserFollowsRepository(auth: FirebaseAuth,firestore: FirebaseFirestore) : UserFollowsRepository{
        return UserFollowsRepository(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideUserLikedPostRepository(auth: FirebaseAuth,firestore: FirebaseFirestore) : UserLikedPostRepository{
        return UserLikedPostRepository(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            /*
            EĞER API BOZULDUYSA YEREL İLE DEĞİŞTİRİN
            */
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideEmotionApiService(retrofit: Retrofit): EmotionApiService {
        return retrofit.create(EmotionApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideEmotionRepository(apiService: EmotionApiService): EmotionRepository {
        return EmotionRepository(apiService)
    }
}