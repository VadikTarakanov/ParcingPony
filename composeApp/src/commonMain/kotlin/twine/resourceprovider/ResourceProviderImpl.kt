package twine.resourceprovider

import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

class ResourceProviderImpl : ResourceProvider {

    override suspend fun getStringValue(id: StringResource): String {
       return getString(id)
    }
}