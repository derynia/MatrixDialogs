object BuildPlugins {
    val firebase by lazy { "com.google.gms:google-services:${Versions.fireBaseVersion}" }
    val android by lazy { "com.android.tools.build:gradle:${Versions.gradlePlugin}" }
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
    val hilt by lazy {"com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"}
}

/**
 * To define dependencies
 */
object Deps {
    // Core
    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.ktx}" }
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}" }
    val coroutines by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}" }

    // firebase
    val firebaseBom by lazy {"com.google.firebase:firebase-bom:${Versions.fireBaseBOMVersion}"}
    val analytics by lazy {"com.google.firebase:firebase-analytics-ktx"}
    val googleAuth by lazy { "com.google.android.gms:play-services-auth:${Versions.playServicesAuth}" }

    // View
    val viewBinding by lazy {"androidx.databinding:viewbinding:${Versions.viewBinding}"}
    val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.appcompat}" }
    val materialDesign by lazy { "com.google.android.material:material:${Versions.material}" }
    val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}" }
    val navigationFragmentKtx by lazy { "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}" }
    val navigationUiKtx by lazy { "androidx.navigation:navigation-ui-ktx:${Versions.navigation}" }

    // Test
    val extJunit by lazy { "androidx.test.ext:junit:${Versions.extJunit}" }
    val espresso by lazy { "androidx.test.espresso:espresso-core:${Versions.espresso}" }
    val junit by lazy { "junit:junit:${Versions.junit}" }

    // Hilt
    val hilt by lazy {"com.google.dagger:hilt-android:${Versions.hilt}"}
    val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hilt}" }
}