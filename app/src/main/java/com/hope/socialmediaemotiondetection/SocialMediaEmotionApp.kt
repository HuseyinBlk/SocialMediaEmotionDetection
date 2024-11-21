package com.hope.socialmediaemotiondetection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
/*
Hilt tanımlarken bu app tanımlamamız lazım zorundayız bir zorunluluk hilt uygulamayı böyle tanıyor
 */
@HiltAndroidApp
class SocialMediaEmotionApp() : Application() {
}