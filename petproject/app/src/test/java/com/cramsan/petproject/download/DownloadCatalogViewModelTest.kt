package com.cramsan.petproject.download

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.PetProjectApplication
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.PresentablePlant
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DownloadCatalogViewModelTest {

    lateinit var application: PetProjectApplication
    lateinit var modelProvider: ModelProviderInterface
    lateinit var log: EventLoggerInterface
    lateinit var metrics: MetricsInterface
    lateinit var thread: ThreadUtilInterface
    lateinit var testDispatcher: CoroutineDispatcher
    lateinit var viewModel: DownloadCatalogViewModel

    @Rule
    @JvmField
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        application = mockk(relaxed = true)
        modelProvider = mockk(relaxed = true)
        log = mockk(relaxed = true)
        metrics = mockk(relaxed = true)
        thread = mockk(relaxed = true)
        testDispatcher = TestCoroutineDispatcher()

        viewModel = DownloadCatalogViewModel(application, log, metrics, thread, modelProvider, testDispatcher)
    }

    @Test
    fun testIsCatalogReadyInitialState() {
        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == false)
        Assert.assertFalse(viewModel.isCatalogReady())
        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == false)
    }

    @Test
    fun testIsCatalogReadyOnceDownloaded() {
        every { modelProvider.isCatalogAvailable(any()) } returns true

        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == false)
        Assert.assertTrue(viewModel.isCatalogReady())
        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == true)
    }

    @Test
    fun testDispatchDownloadCatalogOnBackground() {
        every { modelProvider.isCatalogAvailable(any()) } returns false
        coEvery { modelProvider.getPlantsWithToxicity(any(), any()) } returns listOf(
            PresentablePlant(1, "", "", AnimalType.CAT, ToxicityValue.TOXIC)
        )

        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == false)
        viewModel.downloadCatalog()
        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == true)
    }

    @Test
    fun testDownloadCatalogOnBackground() {
        every { modelProvider.isCatalogAvailable(any()) } returns false
        coEvery { modelProvider.getPlantsWithToxicity(any(), any()) } returns emptyList()

        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == false)
        viewModel.downloadCatalog()
        Assert.assertTrue(viewModel.observableIsDownloadComplete.value == true)
    }
}
