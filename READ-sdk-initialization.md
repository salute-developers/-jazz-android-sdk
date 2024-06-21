## Инициализация SDK
### Шаг 1. В Application на onCreate прописываем installJazzPublicSdk():

```kotlin
 // Устанавливаем необходимые зависимости для Jazz
installJazzPublicSdk(
    jazzConfig = JazzSdk.JazzConfig(
        // Здесь устанавливаем платформенные зависимости Jazz
        platformDependencies =  object : JazzPlatformDependencies by DefaultJazzSdkPlatformDependencies() {
            override val videoCallsFeatureFlags: VideoCallsFeatureFlags = JazzSdkFeatures()
            override val jazzTokenProvider = object : JazzTokenProvider {
                override suspend fun getToken(): String? {
                    return jazzSdkTokenProvider.getToken()
                }
            }
        }
    ),
    coreConfig = JazzSdk.CoreConfig(
        context = applicationContext,
        analyticsDependencies = object : JazzCoreAnalyticsDependencies {},
        loggingDependencies = JazzCoreLoggingDependencies(
            jazzLogMode = JazzLoggerFactory.LogMode.LOG_ALWAYS
        ),
    ),
)

private val jazzSdkTokenProvider by lazy {
    JazzSdkTokenProvider(provider = object : JazzTokenConfigurationProvider {
        override fun getConfiguration(): JazzTokenConfiguration {
            return JazzTokenConfiguration(
                userId = "test",
                secretKey = "Ваш секретный ключ",
                liveTimeDurationInSeconds = 180
            )
        }
    })
}
 ```

Секретный ключ jazz sdk получаем по ссылке https://clck.ru/35aWZw


### Шаг 2. Если переопределили WorkerFactory для WorkManager в своем приложении - нужно поддержать JazzWorkerFactory

<details><summary>Как поддержать JazzWorkerFactory</summary>

#### Добавьте фабрику воркеров SDK JazzIntegrationClientApi.jazzWorkerFactory в свою конфигурацию WorkManager. Это можно сделать следующим образом:

```kotlin
   class MyApplication() : Application(), Configuration.Provider {

   override fun getWorkManagerConfiguration(): Configuration {
      val factory = DelegatingWorkerFactory().apply {
         addFactory(appWorkerFactory) // ваша фабрика
         addFactory(getJazzIntegrationClientApi().jazzWorkerFactory) // фабрика JazzSDK
      }
      return Configuration.Builder()
         .setWorkerFactory(factory)
         // здесь могут быть другие настройки
         .build()
   }
}
 ```

</details>

[Сценарии использования](READ-sdk-scenarios.md)