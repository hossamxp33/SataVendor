package com.example.satadelivery.helper


import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 * Lazy ViewModel Factory to be used for scopes and subscopes.
 *
 * @author juan.saravia
 */
class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown model class: $modelClass")
        }
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Module
abstract class ViewModelBuilderModule {

    @Binds
    abstract fun bindViewModelFactory(
        factory: ViewModelFactory // Extends ViewModelProvider.Factory
    ): ViewModelProvider.Factory // Android Lifecycle ViewModel


}

// This key will enforce using a class that extends ViewModel
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

class MyFragmentFactory @Inject constructor(
    private val creator: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        return creator[fragmentClass]?.get() ?: super.instantiate(classLoader, className)
    }

}

@Module
abstract class FragmentFactoryModule {

    @Binds
    abstract fun bindFragmentFactroy(factory: MyFragmentFactory): FragmentFactory

//    @Binds
//    @IntoMap
//    @FragmentKey(HomeFragment::class)
//    abstract fun bindMainFragment(fragment: HomeFragment): Fragment

}

@MapKey
annotation class FragmentKey(val clazz: KClass<out Fragment>)