## Инициализация SDK
### Шаг 1. В Application на onCreate прописываем installJazzPublicSdk():

```kotlin
 // Устанавливаем необходимые зависимости для Jazz
installJazzPublicSdk(
    jazzConfig = JazzSdk.JazzConfig(
        // Здесь устанавливаем платформенные зависимости Jazz
        platformDependencies = DefaultJazzSdkPlatformDependencies()
    ),
    coreConfig = JazzSdk.CoreConfig(
        context = applicationContext,
        analyticsDependencies = object : JazzCoreAnalyticsDependencies {},
        loggingDependencies = JazzCoreLoggingDependencies(
            jazzLogMode = JazzLoggerFactory.LogMode.LOG_ALWAYS
        ),
    ),
)
 ```


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