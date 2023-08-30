package com.sd.jazz_testapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sd.jazz_testapp.databinding.ActivityMainBinding
import com.sdkit.jazz.client.integration.api.model.AudioDevice
import com.sdkit.jazz.client.integration.api.model.CreateVideoCallArguments
import com.sdkit.jazz.client.integration.api.model.JoinVideoCallArguments
import com.sdkit.jazz.client.integration.api.model.ScheduledConferenceResult
import com.sdkit.jazz.sdk.utils.getJazzIntegrationClientApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createConferenceButton.setOnClickListener {
            getJazzIntegrationClientApi().jazzIntegrationClient.createConference(
                arguments = CreateVideoCallArguments(
                    roomType = "MEETING",
                    roomName = "Новая видеовстреча",
                    userName = "Пользователь",
                    cameraEnabled = false,
                    micEnabled = false,
                    // Закрытая или открытая встреча
                    withGuests = true,
                    // Встреча с комнатой ожидания
                    lobbyEnabled = false,
                    autoRecord = null,
                    audioDevice = AudioDevice.DEFAULT,
                ),
            )
        }

        binding.scheduleJoinConferenceButton.setOnClickListener {
            scheduleAndJoinConference()
        }
    }

    fun scheduleAndJoinConference() {
        lifecycleScope.launchWhenResumed {
            val scheduled = withContext(Dispatchers.IO) {
                getJazzIntegrationClientApi().jazzIntegrationClient.scheduleConference(
                    roomType = "MEETING",
                    name = "Новая видеовстреча",
                    // Закрытая или открытая встреча
                    withGuests = true,
                    // Встреча с комнатой ожидания
                    lobbyEnabled = false,
                    autoRecord = false,
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
                    getJazzIntegrationClientApi().jazzIntegrationClient.joinConference(joinArgs)
                }
                is ScheduledConferenceResult.Error -> {
                    val message = "Title: ${scheduled.title}, description: ${scheduled.description}"
                    Log.e("joinConference", message, scheduled.throwable)
                }
            }
        }
    }
}