package at.sunilson.testcore

import io.mockk.MockKAnnotations
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class, TestCoroutineDispatcherExtension::class)
abstract class BaseUnitTest {
    @BeforeEach
    fun mainBefore() {
        MockKAnnotations.init(this, relaxed = true)
    }
}
