[Основную документацию можно найти тут](https://clck.ru/35aWZB)

[Что бы создать комнату, для начала получите ключ авторизации тут](https://clck.ru/35aWZw)

[Инициализация](READ-sdk-initialization.md)

[Сценарии использования](READ-sdk-scenarios.md)

[Лицензия на использование](https://clck.ru/35F8h3)

## Как подключить JAZZ SDK
### 1 Шаг. В settings.gradle, аналогично заполняем блок repositories, обязательно не забыть про jcenter():
```groovy
dependencyResolutionManagement {
   repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
   repositories {
     // Здесь указан репозиторий с которого можно скачать все необходимые зависимости,
     // что бы не делать excludeGroup достаточно поставить наш репозиторий выше других
     maven { url "https://jazz-sdk.nx.s2b.tech/maven2/" }

     google() {
       content { excludeGroup("com.facebook.react") }
     }
     mavenCentral() {
       content { excludeGroup("com.facebook.react") }
     }
     jcenter() {
       content { excludeGroup("com.facebook.react") }
     }
   }
}
```

### 2 Шаг. Добавляем необходимые опции в build.gradle в модуле app перед sync project:

* Проверяем блок plugins, в нем должны быть необходимые плагины, как на скрине ниже:
```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}
```
* Дополняем блок Android:
  * Указываем compileSdk, должен быть выше 30
  * Подключаем viewBinding и мержим ресурсы(Как на скрине ниже)
```groovy
    buildFeatures {
        viewBinding true
    }
    packagingOptions {
        pickFirst '**/*.so'
        pickFirst 'META-INF/kotlinx_coroutines_core.version'
    }
 ```
* Дополняем блок dependencies:
```groovy
    //Jazz SDK
    implementation("com.sdkit.jazz:jazz-public-sdk:24.03.1.98")
    implementation(platform("com.sdkit.jazz:jazz-public-bom:24.03.1.98"))
    //endregion
 ```

#### Дополнительный материал:

[Инициализация](READ-sdk-initialization.md)

[Сценарии использования](READ-sdk-scenarios.md)