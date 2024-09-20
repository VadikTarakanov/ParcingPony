package twine.di

import twine.data.TimerRepository
import twine.resourceprovider.ResourceProvider
import twine.resourceprovider.ResourceProviderImpl

class CommonDiComponent {

    companion object {
        private var timerRepository: TimerRepository? = null

        private var resourceProvider: ResourceProvider? = null

        fun getTimeRepository(): TimerRepository {
            return if (timerRepository == null) {
                timerRepository = TimerRepository()
                timerRepository!!
            } else {
                timerRepository!!
            }
        }

        fun getResourceProvider(): ResourceProvider {
            return if (resourceProvider == null) {
                resourceProvider = ResourceProviderImpl()
                resourceProvider!!
            } else {
                resourceProvider!!
            }
        }
    }
}