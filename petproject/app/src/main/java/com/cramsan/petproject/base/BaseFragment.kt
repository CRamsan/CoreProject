package com.cramsan.petproject.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.petproject.PetProjectApplication
import org.kodein.di.KodeinAware
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein by lazy { (requireActivity().application as PetProjectApplication).kodein }
    protected val eventLogger: EventLoggerInterface by instance()
    protected val metrics: MetricsInterface by instance()
    private val vmFactory: (ViewModelStoreOwner) -> ViewModelProvider by factory()
    abstract val logTag: String

    abstract val contentViewLayout: Int

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventLogger.log(Severity.INFO, logTag, "onAttach")
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onCreate")
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onCreateView")
        return inflater.inflate(contentViewLayout, container, false)
    }

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onActivityCreated")
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        eventLogger.log(Severity.INFO, logTag, "onStart")
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        eventLogger.log(Severity.INFO, logTag, "onResume")
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        eventLogger.log(Severity.INFO, logTag, "onPause")
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        eventLogger.log(Severity.INFO, logTag, "onStop")
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        eventLogger.log(Severity.INFO, logTag, "onDestroyView")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        eventLogger.log(Severity.INFO, logTag, "onDestroy")
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        eventLogger.log(Severity.INFO, logTag, "onDetach")
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        eventLogger.log(Severity.INFO, logTag, "onSaveInstanceState")
    }

    protected fun <T : ViewModel> getViewModel(modelClass: Class<T>): T {
        return vmFactory.invoke(this).get(modelClass)
    }
}
