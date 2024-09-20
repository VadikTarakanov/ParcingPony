package twine.resourceprovider

import org.jetbrains.compose.resources.StringResource

interface ResourceProvider {
  suspend  fun getStringValue(id: StringResource) : String
}