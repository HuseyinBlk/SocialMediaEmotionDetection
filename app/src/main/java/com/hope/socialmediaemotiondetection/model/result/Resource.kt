package com.hope.socialmediaemotiondetection.model.result

/*
Aslında Sealed class yalnızca belirli durumların olabileceğini garanti eder
Resource aslında api çekerken
Oluşabilicek hataları ön görüyor misal Succes başarılı olduğunda
Failure Başarısız olduğunda
Loading durumu
Idle başlangıç durumu
Sealed api 4 adet durum olabiliceğini kısıtlar aslında kolaylık ve okunabilirliği artırır
 */

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Failure<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Idle<T> : Resource<T>()
}