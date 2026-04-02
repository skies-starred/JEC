package xyz.aerii.jec.config

import com.teamresourceful.resourcefulconfigkt.api.ConfigDelegateProvider
import com.teamresourceful.resourcefulconfigkt.api.RConfigKtEntry
import com.teamresourceful.resourcefulconfigkt.api.builders.EntriesBuilder
import xyz.aerii.jec.handlers.React
import kotlin.reflect.KProperty

fun <T> ConfigDelegateProvider<RConfigKtEntry<T>>.react(): ReactEntry<T> {
    return ReactEntry(this)
}

class ReactEntry<T>(
    private val entry: ConfigDelegateProvider<RConfigKtEntry<T>>,
) : ConfigDelegateProvider<ReactDelegate<T>> {
    override fun provideDelegate(entries: EntriesBuilder, prop: KProperty<*>): ReactDelegate<T> {
        val delegate = entry.provideDelegate(entries, prop)
        val react = React(delegate.get())

        delegate.onChange = { _, n -> react.value = n }
        react.onChange { delegate.set(it) }

        return ReactDelegate(react)
    }
}

class ReactDelegate<T>(val react: React<T>) {
    operator fun getValue(thisRef: Any?, property: Any?): React<T> = react
    operator fun setValue(thisRef: Any?, property: Any?, value: React<T>) {}
}