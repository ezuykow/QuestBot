package ru.coffeecoders.questbot.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.coffeecoders.questbot.logs.LogSender;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class ApplicationShutdownManagerTest {

    @Mock
    private ApplicationContext context;
    @Mock
    private LogSender logger;

    @InjectMocks
    private ApplicationShutdownManager applicationShutdownManager;

    @Test
    public void ShouldExitSystemWithCodeZeroThenCalledStopBot() throws Exception {
        doNothing().when(logger).warn(anyString());

        int expectedExitCode = catchSystemExit(() -> applicationShutdownManager.stopBot());

        Assertions.assertEquals(expectedExitCode, 0);
    }

}