## Сценарии использования Jazz SDK

<details><summary>Простой сценарий создания встречи и присоединения к ней</summary>

```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createConferenceButton.setOnClickListener {
            getJazzIntegrationClientApi().jazzIntegrationClient.createConference()
        }
    }
}
```
</details>

<details><summary>Сценарий создания запланированной встречи и присоединения к ней</summary>

```kotlin
fun scheduleAndJoinConference() {
    lifecycleScope.launchWhenResumed {
        val scheduled = withContext(Dispatchers.IO) {
            JazzSdk.getIntegrationClientApi().jazzIntegrationClient.scheduleConference(
                roomType = RoomType.Anonymous.value,
                name = "Новая видеовстреча",
                // Закрытая или открытая встреча
                withGuests = false,
                // Встреча с комнатой ожидания
                lobbyEnabled = false
            )
        }

        when (scheduled) {
            is ScheduledConferenceResult.Success -> {
                // Сохраняем запланированую встречу
                // Можем разослать ее всем участникам
                // Выполняем присоединение к запланированной встрече
                val joinArgs = JoinVideoCallArguments(
                    userName = "Имя участника",
                    roomCode = scheduled.conference.code,
                    password = scheduled.conference.password,
                    micEnabled = false,
                    cameraEnabled = false
                )
                JazzSdk.getIntegrationClientApi().jazzIntegrationClient.joinConference(joinArgs)
            }
            is ScheduledConferenceResult.Error -> {
                val message = "Title: ${scheduled.title}, description: ${scheduled.description}"
                Log.e("joinConference", message , scheduled.throwable)
            }
        }
    }
}
```

</details>